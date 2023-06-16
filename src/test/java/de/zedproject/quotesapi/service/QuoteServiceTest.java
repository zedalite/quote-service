package de.zedproject.quotesapi.service;

import de.zedproject.quotesapi.data.model.QuoteOfTheDayRequest;
import de.zedproject.quotesapi.data.model.QuoteRequest;
import de.zedproject.quotesapi.data.model.SortField;
import de.zedproject.quotesapi.data.model.SortOrder;
import de.zedproject.quotesapi.exceptions.QotdNotFoundException;
import de.zedproject.quotesapi.exceptions.QuoteNotFoundException;
import de.zedproject.quotesapi.exceptions.ResourceNotFoundException;
import de.zedproject.quotesapi.fixtures.QuoteGenerator;
import de.zedproject.quotesapi.repository.QuoteOfTheDayRepository;
import de.zedproject.quotesapi.repository.QuoteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static de.zedproject.quotesapi.data.model.SortField.AUTHOR;
import static de.zedproject.quotesapi.data.model.SortOrder.ASC;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class QuoteServiceTest {

  @InjectMocks
  private QuoteService instance;

  @Mock
  private QuoteRepository quoteRepository;

  @Mock
  private QuoteOfTheDayRepository qotdRepository;

  @Test
  @DisplayName("Should create quote")
  void shouldCreateQuote() {
    final var quoteRequest = QuoteGenerator.getQuoteRequest();
    instance.create(quoteRequest);

    then(quoteRepository).should().save(quoteRequest);
  }

  @Test
  @DisplayName("Should create quote with creator")
  void shouldCreateQuoteWithCreator() {
    final var quoteRequest = QuoteGenerator.getQuoteRequest();
    instance.create(quoteRequest, "super");

    then(quoteRepository).should().save(quoteRequest);
  }

  @Test
  @DisplayName("Should throw exception when quote not created")
  void shouldThrowExceptionWhenQuoteNotCreated() {
    final var quoteRequest = QuoteGenerator.getQuoteRequest();
    willThrow(QuoteNotFoundException.class).given(quoteRepository).save(any(QuoteRequest.class));

    assertThatCode(() -> instance.create(quoteRequest)).isInstanceOf(ResourceNotFoundException.class);
  }


  @Test
  @DisplayName("Should throw exception when quote not found")
  void shouldThrowExceptionWhenQuoteNotFound() {
    willThrow(QuoteNotFoundException.class).given(quoteRepository).findById(anyInt());

    assertThatCode(() -> instance.find(1)).isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  @DisplayName("Should find all quotes")
  void shouldFindAllQuotes() {
    final var expectedQuotes = QuoteGenerator.getQuotes();
    willReturn(expectedQuotes).given(quoteRepository).findAll();

    final var quotes = instance.findAll();

    assertThat(quotes).hasSize(expectedQuotes.size());
  }

  @Test
  @DisplayName("Should throw exception when quotes not found")
  void shouldThrowExceptionWhenQuotesNotFound() {
    willThrow(QuoteNotFoundException.class).given(quoteRepository).findAll();

    assertThatCode(() -> instance.findAll()).isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  @DisplayName("Should find all quotes ordered")
  void shouldFindAllQuotesOrdered() {
    final var expectedQuotes = QuoteGenerator.getQuotes();
    willReturn(expectedQuotes).given(quoteRepository).findAll(any(SortField.class), any(SortOrder.class));

    final var quotes = instance.findAll(AUTHOR, ASC);

    assertThat(quotes).hasSize(expectedQuotes.size());
  }

  @Test
  @DisplayName("Should throw exception when ordered quotes not found")
  void shouldThrowExceptionWhenOrderedQuotesNotFound() {
    willThrow(QuoteNotFoundException.class).given(quoteRepository).findAll(any(SortField.class), any(SortOrder.class));

    assertThatCode(() -> instance.findAll(AUTHOR, ASC)).isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  @DisplayName("Should find all quotes by ids")
  void shouldFindAllQuotesByIds() {
    final var expectedQuotes = QuoteGenerator.getQuotes();
    willReturn(List.of(expectedQuotes.get(1))).given(quoteRepository).findAllByIds(List.of(1));

    final var quotes = instance.findAll(List.of(1));

    assertThat(quotes).hasSize(1).containsOnly(expectedQuotes.get(1));
  }

  @Test
  @DisplayName("Should throw exception when quotes by ids not found")
  void shouldThrowExceptionWhenQuotesByIdsNotFound() {
    willThrow(QuoteNotFoundException.class).given(quoteRepository).findAllByIds(anyList());

    final var list = List.of(1,3,5);
    assertThatCode(() -> instance.findAll(list)).isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  @DisplayName("Should find quote by id")
  void shouldFindQuoteById() {
    final var expectedQuote = QuoteGenerator.getQuote();
    willReturn(expectedQuote).given(quoteRepository).findById(anyInt());

    final var quote = instance.find(1);

    assertThat(quote.id()).isEqualTo(expectedQuote.id());
  }

  @Test
  @DisplayName("Should update quote")
  void shouldUpdateQuote() {
    final var expectedQuote = QuoteGenerator.getQuote();
    willReturn(expectedQuote).given(quoteRepository).update(anyInt(), any(QuoteRequest.class));

    final var updateQuoteRequest = QuoteGenerator.getQuoteRequest();
    final var updatedQuote = instance.update(1, updateQuoteRequest);

    assertThat(updatedQuote).isEqualTo(expectedQuote);
  }

  @Test
  @DisplayName("Should update quote with creator")
  void shouldUpdateQuoteWithCreator() {
    final var expectedQuote = QuoteGenerator.getQuote();
    willReturn(expectedQuote).given(quoteRepository).update(anyInt(), any(QuoteRequest.class));

    final var updateQuoteRequest = QuoteGenerator.getQuoteRequest();
    final var updatedQuote = instance.update(1, updateQuoteRequest, "super");

    assertThat(updatedQuote).isEqualTo(expectedQuote);

  }

  @Test
  @DisplayName("Should throw exception when quote to be update does not exists")
  void shouldThrowExceptionWhenQuoteToBeUpdateDoesNotExists() {
    willThrow(QuoteNotFoundException.class).given(quoteRepository).update(anyInt(), any(QuoteRequest.class));

    final var updateQuoteRequest = QuoteGenerator.getQuoteRequest();
    assertThatCode(() -> instance.update(1, updateQuoteRequest)).isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  @DisplayName("Should delete quote")
  void shouldDeleteQuote() {
    final var expectedQuote = QuoteGenerator.getQuote();
    willReturn(expectedQuote).given(quoteRepository).delete(anyInt());

    final var deletedQuote = instance.delete(1);

    assertThat(deletedQuote.id()).isEqualTo(1);
  }

  @Test
  @DisplayName("Should throw exception when quote to be deleted does not exists")
  void shouldThrowExceptionWhenQuoteToBeDeletedDoesNotExists() {
    willThrow(QuoteNotFoundException.class).given(quoteRepository).delete(anyInt());

    assertThatCode(() -> instance.delete(1)).isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  @DisplayName("Should find random quote")
  void shouldFindRandomQuote() {
    final var expectedQuote = QuoteGenerator.getQuote();
    willReturn(List.of(1,3)).given(quoteRepository).findAllIds();
    willReturn(expectedQuote).given(quoteRepository).findById(anyInt());

    final var quote = instance.findRandom();

    assertThat(quote.id()).isEqualTo(expectedQuote.id());
  }

  @Test
  @DisplayName("Should throw exception when random quote not found")
  void shouldThrowExceptionWhenRandomQuoteNotFound() {
    willThrow(QuoteNotFoundException.class).given(quoteRepository).findAllIds();

    assertThatCode(() -> instance.findRandom()).isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  @DisplayName("Should find random quotes")
  void shouldFindRandomQuotes() {
    final var expectedQuotes = new ArrayList<>(QuoteGenerator.getQuotes());
    willReturn(List.of(1,2)).given(quoteRepository).findAllIds();
    willReturn(expectedQuotes).given(quoteRepository).findAllByIds(anyList());

    final var quotes = instance.findRandoms(QuoteGenerator.getQuotes().size());

    assertThat(quotes).hasSize(QuoteGenerator.getQuotes().size());
  }

  @Test
  @DisplayName("Should throw exception when random quotes not found")
  void shouldThrowExceptionWhenRandomQuotesNotFound() {
    willThrow(QuoteNotFoundException.class).given(quoteRepository).findAllIds();

    assertThatCode(() -> instance.findRandoms(2)).isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  @DisplayName("Should find quote of the day")
  void shouldFindQuoteOfTheDay() {
    final var expectedQotd = QuoteGenerator.getQuoteOfTheDay();
    final var expectedQuote = QuoteGenerator.getQuote();
    willReturn(10).given(quoteRepository).count();
    willReturn(expectedQotd).given(qotdRepository).findByDate(any(LocalDate.class));
    willReturn(expectedQuote).given(quoteRepository).findById(anyInt());

    final var quote = instance.findQuoteOfTheDay();

    assertThat(quote).isNotNull();
    assertThat(quote.id()).isEqualTo(1);
  }

  @Test
  @DisplayName("Should throw exception when minimum quotes not reached")
  void shouldThrowExceptionWhenMinimumQuotesNotReached() {
    willReturn(9).given(quoteRepository).count();

    assertThatCode(() -> instance.findQuoteOfTheDay()).isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  @DisplayName("Should find quote of the day with determination")
  void shouldFindQuoteOfTheDayWithDetermination() {
    final var expectedQuote = QuoteGenerator.getQuote();
    final var expectedQotd = QuoteGenerator.getQuoteOfTheDay();

    willReturn(10).given(quoteRepository).count();
    willThrow(QotdNotFoundException.class).given(qotdRepository).findByDate(any(LocalDate.class));
    willReturn(List.of(0,1,2,3,4,5,6,7,8,9)).given(quoteRepository).findAllIds();
    willReturn(expectedQotd).given(qotdRepository).save(any(QuoteOfTheDayRequest.class));
    willReturn(expectedQuote).given(quoteRepository).findById(anyInt());

    final var quote = instance.findQuoteOfTheDay();

    assertThat(quote).isNotNull();
    assertThat(quote.id()).isEqualTo(1);
  }

  @Test
  @DisplayName("Should count quotes")
  void shouldCountQuotes() {
    final var count = instance.count();

    assertThat(count).isNotNegative();
  }
}
