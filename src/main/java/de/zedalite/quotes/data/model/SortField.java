package de.zedalite.quotes.data.model;

import java.util.Arrays;

public enum SortField {
  AUTHOR("author"),
  CREATION_DATE("creationDate"),
  TEXT("text");

  private final String name;

  SortField(final String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public static SortField getByName(final String name) {
    return Arrays.stream(SortField.values())
      .filter(sortField -> sortField.name.equals(name))
      .findFirst()
      .orElseThrow(IllegalArgumentException::new);
  }
}
