package de.zedalite.quotes.web;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willReturn;

import de.zedalite.quotes.data.model.*;
import de.zedalite.quotes.fixtures.UserGenerator;
import de.zedalite.quotes.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

  @InjectMocks
  private UserController instance;

  @Mock
  private UserService service;

  @Test
  @DisplayName("Should get user")
  void shouldGetUser() {
    final UserPrincipal userPrincipal = UserGenerator.getUserPrincipal();
    final UserResponse expectedUser = UserGenerator.getUserResponse();
    willReturn(expectedUser).given(service).find(anyInt());

    instance.getUser(userPrincipal);

    then(service).should().find(userPrincipal.getId());
  }

  @Test
  @DisplayName("Should create user")
  void shouldCreateUser() {
    final UserRequest userRequest = UserGenerator.getUserRequest();

    instance.postUser(userRequest);

    then(service).should().create(userRequest);
  }

  @Test
  @DisplayName("Should patch name")
  void shouldPatchName() {
    final UserNameRequest request = UserGenerator.getUserNameRequest();
    final UserPrincipal principal = UserGenerator.getUserPrincipal();

    instance.patchName(request, principal);

    then(service).should().updateName(principal.getId(), request);
  }

  @Test
  @DisplayName("Should patch displayName")
  void shouldPatchDisplayName() {
    final UserDisplayNameRequest request = UserGenerator.getDisplayNameRequest();
    final UserPrincipal principal = UserGenerator.getUserPrincipal();

    instance.patchDisplayName(request, principal);

    then(service).should().updateDisplayName(principal.getId(), request);
  }

  @Test
  @DisplayName("Should patch email")
  void shouldPatchEmail() {
    final UserEmailRequest request = UserGenerator.getUserEmailRequest();
    final UserPrincipal principal = UserGenerator.getUserPrincipal();

    instance.patchEmail(request, principal);

    then(service).should().updateEmail(principal.getId(), request);
  }
}
