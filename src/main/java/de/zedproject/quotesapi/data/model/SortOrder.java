package de.zedproject.quotesapi.data.model;

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
}
