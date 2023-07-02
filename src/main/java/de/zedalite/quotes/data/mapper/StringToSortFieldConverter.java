package de.zedalite.quotes.data.mapper;

import de.zedalite.quotes.data.model.SortField;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToSortFieldConverter implements Converter<String, SortField> {
  @Override
  public SortField convert(final String source) {
    return SortField.valueOf(source.toUpperCase());
  }
}
