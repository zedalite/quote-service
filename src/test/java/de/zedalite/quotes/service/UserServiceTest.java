package de.zedalite.quotes.service;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;

import de.zedalite.quotes.data.model.*;
import de.zedalite.quotes.exception.ResourceAlreadyExitsException;
import de.zedalite.quotes.exception.ResourceNotFoundException;
import de.zedalite.quotes.exception.UserNotFoundException;
import de.zedalite.quotes.fixtures.UserGenerator;
import de.zedalite.quotes.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @InjectMocks
  private UserService instance;

  @Mock
  private UserRepository repository;

  @Test
  @DisplayName("Should create user when it not exist")
  void shouldCreateUserWhenItNotExist() {
    final UserRequest userRequest = UserGenerator.getUserRequest();
    willReturn(false).given(repository).isUsernameTaken(anyString());

    instance.create(userRequest);

    then(repository).should().save(userRequest);
  }

  @Test
  @DisplayName("Should not create user when it already exists")
  void shouldNotCreateUserWhenItAlreadyExists() {
    final UserRequest userRequest = UserGenerator.getUserRequest();
    willReturn(true).given(repository).isUsernameTaken(anyString());

    assertThatCode(() -> instance.create(userRequest)).isInstanceOf(ResourceAlreadyExitsException.class);

    then(repository).should(never()).save(userRequest);
  }

  @Test
  @DisplayName("Should not create user when saving failed ")
  void shouldNotCreateUserWhenSavingFailed() {
    final UserRequest userRequest = UserGenerator.getUserRequest();
    willThrow(UserNotFoundException.class).given(repository).save(userRequest);

    assertThatCode(() -> instance.create(userRequest)).isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  @DisplayName("Should find user")
  void shouldFindUser() {
    final User user = UserGenerator.getUser();
    willReturn(user).given(repository).findById(1);

    instance.find(1);

    then(repository).should().findById(1);
  }

  @Test
  @DisplayName("Should not find user when user non exist")
  void shouldNotFindUserWhenUserNonExist() {
    willThrow(UserNotFoundException.class).given(repository).findById(1);

    assertThatCode(() -> instance.find(1)).isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  @DisplayName("Should update displayName")
  void shouldUpdateDisplayName() {
    final UserDisplayNameRequest userDisplayNameRequest = UserGenerator.getDisplayNameRequest();
    final User user = UserGenerator.getUser();
    final UserRequest userRequest = new UserRequest(user.name(), user.email(), userDisplayNameRequest.displayName());
    willReturn(user).given(repository).findById(anyInt());

    instance.updateDisplayName(1, userDisplayNameRequest);

    then(repository).should().update(1, userRequest);
  }

  @Test
  @DisplayName("Should not update displayName when updating failed")
  void shouldNotUpdateDisplayNameWhenUpdatingFailed() {
    final UserDisplayNameRequest userDisplayNameRequest = UserGenerator.getDisplayNameRequest();
    final User user = UserGenerator.getUser();
    final UserRequest userRequest = new UserRequest(user.name(), user.email(), userDisplayNameRequest.displayName());
    willReturn(user).given(repository).findById(anyInt());
    willThrow(UserNotFoundException.class).given(repository).update(1, userRequest);

    assertThatCode(() -> instance.updateDisplayName(1, userDisplayNameRequest)).isInstanceOf(
      ResourceNotFoundException.class
    );
  }

  @Test
  @DisplayName("Should not update displayname when user not exists")
  void shouldNotUpdateDisplaynameWhenUserNotExists() {
    final UserDisplayNameRequest userDisplayNameRequest = UserGenerator.getDisplayNameRequest();
    final User user = UserGenerator.getUser();
    final UserRequest userRequest = new UserRequest(user.name(), user.email(), userDisplayNameRequest.displayName());
    willThrow(UserNotFoundException.class).given(repository).findById(anyInt());

    assertThatCode(() -> instance.updateDisplayName(1, userDisplayNameRequest)).isInstanceOf(
      ResourceNotFoundException.class
    );
  }

  @Test
  @DisplayName("Should update name")
  void shouldUpdateName() {
    final UserNameRequest userNameRequest = UserGenerator.getUserNameRequest();
    final User user = UserGenerator.getUser();
    final UserRequest userRequest = new UserRequest(userNameRequest.name(), user.email(), user.displayName());
    willReturn(user).given(repository).findById(anyInt());
    willReturn(false).given(repository).isUsernameTaken(anyString());

    instance.updateName(1, userNameRequest);

    then(repository).should().update(1, userRequest);
  }

  @Test
  @DisplayName("Should not update name when updating failed")
  void shouldNotUpdateNameWhenUpdatingFailed() {
    final UserNameRequest userNameRequest = UserGenerator.getUserNameRequest();
    final User user = UserGenerator.getUser();
    final UserRequest userRequest = new UserRequest(userNameRequest.name(), user.email(), user.displayName());
    willReturn(user).given(repository).findById(1);
    willReturn(false).given(repository).isUsernameTaken(anyString());
    willThrow(UserNotFoundException.class).given(repository).update(1, userRequest);

    assertThatCode(() -> instance.updateName(1, userNameRequest)).isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  @DisplayName("Should not update name when name already taken")
  void shouldNotUpdateNameWhenNameAlreadyTaken() {
    final UserNameRequest userNameRequest = UserGenerator.getUserNameRequest();
    willReturn(true).given(repository).isUsernameTaken(anyString());

    assertThatCode(() -> instance.updateName(1, userNameRequest)).isInstanceOf(ResourceAlreadyExitsException.class);
  }

  @Test
  @DisplayName("Should not update name when user not exists")
  void shouldNotUpdateNameWhenUserNotExists() {
    final UserNameRequest userNameRequest = UserGenerator.getUserNameRequest();
    willReturn(false).given(repository).isUsernameTaken(anyString());
    willThrow(UserNotFoundException.class).given(repository).findById(anyInt());

    assertThatCode(() -> instance.updateName(1, userNameRequest)).isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  @DisplayName("Should update email")
  void shouldUpdateEmail() {
    final UserEmailRequest userEmailRequest = UserGenerator.getUserEmailRequest();
    final User user = UserGenerator.getUser();
    final UserRequest userRequest = new UserRequest(user.name(), userEmailRequest.email(), user.displayName());
    willReturn(user).given(repository).findById(anyInt());

    instance.updateEmail(1, userEmailRequest);

    then(repository).should().update(1, userRequest);
  }

  @Test
  @DisplayName("Should not update email when user not exists")
  void shouldNotUpdateEmailWhenUserNotExists() {
    final UserEmailRequest userEmailRequest = UserGenerator.getUserEmailRequest();
    willThrow(UserNotFoundException.class).given(repository).findById(anyInt());

    assertThatCode(() -> instance.updateEmail(1, userEmailRequest)).isInstanceOf(ResourceNotFoundException.class);
  }
}
