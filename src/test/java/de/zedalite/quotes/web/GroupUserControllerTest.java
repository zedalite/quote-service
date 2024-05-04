package de.zedalite.quotes.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willReturn;

import de.zedalite.quotes.data.model.User;
import de.zedalite.quotes.data.model.UserResponse;
import de.zedalite.quotes.fixtures.UserGenerator;
import de.zedalite.quotes.service.GroupUserService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class GroupUserControllerTest {

  @InjectMocks
  private GroupUserController instance;

  @Mock
  private GroupUserService service;

  @Test
  @DisplayName("Should get group users")
  void shouldGetGroupUsers() {
    final List<User> expectedUsers = UserGenerator.getUsers();
    willReturn(expectedUsers).given(service).findAll(anyInt());

    final ResponseEntity<List<UserResponse>> response = instance.getUsers(1);

    then(service).should().findAll(1);
    assertThat(response).isNotNull();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  @DisplayName("Should get group user")
  void shouldGetGroupUser() {
    final UserResponse expectedUser = UserGenerator.getUserResponse();
    willReturn(expectedUser).given(service).find(anyInt(), anyInt());

    final ResponseEntity<UserResponse> response = instance.getUser(1, 1);

    then(service).should().find(1, 1);
    assertThat(response).isNotNull();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  @DisplayName("Should post group user")
  void shouldPostGroupUser() {
    final ResponseEntity<Void> response = instance.createUser(1, 1);

    then(service).should().create(1, 1);
    assertThat(response).isNotNull();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }
}
