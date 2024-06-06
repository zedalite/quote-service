package de.zedalite.quotes.data.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import de.zedalite.quotes.data.jooq.quotes.tables.records.GroupsRecord;
import de.zedalite.quotes.data.model.Group;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

class GroupMapperTest {

  private static final GroupMapper instance = GroupMapper.INSTANCE;

  @Test
  @DisplayName("Should map groupsRecord to group")
  void shouldMapGroupsRecordToGroup() {
    final GroupsRecord groupRec = new GroupsRecord(0, "groupcode", "GROUP", LocalDateTime.MIN, 1);

    final Group group = instance.mapToGroup(groupRec);

    assertThat(group).isNotNull();
    assertThat(group.id()).isZero();
    assertThat(group.inviteCode()).isEqualTo("groupcode");
    assertThat(group.displayName()).isEqualTo("GROUP");
    assertThat(group.creationDate()).isEqualTo(LocalDateTime.MIN);
    assertThat(group.creatorId()).isEqualTo(Optional.of(1));
  }

  @ParameterizedTest
  @DisplayName("Should map empty groupRecord to null")
  @NullSource
  void shouldMapEmptyGroupRecordToNull(final GroupsRecord groupsRecord) {
    final Group group = instance.mapToGroup(groupsRecord);

    assertThat(group).isNull();
  }

  @Test
  @DisplayName("Should map groupRecords to groups")
  void shouldMapGroupRecordsToGroups() {
    final GroupsRecord groupRec1 = new GroupsRecord(0, "group", "GROUP", LocalDateTime.MIN, 1);
    final GroupsRecord groupRec2 = new GroupsRecord(1, "group2", "GROUP_2", LocalDateTime.MIN, 1);

    final List<Group> groups = instance.mapToGroups(List.of(groupRec1, groupRec2));

    assertThat(groups).hasSize(2);
  }

  @ParameterizedTest
  @DisplayName("Should map empty groupRecords to null")
  @NullSource
  void shouldMapEmptyGroupRecordsToNull(final List<GroupsRecord> groupsRecords) {
    final List<Group> group = instance.mapToGroups(groupsRecords);

    assertThat(group).isNull();
  }
}
