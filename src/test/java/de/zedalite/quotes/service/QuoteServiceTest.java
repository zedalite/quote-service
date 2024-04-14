package de.zedalite.quotes.service;

import static org.assertj.core.api.Assertions.assertThat;

import de.zedalite.quotes.repository.QuoteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class QuoteServiceTest {

  @InjectMocks
  private QuoteService instance;

  @Mock
  private QuoteRepository quoteRepository;

  @Test
  @DisplayName("Should count quotes")
  void shouldCountQuotes() {
    final Integer count = instance.count();

    assertThat(count).isNotNegative();
  }
}
