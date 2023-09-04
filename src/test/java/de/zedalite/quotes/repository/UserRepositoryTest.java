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
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@TestPropertySource(value = "classpath:test-no-cache.properties")
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
  @DisplayName("Should find all users")
  void shouldFindAllUsers() {
    final var users = instance.findAll();

    assertThat(users).hasSizeGreaterThanOrEqualTo(1);
  }

  @Test
  @DisplayName("Should find all by ids")
  void shouldFindAllByIds() {
    final var id = instance.findByName("tester").id();
    final var users = instance.findAllByIds(List.of(id));

    assertThat(users).hasSize(1);
    assertThat(users.get(0).name()).isEqualTo("tester");
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
  @DisplayName("Should find user by id")
  void shouldFindUserById() {
    final var id = instance.findByName("tester").id();
    final var user = instance.findById(id);

    assertThat(user).isNotNull();
    assertThat(user.name()).isEqualTo("tester");
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

  @Test
  @DisplayName("Should return true when username is taken")
  void shouldReturnTrueWhenUsernameIsTaken() {
    instance.save(new UserRequest("definitelyTaken", "taken"));

    final var isTaken = instance.isUsernameTaken("definitelyTaken");

    assertThat(isTaken).isTrue();
  }

  @Test
  @DisplayName("Should return false when username is free")
  void shouldReturnFalseWhenUsernameIsFree() {
    final var isTaken = instance.isUsernameTaken("freeUsername");

    assertThat(isTaken).isFalse();
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
  @DisplayName("Should return true when user non exist")
  void shouldReturnTrueWhenUserNonExist() {
    final var isNonExist = instance.doesUserNonExist(9876);

    assertThat(isNonExist).isTrue();
  }

  @Test
  @DisplayName("Should return false when user exist")
  void shouldReturnFalseWhenUserExist() {
    final var isNonExist = instance.doesUserNonExist(1);

    assertThat(isNonExist).isFalse();
  }
}
