package de.zedproject.quotesapi.data.mapper;

import de.zedproject.jooq.tables.records.QuotesOfTheDayRecord;
import de.zedproject.quotesapi.data.model.QuoteOfTheDay;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface QuoteOfTheDayMapper {
  QuoteOfTheDayMapper INSTANCE = Mappers.getMapper(QuoteOfTheDayMapper.class);

  QuoteOfTheDay toQuoteOfTheDay(final QuotesOfTheDayRecord quotesOfTheDayRecord);
}
