package de.zedproject.quotesapi.data.mapper;

import de.zedproject.quotesapi.data.model.SortOrder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StringToSortOrderConverterTest {

  private final StringToSortOrderConverter instance = new StringToSortOrderConverter();

  @ParameterizedTest(name = "Convert \"{0}\" to {1}")
  @DisplayName("Should convert string to sortOrder")
  @CsvSource({"asc,ASC", "desc,DESC"})
  void shouldConvertStringToSortField(final String source, final SortOrder expected) {
    assertThat(instance.convert(source)).isEqualTo(expected);
  }

  @Test
  @DisplayName("Should throw Exception on invalid input")
  void shouldThrowExceptionOnInvalidInput() {
    assertThatThrownBy(() -> instance.convert("invalid")).isInstanceOf(IllegalArgumentException.class);
  }
}
