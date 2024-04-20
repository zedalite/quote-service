package de.zedalite.quotes.repository;

import static org.assertj.core.api.Assertions.assertThat;

import de.zedalite.quotes.TestEnvironmentProvider;
import de.zedalite.quotes.data.model.Quote;
import de.zedalite.quotes.data.model.QuoteRequest;
import de.zedalite.quotes.data.model.UserRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource("classpath:test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class QuoteRepositoryTest extends TestEnvironmentProvider {

  @Autowired
  private QuoteRepository instance;

  @Autowired
  private UserRepository userRepository;

  @BeforeAll
  void setup() {
    final Integer userId = userRepository.save(new UserRequest("quotetester", "test", "Quote Tester")).id();
    final Integer userId2 = userRepository.save(new UserRequest("quotetester2", "test2", "Quote Tester 2")).id();

    instance.save(new QuoteRequest("quoter", "quotes are cool", "in quotversum"), null);
    instance.save(new QuoteRequest("quoter", "One more quotes", "#2"), userId);
    instance.save(new QuoteRequest("quotexpert", "I'm an expert", null), userId2);
  }

  @Test
  @DisplayName("Should save quote")
  void shouldSaveQuote() {
    final QuoteRequest quote = new QuoteRequest("test", "tests are important", "42");

    final Quote savedQuote = instance.save(quote, 2);

    assertThat(savedQuote).isNotNull();
    assertThat(savedQuote.id()).isNotNull();
    assertThat(savedQuote.author()).isEqualTo("test");
    assertThat(savedQuote.text()).isEqualTo("tests are important");
    assertThat(savedQuote.context()).isEqualTo("42");
  }

  @Test
  @DisplayName("Should find quote count")
  void shouldFindQuoteCount() {
    final Integer quoteCount = instance.count();

    assertThat(quoteCount).isGreaterThanOrEqualTo(3);
  }
}
