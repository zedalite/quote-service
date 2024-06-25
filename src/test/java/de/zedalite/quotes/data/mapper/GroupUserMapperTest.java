package de.zedalite.quotes.data.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import de.zedalite.quotes.data.jooq.quotes.tables.records.GroupUsersRecord;
import de.zedalite.quotes.data.model.GroupUser;
import de.zedalite.quotes.data.model.GroupUserResponse;
import de.zedalite.quotes.data.model.User;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

class GroupUserMapperTest {

  private static final GroupUserMapper instance = GroupUserMapper.INSTANCE;

  @Test
  @DisplayName("Should map groupsRecord to group")
  void shouldMapGroupsRecordToGroup() {
    final GroupUsersRecord groupUsersRecord = new GroupUsersRecord(0, 1, "user");

    final GroupUser groupUser = instance.mapToGroupUser(groupUsersRecord);

    assertThat(groupUser).isNotNull();
    assertThat(groupUser.groupId()).isZero();
    assertThat(groupUser.userId()).isEqualTo(1);
    assertThat(groupUser.userDisplayName()).isEqualTo("user");
  }

  @ParameterizedTest
  @DisplayName("Should map empty groupRecord to null")
  @NullSource
  void shouldMapEmptyGroupRecordToNull(final GroupUsersRecord groupsRecord) {
    final GroupUser groupUser = instance.mapToGroupUser(groupsRecord);

    assertThat(groupUser).isNull();
  }

  @Test
  @DisplayName("Should map groupRecords to groups")
  void shouldMapGroupRecordsToGroups() {
    final GroupUsersRecord groupUsersRecord = new GroupUsersRecord(0, 1, "user");
    final GroupUsersRecord groupUsersRecord2 = new GroupUsersRecord(1, 2, "user2");

    final List<GroupUser> groupUsers = instance.mapToGroupUsers(List.of(groupUsersRecord, groupUsersRecord2));

    assertThat(groupUsers).hasSize(2);
    assertThat(groupUsers.getFirst().groupId()).isZero();
    assertThat(groupUsers.getLast().groupId()).isOne();
  }

  @ParameterizedTest
  @DisplayName("Should map empty groupRecords to null")
  @NullSource
  void shouldMapEmptyGroupRecordsToNull(final List<GroupUsersRecord> groupsRecords) {
    final List<GroupUser> groupUsers = instance.mapToGroupUsers(groupsRecords);

    assertThat(groupUsers).isNull();
  }

  @Test
  @DisplayName("Should map groupUser and displayname to response")
  void shouldMapGroupUserAndDisplaynameToResponse() {
    final User user = new User(0, "username", "user@email.net", "User", LocalDateTime.MIN);
    final String displayName = "cat";

    final GroupUserResponse groupUserResponse = instance.mapToResponse(user, displayName);

    assertThat(groupUserResponse).isNotNull();
    assertThat(groupUserResponse.user()).isNotNull();
    assertThat(groupUserResponse.user().id()).isZero();
    assertThat(groupUserResponse.displayName()).isEqualTo("cat");
  }

  @ParameterizedTest
  @DisplayName("Should map empty group to null")
  @NullSource
  void shouldMapEmptyGroupUserToNull(final User user) {
    final GroupUserResponse result = instance.mapToResponse(user, null);

    assertThat(result).isNull();
  }
}
