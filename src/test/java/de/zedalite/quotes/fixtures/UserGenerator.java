package de.zedalite.quotes.fixtures;

import de.zedalite.quotes.data.model.User;
import de.zedalite.quotes.data.model.UserRequest;

import java.time.LocalDateTime;

public class UserGenerator {

  public static UserRequest getUserRequest() {
    return new UserRequest("tester", "apple");
  }

  public static User getUser() {
    return new User(1, "tester", "apple", "TESTER", LocalDateTime.now());
  }

}
