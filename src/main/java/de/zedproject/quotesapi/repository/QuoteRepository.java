package de.zedproject.quotesapi.repository;

import de.zedproject.jooq.tables.Quotes;
import de.zedproject.jooq.tables.records.QuotesRecord;
import de.zedproject.quotesapi.data.mapper.QuoteMapper;
import de.zedproject.quotesapi.data.model.Quote;
import de.zedproject.quotesapi.data.model.QuoteRequest;
import de.zedproject.quotesapi.data.model.SortField;
import de.zedproject.quotesapi.data.model.SortOrder;
import de.zedproject.quotesapi.exceptions.QuoteNotFoundException;
import org.jooq.DSLContext;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class QuoteRepository {

  private static final QuoteMapper QUOTE_MAPPER = QuoteMapper.INSTANCE;
  private static final String QUOTE_NOT_FOUND = "Quote not found";
  private static final Quotes QUOTES = Quotes.QUOTES.as("Quotes");
  private final DSLContext dsl;


  public QuoteRepository(final DSLContext dsl) {
    this.dsl = dsl;
  }

  @CachePut(value = "quotes", key = "#result.id()", unless = "#result == null")
  public Quote save(final QuoteRequest quote) throws QuoteNotFoundException {
    final var savedQuote = dsl.insertInto(QUOTES)
      .set(QUOTES.AUTHOR, quote.author())
      .set(QUOTES.DATETIME, quote.datetime())
      .set(QUOTES.TEXT, quote.text())
      .set(QUOTES.SUBTEXT, quote.subtext())
      .returning()
      .fetchOneInto(QuotesRecord.class);
    if (savedQuote == null) throw new QuoteNotFoundException(QUOTE_NOT_FOUND);
    return QUOTE_MAPPER.quoteRecToQuote(savedQuote);
  }

  public List<Quote> findAll() throws QuoteNotFoundException {
    final var quotes = dsl.selectFrom(QUOTES)
      .fetchInto(QuotesRecord.class);
    if (quotes.isEmpty()) throw new QuoteNotFoundException(QUOTE_NOT_FOUND);
    return QUOTE_MAPPER.quoteRecsToQuotes(quotes);
  }

  public List<Quote> findAll(final SortField field, final SortOrder order) {
    final var quotes = dsl.selectFrom(QUOTES)
      .orderBy(sortParamToJooqField(field, order))
      .fetchInto(QuotesRecord.class);
    if (quotes.isEmpty()) throw new QuoteNotFoundException(QUOTE_NOT_FOUND);
    return QUOTE_MAPPER.quoteRecsToQuotes(quotes);
  }

  public List<Quote> findAllByIds(final List<Integer> ids) throws QuoteNotFoundException {
    final var quotes = dsl.selectFrom(QUOTES)
      .where(QUOTES.ID.in(ids))
      .fetchInto(QuotesRecord.class);
    if (quotes.isEmpty()) throw new QuoteNotFoundException(QUOTE_NOT_FOUND);
    return QUOTE_MAPPER.quoteRecsToQuotes(quotes);
  }

  public List<Integer> findAllIds() throws QuoteNotFoundException {
    final var quotesIds = dsl.select(QUOTES.ID)
      .from(QUOTES)
      .fetchInto(Integer.class);
    if (quotesIds.isEmpty()) throw new QuoteNotFoundException(QUOTE_NOT_FOUND);
    return quotesIds;
  }

  @Cacheable(value = "quotes", key = "#id", unless = "#result == null")
  public Quote findById(final Integer id) throws QuoteNotFoundException {
    final var quote = dsl.selectFrom(QUOTES)
      .where(QUOTES.ID.eq(id))
      .fetchOneInto(QuotesRecord.class);
    if (quote == null) throw new QuoteNotFoundException(QUOTE_NOT_FOUND);
    return QUOTE_MAPPER.quoteRecToQuote(quote);
  }

  @CachePut(value = "quotes", key = "#id", unless = "#result == null")
  public Quote update(final Integer id, final QuoteRequest quote) throws QuoteNotFoundException {
    final var updatedQuote = dsl.update(QUOTES)
      .set(QUOTES.AUTHOR, quote.author())
      .set(QUOTES.DATETIME, quote.datetime())
      .set(QUOTES.TEXT, quote.text())
      .set(QUOTES.SUBTEXT, quote.subtext())
      .where(QUOTES.ID.eq(id))
      .returning()
      .fetchOneInto(QuotesRecord.class);
    if (updatedQuote == null) throw new QuoteNotFoundException(QUOTE_NOT_FOUND);
    return QUOTE_MAPPER.quoteRecToQuote(updatedQuote);
  }

  @CacheEvict(value = "quotes", key = "#id")
  public Quote delete(final Integer id) throws QuoteNotFoundException {
    final var deletedQuote = dsl.deleteFrom(QUOTES)
      .where(QUOTES.ID.eq(id))
      .returning()
      .fetchOneInto(QuotesRecord.class);
    if (deletedQuote == null) throw new QuoteNotFoundException(QUOTE_NOT_FOUND);
    return QUOTE_MAPPER.quoteRecToQuote(deletedQuote);
  }

  public Integer count() {
    return dsl.fetchCount(QUOTES);
  }

  private org.jooq.SortField<? extends Comparable<? extends Comparable<?>>> sortParamToJooqField(final SortField field, final SortOrder order) {
    final var jooqField = switch (field) {
      case AUTHOR -> QUOTES.AUTHOR;
      case TEXT -> QUOTES.TEXT;
      default -> QUOTES.DATETIME;
    };

    final var jooqSortField = switch (order) {
      case ASC -> jooqField.asc();
      default -> jooqField.desc();
    };

    return jooqSortField;
  }
}
