package de.zedalite.quotes.repository;

import de.zedalite.quotes.TestEnvironmentProvider;
import de.zedalite.quotes.data.model.UserRequest;
import de.zedalite.quotes.exceptions.UserNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class UserRepositoryTest extends TestEnvironmentProvider {

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
  @DisplayName("Should find user by name")
  void shouldFindUserByName() {
    final var user = instance.findByName("tester");

    assertThat(user).isNotNull();
    assertThat(user.name()).isEqualTo("tester");
  }

  @Test
  @DisplayName("Should throw exception when user not found")
  void shouldThrowExceptionWhenUserNotFound() {
    assertThatCode(() -> instance.findByName("invalidName")).isInstanceOf(UserNotFoundException.class);
  }

  @Test
  @DisplayName("Should return false when username is already taken")
  void shouldReturnFalseWhenUsernameIsAlreadyTaken() {
    instance.save(new UserRequest("takenName", "taken"));

    final var isAvailable = instance.isUsernameAvailable("takenName");

    assertThat(isAvailable).isFalse();
  }

  @Test
  @DisplayName("Should return true when username is free")
  void shouldReturnTrueWhenUsernameIsFree() {
    final var isAvailable = instance.isUsernameAvailable("abcdefghijklmn");

    assertThat(isAvailable).isTrue();
  }

  @Test
  @DisplayName("Should update user")
  void shouldUpdateUser() {
    final var userId = instance.save(new UserRequest("super", "sUpEr")).id();
    final var request = new UserRequest("mega", "mEgA", "MEGA");

    final var updatedUser = instance.update(userId, request);

    assertThat(updatedUser.id()).isEqualTo(userId);
    assertThat(updatedUser.password()).isEqualTo("mEgA");
    assertThat(updatedUser.displayName()).isEqualTo("MEGA");
  }
}
