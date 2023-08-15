package de.zedalite.quotes.web;

import de.zedalite.quotes.auth.UserPrincipal;
import de.zedalite.quotes.data.model.QuoteRequest;
import de.zedalite.quotes.data.model.SortField;
import de.zedalite.quotes.data.model.SortOrder;
import de.zedalite.quotes.data.model.User;
import de.zedalite.quotes.fixtures.QuoteGenerator;
import de.zedalite.quotes.service.QuoteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willReturn;

@ExtendWith(MockitoExtension.class)
class QuoteControllerTest {

  @InjectMocks
  private QuoteController instance;

  @Mock
  private QuoteService service;

  @Mock
  private SecurityContextHolder securityContextHolder;

  @Test
  @DisplayName("Should get quotes")
  void shouldGetQuotes() {
    final var expectedQuotes = QuoteGenerator.getQuotes();
    willReturn(expectedQuotes).given(service).findAll(any(SortField.class), any(SortOrder.class));

    instance.getQuotes(SortField.CREATION_DATE, SortOrder.ASC);

    then(service).should().findAll(any(SortField.class), any(SortOrder.class));
  }

  @Test
  @DisplayName("Should get quote")
  void shouldGetQuote() {
    final var expectedQuote = QuoteGenerator.getQuote();
    BDDMockito.willReturn(expectedQuote).given(service).find(anyInt());

    instance.getQuote(1);

    then(service).should().find(1);
  }

  @Test
  @DisplayName("Should get random quotes")
  void shouldGetRandomQuotes() {
    final var expectedQuotes = QuoteGenerator.getQuotes();
    willReturn(expectedQuotes).given(service).findRandoms(anyInt());

    instance.getRandomQuotes(2);

    then(service).should().findRandoms(2);
  }

  @Test
  @DisplayName("Should get random quote")
  void shouldGetRandomQuote() {
    final var expectedQuote = QuoteGenerator.getQuote();
    BDDMockito.willReturn(expectedQuote).given(service).findRandom();

    instance.getRandomQuote();

    then(service).should().findRandom();
  }

  @Test
  @DisplayName("Should get quote of the day")
  void shouldGetQuoteOfTheDay() {
    final var expectedQuote = QuoteGenerator.getQuote();
    BDDMockito.willReturn(expectedQuote).given(service).findQuoteOfTheDay();

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
    final var quoteRequest = QuoteGenerator.getQuoteRequest();
    final var expectedQuote = QuoteGenerator.getQuote();
    final var expectedUserDetails = new UserPrincipal(new User(1, "tester", "test", "TESTER", LocalDateTime.now()));
    final var authentication = new UsernamePasswordAuthenticationToken(expectedUserDetails, null, expectedUserDetails.getAuthorities());

    BDDMockito.willReturn(expectedQuote).given(service).create(any(QuoteRequest.class), anyString());
    SecurityContextHolder.getContext().setAuthentication(authentication);

    instance.postQuote(quoteRequest);

    then(service).should().create(quoteRequest, "tester");
  }

  @Test
  @DisplayName("Should put quote")
  void shouldPutQuote() {
    final var quoteRequest = QuoteGenerator.getQuoteRequest();
    final var expectedQuote = QuoteGenerator.getQuote();
    final var expectedUserDetails = new UserPrincipal(new User(1, "tester", "test", "TESTER", LocalDateTime.now()));
    final var authentication = new UsernamePasswordAuthenticationToken(expectedUserDetails, null, expectedUserDetails.getAuthorities());

    BDDMockito.willReturn(expectedQuote).given(service).update(anyInt(), any(QuoteRequest.class), anyString());
    SecurityContextHolder.getContext().setAuthentication(authentication);

    instance.putQuote(1, quoteRequest);

    then(service).should().update(1, quoteRequest, "tester");
  }

  @Test
  @DisplayName("Should delete quote")
  void shouldDeleteQuote() {
    final var expectedQuote = QuoteGenerator.getQuote();
    BDDMockito.willReturn(expectedQuote).given(service).delete(anyInt());

    instance.deleteQuote(1);

    then(service).should().delete(1);
  }
}
