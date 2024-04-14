package de.zedalite.quotes.data.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import de.zedalite.quotes.data.model.SortField;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class StringToSortFieldConverterTest {

  private final StringToSortFieldConverter instance = new StringToSortFieldConverter();

  @ParameterizedTest(name = "Convert \"{0}\" to {1}")
  @DisplayName("Should convert string to sortField")
  @CsvSource({ "author,AUTHOR", "creationDate,CREATION_DATE", "text,TEXT" })
  void shouldConvertStringToSortField(final String source, final SortField expected) {
    assertThat(instance.convert(source)).isEqualTo(expected);
  }

  @Test
  @DisplayName("Should throw Exception on invalid input")
  void shouldThrowExceptionOnInvalidInput() {
    assertThatThrownBy(() -> instance.convert("invalid")).isInstanceOf(IllegalArgumentException.class);
  }
}
