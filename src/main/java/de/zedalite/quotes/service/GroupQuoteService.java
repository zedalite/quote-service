package de.zedalite.quotes.service;

import de.zedalite.quotes.data.mapper.QuoteMapper;
import de.zedalite.quotes.data.model.*;
import de.zedalite.quotes.exception.NotifierException;
import de.zedalite.quotes.exception.QuoteNotFoundException;
import de.zedalite.quotes.exception.ResourceNotFoundException;
import de.zedalite.quotes.repository.GroupQuoteRepository;
import de.zedalite.quotes.repository.PushNotificationRepository;
import de.zedalite.quotes.repository.UserRepository;
import de.zedalite.quotes.utils.StringUtils;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GroupQuoteService {

  private static final Logger LOGGER = LoggerFactory.getLogger(GroupQuoteService.class);

  private static final QuoteMapper QUOTE_MAPPER = QuoteMapper.INSTANCE;

  private final GroupQuoteRepository repository;

  private final UserRepository userRepository;

  private final PushNotificationRepository notifierRepository;

  @Value("${notification.topic.quote-creation}")
  private String quoteCreationTopic;

  public GroupQuoteService(
    final GroupQuoteRepository repository,
    final UserRepository userRepository,
    final PushNotificationRepository notifierRepository
  ) {
    this.repository = repository;
    this.userRepository = userRepository;
    this.notifierRepository = notifierRepository;
  }

  public QuoteMessage create(final Integer id, final QuoteRequest request) {
    QuoteMessage quote = null;
    try {
      quote = getQuoteMessage(repository.save(id, request));

      // TODO extract notification build?
      final PushNotification notification = new PushNotification(
        "New Quote",
        quote.author() + " says " + quote.truncateText() + "...",
        Map.of("type", "NEW_QUOTE", "quoteId", String.valueOf(quote.id()))
      );
      // TODO send to specific group topic or user notification token
      notifierRepository.sendToTopic(quoteCreationTopic, notification);

      return quote;
    } catch (final NotifierException ex) {
      LOGGER.warn("PushNotification for quote creation failed");
      return quote;
    } catch (final QuoteNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }

  public QuoteMessage create(final Integer id, final QuoteRequest request, final Integer creatorId) {
    final Integer creatorIdOrDefault = Objects.requireNonNullElse(request.creatorId(), creatorId);

    return create(id, request.withCreatorId(creatorIdOrDefault));
  }

  public List<QuoteMessage> findAll(final Integer id, final SortField field, final SortOrder order) {
    try {
      final List<Quote> quotes = repository.findAll(id, field, order);
      return getQuoteMessages(quotes);
    } catch (final QuoteNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }

  public QuoteMessage find(final Integer id, final Integer quoteId) {
    try {
      final Quote quote = repository.findById(id, quoteId);
      return getQuoteMessage(quote);
    } catch (final QuoteNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }

  public List<QuoteMessage> findRandoms(final Integer id, final Integer quantity) {
    try {
      return getQuoteMessages(repository.findRandoms(id, quantity));
    } catch (final QuoteNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }

  public Integer count(final Integer id) {
    return repository.count(id);
  }

  private QuoteMessage getQuoteMessage(final Quote quote) {
    final List<User> mentions = getMentions(StringUtils.extractUserIds(quote.text()));

    return QUOTE_MAPPER.mapToQuoteMessage(quote, mentions);
  }

  private List<QuoteMessage> getQuoteMessages(final List<Quote> quotes) {
    return quotes.stream().map(this::getQuoteMessage).toList();
  }

  private List<User> getMentions(final List<Integer> userIds) {
    return userIds.isEmpty() ? Collections.emptyList() : userRepository.findAllByIds(userIds);
  }
}
