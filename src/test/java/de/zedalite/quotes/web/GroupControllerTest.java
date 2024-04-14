package de.zedalite.quotes.web;

import de.zedalite.quotes.data.model.UserPrincipal;
import de.zedalite.quotes.data.model.Group;
import de.zedalite.quotes.data.model.GroupRequest;
import de.zedalite.quotes.fixtures.GroupGenerator;
import de.zedalite.quotes.fixtures.UserGenerator;
import de.zedalite.quotes.service.GroupService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willReturn;

@ExtendWith(MockitoExtension.class)
class GroupControllerTest {

  @InjectMocks
  private GroupController instance;

  @Mock
  private GroupService service;

  @Test
  @DisplayName("Should get group")
  void shouldGetGroup() {
    final Group expectedGroup = GroupGenerator.getGroup();
    willReturn(expectedGroup).given(service).find(anyInt());

    instance.getGroup(1);

    then(service).should().find(1);
  }

  @Test
  @DisplayName("Should post group")
  void shouldPostGroup() {
    final GroupRequest groupRequest = GroupGenerator.getGroupRequest();
    final Group expectedGroup = GroupGenerator.getGroup();
    final UserPrincipal principal = UserGenerator.getUserPrincipal();

    willReturn(expectedGroup).given(service).create(any(GroupRequest.class), anyInt());

    instance.postGroup(groupRequest, principal);

    then(service).should().create(groupRequest, 1);
  }
}
