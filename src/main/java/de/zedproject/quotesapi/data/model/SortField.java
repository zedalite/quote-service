package de.zedproject.quotesapi.data.model;

public enum SortField {
  AUTHOR("author"),
  DATETIME("datetime"),
  TEXT("text");

  private final String name;

  SortField(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
