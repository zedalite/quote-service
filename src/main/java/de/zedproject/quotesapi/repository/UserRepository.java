package de.zedproject.quotesapi.repository;

import de.zedproject.jooq.tables.Users;
import de.zedproject.jooq.tables.records.UsersRecord;
import de.zedproject.quotesapi.data.mapper.UserMapper;
import de.zedproject.quotesapi.data.model.User;
import de.zedproject.quotesapi.data.model.UserRequest;
import de.zedproject.quotesapi.exceptions.ResourceNotFoundException;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
  private static final Users USERS = Users.USERS.as("Users");
  public static final UserMapper USER_MAPPER = UserMapper.INSTANCE;
  public static final String USER_NOT_FOUND = "User not found";
  private final DSLContext dsl;

  public UserRepository(DSLContext dsl) {
    this.dsl = dsl;
  }

  public User save(final UserRequest user) throws ResourceNotFoundException {
    final var savedUser = dsl.insertInto(USERS)
        .set(USERS.NAME, user.name())
        .set(USERS.PASSWORD, user.password())
        .returning()
        .fetchOneInto(UsersRecord.class);
    if (savedUser == null) throw new ResourceNotFoundException(USER_NOT_FOUND);
    return USER_MAPPER.userRecToUser(savedUser);
  }

  public User findByName(final String name) throws ResourceNotFoundException {
    final var user = dsl.selectFrom(USERS)
        .where(USERS.NAME.eq(name))
        .fetchOneInto(UsersRecord.class);
    if (user == null) throw new ResourceNotFoundException(USER_NOT_FOUND);
    return USER_MAPPER.userRecToUser(user);
  }
}
