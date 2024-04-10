package de.zedalite.quotes.service;

import de.zedalite.quotes.data.mapper.QuoteMapper;
import de.zedalite.quotes.data.model.*;
import de.zedalite.quotes.exceptions.NotifierException;
import de.zedalite.quotes.exceptions.QotdNotFoundException;
import de.zedalite.quotes.exceptions.QuoteNotFoundException;
import de.zedalite.quotes.exceptions.ResourceNotFoundException;
import de.zedalite.quotes.repository.PushNotificationRepository;
import de.zedalite.quotes.repository.QuoteOfTheDayRepository;
import de.zedalite.quotes.repository.QuoteRepository;
import de.zedalite.quotes.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Represents a service for managing quotes.
 * This service provides functionality for creating, retrieving, updating, and deleting quotes,
 * as well as generating random quotes and finding the quote of the day.
 */
@Service
public class QuoteService {

  private static final Logger LOGGER = LoggerFactory.getLogger(QuoteService.class);

  private static final QuoteMapper QUOTE_MAPPER = QuoteMapper.INSTANCE;

  private static final Pattern USER_ID_PATTERN = Pattern.compile("<@(\\d+)>");

  private static final String MIN_QUOTES_COUNT = "Minimum number of quotes not reached (10)";

  private final QuoteRepository repository;

  private final UserRepository userRepository;

  private final QuoteOfTheDayRepository qotdRepository;

  private final PushNotificationRepository notifierRepository;

  @Value("${notification.topic.quote-creation}")
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

  /**
   * Creates a new quote based on the provided quote request.
   * This method saves the quote in the repository, sends a push notification to the quoteCreationTopic,
   * and returns the created quote.
   *
   * @param request the quote request object containing the necessary information to create the quote
   * @return the created quote
   * @throws ResourceNotFoundException if the quote is not found in the repository
   */
  public Quote create(final QuoteRequest request) {
    Quote quote = null;
    try {
      quote = repository.save(request);

      final var notification = new PushNotification(
        "New Quote",
        quote.author() + " says " + quote.text() + "...",
        Map.of("type", "NEW_QUOTE", "quoteId", String.valueOf(quote.id())));
      notifierRepository.sendToTopic(quoteCreationTopic, notification);

      return quote;
    } catch (NotifierException ex) {
      LOGGER.warn("PushNotification for quote creation failed");
      return quote;
    } catch (QuoteNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }

  /**
   * Creates a new quote based on the provided quote request and creator.
   * This method saves the quote in the repository, sends a push notification to the quoteCreationTopic,
   * and returns the created quote.
   *
   * @param request the quote request object containing the necessary information to create the quote
   * @param creator the name of the creator of the quote
   * @return the created quote
   * @throws ResourceNotFoundException if the quote is not found in the repository
   */
  public Quote create(final QuoteRequest request, final String creator) {
    // TODO when user not found return null
    final var creatorId = request.creatorId() == null ? userRepository.findByName(creator).id() : request.creatorId();

    return create(request.withCreatorId(creatorId));
  }

  /**
   * Finds and returns all quotes available in the repository.
   *
   * @return a list of all quotes
   * @throws ResourceNotFoundException if no quotes are found in the repository
   */
  public List<Quote> findAll() {
    try {
      return repository.findAll();
    } catch (QuoteNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }

  /**
   * Finds and returns all quotes available in the repository.
   *
   * @param field the field to sort the quotes by
   * @param order the order in which the quotes should be sorted (ascending or descending)
   * @return a list of all quotes
   * @throws ResourceNotFoundException if no quotes are found in the repository
   */
  public List<QuoteMessage> findAll(final SortField field, final SortOrder order) {
    try {
      final var quotes = repository.findAll(field, order);
      return getQuoteMessages(quotes);
    } catch (QuoteNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }

  /**
   * Finds and returns quotes with the given ids from the repository.
   *
   * @param ids the list of quote ids to find
   * @return a list of quotes with the given ids
   * @throws ResourceNotFoundException if any of the quotes with the given ids are not found in the repository
   */
  public List<QuoteMessage> findAll(final List<Integer> ids) {
    try {
      final var matchedQuotes = repository.findAllByIds(ids);
      return getQuoteMessages(matchedQuotes);
    } catch (QuoteNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }

  /**
   * Finds and returns a quote with the given id from the repository.
   *
   * @param id the id of the quote to find
   * @return the quote with the given id
   * @throws ResourceNotFoundException if the quote with the given id is not found in the repository
   */
  public QuoteMessage find(final Integer id) {
    try {
      final var quote = repository.findById(id);
      return getQuoteMessage(quote);
    } catch (QuoteNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }

  /**
   * Updates the quote with the given id in the repository using the provided request.
   *
   * @param id the id of the quote to update
   * @param request the quote update request
   * @return the updated quote
   * @throws ResourceNotFoundException if the quote with the given id is not found in the repository
   */
  public Quote update(final Integer id, final QuoteRequest request) {
    try {
      return repository.update(id, request);
    } catch (QuoteNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }

  /**
   * Updates the quote with the given id in the repository using the provided request and creator name.
   *
   * @param id the id of the quote to update
   * @param request the quote update request
   * @param creator the name of the quote creator
   * @return the updated quote
   * @throws ResourceNotFoundException if the quote with the given id is not found in the repository
   */
  public Quote update(final Integer id, final QuoteRequest request, final String creator) {
    // TODO when user not found return null
    final var creatorId = request.creatorId() == null ? userRepository.findByName(creator).id() : request.creatorId();

    return update(id, request.withCreatorId(creatorId));
  }

  /**
   * Deletes the quote with the given id from the repository.
   *
   * @param id the id of the quote to delete
   * @return the deleted quote
   * @throws ResourceNotFoundException if the quote with the given id is not found in the repository
   */
  public Quote delete(final Integer id) {
    try {
      return repository.delete(id);
    } catch (QuoteNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }

  /**
   * Finds and returns a random quote from the repository.
   *
   * @return the randomly selected quote
   * @throws ResourceNotFoundException if no quotes are found in the repository
   */
  public QuoteMessage findRandom() {
    try {
      final var availableIds = repository.findAllIds();
      final var randIdx = new SecureRandom().nextInt(availableIds.size());
      return find(availableIds.get(randIdx));
    } catch (QuoteNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }

  /**
   * Finds and returns a specified number of random quotes from the repository.
   *
   * @param quantity the number of random quotes to retrieve
   * @return a list of randomly selected quotes
   * @throws ResourceNotFoundException if no quotes are found in the repository
   */
  // TODO optimise with single caching or learn how to manipulate the cache to insert multiple values
  public List<QuoteMessage> findRandoms(final Integer quantity) {
    try {
      final var availableIds = repository.findAllIds();
      final var randIdxs = new SecureRandom().ints(quantity, 0, availableIds.size()).boxed().toList();
      final var quotes = findAll(randIdxs.stream().map(availableIds::get).toList());
      Collections.shuffle(new ArrayList<>(quotes));
      return quotes;
    } catch (QuoteNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }

  /**
   * Returns the total number of quotes in the repository.
   *
   * @return the count of quotes in the repository
   */
  public Integer count() {
    return repository.count();
  }

  /**
   * Finds the quote of the day from the repository based on the current date.
   *
   * @throws ResourceNotFoundException if the total number of quotes in the repository is less than 10
   * @return the quote of the day
   */
  public QuoteMessage findQuoteOfTheDay() {
    if (count() < 10) throw new ResourceNotFoundException(MIN_QUOTES_COUNT);

    QuoteOfTheDay qotd;
    try {
      qotd = qotdRepository.findByDate(LocalDate.now());
    } catch (final QotdNotFoundException ex) {
      final var quoteId = findRandomDayDependent().id();
      qotd = qotdRepository.save(new OldQuoteOfTheDayRequest(quoteId, LocalDateTime.now()));
    }

    return find(qotd.quoteId());
  }

  private QuoteMessage getQuoteMessage(final Quote quote) {
    final var mentions = getMentions(extractUserIds(quote.text()));

    return QUOTE_MAPPER.mapToQuoteMessage(quote, mentions);
  }

  private List<QuoteMessage> getQuoteMessages(final List<Quote> quotes) {
    return quotes.stream()
      .map(this::getQuoteMessage)
      .toList();
  }

  /**
   * Finds a random quote from the repository that is dependent on the current day.
   *
   * @return a random day-dependent quote
   */
  private QuoteMessage findRandomDayDependent() {
    final var remainder = LocalDate.now().getDayOfYear() % 10;
    final var availableIds = repository.findAllIds().stream().filter(n -> n % 10 == remainder).toList();
    final var randIdx = new SecureRandom().nextInt(availableIds.size());
    return find(availableIds.get(randIdx));
  }

  private List<User> getMentions(List<Integer> userIds) {
    return userIds.isEmpty() ? Collections.emptyList() : userRepository.findAllByIds(userIds);
  }

  private static List<Integer> extractUserIds(final String text) {
    return USER_ID_PATTERN
      .matcher(text)
      .results()
      .map(result -> Integer.parseInt(result.group(1)))
      .toList();
  }
}
