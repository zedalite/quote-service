package de.zedalite.quotes.repository;

import static org.assertj.core.api.Assertions.assertThat;

import de.zedalite.quotes.TestEnvironmentProvider;
import de.zedalite.quotes.data.model.Quote;
import de.zedalite.quotes.data.model.QuoteRequest;
import de.zedalite.quotes.data.model.UserRequest;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class QuoteRepositoryTest extends TestEnvironmentProvider {

  @Autowired
  private QuoteRepository instance;

  @Autowired
  private UserRepository userRepository;

  @BeforeAll
  void setup() {
    final Integer userId = userRepository.save(new UserRequest("quotetester", "test")).id();
    final Integer userId2 = userRepository.save(new UserRequest("quotetester2", "test2")).id();

    instance.save(new QuoteRequest("quoter", LocalDateTime.now(), "quotes are cool", "in quotversum", null));
    instance.save(new QuoteRequest("quoter", LocalDateTime.now(), "One more quotes", "#2", userId));
    instance.save(new QuoteRequest("quotexpert", LocalDateTime.now(), "I'm an expert", null, userId2));
  }

  @Test
  @DisplayName("Should save quote")
  void shouldSaveQuote() {
    final LocalDateTime dateTime = LocalDateTime.of(2023, 5, 29, 21, 0, 0);
    final QuoteRequest quote = new QuoteRequest("test", dateTime, "tests are important", "42", 2);

    final Quote savedQuote = instance.save(quote);

    assertThat(savedQuote).isNotNull();
    assertThat(savedQuote.id()).isNotNull();
    assertThat(savedQuote.author()).isEqualTo("test");
    assertThat(savedQuote.creationDate()).isEqualTo(LocalDateTime.of(2023, 5, 29, 21, 0, 0));
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
