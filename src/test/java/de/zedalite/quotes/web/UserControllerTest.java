package de.zedalite.quotes.web;

import de.zedalite.quotes.data.model.UserPrincipal;
import de.zedalite.quotes.data.model.DisplayNameRequest;
import de.zedalite.quotes.data.model.User;
import de.zedalite.quotes.fixtures.UserGenerator;
import de.zedalite.quotes.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willReturn;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

  @InjectMocks
  private UserController instance;

  @Mock
  private UserService service;

  @Test
  @DisplayName("Should get users")
  void shouldGetUsers() {
    final List<User> expectedUsers = UserGenerator.getUsers();
    willReturn(expectedUsers).given(service).findAll();

    instance.getUsers();

    then(service).should().findAll();
  }

  @Test
  @DisplayName("Should get user")
  void shouldGetUser() {
    final User expectedUser = UserGenerator.getUser();
    willReturn(expectedUser).given(service).find(anyInt());

    instance.getUser(1);

    then(service).should().find(1);
  }


  @Test
  @DisplayName("Should patch displayName")
  void shouldPatchDisplayName() {
    final DisplayNameRequest request = UserGenerator.getDisplayNameRequest();
    final UserPrincipal principal = UserGenerator.getUserPrincipal();

    instance.patchDisplayName(request, principal);

    then(service).should().updateDisplayName(principal.getId(), request);
  }
}
