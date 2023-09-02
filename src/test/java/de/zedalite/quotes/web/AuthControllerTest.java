package de.zedalite.quotes.web;

import de.zedalite.quotes.data.model.AuthRequest;
import de.zedalite.quotes.data.model.AuthResponse;
import de.zedalite.quotes.data.model.User;
import de.zedalite.quotes.data.model.UserRequest;
import de.zedalite.quotes.fixtures.UserGenerator;
import de.zedalite.quotes.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willReturn;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

  @InjectMocks
  private AuthController instance;

  @Mock
  private UserService service;

  @Test
  @DisplayName("Should signup user")
  void shouldSignupUser() {
    final var userRequest = new UserRequest("tester", "test");
    final var expectedUser = new User(1, "tester", "test", "TESTER", LocalDateTime.now());
    willReturn(expectedUser).given(service).create(any(UserRequest.class));

    instance.signup(userRequest);

    then(service).should().create(userRequest);
  }

  @Test
  @DisplayName("Should login user")
  void shouldLoginUser() {
    final var authRequest = new AuthRequest("tester", "test");
    final var expectedUserResponse = new AuthResponse("uezhag");
    willReturn(expectedUserResponse).given(service).authenticate(any(AuthRequest.class));

    instance.login(authRequest);

    then(service).should().authenticate(authRequest);
  }

  @Test
  @DisplayName("Should refresh user token")
  void shouldRefreshUserToken() {
    final var expectedUserResponse = new AuthResponse("uezhag");
    final var principal = UserGenerator.getUserPrincipal();

    willReturn(expectedUserResponse).given(service).refreshToken(anyString());

    instance.refresh(principal);

    then(service).should().refreshToken("tester");
  }
}
