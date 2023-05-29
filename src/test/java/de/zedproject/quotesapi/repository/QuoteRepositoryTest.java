package de.zedproject.quotesapi.repository;

import de.zedproject.quotesapi.DatabaseContainerBaseTest;
import de.zedproject.quotesapi.data.model.Quote;
import de.zedproject.quotesapi.data.model.QuoteRequest;
import de.zedproject.quotesapi.exceptions.QuoteNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import static de.zedproject.quotesapi.data.model.SortField.DATETIME;
import static de.zedproject.quotesapi.data.model.SortOrder.ASC;
import static de.zedproject.quotesapi.data.model.SortOrder.DESC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class QuoteRepositoryTest extends DatabaseContainerBaseTest {

  @Autowired
  private QuoteRepository instance;

  @BeforeAll
  void setup() {
    instance.save(new QuoteRequest("quoter", LocalDateTime.now(), "quotes are cool", "in quotversum"));
    instance.save(new QuoteRequest("quoter", LocalDateTime.now(), "One more quotes", "#2"));
    instance.save(new QuoteRequest("quotexpert", LocalDateTime.now(), "I'm an expert", null));
  }

  @Test
  @DisplayName("Should save quote")
  void shouldSaveQuote() {
    final var dateTime = LocalDateTime.of(2023, 5, 29, 21, 0,0);
    final var quote = new QuoteRequest("test", dateTime, "tests are important", "42");

    final var savedQuote = instance.save(quote);

    assertThat(savedQuote).isNotNull();
    assertThat(savedQuote.id()).isNotNull();
    assertThat(savedQuote.author()).isEqualTo("test");
    assertThat(savedQuote.datetime()).isEqualTo(LocalDateTime.of(2023, 5, 29, 21, 0,0));
    assertThat(savedQuote.text()).isEqualTo("tests are important");
    assertThat(savedQuote.subtext()).isEqualTo("42");
  }

  @Test
  @DisplayName("Should find all quotes")
  void shouldFindAllQuotes() {

    final var quotes = instance.findAll();

    assertThat(quotes).hasSizeGreaterThanOrEqualTo(3);
  }

  @Test
  @DisplayName("Should find all quotes by ids")
  void shouldFindAllQuotesByIds() {
    final var quotes = instance.findAllByIds(List.of(1,3));

    assertThat(quotes).map(Quote::id).containsAll(List.of(1,3)).doesNotContain(2);
  }

  @Test
  @DisplayName("Should find all quote ids")
  void shouldFindAllQuoteIds() {
    final var quotes = instance.findAllIds();

    assertThat(quotes).containsAll(List.of(1,2,3));
  }

  @Test
  @DisplayName("Should find quote by id")
  void shouldFindQuoteById() {
    final var quote = instance.findById(1);

    assertThat(quote.id()).isEqualTo(1);
  }

  @Test
  @DisplayName("Should update quote")
  void shouldUpdateQuote() {
    final var newQuote = new QuoteRequest("quoter", LocalDateTime.now(), "quotes are awesome", "in quotversum");
    final var updatedQuote = instance.update(1, newQuote);

    assertThat(updatedQuote.id()).isEqualTo(1);
    assertThat(updatedQuote.text()).isEqualTo("quotes are awesome");
  }

  @Test
  @DisplayName("Should delete quote")
  void shouldDeleteQuote() {
    final var quote = instance.save(new QuoteRequest("unstable", LocalDateTime.now(), "I'll be deleted", null));
    final var quoteId = quote.id();

    final var deletedQuote = instance.delete(quoteId);

    assertThat(deletedQuote.id()).isEqualTo(quoteId);
    assertThatCode(() -> instance.findById(quoteId)).isInstanceOf(QuoteNotFoundException.class);
  }

  @Test
  @DisplayName("Should find quote count")
  void shouldFindQuoteCount() {
    final var quoteCount = instance.count();

    assertThat(quoteCount).isGreaterThanOrEqualTo(3);
  }

  @Test
  @DisplayName("Should throw Exception when quote not found")
  void shouldThrowExceptionWhenQuoteNotFound() {
    final var quote = new QuoteRequest("pseudo", LocalDateTime.now(), "to be filled", null);

    assertThatCode(() -> instance.findById(99999)).isInstanceOf(QuoteNotFoundException.class);
    assertThatCode(() -> instance.update(77777, quote)).isInstanceOf(QuoteNotFoundException.class);
    assertThatCode(() -> instance.delete(55555)).isInstanceOf(QuoteNotFoundException.class);
  }

  @Test
  @DisplayName("Should find quotes in sorted order")
  void shouldFindQuotesInSortedOrder() {
    final var sortedQuotesAsc = instance.findAll(DATETIME, ASC);
    final var sortedQuotesDesc = instance.findAll(DATETIME, DESC);

    assertThat(sortedQuotesAsc).map(Quote::datetime).isSortedAccordingTo(Comparator.naturalOrder());
    assertThat(sortedQuotesDesc).map(Quote::datetime).isSortedAccordingTo(Comparator.reverseOrder());
  }
}