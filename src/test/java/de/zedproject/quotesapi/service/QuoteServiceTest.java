package de.zedproject.quotesapi.service;

import de.zedproject.quotesapi.data.model.Quote;
import de.zedproject.quotesapi.data.model.QuoteRequest;
import de.zedproject.quotesapi.data.model.SortField;
import de.zedproject.quotesapi.data.model.SortOrder;
import de.zedproject.quotesapi.exceptions.QuoteNotFoundException;
import de.zedproject.quotesapi.exceptions.ResourceNotFoundException;
import de.zedproject.quotesapi.repository.QuoteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
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

  @Test
  @DisplayName("Should create quote")
  void shouldCreateQuote() {
    final var quoteRequest = new QuoteRequest("tester", LocalDateTime.now(), "text", null);
    instance.create(quoteRequest);

    then(quoteRepository).should().save(quoteRequest);
  }

  @Test
  @DisplayName("Should throw exception when quote not created")
  void shouldThrowExceptionWhenQuoteNotCreated() {
    final var quoteRequest = new QuoteRequest("tester", LocalDateTime.now(), "text", null);
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
    final var expectedQuotes = List.of(
      new Quote(1, "tester", LocalDateTime.now(), "text", null),
      new Quote(2, "tester", LocalDateTime.now(), "second text", null));
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
    final var expectedQuotes = List.of(
      new Quote(1, "tester", LocalDateTime.now(), "text", null),
      new Quote(2, "tester", LocalDateTime.now(), "second text", null));
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
    final var expectedQuotes = List.of(
      new Quote(1, "tester", LocalDateTime.now(), "text", null),
      new Quote(2, "tester", LocalDateTime.now(), "second text", null));
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
    final var expectedQuote = new Quote(1, "tester", LocalDateTime.now(), "text", null);
    willReturn(expectedQuote).given(quoteRepository).findById(anyInt());

    final var quote = instance.find(1);

    assertThat(quote.id()).isEqualTo(expectedQuote.id());
  }

  @Test
  @DisplayName("Should update quote")
  void shouldUpdateQuote() {
    final var expectedQuote = new Quote(1, "tester", LocalDateTime.now(), "new text", null);
    willReturn(expectedQuote).given(quoteRepository).update(anyInt(), any(QuoteRequest.class));

    final var updateQuoteRequest = new QuoteRequest("tester", LocalDateTime.now(), "new text", null);
    final var updatedQuote = instance.update(1, updateQuoteRequest);

    assertThat(updatedQuote.id()).isEqualTo(expectedQuote.id());
    assertThat(updatedQuote.text()).isEqualTo("new text");
  }

  @Test
  @DisplayName("Should throw exception when quote to be update does not exists")
  void shouldThrowExceptionWhenQuoteToBeUpdateDoesNotExists() {
    willThrow(QuoteNotFoundException.class).given(quoteRepository).update(anyInt(), any(QuoteRequest.class));

    final var updateQuoteRequest = new QuoteRequest("tester", LocalDateTime.now(), "new text", null);
    assertThatCode(() -> instance.update(1, updateQuoteRequest)).isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  @DisplayName("Should delete quote")
  void shouldDeleteQuote() {
    final var expectedQuote = new Quote(1, "tester", LocalDateTime.now(), "text", null);
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
    final var expectedQuote = new Quote(1, "tester", LocalDateTime.now(), "new text", null);
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
  @DisplayName("Should count quotes")
  void shouldCountQuotes() {
    final var count = instance.count();

    assertThat(count).isNotNegative();
  }
}
