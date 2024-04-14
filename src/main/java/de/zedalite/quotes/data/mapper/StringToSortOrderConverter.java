package de.zedalite.quotes.data.mapper;

import de.zedalite.quotes.data.model.SortOrder;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


/**
 * A converter that converts a String to a SortOrder.
 *
 * <p>
 * This class is a Spring framework converter implemented by implementing the
 * {@link Converter} interface. It converts a String to a {@link SortOrder} by
 * parsing the input String and mapping it to the appropriate SortOrder enum
 * value.
 * </p>
 * <p>
 * The conversion is case-insensitive, the input String is converted to uppercase
 * before mapping it to the SortOrder enum value.
 * </p>
 *
 * @see Converter
 * @see SortOrder
 */
@Component
public class StringToSortOrderConverter implements Converter<String, SortOrder> {
  @Override
  public SortOrder convert(final @NotNull String source) {
    return SortOrder.getByName(source);
  }
}
