package de.zedalite.quotes.web;

import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willReturn;

import de.zedalite.quotes.service.QuoteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class QuoteControllerTest {

  @InjectMocks
  private QuoteController instance;

  @Mock
  private QuoteService service;

  @Mock
  private SecurityContextHolder securityContextHolder;

  @Test
  @DisplayName("Should get quote count")
  void shouldGetQuoteCount() {
    willReturn(5).given(service).count();

    instance.getQuotesCount();

    then(service).should().count();
  }
}
