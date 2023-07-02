package de.zedalite.quotes.web;

import de.zedalite.quotes.auth.UserPrincipal;
import de.zedalite.quotes.data.model.User;
import de.zedalite.quotes.data.model.UserRequest;
import de.zedalite.quotes.data.model.UserResponse;
import de.zedalite.quotes.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;

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

  @Mock
  private UserDetailsService userDetailsService;

  @Test
  @DisplayName("Should signup user")
  void shouldSignupUser() {
    final var userRequest = new UserRequest("tester", "test");
    final var expectedUser = new User(1, "tester", "test");
    willReturn(expectedUser).given(service).create(any(UserRequest.class));

    instance.signup(userRequest);

    then(service).should().create(userRequest);
  }

  @Test
  @DisplayName("Should login user")
  void shouldLoginUser() {
    final var userRequest = new UserRequest("tester", "test");
    final var expectedUserResponse = new UserResponse("uezhag");
    willReturn(expectedUserResponse).given(service).authenticate(any(UserRequest.class));

    instance.login(userRequest);

    then(service).should().authenticate(userRequest);
  }

  @Test
  @DisplayName("Should refresh user token")
  void shouldRefreshUserToken() {
    final var expectedUserDetails = new UserPrincipal(new User(1, "tester", "test"));
    final var expectedUserResponse = new UserResponse("uezhag");

    willReturn(expectedUserResponse).given(service).refreshToken(anyString());

    final var authentication = new UsernamePasswordAuthenticationToken(expectedUserDetails, null, expectedUserDetails.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(authentication);

    instance.refresh();

    then(service).should().refreshToken("tester");
  }
}
