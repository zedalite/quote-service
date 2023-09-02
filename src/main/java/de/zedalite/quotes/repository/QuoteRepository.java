package de.zedalite.quotes.repository;

import de.zedalite.quotes.data.jooq.tables.Quotes;
import de.zedalite.quotes.data.jooq.tables.records.QuotesRecord;
import de.zedalite.quotes.data.mapper.QuoteMapper;
import de.zedalite.quotes.data.model.Quote;
import de.zedalite.quotes.data.model.QuoteRequest;
import de.zedalite.quotes.data.model.SortField;
import de.zedalite.quotes.data.model.SortOrder;
import de.zedalite.quotes.exceptions.QuoteNotFoundException;
import org.jooq.DSLContext;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Provides methods for interacting with the database to perform CRUD operations on quotes.
 */
@Repository
public class QuoteRepository {

  private static final QuoteMapper QUOTE_MAPPER = QuoteMapper.INSTANCE;
  private static final String QUOTE_NOT_FOUND = "Quote not found";
  private static final Quotes QUOTES = Quotes.QUOTES.as("Quotes");
  private final DSLContext dsl;


  public QuoteRepository(final DSLContext dsl) {
    this.dsl = dsl;
  }

  /**
   * Saves a quote by inserting it into the database and returning the saved quote.
   *
   * @param quote The QuoteRequest object representing the quote to be saved.
   * @return The saved Quote object.
   * @throws QuoteNotFoundException If the saved quote is null.
   */
  @CachePut(value = "quotes", key = "#result.id()", unless = "#result == null")
  public Quote save(final QuoteRequest quote) throws QuoteNotFoundException {
    final var savedQuote = dsl.insertInto(QUOTES)
      .set(QUOTES.AUTHOR, quote.author())
      .set(QUOTES.CREATION_DATE, quote.creationDate())
      .set(QUOTES.TEXT, quote.text())
      .set(QUOTES.CONTEXT, quote.context())
      .set(QUOTES.CREATOR_ID, quote.creatorId())
      .returning()
      .fetchOneInto(QuotesRecord.class);
    if (savedQuote == null) throw new QuoteNotFoundException(QUOTE_NOT_FOUND);
    return QUOTE_MAPPER.mapToQuote(savedQuote);
  }

  /**
   * Retrieves all quotes from the database and returns them.
   *
   * @return A list of Quote objects representing all the quotes in the database.
   * @throws QuoteNotFoundException If no quotes are found in the database.
   */
  public List<Quote> findAll() throws QuoteNotFoundException {
    final var quotes = dsl.selectFrom(QUOTES)
      .fetchInto(QuotesRecord.class);
    if (quotes.isEmpty()) throw new QuoteNotFoundException(QUOTE_NOT_FOUND);
    return QUOTE_MAPPER.mapToQuoteList(quotes);
  }

  /**
   * Retrieves all quotes from the database and returns them, sorted by the specified field and order.
   *
   * @param field The field to sort the quotes by.
   * @param order The order in which to sort the quotes.
   * @return A list of Quote objects representing all the quotes in the database, sorted by the specified field and order.
   * @throws QuoteNotFoundException If no quotes are found in the database.
   */
  public List<Quote> findAll(final SortField field, final SortOrder order) {
    final var quotes = dsl.selectFrom(QUOTES)
      .orderBy(mapToJooqSortField(field, order))
      .fetchInto(QuotesRecord.class);
    if (quotes.isEmpty()) throw new QuoteNotFoundException(QUOTE_NOT_FOUND);
    return QUOTE_MAPPER.mapToQuoteList(quotes);
  }

  /**
   * Retrieves a list of quotes from the database based on the given list of IDs.
   *
   * @param ids A list of integers representing the IDs of the quotes to retrieve.
   * @return A list of Quote objects representing the quotes with matching IDs.
   * @throws QuoteNotFoundException If no quotes with the given IDs are found in the database.
   */
  public List<Quote> findAllByIds(final List<Integer> ids) throws QuoteNotFoundException {
    final var quotes = dsl.selectFrom(QUOTES)
      .where(QUOTES.ID.in(ids))
      .fetchInto(QuotesRecord.class);
    if (quotes.isEmpty()) throw new QuoteNotFoundException(QUOTE_NOT_FOUND);
    return QUOTE_MAPPER.mapToQuoteList(quotes);
  }

  /**
   * Retrieves a list of all quote IDs from the database.
   *
   * @return A list of integers representing the IDs of all quotes in the database.
   * @throws QuoteNotFoundException If no quotes are found in the database.
   */
  public List<Integer> findAllIds() throws QuoteNotFoundException {
    final var quotesIds = dsl.select(QUOTES.ID)
      .from(QUOTES)
      .fetchInto(Integer.class);
    if (quotesIds.isEmpty()) throw new QuoteNotFoundException(QUOTE_NOT_FOUND);
    return quotesIds;
  }

  /**
   * Retrieves a quote from the database based on its ID.
   *
   * @param id The ID of the quote to retrieve.
   * @return The quote with the specified ID, or null if no quote is found.
   * @throws QuoteNotFoundException If no quote is found with the specified ID.
   */
  @Cacheable(value = "quotes", key = "#id", unless = "#result == null")
  public Quote findById(final Integer id) throws QuoteNotFoundException {
    final var quote = dsl.selectFrom(QUOTES)
      .where(QUOTES.ID.eq(id))
      // TODO use fetchOptionalInto
      .fetchOneInto(QuotesRecord.class);
    if (quote == null) throw new QuoteNotFoundException(QUOTE_NOT_FOUND);
    return QUOTE_MAPPER.mapToQuote(quote);
  }

  /**
   * Updates a quote in the database with the specified ID.
   *
   * @param id    The ID of the quote to update.
   * @param quote The updated quote request object.
   * @return The updated quote.
   * @throws QuoteNotFoundException If no quote is found with the specified ID.
   */
  @CachePut(value = "quotes", key = "#id", unless = "#result == null")
  public Quote update(final Integer id, final QuoteRequest quote) throws QuoteNotFoundException {
    final var updatedQuote = dsl.update(QUOTES)
      .set(QUOTES.AUTHOR, quote.author())
      .set(QUOTES.CREATION_DATE, quote.creationDate())
      .set(QUOTES.TEXT, quote.text())
      .set(QUOTES.CONTEXT, quote.context())
      .set(QUOTES.CREATOR_ID, quote.creatorId())
      .where(QUOTES.ID.eq(id))
      .returning()
      .fetchOneInto(QuotesRecord.class);
    if (updatedQuote == null) throw new QuoteNotFoundException(QUOTE_NOT_FOUND);
    return QUOTE_MAPPER.mapToQuote(updatedQuote);
  }

  /**
   * Deletes a quote from the database with the specified ID.
   *
   * @param id The ID of the quote to delete.
   * @return The deleted quote.
   * @throws QuoteNotFoundException If no quote is found with the specified ID.
   */
  @CacheEvict(value = "quotes", key = "#id")
  public Quote delete(final Integer id) throws QuoteNotFoundException {
    final var deletedQuote = dsl.deleteFrom(QUOTES)
      .where(QUOTES.ID.eq(id))
      .returning()
      .fetchOneInto(QuotesRecord.class);
    if (deletedQuote == null) throw new QuoteNotFoundException(QUOTE_NOT_FOUND);
    return QUOTE_MAPPER.mapToQuote(deletedQuote);
  }

  /**
   * Returns the count of quotes in the database.
   *
   * @return The count of quotes.
   */
  public Integer count() {
    return dsl.fetchCount(QUOTES);
  }

  /**
   * Maps a field and order to a corresponding JOOQ SortField.
   *
   * @param field The field to map. Valid values are {@code SortField.AUTHOR}, {@code SortField.TEXT}, {@code SortField.CREATION_DATE}.
   * @param order The order to map. Valid values are {@code SortOrder.ASC}, {@code SortOrder.DESC}.
   * @return The JOOQ SortField corresponding to the field and order.
   */
  private org.jooq.SortField<? extends Comparable<? extends Comparable<?>>> mapToJooqSortField(final SortField field, final SortOrder order) {
    final var jooqField = switch (field) {
      case AUTHOR -> QUOTES.AUTHOR;
      case TEXT -> QUOTES.TEXT;
      default -> QUOTES.CREATION_DATE;
    };

    return order == SortOrder.ASC ? jooqField.asc() : jooqField.desc();
  }
}
