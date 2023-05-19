package de.zedproject.quotesapi.data.mapper;

import de.zedproject.jooq.tables.records.QuotesRecord;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class QuoteMapperTest {

  private static final QuoteMapper instance = QuoteMapper.INSTANCE;

  @Test
  @DisplayName("Should map quoteRecord to quote")
  void shouldMapQuoteRecordToQuote() {
    final var quoteRec = new QuotesRecord(0, "test", LocalDateTime.MIN, "Successful test.", "sub");

    final var quote = instance.quoteRecToQuote(quoteRec);

    assertThat(quote).isNotNull();
    assertThat(quote.id()).isZero();
    assertThat(quote.author()).isEqualTo("test");
    assertThat(quote.datetime()).isEqualTo(LocalDateTime.MIN);
    assertThat(quote.text()).isEqualTo("Successful test.");
    assertThat(quote.subtext()).isEqualTo("sub");
  }

  @ParameterizedTest
  @DisplayName("Should map empty quoteRecord to null")
  @NullSource
  void shouldMapEmptyQuoteRecordToNull(final QuotesRecord quotesRecord) {
    final var quote = instance.quoteRecToQuote(quotesRecord);

    assertThat(quote).isNull();
  }

  @Test
  @DisplayName("Should map quoteRecords to quotes")
  void shouldMapQuoteRecordsToQuotes() {
    final var quoteRecSonar = new QuotesRecord(0, "sonar", LocalDateTime.MIN, "I like code coverage.", "sub");
    final var quoteRecMapper = new QuotesRecord(1, "mapper", LocalDateTime.MAX, "Mappers facilitate the work.", null);
    final var quotesRecords = List.of(quoteRecSonar, quoteRecMapper);

    final var quotes = instance.quoteRecsToQuotes(quotesRecords);

    final var quoteSonar = quotes.get(0);
    final var quoteMapper = quotes.get(1);

    final var softly = new SoftAssertions();

    softly.assertThat(quoteSonar).isNotNull();
    softly.assertThat(quoteSonar.id()).isZero();
    softly.assertThat(quoteSonar.author()).isEqualTo("sonar");
    softly.assertThat(quoteSonar.datetime()).isEqualTo(LocalDateTime.MIN);
    softly.assertThat(quoteSonar.text()).isEqualTo("I like code coverage.");
    softly.assertThat(quoteSonar.subtext()).isEqualTo("sub");

    softly.assertThat(quoteMapper).isNotNull();
    softly.assertThat(quoteMapper.id()).isEqualTo(1);
    softly.assertThat(quoteMapper.author()).isEqualTo("mapper");
    softly.assertThat(quoteMapper.datetime()).isEqualTo(LocalDateTime.MAX);
    softly.assertThat(quoteMapper.text()).isEqualTo("Mappers facilitate the work.");
    softly.assertThat(quoteMapper.subtext()).isNull();

    softly.assertAll();
  }

  @ParameterizedTest
  @DisplayName("Should map empty quoteRecords to null")
  @NullSource
  void shouldMapEmptyQuoteRecordsToNull(final List<QuotesRecord> quotesRecords) {
    final var quotes = instance.quoteRecsToQuotes(quotesRecords);

    assertThat(quotes).isNull();
  }
}