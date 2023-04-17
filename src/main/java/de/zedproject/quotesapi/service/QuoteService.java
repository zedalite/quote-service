package de.zedproject.quotesapi.service;

import de.zedproject.quotesapi.data.mapper.QuoteMapper;
import de.zedproject.quotesapi.data.models.Quote;
import de.zedproject.quotesapi.data.payloads.QuoteRequest;
import de.zedproject.quotesapi.data.repository.QuoteRepository;
import de.zedproject.quotesapi.exceptions.BadRequestException;
import de.zedproject.quotesapi.exceptions.ResourceNotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;

@Service
public class QuoteService {
  public static final QuoteMapper QUOTE_MAPPER = QuoteMapper.INSTANCE;
  public static final String QUOTE_NOT_FOUND = "Quote not found";
  public static final String QUOTES_NOT_FOUND = "Quotes not found";
  private final QuoteRepository repository;

  public QuoteService(final QuoteRepository repository) {
    this.repository = repository;
  }

  public Quote createEntity(final QuoteRequest request) {
    final var quotesRecord = repository.save(request);
    if (quotesRecord == null) throw new BadRequestException();
    return QUOTE_MAPPER.quoteRecToQuote(quotesRecord);
  }

  public List<Quote> getEntities() {
    final var quotesRecords = repository.findAll();
    if (quotesRecords.isEmpty()) throw new ResourceNotFoundException(QUOTES_NOT_FOUND);
    return QUOTE_MAPPER.quoteRecsToQuotes(quotesRecords);
  }

  public List<Quote> getEntities(final List<Integer> ids) {
    final var quotesRecords = repository.findAllByIds(ids);
    if (quotesRecords.isEmpty()) throw new ResourceNotFoundException(QUOTES_NOT_FOUND);
    return QUOTE_MAPPER.quoteRecsToQuotes(quotesRecords);
  }

  @Cacheable("quotes")
  public Quote getEntity(final Integer id) {
    final var quotesRecord = repository.findById(id);
    if (quotesRecord == null) throw new ResourceNotFoundException(QUOTE_NOT_FOUND);
    return QUOTE_MAPPER.quoteRecToQuote(quotesRecord);
  }

  @CachePut(value = "quotes", key = "#id")
  public Quote updateEntity(final Integer id, final QuoteRequest request) {
    final var quotesRecord = repository.update(id, request);
    if (quotesRecord == null) throw new ResourceNotFoundException(QUOTE_NOT_FOUND);
    return QUOTE_MAPPER.quoteRecToQuote(quotesRecord);
  }

  @CacheEvict(value = "quotes", key = "#id")
  public Quote deleteEntity(final Integer id) {
    final var quotesRecord = repository.delete(id);
    if (quotesRecord == null) throw new ResourceNotFoundException(QUOTE_NOT_FOUND);
    return QUOTE_MAPPER.quoteRecToQuote(quotesRecord);
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
