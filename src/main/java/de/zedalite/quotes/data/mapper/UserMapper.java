package de.zedalite.quotes.data.mapper;

import de.zedalite.quotes.data.jooq.tables.records.UsersRecord;
import de.zedalite.quotes.data.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * This interface represents the UserMapper which is responsible for mapping the UsersRecord object to User object.
 */
@Mapper
public interface UserMapper {
  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  User userRecToUser(final UsersRecord usersRecord);

  List<User> userRecsToUsers(final List<UsersRecord> users);
}
