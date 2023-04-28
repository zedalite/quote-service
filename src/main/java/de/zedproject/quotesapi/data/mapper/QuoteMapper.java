package de.zedproject.quotesapi.data.mapper;

import de.zedproject.jooq.quote.tables.records.QuotesRecord;
import de.zedproject.quotesapi.data.model.Quote;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface QuoteMapper {
  QuoteMapper INSTANCE = Mappers.getMapper(QuoteMapper.class);

  Quote quoteRecToQuote(final QuotesRecord quotesRecord);

  List<Quote> quoteRecsToQuotes(final List<QuotesRecord> quotesResRecords);
}
