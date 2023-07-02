package de.zedalite.quotes.data.mapper;

import de.zedalite.quotes.data.jooq.tables.records.QuotesRecord;
import de.zedalite.quotes.data.model.Quote;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface QuoteMapper {
  QuoteMapper INSTANCE = Mappers.getMapper(QuoteMapper.class);

  Quote quoteRecToQuote(final QuotesRecord quotesRecord);

  List<Quote> quoteRecsToQuotes(final List<QuotesRecord> quotesResRecords);
}
