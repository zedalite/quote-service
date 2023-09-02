package de.zedalite.quotes.service;

import de.zedalite.quotes.data.model.GroupRequest;
import de.zedalite.quotes.exceptions.GroupNotFoundException;
import de.zedalite.quotes.exceptions.ResourceNotFoundException;
import de.zedalite.quotes.fixtures.GroupGenerator;
import de.zedalite.quotes.fixtures.UserGenerator;
import de.zedalite.quotes.repository.GroupRepository;
import de.zedalite.quotes.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

  @InjectMocks
  private GroupService instance;

  @Mock
  private GroupRepository groupRepository;

  @Mock
  private UserRepository userRepository;

  @Test
  @DisplayName("Should create group")
  void shouldCreateGroup() {
    final var groupRequest = GroupGenerator.getGroupRequest();
    final var expectedGroup = GroupGenerator.getGroup();
    willReturn(expectedGroup).given(groupRepository).save(any(GroupRequest.class));

    instance.create(groupRequest);

    then(groupRepository).should().save(groupRequest);
  }

  @Test
  @DisplayName("Should create group with creator")
  void shouldCreateGroupWithCreator() {
    final var groupRequest = GroupGenerator.getGroupRequest();
    final var expectedGroup = GroupGenerator.getGroup();
    willReturn(expectedGroup).given(groupRepository).save(any(GroupRequest.class));

    instance.create(groupRequest, 1);

    then(groupRepository).should().save(groupRequest);
  }

  @Test
  @DisplayName("Should create group with creatorId")
  void shouldCreateGroupWithCreatorId() {
    final var user = UserGenerator.getUser();
    final var groupRequest = GroupGenerator.getGroupRequest();
    final var expectedGroup = GroupGenerator.getGroup();
    willReturn(expectedGroup).given(groupRepository).save(any(GroupRequest.class));

    instance.create(groupRequest, 1);

    then(groupRepository).should().save(groupRequest.withCreatorId(user.id()));
  }

  @Test
  @DisplayName("Should throw exception when group not created")
  void shouldThrowExceptionWhenGroupNotCreated() {
    final var groupRequest = GroupGenerator.getGroupRequest();
    willThrow(GroupNotFoundException.class).given(groupRepository).save(any(GroupRequest.class));

    assertThatCode(() -> instance.create(groupRequest)).isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  @DisplayName("Should find group by id")
  void shouldFindGroupById() {
    final var expectedGroup = GroupGenerator.getGroup();
    willReturn(expectedGroup).given(groupRepository).findById(anyInt());

    final var group = instance.find(1);

    assertThat(group.id()).isEqualTo(expectedGroup.id());
  }

  @Test
  @DisplayName("Should throw exception when group not found")
  void shouldThrowExceptionWhenGroupNotFound() {
    willThrow(GroupNotFoundException.class).given(groupRepository).findById(anyInt());

    assertThatCode(() -> instance.find(1)).isInstanceOf(ResourceNotFoundException.class);
  }
}
