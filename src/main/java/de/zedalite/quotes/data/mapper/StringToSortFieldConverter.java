package de.zedalite.quotes.data.mapper;

import de.zedalite.quotes.data.model.SortField;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * A converter that converts a String to a SortField.
 *
 * <p>
 * This class is a Spring framework converter implemented by implementing the
 * {@link Converter} interface. It converts a String to a {@link SortField} by
 * parsing the input String and mapping it to the appropriate SortField enum
 * value.
 * </p>
 * <p>
 * The conversion is case-insensitive, the input String is converted to uppercase
 * before mapping it to the SortField enum value.
 * </p>
 *
 * @see Converter
 * @see SortField
 */
@Component
public class StringToSortFieldConverter implements Converter<String, SortField> {
  @Override
  public SortField convert(final String source) {
    return SortField.valueOf(source.toUpperCase());
  }
}
