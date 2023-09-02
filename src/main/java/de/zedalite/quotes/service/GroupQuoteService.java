package de.zedalite.quotes.service;

import de.zedalite.quotes.data.mapper.QuoteMapper;
import de.zedalite.quotes.data.model.*;
import de.zedalite.quotes.exceptions.NotifierException;
import de.zedalite.quotes.exceptions.QuoteNotFoundException;
import de.zedalite.quotes.exceptions.ResourceNotFoundException;
import de.zedalite.quotes.repository.GroupQuoteRepository;
import de.zedalite.quotes.repository.PushNotificationRepository;
import de.zedalite.quotes.repository.UserRepository;
import de.zedalite.quotes.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class GroupQuoteService {

  private static final Logger LOGGER = LoggerFactory.getLogger(GroupQuoteService.class);

  private static final QuoteMapper QUOTE_MAPPER = QuoteMapper.INSTANCE;

  private final GroupQuoteRepository repository;

  private final UserRepository userRepository;

  private final PushNotificationRepository notifierRepository;

  @Value("${notification.topic.quote-creation}")
  private String quoteCreationTopic;

  public GroupQuoteService(final GroupQuoteRepository repository, final UserRepository userRepository, final PushNotificationRepository notifierRepository) {
    this.repository = repository;
    this.userRepository = userRepository;
    this.notifierRepository = notifierRepository;
  }

  public QuoteMessage create(final Integer id, final QuoteRequest request) {
    Quote quote = null;
    try {
      quote = repository.save(id, request);

      // TODO extract notification build?
      final var notification = new PushNotification(
        "New Quote",
        quote.author() + " says " + quote.truncateText() + "...",
        Map.of("type", "NEW_QUOTE", "quoteId", String.valueOf(quote.id())));
      // TODO send to specific group topic or user notification token
      notifierRepository.sendToTopic(quoteCreationTopic, notification);

      return getQuoteMessage(quote);
    } catch (NotifierException ex) {
      LOGGER.warn("PushNotification for quote creation failed");
      return getQuoteMessage(quote);
    } catch (QuoteNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }

  public QuoteMessage create(final Integer id, final QuoteRequest request, final Integer creatorId) {
    final var creatorIdOrDefault = request.creatorId() == null ? creatorId : request.creatorId();

    return create(id, request.withCreatorId(creatorIdOrDefault));
  }

  public List<QuoteMessage> findAll(final Integer id, final SortField field, final SortOrder order) {
    try {
      final var quotes = repository.findAll(id, field, order);
      return getQuoteMessages(quotes);
    } catch (QuoteNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }

  public List<QuoteMessage> findAll(final Integer id, final List<Integer> quoteIds) {
    try {
      final var matchedQuotes = repository.findAllByIds(id, quoteIds);
      return getQuoteMessages(matchedQuotes);
    } catch (QuoteNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }

  public QuoteMessage find(final Integer id, final Integer quoteId) {
    try {
      final var quote = repository.findById(id, quoteId);
      return getQuoteMessage(quote);
    } catch (QuoteNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }

  public Integer count(final Integer id) {
    return repository.count(id);
  }

  // TODO optimise with single caching or learn how to manipulate the cache to insert multiple values
  public List<QuoteMessage> findRandoms(final Integer id, final Integer quantity) {
    try {
      final var availableIds = repository.findAllIds(id);
      final var randIdxs = new SecureRandom().ints(quantity, 0, availableIds.size()).boxed().toList();
      final var quotes = findAll(id, randIdxs.stream().map(availableIds::get).toList());
      Collections.shuffle(new ArrayList<>(quotes));
      return quotes;
    } catch (QuoteNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }

  // TODO move methods to helper class?

  private QuoteMessage getQuoteMessage(final Quote quote) {
    final var mentions = getMentions(StringUtils.extractUserIds(quote.text()));

    return QUOTE_MAPPER.mapToQuoteMessage(quote, mentions);
  }

  private List<QuoteMessage> getQuoteMessages(final List<Quote> quotes) {
    return quotes.stream()
      .map(this::getQuoteMessage)
      .toList();
  }

  private List<User> getMentions(List<Integer> userIds) {
    return userIds.isEmpty() ? Collections.emptyList() : userRepository.findAllByIds(userIds);
  }
}
