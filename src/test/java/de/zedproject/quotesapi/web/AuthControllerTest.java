package de.zedproject.quotesapi.web;

import de.zedproject.quotesapi.data.model.User;
import de.zedproject.quotesapi.data.model.UserRequest;
import de.zedproject.quotesapi.data.model.UserResponse;
import de.zedproject.quotesapi.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
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

}
