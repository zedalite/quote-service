package de.zedproject.quotesapi.service;

import de.zedproject.quotesapi.data.model.User;
import de.zedproject.quotesapi.data.model.UserRequest;
import de.zedproject.quotesapi.exceptions.ResourceAlreadyExitsException;
import de.zedproject.quotesapi.exceptions.ResourceNotFoundException;
import de.zedproject.quotesapi.exceptions.UserNotFoundException;
import de.zedproject.quotesapi.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @InjectMocks
  private UserService instance;

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private JwtTokenService tokenService;

  @Mock
  private AuthenticationManager authenticationManager;

  @Test
  @DisplayName("Should find user")
  void shouldFindUser() {
    final var userRequest = new UserRequest("tester", "test");
    final var expectedUser = new User(1, "tester", "test");
    willReturn(expectedUser).given(userRepository).findByName(anyString());

    final var user = instance.find(userRequest.name());

    assertThat(user.name()).isEqualTo(expectedUser.name());
  }

  @Test
  @DisplayName("Should throw exception when user not found")
  void shouldThrowExceptionWhenUserNotFound() {
    willThrow(UserNotFoundException.class).given(userRepository).findByName(anyString());

    assertThatCode(() -> instance.find("tester")).isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  @DisplayName("Should create user when it not exist")
  void shouldCreateUserWhenItNotExist() {
    final var userRequest = new UserRequest("tester", "test");
    willReturn("test").given(passwordEncoder).encode(anyString());
    willThrow(UserNotFoundException.class).given(userRepository).findByName(anyString());

    instance.create(userRequest);

    then(userRepository).should().save(userRequest);
  }

  @Test
  @DisplayName("Should not create user when it already exists")
  void shouldNotCreateUserWhenItAlreadyExists() {
    final var userRequest = new UserRequest("tester", "test");
    final var expectedUser = new User(1, "tester", "test");
    willReturn(expectedUser).given(userRepository).findByName(anyString());

    assertThatCode(() -> instance.create(userRequest)).isInstanceOf(ResourceAlreadyExitsException.class);

    then(userRepository).should(never()).save(userRequest);
  }

  @Test
  @DisplayName("Should authenticate valid user")
  void shouldAuthenticateValidUser() {
    final var userRequest = new UserRequest("tester", "test");
    willReturn("e54f6rmh7g").given(tokenService).generateToken(userRequest.name());

    final var userResponse = instance.authenticate(userRequest);

    assertThat(userResponse.token()).isEqualTo("e54f6rmh7g");
  }
}
