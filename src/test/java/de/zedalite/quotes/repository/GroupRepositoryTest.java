package de.zedalite.quotes.repository;

import de.zedalite.quotes.TestEnvironmentProvider;
import de.zedalite.quotes.data.model.GroupRequest;
import de.zedalite.quotes.data.model.UserRequest;
import de.zedalite.quotes.exceptions.GroupNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GroupRepositoryTest extends TestEnvironmentProvider {

  @Autowired
  private GroupRepository instance;

  @Autowired
  private UserRepository userRepository;

  @BeforeAll
  void setup() {
    final var userId = userRepository.save(new UserRequest("grouptester", "test")).id();
    final var userId2 = userRepository.save(new UserRequest("grouptester2", "test2")).id();

    instance.save(new GroupRequest("test-group", "TESTGROUP", LocalDateTime.now(), userId));
    instance.save(new GroupRequest("best-quoter", "The Best Quoter", LocalDateTime.now(), userId2));
  }

  @Test
  @DisplayName("Should save group")
  void shouldSaveGroup() {
    final var creationDate = LocalDateTime.of(2023, 11, 15, 15, 0, 0);
    final var groupRequest = new GroupRequest("test-group", "TestGroup", creationDate, 1);

    final var savedGroup = instance.save(groupRequest);

    assertThat(savedGroup).isNotNull();
    assertThat(savedGroup.id()).isNotNull();
    assertThat(savedGroup.name()).isEqualTo("test-group");
    assertThat(savedGroup.displayName()).isEqualTo("TestGroup");
    assertThat(savedGroup.creationDate()).isEqualTo(creationDate);
    assertThat(savedGroup.creatorId()).isEqualTo(1);
  }

  @Test
  @DisplayName("Should find group by id")
  void shouldFindGroupById() {
    final var group = instance.findById(2);

    assertThat(group).isNotNull();
    assertThat(group.id()).isEqualTo(2);
  }

  @Test
  @DisplayName("Should throw exception finding group by non-existing id")
  void shouldThrowExceptionFindingGroupByNonExistingId() {
    assertThatCode(() -> instance.findById(999)).isInstanceOf(GroupNotFoundException.class);
  }
}
