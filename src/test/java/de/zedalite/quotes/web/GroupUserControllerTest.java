package de.zedalite.quotes.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willReturn;

import de.zedalite.quotes.data.model.GroupUserRequest;
import de.zedalite.quotes.data.model.GroupUserResponse;
import de.zedalite.quotes.data.model.User;
import de.zedalite.quotes.data.model.UserPrincipal;
import de.zedalite.quotes.fixtures.GroupUserGenerator;
import de.zedalite.quotes.fixtures.UserGenerator;
import de.zedalite.quotes.service.GroupUserService;
import java.util.List;
import java.util.Objects;
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

    final ResponseEntity<List<GroupUserResponse>> response = instance.getUsers(1);

    then(service).should().findAll(1);
    assertThat(response).isNotNull();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  @DisplayName("Should get group user")
  void shouldGetGroupUser() {
    final GroupUserResponse expectedGroupUser = GroupUserGenerator.getGroupUserResponse();
    willReturn(expectedGroupUser).given(service).find(anyInt(), anyInt());

    final ResponseEntity<GroupUserResponse> response = instance.getUser(1, 1);

    then(service).should().find(1, 1);
    assertThat(response).isNotNull();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(Objects.requireNonNull(response.getBody())).isEqualTo(expectedGroupUser);
  }

  @Test
  @DisplayName("Should post group user")
  void shouldPostGroupUser() {
    final GroupUserResponse groupUserResponse = GroupUserGenerator.getGroupUserResponse();
    willReturn(groupUserResponse).given(service).create(anyInt(), any(GroupUserRequest.class));
    final GroupUserRequest request = new GroupUserRequest(1, null);

    final ResponseEntity<GroupUserResponse> response = instance.createUser(1, request);

    then(service).should().create(1, request);
    assertThat(response).isNotNull();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isEqualTo(groupUserResponse);
  }

  @Test
  @DisplayName("Should leave group")
  void shouldLeaveGroup() {
    final UserPrincipal principal = UserGenerator.getUserPrincipal();

    final ResponseEntity<Void> response = instance.leaveGroup(1, principal);

    then(service).should().leave(1, principal.getId());
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
  }
}
