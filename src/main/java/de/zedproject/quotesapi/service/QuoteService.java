package de.zedproject.quotesapi.service;

import de.zedproject.quotesapi.data.model.*;
import de.zedproject.quotesapi.exceptions.NotifierException;
import de.zedproject.quotesapi.exceptions.QotdNotFoundException;
import de.zedproject.quotesapi.exceptions.QuoteNotFoundException;
import de.zedproject.quotesapi.exceptions.ResourceNotFoundException;
import de.zedproject.quotesapi.repository.PushNotificationRepository;
import de.zedproject.quotesapi.repository.QuoteOfTheDayRepository;
import de.zedproject.quotesapi.repository.QuoteRepository;
import de.zedproject.quotesapi.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class QuoteService {

  private static final Logger LOGGER = LoggerFactory.getLogger(QuoteService.class);

  private static final String MIN_QUOTES_COUNT = "Minimum number of quotes not reached (10)";

  private final QuoteRepository repository;

  private final UserRepository userRepository;

  private final QuoteOfTheDayRepository qotdRepository;

  private final PushNotificationRepository notifierRepository;

  @Value("{notification.topic.quote-creation}")
  private String quoteCreationTopic;

  public QuoteService(final QuoteRepository repository,
                      final UserRepository userRepository,
                      final QuoteOfTheDayRepository qotdRepository,
                      final PushNotificationRepository notifierRepository) {
    this.repository = repository;
    this.userRepository = userRepository;
    this.qotdRepository = qotdRepository;
    this.notifierRepository = notifierRepository;
  }

  public Quote create(final QuoteRequest request) {
    Quote quote = null;
    try {
      quote = repository.save(request);

      final var notification = new PushNotification(
        "New Quote",
        quote.author() + " says " + quote.truncateText() + "...");
      notifierRepository.sendToTopic(quoteCreationTopic, notification);

      return quote;
    } catch (NotifierException ex) {
      LOGGER.warn("PushNotification for quote creation failed");
      return quote;
    } catch (QuoteNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }

  public Quote create(final QuoteRequest request, final String creator) {
    // TODO when user not found return null
    final var creatorId = request.creatorId() == null ? userRepository.findByName(creator).id() : request.creatorId();

    return create(request.withCreatorId(creatorId));
  }

  public List<Quote> findAll() {
    try {
      return repository.findAll();
    } catch (QuoteNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }

  public List<Quote> findAll(final SortField field, final SortOrder order) {
    try {
      return repository.findAll(field, order);
    } catch (QuoteNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }

  public List<Quote> findAll(final List<Integer> ids) {
    try {
      return repository.findAllByIds(ids);
    } catch (QuoteNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }

  public Quote find(final Integer id) {
    try {
      return repository.findById(id);
    } catch (QuoteNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }

  public Quote update(final Integer id, final QuoteRequest request) {
    try {
      return repository.update(id, request);
    } catch (QuoteNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }

  public Quote update(final Integer id, final QuoteRequest request, final String creator) {
    // TODO when user not found return null
    final var creatorId = request.creatorId() == null ? userRepository.findByName(creator).id() : request.creatorId();

    return update(id, request.withCreatorId(creatorId));
  }

  public Quote delete(final Integer id) {
    try {
      return repository.delete(id);
    } catch (QuoteNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }

  public Quote findRandom() {
    try {
      final var availableIds = repository.findAllIds();
      final var randIdx = new SecureRandom().nextInt(availableIds.size());
      return find(availableIds.get(randIdx));
    } catch (QuoteNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }

  // TODO optimise with single caching or learn how to manipulate the cache to insert multiple values
  public List<Quote> findRandoms(final Integer quantity) {
    try {
      final var availableIds = repository.findAllIds();
      final var randIdxs = new SecureRandom().ints(quantity, 0, availableIds.size()).boxed().toList();
      final var quotes = findAll(randIdxs.stream().map(availableIds::get).toList());
      Collections.shuffle(quotes);
      return quotes;
    } catch (QuoteNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }

  public Integer count() {
    return repository.count();
  }

  public Quote findQuoteOfTheDay() {
    if (count() < 10) throw new ResourceNotFoundException(MIN_QUOTES_COUNT);

    QuoteOfTheDay qotd;
    try {
      qotd = qotdRepository.findByDate(LocalDate.now());
    } catch (final QotdNotFoundException ex) {
      final var quote = findRandomDayDependent();
      qotd = qotdRepository.save(new QuoteOfTheDayRequest(quote.id(), LocalDateTime.now()));
    }
    return repository.findById(qotd.quoteId());
  }

  private Quote findRandomDayDependent() {
    final var remainder = LocalDate.now().getDayOfYear() % 10;
    final var availableIds = repository.findAllIds().stream().filter(n -> n % 10 == remainder).toList();
    final var randIdx = new SecureRandom().nextInt(availableIds.size());
    return find(availableIds.get(randIdx));
  }
}
