package de.zedalite.quotes.data.mapper;

import de.zedalite.quotes.data.jooq.users.tables.records.UsersRecord;
import de.zedalite.quotes.data.model.User;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * This interface represents the UserMapper which is responsible for mapping the UsersRecord object to User object.
 */
@Mapper
public interface UserMapper {
  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  User mapToUser(final UsersRecord usersRecord);

  List<User> mapToUserList(final List<UsersRecord> users);
}
