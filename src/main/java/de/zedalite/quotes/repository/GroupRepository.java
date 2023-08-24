package de.zedalite.quotes.repository;

import de.zedalite.quotes.data.jooq.tables.Groups;
import de.zedalite.quotes.data.jooq.tables.records.GroupsRecord;
import de.zedalite.quotes.data.mapper.GroupMapper;
import de.zedalite.quotes.data.model.Group;
import de.zedalite.quotes.data.model.GroupRequest;
import de.zedalite.quotes.exceptions.GroupNotFoundException;
import de.zedalite.quotes.exceptions.QuoteNotFoundException;
import org.jooq.DSLContext;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

/**
 * Provides methods for interacting with the database to perform CRUD operations on quotes.
 */
@Repository
public class GroupRepository {

  private static final GroupMapper GROUP_MAPPER = GroupMapper.INSTANCE;

  private static final String GROUP_NOT_FOUND = "Group not found";

  private static final Groups GROUPS = Groups.GROUPS.as("groups");

  private final DSLContext dsl;

  public GroupRepository(final DSLContext dsl) {
    this.dsl = dsl;
  }

  @CachePut(value = "groups", key = "#result.id()", unless = "#result == null")
  public Group save(final GroupRequest group) throws QuoteNotFoundException {
    final var savedGroup = dsl.insertInto(GROUPS)
      .set(GROUPS.NAME, group.name())
      .set(GROUPS.DISPLAY_NAME, group.displayName())
      .set(GROUPS.CREATION_DATE, group.creationDate())
      .set(GROUPS.CREATOR_ID, group.creatorId())
      .returning()
      .fetchOptionalInto(GroupsRecord.class);
    if (savedGroup.isEmpty()) throw new GroupNotFoundException(GROUP_NOT_FOUND);
    return GROUP_MAPPER.mapToGroup(savedGroup.get());
  }

  @Cacheable(value = "groups", key = "#id", unless = "#result == null")
  public Group findById(final Integer id) throws GroupNotFoundException {
    final var group = dsl.selectFrom(GROUPS)
      .where(GROUPS.ID.eq(id))
      .fetchOptionalInto(GroupsRecord.class);
    if (group.isEmpty()) throw new GroupNotFoundException(GROUP_NOT_FOUND);
    return GROUP_MAPPER.mapToGroup(group.get());
  }
}
