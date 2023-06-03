package de.zedproject.quotesapi.service;

import de.zedproject.quotesapi.data.model.*;
import de.zedproject.quotesapi.exceptions.QotdNotFoundException;
import de.zedproject.quotesapi.exceptions.QuoteNotFoundException;
import de.zedproject.quotesapi.exceptions.ResourceNotFoundException;
import de.zedproject.quotesapi.repository.QuoteOfTheDayRepository;
import de.zedproject.quotesapi.repository.QuoteRepository;
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

  public Quote create(final QuoteRequest request) {
    try {
      return repository.save(request);
    } catch (QuoteNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
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
    // TODO too few quotes available -> throw exception
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
    try {
      final var remainder = LocalDate.now().getDayOfYear() % 10;
      final var availableIds = repository.findAllIds().stream().filter(n -> n % 10 == remainder).toList();
      final var randIdx = new SecureRandom().nextInt(availableIds.size());
      return find(availableIds.get(randIdx));
    } catch (QuoteNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }
}
