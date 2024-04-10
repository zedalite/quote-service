package de.zedalite.quotes.scheduling;

import de.zedalite.quotes.service.GroupQuoteOfTheDayService;
import de.zedalite.quotes.service.GroupService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.then;

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
    instance.resetQuoteOfTheDay();

    then(groupService).should().findAllIds();
  }
}
