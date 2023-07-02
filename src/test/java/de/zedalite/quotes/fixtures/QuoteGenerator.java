package de.zedalite.quotes.fixtures;

import de.zedalite.quotes.data.model.Quote;
import de.zedalite.quotes.data.model.QuoteRequest;
import de.zedalite.quotes.data.model.QuoteOfTheDay;

import java.time.LocalDateTime;
import java.util.List;

public class QuoteGenerator {

  public static QuoteRequest getQuoteRequest() {
    return new QuoteRequest("tester", LocalDateTime.now(), "quotes are 42", "@Mars",1);
  }

  public static Quote getQuote() {
    return new Quote(1, "tester", LocalDateTime.now(), "quotes are awesome", "@Home",1);
  }

  public static List<Quote> getQuotes() {
    return List.of(
      new Quote(1, "tester", LocalDateTime.now(), "quotes are awesome", "@Home",4),
      new Quote(2, "qa", LocalDateTime.MIN, "tests are important", null,5),
      new Quote(3, "pipeline", LocalDateTime.MAX, "Going brrrrr", "@Work",null)
    );
  }

  public static QuoteOfTheDay getQuoteOfTheDay() {
    return new QuoteOfTheDay(1, 1, LocalDateTime.now());
  }
}
