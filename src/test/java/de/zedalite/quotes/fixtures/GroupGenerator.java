package de.zedalite.quotes.fixtures;

import de.zedalite.quotes.data.model.Group;
import de.zedalite.quotes.data.model.GroupRequest;
import java.time.LocalDateTime;
import java.util.List;

public class GroupGenerator {

  public static GroupRequest getGroupRequest() {
    return new GroupRequest("test-group", "TestGroup", LocalDateTime.now(), 1);
  }

  public static Group getGroup() {
    return new Group(1, "test-group", "TestGroup", LocalDateTime.now(), 1);
  }

  public static List<Group> getGroups() {
    return List.of(
      new Group(1, "space", "Space", LocalDateTime.now(), 1),
      new Group(2, "outerspace", "Outer Space", LocalDateTime.now(), 1)
    );
  }
}
