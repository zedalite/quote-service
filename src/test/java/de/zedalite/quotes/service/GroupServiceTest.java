package de.zedalite.quotes.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.BDDMockito.*;

import de.zedalite.quotes.data.model.Group;
import de.zedalite.quotes.data.model.GroupRequest;
import de.zedalite.quotes.data.model.User;
import de.zedalite.quotes.exception.GroupNotFoundException;
import de.zedalite.quotes.exception.ResourceNotFoundException;
import de.zedalite.quotes.fixtures.GroupGenerator;
import de.zedalite.quotes.fixtures.UserGenerator;
import de.zedalite.quotes.repository.GroupRepository;
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

  @Test
  @DisplayName("Should create group")
  void shouldCreateGroup() {
    final GroupRequest groupRequest = GroupGenerator.getGroupRequest();
    final Group expectedGroup = GroupGenerator.getGroup();
    willReturn(expectedGroup).given(groupRepository).save(any(GroupRequest.class));

    instance.create(groupRequest);

    then(groupRepository).should().save(groupRequest);
  }

  @Test
  @DisplayName("Should create group with creator")
  void shouldCreateGroupWithCreator() {
    final GroupRequest groupRequest = GroupGenerator.getGroupRequest();
    final Group expectedGroup = GroupGenerator.getGroup();
    willReturn(expectedGroup).given(groupRepository).save(any(GroupRequest.class));

    instance.create(groupRequest, 1);

    then(groupRepository).should().save(groupRequest);
  }

  @Test
  @DisplayName("Should create group with creatorId")
  void shouldCreateGroupWithCreatorId() {
    final User user = UserGenerator.getUser();
    final GroupRequest groupRequest = GroupGenerator.getGroupRequest();
    final Group expectedGroup = GroupGenerator.getGroup();
    willReturn(expectedGroup).given(groupRepository).save(any(GroupRequest.class));

    instance.create(groupRequest, 1);

    then(groupRepository).should().save(groupRequest.withCreatorId(user.id()));
  }

  @Test
  @DisplayName("Should create group with creatorId when requestCreatorId not present")
  void shouldCreateGroupWithCreatorIdWhenRequestCreatorIdNotPresent() {
    final User user = UserGenerator.getUser();
    final GroupRequest groupRequest = GroupGenerator.getGroupRequest().withCreatorId(null);
    final Group expectedGroup = GroupGenerator.getGroup();
    willReturn(expectedGroup).given(groupRepository).save(any(GroupRequest.class));

    instance.create(groupRequest, 1);

    then(groupRepository).should().save(groupRequest.withCreatorId(user.id()));
  }

  @Test
  @DisplayName("Should throw exception when group not created")
  void shouldThrowExceptionWhenGroupNotCreated() {
    final GroupRequest groupRequest = GroupGenerator.getGroupRequest();
    willThrow(GroupNotFoundException.class).given(groupRepository).save(any(GroupRequest.class));

    assertThatCode(() -> instance.create(groupRequest)).isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  @DisplayName("Should find group by id")
  void shouldFindGroupById() {
    final Group expectedGroup = GroupGenerator.getGroup();
    willReturn(expectedGroup).given(groupRepository).findById(anyInt());

    final Group group = instance.find(1);

    assertThat(group.id()).isEqualTo(expectedGroup.id());
  }

  @Test
  @DisplayName("Should throw exception when group not found")
  void shouldThrowExceptionWhenGroupNotFound() {
    willThrow(GroupNotFoundException.class).given(groupRepository).findById(anyInt());

    assertThatCode(() -> instance.find(1)).isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  @DisplayName("Should find all groups")
  void shouldFindAllGroups() {
    final List<Group> expectedGroups = GroupGenerator.getGroups();
    willReturn(expectedGroups).given(groupRepository).findAll();

    instance.findAll();

    then(groupRepository).should().findAll();
  }

  @Test
  @DisplayName("Should throw exception when groups not found")
  void shouldThrowExceptionWhenGroupsNotFound() {
    willThrow(GroupNotFoundException.class).given(groupRepository).findAll();

    assertThatCode(() -> instance.findAll()).isInstanceOf(ResourceNotFoundException.class);
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
