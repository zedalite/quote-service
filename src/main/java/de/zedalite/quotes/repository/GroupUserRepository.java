package de.zedalite.quotes.repository;

import de.zedalite.quotes.data.jooq.quotes.tables.GroupUsers;
import de.zedalite.quotes.data.jooq.quotes.tables.Groups;
import de.zedalite.quotes.data.jooq.users.tables.Users;
import de.zedalite.quotes.data.model.Group;
import de.zedalite.quotes.data.model.User;
import de.zedalite.quotes.exception.GroupNotFoundException;
import de.zedalite.quotes.exception.UserNotFoundException;
import java.util.List;
import java.util.Optional;
import org.jooq.DSLContext;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

@Repository
public class GroupUserRepository {

  private static final Groups GROUPS = Groups.GROUPS.as("groups");

  private static final Users USERS = Users.USERS_.as("users");

  private static final GroupUsers GROUP_USERS = GroupUsers.GROUP_USERS.as("group_users");

  private static final String GROUP_USER_NOT_FOUND = "Group user not found";

  private final DSLContext dsl;

  public GroupUserRepository(final DSLContext dsl) {
    this.dsl = dsl;
  }

  public Boolean save(final Integer id, final Integer userId) {
    final boolean isSaved =
      dsl.insertInto(GROUP_USERS).set(GROUP_USERS.GROUP_ID, id).set(GROUP_USERS.USER_ID, userId).execute() == 1;
    if (!isSaved) throw new UserNotFoundException(GROUP_USER_NOT_FOUND);
    return true;
  }

  @Cacheable(value = "group_users", key = "{#id,#userId}", unless = "#result = null")
  public User findById(final Integer id, final Integer userId) {
    final Optional<User> user = dsl
      .select(USERS)
      .from(GROUP_USERS.join(USERS).on(GROUP_USERS.USER_ID.eq(USERS.ID)))
      .where(GROUP_USERS.GROUP_ID.eq(id).and(GROUP_USERS.USER_ID.eq(userId)))
      .fetchOptionalInto(User.class);
    if (user.isEmpty()) throw new UserNotFoundException(GROUP_USER_NOT_FOUND);
    return user.get();
  }

  public List<User> findUsers(final Integer groupId) {
    final List<User> users = dsl
      .select(USERS)
      .from(GROUP_USERS.join(USERS).on(GROUP_USERS.USER_ID.eq(USERS.ID)))
      .where(GROUP_USERS.GROUP_ID.eq(groupId))
      .fetchInto(User.class);
    if (users.isEmpty()) throw new UserNotFoundException(GROUP_USER_NOT_FOUND);
    return users;
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
