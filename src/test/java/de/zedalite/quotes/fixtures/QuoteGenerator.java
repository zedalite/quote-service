package de.zedalite.quotes.fixtures;

import de.zedalite.quotes.data.mapper.QuoteMapper;
import de.zedalite.quotes.data.model.Quote;
import de.zedalite.quotes.data.model.QuoteMessage;
import de.zedalite.quotes.data.model.QuoteRequest;
import de.zedalite.quotes.data.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class QuoteGenerator {

  private static final QuoteMapper QUOTE_MAPPER = QuoteMapper.INSTANCE;

  public static QuoteRequest getQuoteRequest() {
    return new QuoteRequest("tester", LocalDateTime.now(), "quotes are 42", "@Mars", 1);
  }

  public static Quote getQuote() {
    return new Quote(1, "tester", LocalDateTime.now(), "quotes are awesome", "@Home", 1);
  }

  public static QuoteMessage getQuoteMessage() {
    return QUOTE_MAPPER.mapToQuoteMessage(getQuote(), getMentions());
  }

  public static List<Quote> getQuotes() {
    return List.of(
      new Quote(1, "tester", LocalDateTime.now(), "quotes are awesome", "@Home", 4),
      new Quote(2, "qa", LocalDateTime.MIN, "tests are important", null, 5),
      new Quote(3, "pipeline", LocalDateTime.MAX, "Going brrrrr", "@Work", null)
    );
  }

  public static List<QuoteMessage> getQuoteMessages() {
    return getQuotes().stream().map(quote -> QUOTE_MAPPER.mapToQuoteMessage(quote, getMentions())).toList();
  }

  private static List<User> getMentions() {
    return List.of(new User(12, "mentionedUser", "secret555", "mentionedUser", LocalDateTime.MIN));
  }
}
