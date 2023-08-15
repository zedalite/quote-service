package de.zedalite.quotes.data.mapper;

import de.zedalite.quotes.data.jooq.tables.records.QuotesOfTheDayRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class QuoteOfTheDayMapperTest {

  private static final QuoteOfTheDayMapper instance = QuoteOfTheDayMapper.INSTANCE;

  @Test
  @DisplayName("Should map quoteOfTheDayRecord to quoteOfTheDay")
  void shouldMapQuoteOfTheDayRecordToQuoteOfTheDay() {
    final var qotdRec = new QuotesOfTheDayRecord(0, 0, LocalDateTime.MIN);

    final var qotd = instance.toQuoteOfTheDay(qotdRec);

    assertThat(qotd).isNotNull();
    assertThat(qotd.id()).isZero();
    assertThat(qotd.quoteId()).isZero();
    assertThat(qotd.creationDate()).isEqualTo(LocalDateTime.MIN);
  }

  @ParameterizedTest
  @DisplayName("Should map empty quoteOfTheDayRecord to null")
  @NullSource
  void shouldMapEmptyQuoteOfTheDayRecordToNull(final QuotesOfTheDayRecord qotdRecord) {
    final var qotd = instance.toQuoteOfTheDay(qotdRecord);

    assertThat(qotd).isNull();
  }
}
