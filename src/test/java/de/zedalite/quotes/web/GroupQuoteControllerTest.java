package de.zedalite.quotes.web;

import de.zedalite.quotes.data.model.QuoteRequest;
import de.zedalite.quotes.data.model.SortField;
import de.zedalite.quotes.data.model.SortOrder;
import de.zedalite.quotes.fixtures.QuoteGenerator;
import de.zedalite.quotes.fixtures.UserGenerator;
import de.zedalite.quotes.service.GroupQuoteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willReturn;

@ExtendWith(MockitoExtension.class)
class GroupQuoteControllerTest {

  @InjectMocks
  private GroupQuoteController instance;

  @Mock
  private GroupQuoteService service;

  @Test
  @DisplayName("Should get group quotes")
  void shouldGetGroupQuotes() {
    final var expectedQuotes = QuoteGenerator.getQuoteMessages();
    willReturn(expectedQuotes).given(service).findAll(anyInt(), any(SortField.class), any(SortOrder.class));

    instance.getQuotes(1, SortField.CREATION_DATE, SortOrder.ASC);

    then(service).should().findAll(1, SortField.CREATION_DATE, SortOrder.ASC);
  }

  @Test
  @DisplayName("Should get group quote")
  void shouldGetGroupQuote() {
    final var expectedQuote = QuoteGenerator.getQuoteMessage();
    willReturn(expectedQuote).given(service).find(anyInt(), anyInt());

    instance.getQuote(1, 8);

    then(service).should().find(1, 8);
  }

  @Test
  @DisplayName("Should get quote count")
  void shouldGetQuoteCount() {
    willReturn(5).given(service).count(anyInt());

    instance.getQuotesCount(1);

    then(service).should().count(1);
  }

  @Test
  @DisplayName("Should post group quote")
  void shouldPostGroupQuote() {
    final var quoteRequest = QuoteGenerator.getQuoteRequest();
    final var principal = UserGenerator.getUserPrincipal();
    final var expectedQuote = QuoteGenerator.getQuoteMessage();
    willReturn(expectedQuote).given(service).create(anyInt(), any(QuoteRequest.class), anyInt());

    instance.postQuote(1, quoteRequest, principal);

    then(service).should().create(1, quoteRequest, 1);
  }

  @Test
  @DisplayName("Should get random group quotes")
  void shouldGetRandomGroupQuotes() {
    final var expectedQuote = QuoteGenerator.getQuoteMessages();
    willReturn(expectedQuote).given(service).findRandoms(anyInt(), anyInt());

    instance.getRandomQuotes(1, 8);

    then(service).should().findRandoms(1, 8);
  }
}
