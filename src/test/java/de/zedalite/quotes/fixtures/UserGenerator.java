package de.zedalite.quotes.fixtures;

import de.zedalite.quotes.data.model.User;
import de.zedalite.quotes.data.model.UserRequest;

public class UserGenerator {

  public static UserRequest getUserRequest() {
    return new UserRequest("tester", "apple");
  }

  public static User getUser() {
    return new User(1, "tester", "apple");
  }

}
