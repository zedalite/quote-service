package de.zedproject.quotesapi.service;

import de.zedproject.quotesapi.data.model.*;
import de.zedproject.quotesapi.exceptions.QotdNotFoundException;
import de.zedproject.quotesapi.repository.QuoteOfTheDayRepository;
import de.zedproject.quotesapi.repository.QuoteRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class QuoteService {

  private final QuoteRepository repository;

  private final QuoteOfTheDayRepository qotdRepository;

  public QuoteService(final QuoteRepository repository, QuoteOfTheDayRepository qotdRepository) {
    this.repository = repository;
    this.qotdRepository = qotdRepository;
  }

  public Quote createEntity(final QuoteRequest request) {
    return repository.save(request);
  }

  public List<Quote> getEntities() {
    return repository.findAll();
  }

  public List<Quote> getEntities(final SortField field, final SortOrder order) {
    return repository.findAll(field, order);
  }

  public List<Quote> getEntities(final List<Integer> ids) {
    return repository.findAllByIds(ids);
  }

  @Cacheable(value = "quotes", key = "#id", unless = "#result == null")
  public Quote getEntity(final Integer id) {
    return repository.findById(id);
  }

  @CachePut(value = "quotes", key = "#id")
  public Quote updateEntity(final Integer id, final QuoteRequest request) {
    return repository.update(id, request);
  }

  @CacheEvict(value = "quotes", key = "#id")
  public Quote deleteEntity(final Integer id) {
    return repository.delete(id);
  }

  public Quote getRandomEntity() {
    final var availableIds = repository.findAllIds();
    final var randIdx = new SecureRandom().nextInt(availableIds.size());
    return getEntity(availableIds.get(randIdx));
  }

  // TODO optimise with single caching or learn how to manipulate the cache to insert multiple values
  public List<Quote> getRandomEntities(final Integer quantity) {
    final var availableIds = repository.findAllIds();
    final var randIdxs = new SecureRandom().ints(quantity, 0, availableIds.size()).boxed().toList();
    final var quotes = getEntities(randIdxs.stream().map(availableIds::get).toList());
    Collections.shuffle(quotes);
    return quotes;
  }

  public Integer countEntities() {
    return repository.count();
  }

  @Cacheable(value = "qotd")
  public Quote getQuoteOfTheDay() {
    // TODO too few quotes available -> throw exception
    QuoteOfTheDay qotd;
    try {
      qotd = qotdRepository.findByDate(LocalDate.now());
    } catch (final QotdNotFoundException ex) {
      final var quote = getRandomQuoteDayDependent();
      qotd = qotdRepository.save(new QuoteOfTheDayRequest(quote.id(), LocalDateTime.now()));
    }
    return repository.findById(qotd.quoteId());
  }

  private Quote getRandomQuoteDayDependent() {
    final var remainder = LocalDate.now().getDayOfYear() % 10;
    final var availableIds = repository.findAllIds().stream().filter(n -> n % 10 == remainder).toList();
    final var randIdx = new SecureRandom().nextInt(availableIds.size());
    return getEntity(availableIds.get(randIdx));
  }
}
