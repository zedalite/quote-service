package de.zedproject.quotesapi.repository;

import de.zedproject.quotesapi.DatabaseContainerBaseTest;
import de.zedproject.quotesapi.data.model.UserRequest;
import de.zedproject.quotesapi.exceptions.UserNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.TestInstance.Lifecycle;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class UserRepositoryTest extends DatabaseContainerBaseTest {

  @Autowired
  private UserRepository instance;

  @BeforeAll
  void setup() {
    instance.save(new UserRequest("tester", "test"));
  }

  @Test
  @DisplayName("Should save user")
  void shouldSaveUser() {
    final var user = new UserRequest("newuser", "safepw");

    final var savedUser = instance.save(user);

    assertThat(savedUser).isNotNull();
    assertThat(savedUser.id()).isNotNull();
    assertThat(savedUser.name()).isEqualTo("newuser");
    assertThat(savedUser.password()).isEqualTo("safepw");
  }

  @Test
  @DisplayName("Should find user")
  void shouldFindUser() {
    final var user = instance.findByName("tester");

    assertThat(user).isNotNull();
    assertThat(user.name()).isEqualTo("tester");
  }

  @Test
  @DisplayName("Should throw exception when user not found")
  void shouldThrowExceptionWhenUserNotFound() {
    assertThatCode(() -> instance.findByName("invalidName")).isInstanceOf(UserNotFoundException.class);
  }
}