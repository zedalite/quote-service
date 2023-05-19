package de.zedproject.quotesapi.data.mapper;

import de.zedproject.jooq.tables.records.UsersRecord;
import de.zedproject.quotesapi.data.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  User userRecToUser(final UsersRecord usersRecord);
}
