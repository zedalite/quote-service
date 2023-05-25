package de.zedproject.quotesapi.data.mapper;

import de.zedproject.jooq.tables.records.UsersRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

  private static final UserMapper instance = UserMapper.INSTANCE;

  @Test
  @DisplayName("Should map userRecord to user")
  void shouldMapUserRecordToUser() {
    final var userRec = new UsersRecord(0, "user", "password");

    final var user = instance.userRecToUser(userRec);

    assertThat(user).isNotNull();
    assertThat(user.id()).isZero();
    assertThat(user.name()).isEqualTo("user");
    assertThat(user.password()).isEqualTo("password");
  }

  @ParameterizedTest
  @DisplayName("Should map empty userRecord to null")
  @NullSource
  void shouldMapEmptyUserRecordToNull(final UsersRecord usersRecord) {
    final var user = instance.userRecToUser(usersRecord);

    assertThat(user).isNull();
  }
}
