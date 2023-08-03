package de.zedalite.quotes.service;

import de.zedalite.quotes.data.model.AuthRequest;
import de.zedalite.quotes.exceptions.ResourceAlreadyExitsException;
import de.zedalite.quotes.exceptions.ResourceNotFoundException;
import de.zedalite.quotes.exceptions.UserNotFoundException;
import de.zedalite.quotes.fixtures.UserGenerator;
import de.zedalite.quotes.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
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
    final var userRequest = UserGenerator.getUserRequest();
    final var expectedUser = UserGenerator.getUser();
    BDDMockito.willReturn(expectedUser).given(userRepository).findByName(anyString());

    final var user = instance.find(userRequest.name());

    Assertions.assertThat(user.name()).isEqualTo(expectedUser.name());
  }

  @Test
  @DisplayName("Should throw exception when user not found")
  void shouldThrowExceptionWhenUserNotFound() {
    BDDMockito.willThrow(UserNotFoundException.class).given(userRepository).findByName(anyString());

    assertThatCode(() -> instance.find("tester")).isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  @DisplayName("Should create user when it not exist")
  void shouldCreateUserWhenItNotExist() {
    final var userRequest = UserGenerator.getUserRequest();
    willReturn(UserGenerator.getUserRequest().password()).given(passwordEncoder).encode(anyString());
    willThrow(UserNotFoundException.class).given(userRepository).findByName(anyString());

    instance.create(userRequest);

    then(userRepository).should().save(userRequest);
  }

  @Test
  @DisplayName("Should not create user when it already exists")
  void shouldNotCreateUserWhenItAlreadyExists() {
    final var userRequest = UserGenerator.getUserRequest();
    final var expectedUser = UserGenerator.getUser();
    BDDMockito.willReturn(expectedUser).given(userRepository).findByName(anyString());

    assertThatCode(() -> instance.create(userRequest)).isInstanceOf(ResourceAlreadyExitsException.class);

    then(userRepository).should(never()).save(userRequest);
  }

  @Test
  @DisplayName("Should authenticate valid user")
  void shouldAuthenticateValidUser() {
    final var userRequest = UserGenerator.getUserRequest();
    final var authRequest = new AuthRequest(userRequest.name(), userRequest.password());
    willReturn("e54f6rmh7g").given(tokenService).generateToken(userRequest.name());

    final var userResponse = instance.authenticate(authRequest);

    assertThat(userResponse.token()).isEqualTo("e54f6rmh7g");
  }

  @Test
  @DisplayName("Should refresh token")
  void shouldRefreshToken() {
    final var userName = "test";
    willReturn("e3432jh3").given(tokenService).generateToken(userName);

    final var userResponse = instance.refreshToken(userName);

    assertThat(userResponse.token()).isEqualTo("e3432jh3");
  }
}
