package de.zedalite.quotes.service;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;

import de.zedalite.quotes.data.model.DisplayNameRequest;
import de.zedalite.quotes.data.model.User;
import de.zedalite.quotes.data.model.UserRequest;
import de.zedalite.quotes.exception.ResourceAlreadyExitsException;
import de.zedalite.quotes.exception.ResourceNotFoundException;
import de.zedalite.quotes.exception.UserNotFoundException;
import de.zedalite.quotes.fixtures.UserGenerator;
import de.zedalite.quotes.repository.UserRepository;
import java.util.List;
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
  @DisplayName("Should find users")
  void shouldFindUsers() {
    final List<User> users = UserGenerator.getUsers();
    willReturn(users).given(repository).findAll();

    instance.findAll();

    then(repository).should().findAll();
  }

  @Test
  @DisplayName("Should not find users when users non exist")
  void shouldNotFindUsersWhenUsersNonExist() {
    willThrow(UserNotFoundException.class).given(repository).findAll();

    assertThatCode(instance::findAll).isInstanceOf(ResourceNotFoundException.class);
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
    final DisplayNameRequest displayNameRequest = UserGenerator.getDisplayNameRequest();
    final User user = UserGenerator.getUser();
    final UserRequest userRequest = new UserRequest(user.name(), user.email(), displayNameRequest.displayName());
    willReturn(user).given(repository).findById(1);

    instance.updateDisplayName(1, displayNameRequest);

    then(repository).should().update(1, userRequest);
  }

  @Test
  @DisplayName("Should not update displayName when updating failed")
  void shouldNotUpdateDisplayNameWhenUpdatingFailed() {
    final DisplayNameRequest displayNameRequest = UserGenerator.getDisplayNameRequest();
    final User user = UserGenerator.getUser();
    final UserRequest userRequest = new UserRequest(user.name(), user.email(), displayNameRequest.displayName());
    willReturn(user).given(repository).findById(1);
    willThrow(UserNotFoundException.class).given(repository).update(1, userRequest);

    assertThatCode(() -> instance.updateDisplayName(1, displayNameRequest)).isInstanceOf(
      ResourceNotFoundException.class
    );
  }
}
