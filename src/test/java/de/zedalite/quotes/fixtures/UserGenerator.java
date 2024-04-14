package de.zedalite.quotes.fixtures;

import de.zedalite.quotes.data.model.DisplayNameRequest;
import de.zedalite.quotes.data.model.User;
import de.zedalite.quotes.data.model.UserPrincipal;
import de.zedalite.quotes.data.model.UserRequest;
import java.time.LocalDateTime;
import java.util.List;

public class UserGenerator {

  public static UserRequest getUserRequest() {
    return new UserRequest("tester", "apple");
  }

  public static User getUser() {
    return new User(1, "tester", "apple", "TESTER", LocalDateTime.now());
  }

  public static UserPrincipal getUserPrincipal() {
    return new UserPrincipal(new User(1, "tester", "test", "TESTER", LocalDateTime.now()));
  }

  public static List<User> getUsers() {
    return List.of(
      new User(1, "tester", "apple", "TESTER", LocalDateTime.now()),
      new User(2, "manager", "best", "MANAGER", LocalDateTime.MIN)
    );
  }

  public static DisplayNameRequest getDisplayNameRequest() {
    return new DisplayNameRequest("Warlord");
  }
}
