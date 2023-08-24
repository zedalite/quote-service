package de.zedalite.quotes.web;

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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willReturn;

@ExtendWith(MockitoExtension.class)
class GroupControllerTest {

  @InjectMocks
  private GroupController instance;

  @Mock
  private GroupService service;

  @Mock
  private SecurityContextHolder securityContextHolder;

  @Test
  @DisplayName("Should get group")
  void shouldGetGroup() {
    final var expectedGroup = GroupGenerator.getGroup();
    willReturn(expectedGroup).given(service).find(anyInt());

    instance.getGroup(1);

    then(service).should().find(1);
  }

  @Test
  @DisplayName("Should post group")
  void shouldPostGroup() {
    final var groupRequest = GroupGenerator.getGroupRequest();
    final var expectedGroup = GroupGenerator.getGroup();
    final var userPrincipal = UserGenerator.getUserPrincipal();
    final var authentication = new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(authentication);

    willReturn(expectedGroup).given(service).create(any(GroupRequest.class), anyString());

    instance.postGroup(groupRequest);

    then(service).should().create(groupRequest, "tester");
  }
}
