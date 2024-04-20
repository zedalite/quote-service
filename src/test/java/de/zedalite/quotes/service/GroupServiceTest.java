package de.zedalite.quotes.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import de.zedalite.quotes.data.model.Group;
import de.zedalite.quotes.data.model.GroupRequest;
import de.zedalite.quotes.data.model.GroupResponse;
import de.zedalite.quotes.data.model.User;
import de.zedalite.quotes.exception.GroupNotFoundException;
import de.zedalite.quotes.exception.ResourceNotFoundException;
import de.zedalite.quotes.exception.UserNotFoundException;
import de.zedalite.quotes.fixtures.GroupGenerator;
import de.zedalite.quotes.fixtures.UserGenerator;
import de.zedalite.quotes.repository.GroupRepository;
import de.zedalite.quotes.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

  @InjectMocks
  private GroupService instance;

  @Mock
  private GroupRepository groupRepository;

  @Mock
  private UserRepository userRepository;

  @Test
  @DisplayName("Should create group with creator")
  void shouldCreateGroupWithCreator() {
    final GroupRequest groupRequest = GroupGenerator.getGroupRequest();
    final Group expectedGroup = GroupGenerator.getGroup();
    final User expectedUser = UserGenerator.getUser();
    willReturn(expectedGroup).given(groupRepository).save(any(GroupRequest.class), anyInt());
    willReturn(expectedUser).given(userRepository).findById(anyInt());

    instance.create(groupRequest, 1);

    then(groupRepository).should().save(groupRequest, 1);
  }

  @Test
  @DisplayName("Should throw exception when group not created")
  void shouldThrowExceptionWhenGroupNotCreated() {
    final GroupRequest groupRequest = GroupGenerator.getGroupRequest();
    willThrow(GroupNotFoundException.class).given(groupRepository).save(any(GroupRequest.class), anyInt());

    assertThatCode(() -> instance.create(groupRequest, 1)).isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  @DisplayName("Should find group by id")
  void shouldFindGroupById() {
    final Group expectedGroup = GroupGenerator.getGroup();
    final User expectedUser = UserGenerator.getUser();
    willReturn(expectedGroup).given(groupRepository).findById(anyInt());
    willReturn(expectedUser).given(userRepository).findById(anyInt());

    final GroupResponse group = instance.find(1);

    assertThat(group.group().id()).isEqualTo(expectedGroup.id());
    assertThat(group.creator()).isPresent();
  }

  @Test
  @DisplayName("Should find group by id without creator")
  void shouldFindGroupByIdWithoutCreator() {
    final Group expectedGroup = GroupGenerator.getGroupNoCreator();
    willReturn(expectedGroup).given(groupRepository).findById(anyInt());

    final GroupResponse group = instance.find(1);

    assertThat(group.group().id()).isEqualTo(expectedGroup.id());
    assertThat(group.creator()).isEmpty();
  }

  @Test
  @DisplayName("Should still find group by id when creator throws exception")
  void shouldFindGroupByIdWhenCreatorThrowsException() {
    final Group expectedGroup = GroupGenerator.getGroup();
    willReturn(expectedGroup).given(groupRepository).findById(anyInt());
    willThrow(UserNotFoundException.class).given(userRepository).findById(anyInt());

    final GroupResponse group = instance.find(1);

    assertThat(group.group().id()).isEqualTo(expectedGroup.id());
    assertThat(group.creator()).isEmpty();
  }

  @Test
  @DisplayName("Should throw exception when group not found")
  void shouldThrowExceptionWhenGroupNotFound() {
    willThrow(GroupNotFoundException.class).given(groupRepository).findById(anyInt());

    assertThatCode(() -> instance.find(1)).isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  @DisplayName("Should find all group ids")
  void shouldFindAllGroupIds() {
    final List<Integer> expectedIds = GroupGenerator.getGroups().stream().map(Group::id).toList();
    willReturn(expectedIds).given(groupRepository).findAllIds();

    instance.findAllIds();

    then(groupRepository).should().findAllIds();
  }

  @Test
  @DisplayName("Should throw exception when group ids not found")
  void shouldThrowExceptionWhenGroupIdsNotFound() {
    willThrow(GroupNotFoundException.class).given(groupRepository).findAllIds();

    assertThatCode(() -> instance.findAllIds()).isInstanceOf(ResourceNotFoundException.class);
  }
}
