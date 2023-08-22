package de.zedalite.quotes.repository;

import de.zedalite.quotes.data.jooq.tables.Users;
import de.zedalite.quotes.data.jooq.tables.records.UsersRecord;
import de.zedalite.quotes.data.mapper.UserMapper;
import de.zedalite.quotes.data.model.User;
import de.zedalite.quotes.data.model.UserRequest;
import de.zedalite.quotes.exceptions.UserNotFoundException;
import org.jooq.DSLContext;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * The UserRepository class is responsible for interacting with the user data in the database.
 * It provides methods for saving and retrieving user information.
 */
@Repository
public class UserRepository {

  private static final UserMapper USER_MAPPER = UserMapper.INSTANCE;

  private static final String USER_NOT_FOUND = "User not found";

  private static final Users USERS = Users.USERS.as("Users");

  private final DSLContext dsl;

  public UserRepository(final DSLContext dsl) {
    this.dsl = dsl;
  }

  /**
   * Saves a user to the database.
   *
   * @param user the user details to be saved
   * @return the saved user
   * @throws UserNotFoundException if the user is not found in the database
   */
  @CachePut(value = "users", key = "#user.name()", unless = "#result == null")
  public User save(final UserRequest user) throws UserNotFoundException {
    final var savedUser = dsl.insertInto(USERS)
      .set(USERS.NAME, user.name())
      .set(USERS.PASSWORD, user.password())
      .set(USERS.CREATION_DATE, LocalDateTime.now())
      .set(USERS.DISPLAY_NAME, user.displayName())
      .returning()
      .fetchOneInto(UsersRecord.class);
    if (savedUser == null) throw new UserNotFoundException(USER_NOT_FOUND);
    return USER_MAPPER.mapToUser(savedUser);
  }

  // TODO Cache result or better integrate in user cache
  public List<User> findAllByIds(final List<Integer> ids) throws UserNotFoundException {
    final var users = dsl.selectFrom(USERS)
      .where(USERS.ID.in(ids))
      .fetchInto(UsersRecord.class);
    if (users.isEmpty()) throw new UserNotFoundException(USER_NOT_FOUND);
    return USER_MAPPER.mapToUserList(users);
  }

  @Cacheable(value = "users", key = "#name", unless = "#result == null")
  public User findByName(final String name) {
    final var user = dsl.selectFrom(USERS)
      .where(USERS.NAME.eq(name))
      .fetchOneInto(UsersRecord.class);
    if (user == null) throw new UserNotFoundException(USER_NOT_FOUND);
    return USER_MAPPER.mapToUser(user);
  }

  @Cacheable(value = "usernames", key = "#name")
  public boolean isUsernameAvailable(final String name) {
    final var count = dsl.selectCount().from(USERS).where(USERS.NAME.eq(name)).fetchAnyInto(Integer.class);
    return count != null && count == 0;
  }

  @CachePut(value = "users", key = "#name", unless = "#result == null")
  public User update(final String name, final UserRequest user) throws UserNotFoundException {
    final var updatedUser = dsl.update(USERS)
      .set(USERS.NAME, user.name())
      .set(USERS.PASSWORD, user.password())
      .set(USERS.DISPLAY_NAME, user.displayName())
      .where(USERS.NAME.eq(name))
      .returning()
      .fetchOneInto(UsersRecord.class);
    if (updatedUser == null) throw new UserNotFoundException(USER_NOT_FOUND);
    return USER_MAPPER.mapToUser(updatedUser);
  }
}
