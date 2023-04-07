package de.zedproject.quotesapi.data.mapper;

import de.zedproject.jooq.quote.tables.records.QuotesRecord;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class QuoteMapperTest {

  private static final QuoteMapper instance = QuoteMapper.INSTANCE;

  @Test
  @DisplayName("Should map quote record to quote")
  void shouldMapQuoteRecordToQuote() {
    final var quoteRec = new QuotesRecord(0, "test", LocalDateTime.MIN, "Successful test.", "sub");

    final var quote = instance.quoteRecToQuote(quoteRec);

    assertThat(quote).isNotNull();
    assertThat(quote.id()).isZero();
    assertThat(quote.author()).isEqualTo("test");
    assertThat(quote.datetime()).isEqualTo(LocalDateTime.MIN);
    assertThat(quote.text()).isEqualTo("Successful test.");
    assertThat(quote.subtext()).isEqualTo(Optional.of("sub"));
  }

  @ParameterizedTest
  @DisplayName("Should map empty quote record to null")
  @NullSource
  void shouldMapEmptyQuoteRecordToNull(final QuotesRecord quotesRecord) {
    final var quote = instance.quoteRecToQuote(quotesRecord);

    assertThat(quote).isNull();
  }

  @Test
  @DisplayName("Should map quote records to quotes")
  void shouldMapQuoteRecordsToQuotes() {
    final var quoteRecSonar = new QuotesRecord(0, "sonar", LocalDateTime.MIN, "I like code coverage.", "sub");
    final var quoteRecMapper = new QuotesRecord(1, "mapper", LocalDateTime.MAX, "Mappers facilitate the work.", "sub");
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
    softly.assertThat(quoteSonar.subtext()).isEqualTo(Optional.of("sub"));

    softly.assertThat(quoteMapper).isNotNull();
    softly.assertThat(quoteMapper.id()).isEqualTo(1);
    softly.assertThat(quoteMapper.author()).isEqualTo("mapper");
    softly.assertThat(quoteMapper.datetime()).isEqualTo(LocalDateTime.MAX);
    softly.assertThat(quoteMapper.text()).isEqualTo("Mappers facilitate the work.");
    softly.assertThat(quoteMapper.subtext()).isEqualTo(Optional.of("sub"));

    softly.assertAll();
  }

  @ParameterizedTest
  @DisplayName("Should map empty quote records to null")
  @NullSource
  void shouldMapEmptyQuoteRecordsToNull(final List<QuotesRecord> quotesRecords) {
    final var quotes = instance.quoteRecsToQuotes(quotesRecords);

    assertThat(quotes).isNull();
  }

  @Test
  @DisplayName("Should map optional string")
  void shouldMapOptionalString() {
    final var string = "mapper";

    final var optionalString = instance.mapOptional(string);

    assertThat(optionalString).isEqualTo(Optional.of("mapper"));
  }

  @Test
  @DisplayName("Should map optional integer")
  void shouldMapOptionalInteger() {
    final var integer = Integer.valueOf(42);

    final var optionalInteger = instance.mapOptional(integer);

    assertThat(optionalInteger).isEqualTo(Optional.of(42));
  }

  @Test
  @DisplayName("Should map optional date")
  void shouldMapOptionalDate() {
    final var localDateTime = LocalDate.parse("2023-02-01").atStartOfDay();

    final var optionalLocalDateTime = instance.mapOptional(localDateTime);

    assertThat(optionalLocalDateTime).isEqualTo(Optional.of(LocalDate.parse("2023-02-01").atStartOfDay()));
  }

  @ParameterizedTest
  @DisplayName("Should map optional empty")
  @NullSource
  void shouldMapOptionalEmpty(final Object object) {
    final var optionalObject = instance.mapOptional(object);

    assertThat(optionalObject).isEmpty();
  }
}