package de.zedalite.quotes.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class StringUtilsTest {

  @Test
  @DisplayName("Should throw when constructor called")
  void shouldThrowWhenConstructorCalled() {
    assertThatCode(StringUtils::new).isInstanceOf(IllegalStateException.class);
  }

  @Test
  @DisplayName("Should extract user ids")
  void shouldExtractUserIds() {
    final var text = "<@2>The quick <@2313> brown fox <@jumps> <@-1>over the la<@13>zy dog.<@3>";

    final var userIds = StringUtils.extractUserIds(text);

    assertThat(userIds).containsAll(List.of(2,2313,13,3));
  }

  @Test
  @DisplayName("Should return empty list when no user ids found")
  void shouldReturnEmptyListWhenNoUserIdsFound() {
    final var text = "The quick brown fox jumps over the lazy dog.";

    final var userIds = StringUtils.extractUserIds(text);

    assertThat(userIds).isEmpty();
  }

  @Test
  @DisplayName("Should handle too large numbers")
  void shouldHandleTooLargeNumbers() {
    final var text = "<@2147483648>";

    final var userIds = StringUtils.extractUserIds(text);

    assertThat(userIds).isEmpty();
  }

}
