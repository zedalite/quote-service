package de.zedalite.quotes.web;

import de.zedalite.quotes.data.model.User;
import de.zedalite.quotes.fixtures.UserGenerator;
import de.zedalite.quotes.service.GroupUserService;
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
    
    instance.getUsers(1);
    
    then(service).should().findAll(1);
  }

  @Test
  @DisplayName("Should get group user")
  void shouldGetGroupUser() {
    final User expectedUser = UserGenerator.getUser();
    willReturn(expectedUser).given(service).find(anyInt(), anyInt());
    
    instance.getUser(1, 1);
    
    then(service).should().find(1, 1);
  }

  @Test
  @DisplayName("Should post group user")
  void shouldPostGroupUser() {
    instance.postUser(1, 1);

    then(service).should().create(1, 1);
  }

}
