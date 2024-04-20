package de.zedalite.quotes.repository;

import de.zedalite.quotes.data.jooq.quotes.tables.Quotes;
import de.zedalite.quotes.data.jooq.quotes.tables.records.QuotesRecord;
import de.zedalite.quotes.data.mapper.QuoteMapper;
import de.zedalite.quotes.data.model.Quote;
import de.zedalite.quotes.data.model.QuoteRequest;
import de.zedalite.quotes.exception.QuoteNotFoundException;
import java.time.LocalDateTime;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
public class QuoteRepository {

  private static final QuoteMapper QUOTE_MAPPER = QuoteMapper.INSTANCE;
  private static final String QUOTE_NOT_FOUND = "Quote not found";
  private static final Quotes QUOTES = Quotes.QUOTES_.as("quotes");

  private final DSLContext dsl;

  public QuoteRepository(final DSLContext dsl) {
    this.dsl = dsl;
  }

  public Integer count() {
    return dsl.fetchCount(QUOTES);
  }

  public Quote save(final QuoteRequest quote, final Integer creatorId) throws QuoteNotFoundException {
    final QuotesRecord savedQuote = dsl
      .insertInto(QUOTES)
      .set(QUOTES.AUTHOR, quote.author())
      .set(QUOTES.CREATION_DATE, LocalDateTime.now())
      .set(QUOTES.TEXT, quote.text())
      .set(QUOTES.CONTEXT, quote.context())
      .set(QUOTES.CREATOR_ID, creatorId)
      .returning()
      .fetchOneInto(QuotesRecord.class);
    if (savedQuote == null) throw new QuoteNotFoundException(QUOTE_NOT_FOUND);
    return QUOTE_MAPPER.mapToQuote(savedQuote);
  }
}
