package de.zedproject.quotesapi.fixtures;

import de.zedproject.quotesapi.data.model.Quote;
import de.zedproject.quotesapi.data.model.QuoteOfTheDay;
import de.zedproject.quotesapi.data.model.QuoteRequest;

import java.time.LocalDateTime;
import java.util.List;

public class QuoteGenerator {

  public static QuoteRequest getQuoteRequest() {
    return new QuoteRequest("tester", LocalDateTime.now(), "quotes are 42", "@Mars");
  }

  public static Quote getQuote() {
    return new Quote(1, "tester", LocalDateTime.now(), "quotes are awesome", "@Home");
  }

  public static List<Quote> getQuotes() {
    return List.of(
      new Quote(1, "tester", LocalDateTime.now(), "quotes are awesome", "@Home"),
      new Quote(2, "qa", LocalDateTime.MIN, "tests are important", null),
      new Quote(3, "pipeline", LocalDateTime.MAX, "Going brrrrr", "@Work")
    );
  }

  public static QuoteOfTheDay getQuoteOfTheDay() {
    return new QuoteOfTheDay(1, 1, LocalDateTime.now());
  }
}
