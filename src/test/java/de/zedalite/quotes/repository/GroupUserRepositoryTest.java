package de.zedalite.quotes.repository;

import de.zedalite.quotes.TestEnvironmentProvider;
import de.zedalite.quotes.data.model.GroupRequest;
import de.zedalite.quotes.data.model.User;
import de.zedalite.quotes.data.model.UserRequest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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

  @BeforeAll
  void setup() {
    userId = userRepository.save(new UserRequest("grouper", "test")).id();
    groupId = groupRepository.save(new GroupRequest("groupers-group", "GroupersGroup", LocalDateTime.now(), userId)).id();
  }

  @Test
  @DisplayName("Should save group user")
  void shouldSaveGroupUser() {
    final Boolean isSaved = instance.save(groupId, userId);

    assertThat(isSaved).isTrue();
  }

  @Test
  @DisplayName("Should find all group users")
  void shouldFindAllGroupUsers() {
    final Integer userId = userRepository.save(new UserRequest("operator", "op")).id();
    final Integer groupId = groupRepository.save(new GroupRequest("new-group", "New Group", LocalDateTime.now(), userId)).id();
    instance.save(groupId, userId);

    final List<User> users = instance.findAll(groupId);

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
  @DisplayName("Should return true when user is in group")
  void shouldReturnTrueWhenUserIsInGroup() {
    final boolean isInGroup = instance.isUserInGroup(groupId, userId);

    assertThat(isInGroup).isTrue();
  }

  @Test
  @DisplayName("Should return false when user is not in group")
  void shouldReturnFalseWhenUserIsNotInGroup() {
    final boolean isInGroup = instance.isUserInGroup(9876, 345);

    assertThat(isInGroup).isFalse();
  }
}
