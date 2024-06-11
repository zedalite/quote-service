package de.zedalite.quotes.repository;

import static org.assertj.core.api.Assertions.assertThat;

import de.zedalite.quotes.TestEnvironmentProvider;
import de.zedalite.quotes.data.model.Group;
import de.zedalite.quotes.data.model.GroupRequest;
import de.zedalite.quotes.data.model.User;
import de.zedalite.quotes.data.model.UserRequest;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(value = "classpath:test-no-cache.properties")
class GroupUserRepositoryTest extends TestEnvironmentProvider {

  @Autowired
  private GroupUserRepository instance;

  @Autowired
  private GroupRepository groupRepository;

  @Autowired
  private UserRepository userRepository;

  private Integer userId;

  private Integer groupId;

  //TODO refactor Usergenerator with random usersnames
  @BeforeAll
  void setup() {
    userId = userRepository.save(new UserRequest("grouper", "test", "Grouper")).id();
    groupId = groupRepository.save(new GroupRequest("groupers-group", "Groupers"), userId).id();
    instance.save(groupId, userId);
  }

  @Test
  @DisplayName("Should save group user")
  void shouldSaveGroupUser() {
    final Integer newUserId = userRepository.save(new UserRequest("real operator", "op", "Real Operator")).id();
    final Integer newGroupId = groupRepository.save(new GroupRequest("random-group", "sfsfefs"), userId).id();
    final Boolean isSaved = instance.save(newGroupId, newUserId);

    assertThat(isSaved).isTrue();
  }

  @Test
  @DisplayName("Should find all group users")
  void shouldFindAllGroupUsers() {
    final List<User> users = instance.findUsers(groupId);

    assertThat(users).hasSizeGreaterThanOrEqualTo(1);
  }

  @Test
  @DisplayName("Should find group user by id")
  void shouldFindGroupUserById() {
    final User user = instance.findById(groupId, userId);

    assertThat(user).isNotNull();
    assertThat(user.id()).isEqualTo(userId);
  }

  @Test
  @DisplayName("Should find all groups by user id")
  void shouldFindAllGroupsByUserId() {
    final List<Group> groups = instance.findGroups(userId);

    assertThat(groups).hasSizeGreaterThanOrEqualTo(1);
  }

  @Test
  @DisplayName("Should return true when user is in group")
  void shouldReturnTrueWhenUserIsInGroup() {
    final boolean isInGroup = instance.isUserInGroup(groupId, userId);

    assertThat(isInGroup).isTrue();
  }

  @Test
  @DisplayName("Should delete group user")
  void shouldDeleteGroupUser() {
    final Integer newUserId = userRepository.save(new UserRequest("operator", "op", "Operator")).id();
    final Integer newGroupId = groupRepository.save(new GroupRequest("new-group", "NewGr"), userId).id();
    instance.save(newGroupId, newUserId);

    final boolean isInGroup = instance.isUserInGroup(newGroupId, newUserId);
    assertThat(isInGroup).isTrue();

    instance.delete(newGroupId, newUserId);

    final boolean isInGroupAfterDelete = instance.isUserInGroup(newGroupId, newUserId);
    assertThat(isInGroupAfterDelete).isFalse();
  }

  @Test
  @DisplayName("Should return false when user is not in group")
  void shouldReturnFalseWhenUserIsNotInGroup() {
    final boolean isInGroup = instance.isUserInGroup(9876, 345);

    assertThat(isInGroup).isFalse();
  }
}
