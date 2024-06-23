package de.zedalite.quotes.repository;

import de.zedalite.quotes.data.jooq.quotes.tables.GroupUsers;
import de.zedalite.quotes.data.jooq.quotes.tables.Groups;
import de.zedalite.quotes.data.jooq.quotes.tables.records.GroupUsersRecord;
import de.zedalite.quotes.data.mapper.GroupUserMapper;
import de.zedalite.quotes.data.model.Group;
import de.zedalite.quotes.data.model.GroupUser;
import de.zedalite.quotes.data.model.GroupUserRequest;
import de.zedalite.quotes.exception.GroupNotFoundException;
import de.zedalite.quotes.exception.UserNotFoundException;
import java.util.List;
import java.util.Optional;
import org.jooq.DSLContext;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

@Repository
public class GroupUserRepository {

  private static final GroupUserMapper GROUP_USER_MAPPER = GroupUserMapper.INSTANCE;
  private static final Groups GROUPS = Groups.GROUPS.as("groups");
  private static final GroupUsers GROUP_USERS = GroupUsers.GROUP_USERS.as("group_users");
  private static final String GROUP_USER_NOT_FOUND = "Group user not found";

  private final DSLContext dsl;

  public GroupUserRepository(final DSLContext dsl) {
    this.dsl = dsl;
  }

  public GroupUser save(final Integer id, final GroupUserRequest request) {
    final Optional<GroupUsersRecord> savedGroupUser = dsl
      .insertInto(GROUP_USERS)
      .set(GROUP_USERS.GROUP_ID, id)
      .set(GROUP_USERS.USER_ID, request.userId())
      .set(GROUP_USERS.USER_DISPLAY_NAME, request.displayName())
      .returning()
      .fetchOptionalInto(GroupUsersRecord.class);

    if (savedGroupUser.isEmpty()) throw new UserNotFoundException(GROUP_USER_NOT_FOUND);

    return GROUP_USER_MAPPER.mapToGroupUser(savedGroupUser.get());
  }

  @Cacheable(value = "group_users", key = "{#id,#userId}", unless = "#result = null")
  public GroupUser findById(final Integer id, final Integer userId) {
    final Optional<GroupUsersRecord> user = dsl
      .selectFrom(GROUP_USERS)
      .where(GROUP_USERS.GROUP_ID.eq(id).and(GROUP_USERS.USER_ID.eq(userId)))
      .fetchOptionalInto(GroupUsersRecord.class);

    if (user.isEmpty()) throw new UserNotFoundException(GROUP_USER_NOT_FOUND);

    return GROUP_USER_MAPPER.mapToGroupUser(user.get());
  }

  public List<GroupUser> findUsers(final Integer groupId) {
    final List<GroupUsersRecord> users = dsl
      .selectFrom(GROUP_USERS)
      .where(GROUP_USERS.GROUP_ID.eq(groupId))
      .fetchInto(GroupUsersRecord.class);
    if (users.isEmpty()) throw new UserNotFoundException(GROUP_USER_NOT_FOUND);
    return GROUP_USER_MAPPER.mapToGroupUsers(users);
  }

  public List<Group> findGroups(final Integer userId) {
    final List<Group> groups = dsl
      .select(GROUPS)
      .from(GROUP_USERS.join(GROUPS).on(GROUP_USERS.GROUP_ID.eq(GROUPS.ID)))
      .where(GROUP_USERS.USER_ID.eq(userId))
      .fetchInto(Group.class);
    if (groups.isEmpty()) throw new GroupNotFoundException(GROUP_USER_NOT_FOUND);
    return groups;
  }

  public void delete(final Integer id, final Integer userId) {
    final boolean isDeleted =
      dsl.deleteFrom(GROUP_USERS).where(GROUP_USERS.GROUP_ID.eq(id)).and(GROUP_USERS.USER_ID.eq(userId)).execute() == 1;
    if (!isDeleted) throw new GroupNotFoundException(GROUP_USER_NOT_FOUND);
  }

  public boolean isUserInGroup(final Integer id, final Integer userId) {
    return dsl.fetchExists(
      dsl.selectFrom(GROUP_USERS).where(GROUP_USERS.GROUP_ID.eq(id)).and(GROUP_USERS.USER_ID.eq(userId))
    );
  }
}
