package de.zedalite.quotes.data.mapper;

import de.zedalite.quotes.data.jooq.tables.records.UsersRecord;
import de.zedalite.quotes.data.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  User userRecToUser(final UsersRecord usersRecord);
}
