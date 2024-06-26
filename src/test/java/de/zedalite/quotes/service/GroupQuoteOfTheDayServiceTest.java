package de.zedalite.quotes.service;

import de.zedalite.quotes.data.model.Quote;
import de.zedalite.quotes.data.model.QuoteMessage;
import de.zedalite.quotes.data.model.QuoteOfTheDayRequest;
import de.zedalite.quotes.exceptions.QotdNotFoundException;
import de.zedalite.quotes.exceptions.ResourceNotFoundException;
import de.zedalite.quotes.fixtures.QuoteGenerator;
import de.zedalite.quotes.repository.GroupQuoteOfTheDayRepository;
import de.zedalite.quotes.repository.GroupQuoteRepository;
import de.zedalite.quotes.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class GroupQuoteOfTheDayServiceTest {

  @InjectMocks
  private GroupQuoteOfTheDayService instance;

  @Mock
  private GroupQuoteOfTheDayRepository repository;

  @Mock
  private GroupQuoteRepository groupQuoteRepository;

  @Mock
  private UserRepository userRepository;


  @Test
  @DisplayName("Should find quote of the day")
  void shouldFindQuoteOfTheDay() {
    final Quote expectedQotd = QuoteGenerator.getQuote();

    willReturn(10).given(groupQuoteRepository).count(anyInt());
    willReturn(expectedQotd).given(repository).findByDate(anyInt(), any(LocalDate.class));

    final QuoteMessage quoteOfTheDay = instance.findQuoteOfTheDay(1);

    assertThat(quoteOfTheDay).isNotNull();
    assertThat(quoteOfTheDay.id()).isEqualTo(expectedQotd.id());
  }

  @Test
  @DisplayName("Should throw exception when minimum quotes not reached")
  void shouldThrowExceptionWhenMinimumQuotesNotReached() {
    willReturn(9).given(groupQuoteRepository).count(anyInt());

    assertThatCode(() -> instance.findQuoteOfTheDay(1)).isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  @DisplayName("Should find new qotd when no current qotd set")
  void shouldFindNewQotdWhenNoCurrentQotdSet() {
    final Quote expectedQotd = QuoteGenerator.getQuote();

    willReturn(10).given(groupQuoteRepository).count(anyInt());
    willThrow(QotdNotFoundException.class).given(repository).findByDate(anyInt(), any(LocalDate.class));
    willReturn(List.of(expectedQotd)).given(groupQuoteRepository).findRandoms(anyInt(), anyInt());

    final QuoteMessage quoteOfTheDay = instance.findQuoteOfTheDay(1);

    then(repository).should().save(expectedQotd.id(), new QuoteOfTheDayRequest(expectedQotd.id(), LocalDate.now()));

    assertThat(quoteOfTheDay).isNotNull();
    assertThat(quoteOfTheDay.id()).isEqualTo(expectedQotd.id());
  }
}
