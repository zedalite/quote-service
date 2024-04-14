package de.zedalite.quotes.service;

import de.zedalite.quotes.data.mapper.QuoteMapper;
import de.zedalite.quotes.data.model.Quote;
import de.zedalite.quotes.data.model.QuoteMessage;
import de.zedalite.quotes.data.model.QuoteOfTheDayRequest;
import de.zedalite.quotes.data.model.User;
import de.zedalite.quotes.exception.QotdNotFoundException;
import de.zedalite.quotes.exception.ResourceNotFoundException;
import de.zedalite.quotes.repository.GroupQuoteOfTheDayRepository;
import de.zedalite.quotes.repository.GroupQuoteRepository;
import de.zedalite.quotes.repository.UserRepository;
import de.zedalite.quotes.utils.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
public class GroupQuoteOfTheDayService {

  private static final QuoteMapper QUOTE_MAPPER = QuoteMapper.INSTANCE;

  private static final String MIN_QUOTES_COUNT = "Minimum number of quotes not reached (10)";

  private final GroupQuoteOfTheDayRepository repository;

  private final GroupQuoteRepository groupQuoteRepository;

  private final UserRepository userRepository;

  public GroupQuoteOfTheDayService(final GroupQuoteOfTheDayRepository repository, final GroupQuoteRepository groupQuoteRepository, final UserRepository userRepository) {
    this.repository = repository;
    this.groupQuoteRepository = groupQuoteRepository;
    this.userRepository = userRepository;
  }

  public QuoteMessage findQuoteOfTheDay(final Integer id) throws  ResourceNotFoundException {
    if (groupQuoteRepository.count(id) < 10) throw new ResourceNotFoundException(MIN_QUOTES_COUNT);

    Quote qotd;
    try {
      qotd = repository.findByDate(id, LocalDate.now());
    } catch (final QotdNotFoundException ex) {
      qotd = groupQuoteRepository.findRandoms(id, 1).getFirst();
      repository.save(id, new QuoteOfTheDayRequest(qotd.id(), LocalDate.now()));
    }

    return getQuoteMessage(qotd);
  }

  private QuoteMessage getQuoteMessage(final Quote quote) {
    final List<User> mentions = getMentions(StringUtils.extractUserIds(quote.text()));

    return QUOTE_MAPPER.mapToQuoteMessage(quote, mentions);
  }

  private List<User> getMentions(List<Integer> userIds) {
    return userIds.isEmpty() ? Collections.emptyList() : userRepository.findAllByIds(userIds);
  }
}
