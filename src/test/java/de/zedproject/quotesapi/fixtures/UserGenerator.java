package de.zedproject.quotesapi.fixtures;

import de.zedproject.quotesapi.data.model.User;
import de.zedproject.quotesapi.data.model.UserRequest;

public class UserGenerator {

  public static UserRequest getUserRequest() {
    return new UserRequest("tester", "apple");
  }

  public static User getUser() {
    return new User(1,"tester", "apple");
  }

}
