package de.zedalite.quotes.fixtures;

import de.zedalite.quotes.data.model.GroupUser;
import de.zedalite.quotes.data.model.GroupUserResponse;
import de.zedalite.quotes.data.model.UserResponse;

public class GroupUserGenerator {

  public static GroupUser getGroupUser() {
    return new GroupUser(1, 1, "KING OF THE WORLD");
  }

  public static GroupUserResponse getGroupUserResponse() {
    final UserResponse userResponse = new UserResponse(1, "tester", "TESTER", null);
    return new GroupUserResponse(userResponse, "KING OF THE WORLD");
  }
}
