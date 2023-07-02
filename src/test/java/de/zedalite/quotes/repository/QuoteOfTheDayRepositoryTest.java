package de.zedalite.quotes.repository;

import de.zedalite.quotes.TestEnvironmentProvider;
import de.zedalite.quotes.data.model.QuoteOfTheDayRequest;
import de.zedalite.quotes.data.model.QuoteRequest;
import de.zedalite.quotes.exceptions.QotdNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class QuoteOfTheDayRepositoryTest extends TestEnvironmentProvider {

  @Autowired
  private QuoteOfTheDayRepository instance;

  @Autowired
  private QuoteRepository quoteRepository;
  
  private Integer exampleQuoteId;

  @BeforeAll
  void setup() {
    exampleQuoteId = quoteRepository.save(new QuoteRequest("qotd", LocalDateTime.now(), "I'm the best", null,null)).id();
    instance.save(new QuoteOfTheDayRequest(exampleQuoteId, LocalDateTime.now()));
  }

  @Test
  @DisplayName("Should save quote of the day")
  void shouldSaveQuoteOfTheDay() {
    final var qotd = new QuoteOfTheDayRequest(exampleQuoteId, LocalDateTime.now());
    
    final var savedQotd = instance.save(qotd);
    
    assertThat(savedQotd).isNotNull();
    assertThat(savedQotd.quoteId()).isEqualTo(exampleQuoteId);
  }

  @Test
  @DisplayName("Should find quote of the day according to current date")
  void shouldFindQuoteOfTheDayAccordingToCurrentDate() {
    final var qotd = instance.findByDate(LocalDate.now());

    assertThat(qotd).isNotNull();
    assertThat(qotd.quoteId()).isEqualTo(exampleQuoteId);
  }

  @Test
  @DisplayName("Should throw exception if quote of the day not found")
  void shouldThrowExceptionIfQuoteOfTheDayNotFound() {
    final var futureDate = LocalDate.now().plusDays(5);

    assertThatCode(() -> instance.findByDate(futureDate)).isInstanceOf(QotdNotFoundException.class);
  }

}