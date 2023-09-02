package de.zedalite.quotes.repository;

import de.zedalite.quotes.data.jooq.tables.GroupQuotes;
import de.zedalite.quotes.data.jooq.tables.Quotes;
import de.zedalite.quotes.data.jooq.tables.records.QuotesRecord;
import de.zedalite.quotes.data.mapper.QuoteMapper;
import de.zedalite.quotes.data.model.Quote;
import de.zedalite.quotes.data.model.QuoteRequest;
import de.zedalite.quotes.data.model.SortField;
import de.zedalite.quotes.data.model.SortOrder;
import de.zedalite.quotes.exceptions.QuoteNotFoundException;
import org.jooq.DSLContext;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GroupQuoteRepository {

  private static final QuoteMapper QUOTE_MAPPER = QuoteMapper.INSTANCE;

  private static final Quotes QUOTES = Quotes.QUOTES.as("quotes");

  private static final GroupQuotes GROUP_QUOTES = GroupQuotes.GROUP_QUOTES.as("group_quotes");

  private static final String GROUP_QUOTE_NOT_FOUND = "Group quote not found";

  private final DSLContext dsl;

  public GroupQuoteRepository(final DSLContext dsl) {
    this.dsl = dsl;
  }

  @CachePut(value = "group_quotes", key = "{#id,#result.id}", unless = "#result == null")
  public Quote save(final Integer id, final QuoteRequest quote) {
    final var savedQuoteRec = dsl.insertInto(QUOTES)
      .set(QUOTES.AUTHOR, quote.author())
      .set(QUOTES.CREATION_DATE, quote.creationDate())
      .set(QUOTES.TEXT, quote.text())
      .set(QUOTES.CONTEXT, quote.context())
      .set(QUOTES.CREATOR_ID, quote.creatorId())
      .returning()
      .fetchOptionalInto(QuotesRecord.class);
    if (savedQuoteRec.isEmpty()) throw new QuoteNotFoundException(GROUP_QUOTE_NOT_FOUND);
    final var savedQuote = QUOTE_MAPPER.mapToQuote(savedQuoteRec.get());

    dsl.insertInto(GROUP_QUOTES)
      .set(GROUP_QUOTES.GROUP_ID, id)
      .set(GROUP_QUOTES.QUOTE_ID, savedQuote.id())
      .execute();

    return savedQuote;
  }

  @Cacheable(value = "group_quotes", key = "{#id,#quoteId}", unless = "#result = null")
  public Quote findById(final Integer id, final Integer quoteId) {
    final var quote = dsl.select(QUOTES)
      .from(GROUP_QUOTES.join(QUOTES).on(GROUP_QUOTES.QUOTE_ID.eq(QUOTES.ID)))
      .where(GROUP_QUOTES.GROUP_ID.eq(id)
        .and(GROUP_QUOTES.QUOTE_ID.eq(quoteId)))
      .fetchOptionalInto(Quote.class);
    if (quote.isEmpty()) throw new QuoteNotFoundException(GROUP_QUOTE_NOT_FOUND);
    return quote.get();
  }

  // TODO implement caching
  public List<Quote> findAll(final Integer id, final SortField field, final SortOrder order) {
    final var quotes = dsl.select(QUOTES)
      .from(GROUP_QUOTES.join(QUOTES).on(GROUP_QUOTES.QUOTE_ID.eq(QUOTES.ID)))
      .where(GROUP_QUOTES.GROUP_ID.eq(id))
      .orderBy(mapToJooqSortField(field, order))
      .fetchInto(Quote.class);
    if (quotes.isEmpty()) throw new QuoteNotFoundException(GROUP_QUOTE_NOT_FOUND);
    return quotes;
  }

  public List<Quote> findAllByIds(final Integer id, final List<Integer> quoteIds) throws QuoteNotFoundException {
    final var quotes = dsl.select(QUOTES)
      .from(GROUP_QUOTES.join(QUOTES).on(GROUP_QUOTES.QUOTE_ID.eq(QUOTES.ID)))
      .where(GROUP_QUOTES.GROUP_ID.eq(id))
      .and(QUOTES.ID.in(quoteIds))
      .fetchInto(Quote.class);
    if (quotes.isEmpty()) throw new QuoteNotFoundException(GROUP_QUOTE_NOT_FOUND);
    return quotes;
  }

  public List<Integer> findAllIds(final Integer id) {
    final var quotesIds = dsl.select(GROUP_QUOTES.QUOTE_ID)
      .from(GROUP_QUOTES)
      .where(GROUP_QUOTES.GROUP_ID.eq(id))
      .fetchInto(Integer.class);
    if (quotesIds.isEmpty()) throw new QuoteNotFoundException(GROUP_QUOTE_NOT_FOUND);
    return quotesIds;
  }

  public Integer count(final Integer id) {
    return dsl.fetchCount(GROUP_QUOTES, GROUP_QUOTES.GROUP_ID.eq(id));
  }

  private org.jooq.SortField<? extends Comparable<? extends Comparable<?>>> mapToJooqSortField(final SortField field, final SortOrder order) {
    final var jooqField = switch (field) {
      case AUTHOR -> QUOTES.AUTHOR;
      case TEXT -> QUOTES.TEXT;
      default -> QUOTES.CREATION_DATE;
    };

    return order == SortOrder.ASC ? jooqField.asc() : jooqField.desc();
  }
}
