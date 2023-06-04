package de.zedproject.quotesapi.web;

import de.zedproject.quotesapi.data.model.Quote;
import de.zedproject.quotesapi.data.model.QuoteRequest;
import de.zedproject.quotesapi.data.model.SortField;
import de.zedproject.quotesapi.data.model.SortOrder;
import de.zedproject.quotesapi.service.QuoteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static de.zedproject.quotesapi.data.model.SortField.DATETIME;
import static de.zedproject.quotesapi.data.model.SortOrder.ASC;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willReturn;

@ExtendWith(MockitoExtension.class)
class QuoteControllerTest {

  @InjectMocks
  private QuoteController instance;

  @Mock
  private QuoteService service;

  @Test
  @DisplayName("Should get quotes")
  void shouldGetQuotes() {
    final var expectedQuotes = List.of(
      new Quote(1, "tester", LocalDateTime.now(), "text", null),
      new Quote(2, "tester", LocalDateTime.now(), "second text", null));
    willReturn(expectedQuotes).given(service).findAll(any(SortField.class), any(SortOrder.class));

    instance.getQuotes(DATETIME, ASC);

    then(service).should().findAll(any(SortField.class), any(SortOrder.class));
  }

  @Test
  @DisplayName("Should get quote")
  void shouldGetQuote() {
    final var expectedQuote = new Quote(1, "tester", LocalDateTime.now(), "text", null);
    willReturn(expectedQuote).given(service).find(anyInt());

    instance.getQuote(1);

    then(service).should().find(1);
  }

  @Test
  @DisplayName("Should get random quotes")
  void shouldGetRandomQuotes() {
    final var expectedQuotes = List.of(
      new Quote(1, "tester", LocalDateTime.now(), "text", null),
      new Quote(2, "tester", LocalDateTime.now(), "second text", null));
    willReturn(expectedQuotes).given(service).findRandoms(anyInt());

    instance.getRandomQuotes(2);

    then(service).should().findRandoms(2);
  }

  @Test
  @DisplayName("Should get random quote")
  void shouldGetRandomQuote() {
    final var expectedQuote = new Quote(1, "tester", LocalDateTime.now(), "text", null);
    willReturn(expectedQuote).given(service).findRandom();

    instance.getRandomQuote();

    then(service).should().findRandom();
  }

  @Test
  @DisplayName("Should get quote of the day")
  void shouldGetQuoteOfTheDay() {
    final var expectedQuote = new Quote(1, "tester", LocalDateTime.now(), "text", null);
    willReturn(expectedQuote).given(service).findQuoteOfTheDay();

    instance.getQuoteOfTheDay();

    then(service).should().findQuoteOfTheDay();
  }

  @Test
  @DisplayName("Should get quote count")
  void shouldGetQuoteCount() {
    willReturn(5).given(service).count();

    instance.getQuotesCount();

    then(service).should().count();
  }

  @Test
  @DisplayName("Should post quote")
  void shouldPostQuote() {
    final var quoteRequest = new QuoteRequest("tester", LocalDateTime.now(), "text", null);
    final var expectedQuote = new Quote(1, "tester", LocalDateTime.now(), "text", null);
    willReturn(expectedQuote).given(service).create(any(QuoteRequest.class));

    instance.postQuote(quoteRequest);

    then(service).should().create(quoteRequest);
  }

  @Test
  @DisplayName("Should put quote")
  void shouldPutQuote() {
    final var quoteRequest = new QuoteRequest("tester", LocalDateTime.now(), "new text", "cool");
    final var expectedQuote = new Quote(1, "tester", LocalDateTime.now(), "new text", "cool");
    willReturn(expectedQuote).given(service).update(anyInt(), any(QuoteRequest.class));

    instance.putQuote(1, quoteRequest);

    then(service).should().update(1, quoteRequest);
  }

  @Test
  @DisplayName("Should delete quote")
  void shouldDeleteQuote() {
    final var expectedQuote = new Quote(1, "tester", LocalDateTime.now(), "text", null);
    willReturn(expectedQuote).given(service).delete(anyInt());

    instance.deleteQuote(1);

    then(service).should().delete(1);
  }
}
