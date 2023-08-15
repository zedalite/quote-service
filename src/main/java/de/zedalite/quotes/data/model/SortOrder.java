package de.zedalite.quotes.data.model;

import java.util.Arrays;

public enum SortOrder {
  ASC("asc"),
  DESC("desc");

  private final String name;

  SortOrder(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public static SortOrder getByName(final String name) {
    return Arrays.stream(SortOrder.values())
      .filter(sortOrder -> sortOrder.name.equals(name))
      .findFirst()
      .orElseThrow(IllegalArgumentException::new);
  }
}
