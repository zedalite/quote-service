package de.zedalite.quotes.data.mapper;

import de.zedalite.quotes.data.jooq.quotes.tables.records.GroupUsersRecord;
import de.zedalite.quotes.data.model.GroupUser;
import de.zedalite.quotes.data.model.GroupUserResponse;
import de.zedalite.quotes.data.model.User;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = { OptionalMapper.class, UserMapper.class })
public interface GroupUserMapper {
  GroupUserMapper INSTANCE = Mappers.getMapper(GroupUserMapper.class);

  GroupUser mapToGroupUser(final GroupUsersRecord groupUsersRecord);

  List<GroupUser> mapToGroupUsers(List<GroupUsersRecord> users);

  @Mapping(target = "user", source = "user")
  GroupUserResponse mapToResponse(final User user, final String displayName);
}
