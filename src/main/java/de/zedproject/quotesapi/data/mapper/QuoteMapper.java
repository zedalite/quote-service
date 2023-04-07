package de.zedproject.quotesapi.data.mapper;

import de.zedproject.jooq.quote.tables.records.QuotesRecord;
import de.zedproject.quotesapi.data.models.Quote;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Optional;

@Mapper
public interface QuoteMapper {

  QuoteMapper INSTANCE = Mappers.getMapper(QuoteMapper.class);

  Quote quoteRecToQuote(final QuotesRecord quotesRecord);

  List<Quote> quoteRecsToQuotes(final List<QuotesRecord> quotesResRecords);

  default <T> Optional<T> mapOptional(final T s) {
    return Optional.ofNullable(s);
  }
}
