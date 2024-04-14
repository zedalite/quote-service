package de.zedalite.quotes.data.mapper;

import de.zedalite.quotes.data.jooq.quotes.tables.records.QuotesRecord;
import de.zedalite.quotes.data.model.Quote;
import de.zedalite.quotes.data.model.QuoteMessage;
import de.zedalite.quotes.data.model.User;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * This class is responsible for mapping the QuotesRecord entity to Quote entity.
 */
@Mapper
public interface QuoteMapper {
  QuoteMapper INSTANCE = Mappers.getMapper(QuoteMapper.class);

  Quote mapToQuote(final QuotesRecord quotesRecord);

  List<Quote> mapToQuoteList(final List<QuotesRecord> quotesRecords);

  QuoteMessage mapToQuoteMessage(final Quote quote, List<User> mentions);
}
