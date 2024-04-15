package de.zedalite.quotes.scheduling;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import de.zedalite.quotes.exception.ResourceNotFoundException;
import de.zedalite.quotes.service.GroupQuoteOfTheDayService;
import de.zedalite.quotes.service.GroupService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class QuoteOfTheDaySchedulerTest {

  @InjectMocks
  private QuoteOfTheDayScheduler instance;

  @Mock
  private GroupService groupService;

  @Mock
  private GroupQuoteOfTheDayService groupQuoteOfTheDayService;

  @Test
  @DisplayName("Should reset quoteOfTheDay")
  void shouldResetQuoteOfTheDay() {
    final List<Integer> allIds = List.of(1, 2);
    given(groupService.findAllIds()).willReturn(allIds);

    instance.resetQuoteOfTheDay();

    then(groupQuoteOfTheDayService).should(times(2)).findQuoteOfTheDay(anyInt());
  }

  @Test
  @DisplayName("Should ignore ResourceException")
  void shouldIgnoreResourceException() {
    final List<Integer> allIds = List.of(1, 2);
    given(groupService.findAllIds()).willReturn(allIds);
    given(groupQuoteOfTheDayService.findQuoteOfTheDay(anyInt())).willThrow(ResourceNotFoundException.class);

    assertThatCode(() -> instance.resetQuoteOfTheDay()).doesNotThrowAnyException();
  }
}
