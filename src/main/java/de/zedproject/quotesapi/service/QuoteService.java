package de.zedproject.quotesapi.service;

import de.zedproject.quotesapi.data.model.Quote;
import de.zedproject.quotesapi.data.model.QuoteRequest;
import de.zedproject.quotesapi.repository.QuoteRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;

@Service
public class QuoteService {
  private final QuoteRepository repository;

  public QuoteService(final QuoteRepository repository) {
    this.repository = repository;
  }

  public Quote createEntity(final QuoteRequest request) {
    return repository.save(request);
  }

  public List<Quote> getEntities() {
    return repository.findAll();
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
}
