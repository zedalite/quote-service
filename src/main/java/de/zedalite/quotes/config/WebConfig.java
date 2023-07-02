package de.zedalite.quotes.config;

import de.zedalite.quotes.data.mapper.StringToSortFieldConverter;
import de.zedalite.quotes.data.mapper.StringToSortOrderConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
  @Override
  public void addFormatters(final FormatterRegistry registry) {
    registry.addConverter(new StringToSortFieldConverter());
    registry.addConverter(new StringToSortOrderConverter());
  }
}
