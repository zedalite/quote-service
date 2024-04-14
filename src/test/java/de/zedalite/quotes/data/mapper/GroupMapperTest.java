package de.zedalite.quotes.data.mapper;

import de.zedalite.quotes.data.jooq.quotes.tables.records.GroupsRecord;
import de.zedalite.quotes.data.model.Group;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class GroupMapperTest {

  private static final GroupMapper instance = GroupMapper.INSTANCE;

  @Test
  @DisplayName("Should map groupsRecord to group")
  void shouldMapGroupsRecordToGroup() {
    final GroupsRecord groupRec = new GroupsRecord(0, "group", "GROUP", LocalDateTime.MIN, 1);

    final Group group = instance.mapToGroup(groupRec);

    assertThat(group).isNotNull();
    assertThat(group.id()).isZero();
    assertThat(group.name()).isEqualTo("group");
    assertThat(group.displayName()).isEqualTo("GROUP");
    assertThat(group.creationDate()).isEqualTo(LocalDateTime.MIN);
    assertThat(group.creatorId()).isOne();
  }

  @ParameterizedTest
  @DisplayName("Should map empty groupRecord to null")
  @NullSource
  void shouldMapEmptyGroupRecordToNull(final GroupsRecord groupsRecord) {
    final Group group = instance.mapToGroup(groupsRecord);

    assertThat(group).isNull();
  }

}
