package de.zedalite.quotes.data.mapper;

import de.zedalite.quotes.data.model.SortOrder;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToSortOrderConverter implements Converter<String, SortOrder> {
  @Override
  public SortOrder convert(final String source) {
    return SortOrder.valueOf(source.toUpperCase());
  }
}
