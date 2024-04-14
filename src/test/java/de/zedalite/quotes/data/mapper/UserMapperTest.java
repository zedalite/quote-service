package de.zedalite.quotes.data.mapper;

import de.zedalite.quotes.data.jooq.users.tables.records.UsersRecord;
import de.zedalite.quotes.data.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

  private static final UserMapper instance = UserMapper.INSTANCE;

  @Test
  @DisplayName("Should map userRecord to user")
  void shouldMapUserRecordToUser() {
    final UsersRecord userRec = new UsersRecord(0, "user", "email@email.net", "USER", LocalDateTime.MIN);

    final User user = instance.mapToUser(userRec);

    assertThat(user).isNotNull();
    assertThat(user.id()).isEqualTo(userRec.getId());
    assertThat(user.name()).isEqualTo(userRec.getName());
    assertThat(user.email()).isEqualTo(userRec.getEmail());
    assertThat(user.creationDate()).isEqualTo(userRec.getCreationDate());
    assertThat(user.displayName()).isEqualTo(userRec.getDisplayName());
  }

  @ParameterizedTest
  @DisplayName("Should map empty userRecord to null")
  @NullSource
  void shouldMapEmptyUserRecordToNull(final UsersRecord usersRecord) {
    final User user = instance.mapToUser(usersRecord);

    assertThat(user).isNull();
  }
}
