package de.zedalite.quotes.fixtures;

import de.zedalite.quotes.data.mapper.GroupMapper;
import de.zedalite.quotes.data.model.Group;
import de.zedalite.quotes.data.model.GroupRequest;

import java.time.LocalDateTime;

public class GroupGenerator {

  public static GroupRequest getGroupRequest() {
    return new GroupRequest("test-group", "TestGroup", LocalDateTime.now(), 1);
  }

  public static Group getGroup() {
    return new Group(1, "test-group", "TestGroup", LocalDateTime.now(), 1);
  }
}
