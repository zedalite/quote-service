package de.zedalite.quotes.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willReturn;

import de.zedalite.quotes.data.model.GroupRequest;
import de.zedalite.quotes.data.model.GroupResponse;
import de.zedalite.quotes.data.model.UserPrincipal;
import de.zedalite.quotes.fixtures.GroupGenerator;
import de.zedalite.quotes.fixtures.UserGenerator;
import de.zedalite.quotes.service.GroupService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class GroupControllerTest {

  @InjectMocks
  private GroupController instance;

  @Mock
  private GroupService service;

  @Test
  @DisplayName("Should get group")
  void shouldGetGroup() {
    final GroupResponse expectedGroup = GroupGenerator.getGroupResponse();
    willReturn(expectedGroup).given(service).find(anyInt());

    final ResponseEntity<GroupResponse> response = instance.getGroup(1);

    then(service).should().find(1);
    assertThat(response).isNotNull();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  @DisplayName("Should post group")
  void shouldPostGroup() {
    final GroupRequest groupRequest = GroupGenerator.getGroupRequest();
    final GroupResponse expectedGroup = GroupGenerator.getGroupResponse();
    final UserPrincipal principal = UserGenerator.getUserPrincipal();

    willReturn(expectedGroup).given(service).create(any(GroupRequest.class), anyInt());

    final ResponseEntity<GroupResponse> response = instance.createGroup(groupRequest, principal);

    then(service).should().create(groupRequest, 1);
    assertThat(response).isNotNull();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }
}
