package de.zedalite.quotes.service;

import de.zedalite.quotes.data.model.*;
import de.zedalite.quotes.exceptions.ResourceAlreadyExitsException;
import de.zedalite.quotes.exceptions.ResourceNotFoundException;
import de.zedalite.quotes.exceptions.UserNotFoundException;
import de.zedalite.quotes.fixtures.UserGenerator;
import de.zedalite.quotes.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @InjectMocks
  private UserService instance;

  @Mock
  private UserRepository repository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private JwtTokenService tokenService;

  @Mock
  private AuthenticationManager authenticationManager;

  @Test
  @DisplayName("Should find user by name")
  void shouldFindUserByName() {
    final UserRequest userRequest = UserGenerator.getUserRequest();
    final User expectedUser = UserGenerator.getUser();
    willReturn(expectedUser).given(repository).findByName(anyString());

    final User user = instance.findByName(userRequest.name());

    Assertions.assertThat(user.name()).isEqualTo(expectedUser.name());
  }

  @Test
  @DisplayName("Should throw exception when user not found")
  void shouldThrowExceptionWhenUserNotFound() {
    willThrow(UserNotFoundException.class).given(repository).findByName(anyString());

    assertThatCode(() -> instance.findByName("tester")).isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  @DisplayName("Should create user when it not exist")
  void shouldCreateUserWhenItNotExist() {
    final UserRequest userRequest = UserGenerator.getUserRequest();
    willReturn(UserGenerator.getUserRequest().password()).given(passwordEncoder).encode(anyString());
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
    willReturn(userRequest.password()).given(passwordEncoder).encode(anyString());
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
  @DisplayName("Should update password")
  void shouldUpdatePassword() {
    final PasswordRequest passwordRequest = UserGenerator.getPasswordRequest();
    final User user = UserGenerator.getUser();
    final UserRequest userRequest = new UserRequest(user.name(), passwordEncoder.encode(passwordRequest.password()), user.displayName());
    willReturn(user).given(repository).findById(1);

    instance.updatePassword(1, passwordRequest);

    then(repository).should().update(1, userRequest);
  }

  @Test
  @DisplayName("Should not update when updating failed")
  void shouldNotUpdateWhenUpdatingFailed() {
    final PasswordRequest passwordRequest = UserGenerator.getPasswordRequest();
    final User user = UserGenerator.getUser();
    final UserRequest userRequest = new UserRequest(user.name(), passwordEncoder.encode(passwordRequest.password()), user.displayName());
    willReturn(user).given(repository).findById(1);
    willThrow(UserNotFoundException.class).given(repository).update(1, userRequest);

    assertThatCode(() -> instance.updatePassword(1, passwordRequest)).isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  @DisplayName("Should update displayName")
  void shouldUpdateDisplayName() {
    final DisplayNameRequest displayNameRequest = UserGenerator.getDisplayNameRequest();
    final User user = UserGenerator.getUser();
    final UserRequest userRequest = new UserRequest(user.name(), user.password(), displayNameRequest.displayName());
    willReturn(user).given(repository).findById(1);

    instance.updateDisplayName(1, displayNameRequest);

    then(repository).should().update(1, userRequest);
  }

  @Test
  @DisplayName("Should not update displayName when updating failed")
  void shouldNotUpdateDisplayNameWhenUpdatingFailed() {
    final DisplayNameRequest displayNameRequest = UserGenerator.getDisplayNameRequest();
    final User user = UserGenerator.getUser();
    final UserRequest userRequest = new UserRequest(user.name(), user.password(), displayNameRequest.displayName());
    willReturn(user).given(repository).findById(1);
    willThrow(UserNotFoundException.class).given(repository).update(1, userRequest);

    assertThatCode(() -> instance.updateDisplayName(1, displayNameRequest)).isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  @DisplayName("Should authenticate valid user")
  void shouldAuthenticateValidUser() {
    final UserRequest userRequest = UserGenerator.getUserRequest();
    final AuthRequest authRequest = new AuthRequest(userRequest.name(), userRequest.password());
    willReturn("e54f6rmh7g").given(tokenService).generateToken(userRequest.name());

    final AuthResponse userResponse = instance.authenticate(authRequest);

    assertThat(userResponse.token()).isEqualTo("e54f6rmh7g");
  }

  @Test
  @DisplayName("Should refresh token")
  void shouldRefreshToken() {
    final String userName = "test";
    willReturn("e3432jh3").given(tokenService).generateToken(userName);

    final AuthResponse userResponse = instance.refreshToken(userName);

    assertThat(userResponse.token()).isEqualTo("e3432jh3");
  }
}
