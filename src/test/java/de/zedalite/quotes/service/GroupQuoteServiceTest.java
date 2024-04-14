package de.zedalite.quotes.service;

import de.zedalite.quotes.data.model.PushNotification;
import de.zedalite.quotes.data.model.Quote;
import de.zedalite.quotes.data.model.QuoteMessage;
import de.zedalite.quotes.data.model.QuoteRequest;
import de.zedalite.quotes.exceptions.NotifierException;
import de.zedalite.quotes.exceptions.QuoteNotFoundException;
import de.zedalite.quotes.exceptions.ResourceNotFoundException;
import de.zedalite.quotes.fixtures.QuoteGenerator;
import de.zedalite.quotes.repository.GroupQuoteRepository;
import de.zedalite.quotes.repository.PushNotificationRepository;
import de.zedalite.quotes.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static de.zedalite.quotes.data.model.SortField.CREATION_DATE;
import static de.zedalite.quotes.data.model.SortOrder.DESC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class GroupQuoteServiceTest {

  @InjectMocks
  private GroupQuoteService instance;

  @Mock
  private GroupQuoteRepository repository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private PushNotificationRepository notifierRepository;

  @Test
  @DisplayName("Should create group quote")
  void shouldCreateGroupQuote() {
    final QuoteRequest quoteRequest = QuoteGenerator.getQuoteRequest();
    final Quote expectedQuote = QuoteGenerator.getQuote();
    willReturn(expectedQuote).given(repository).save(1, quoteRequest);

    instance.create(1, quoteRequest);

    then(repository).should().save(1, quoteRequest);
    then(notifierRepository).should().sendToTopic(any(), any(PushNotification.class));
  }

  @Test
  @DisplayName("Should create group quote with creator")
  void shouldCreateGroupQuoteWithCreator() {
    final QuoteRequest quoteRequest = QuoteGenerator.getQuoteRequest();
    final Quote expectedQuote = QuoteGenerator.getQuote();
    willReturn(expectedQuote).given(repository).save(1, quoteRequest);

    instance.create(1, quoteRequest, 2);

    then(repository).should().save(1, quoteRequest);
    then(notifierRepository).should().sendToTopic(any(), any(PushNotification.class));
  }

  @Test
  @DisplayName("Should create group quote with principal as creator")
  void shouldCreateGroupQuoteWithPrincipalAsCreator() {
    final QuoteRequest quoteRequest = QuoteGenerator.getQuoteRequest().withCreatorId(null);
    final Quote expectedQuote = QuoteGenerator.getQuote();
    willReturn(expectedQuote).given(repository).save(1, quoteRequest.withCreatorId(2));

    instance.create(1, quoteRequest, 2);

    then(repository).should().save(1, quoteRequest.withCreatorId(2));
    then(notifierRepository).should().sendToTopic(any(), any(PushNotification.class));
  }

  @Test
  @DisplayName("Should create group quote with failed notification")
  void shouldCreateGroupQuoteWithFailedNotification() {
    final QuoteRequest quoteRequest = QuoteGenerator.getQuoteRequest();
    final Quote expectedQuote = QuoteGenerator.getQuote();
    willReturn(expectedQuote).given(repository).save(1, quoteRequest);
    willThrow(NotifierException.class).given(notifierRepository).sendToTopic(any(), any(PushNotification.class));

    instance.create(1, quoteRequest, 2);

    then(repository).should().save(1, quoteRequest);
  }

  @Test
  @DisplayName("Should throw exception when group quote not created")
  void shouldThrowExceptionWhenGroupQuoteNotCreated() {
    final QuoteRequest quoteRequest = QuoteGenerator.getQuoteRequest();
    willThrow(QuoteNotFoundException.class).given(repository).save(1, quoteRequest);

    assertThatCode(() -> instance.create(1, quoteRequest)).isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  @DisplayName("Should find all group quotes")
  void shouldFindAllGroupQuotes() {
    final List<Quote> expectedQuotes = QuoteGenerator.getQuotes();
    willReturn(expectedQuotes).given(repository).findAll(1, CREATION_DATE, DESC);

    final List<QuoteMessage> quotes = instance.findAll(1, CREATION_DATE, DESC);

    assertThat(quotes).hasSize(expectedQuotes.size());
  }

  @Test
  @DisplayName("Should throw exception when group quotes not found")
  void shouldThrowExceptionWhenGroupQuotesNotFound() {
    willThrow(QuoteNotFoundException.class).given(repository).findAll(1, CREATION_DATE, DESC);

    assertThatCode(() -> instance.findAll(1, CREATION_DATE, DESC)).isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  @DisplayName("Should find group quote")
  void shouldFindGroupQuote() {
    final Quote expectedQuote = QuoteGenerator.getQuote();
    willReturn(expectedQuote).given(repository).findById(1, 2);
    
    instance.find(1, 2);
    
    then(repository).should().findById(1, 2);
  }

  @Test
  @DisplayName("Should throw exception when group quote not found")
  void shouldThrowExceptionWhenGroupQuoteNotFound() {
    willThrow(QuoteNotFoundException.class).given(repository).findById(1, 2);

    assertThatCode(() -> instance.find(1, 2)).isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  @DisplayName("Should find random group quotes")
  void shouldFindRandomGroupQuotes() {
    final List<Quote> expectedQuotes = QuoteGenerator.getQuotes();
    willReturn(expectedQuotes).given(repository).findRandoms(1, 3);

    final List<QuoteMessage> quotes = instance.findRandoms(1, 3);

    assertThat(quotes).hasSize(expectedQuotes.size());
  }

  @Test
  @DisplayName("Should throw exception when random group quotes not found")
  void shouldThrowExceptionWhenRandomGroupQuotesNotFound() {
    willThrow(QuoteNotFoundException.class).given(repository).findRandoms(1, 3);

    assertThatCode(() -> instance.findRandoms(1, 3)).isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  @DisplayName("Should count quotes")
  void shouldCountQuotes() {
    willReturn(5).given(repository).count(1);

    final Integer count = instance.count(1);

    assertThat(count).isEqualTo(5);
    then(repository).should().count(1);
  }
}
