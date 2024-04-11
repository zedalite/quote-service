package de.zedalite.quotes.web;

import de.zedalite.quotes.data.model.ErrorDetails;
import de.zedalite.quotes.data.model.Quote;
import de.zedalite.quotes.service.QuoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Quotes", description = "Operations related to quotes")
@RequestMapping("quotes")
@CrossOrigin(origins = "*")
public class QuoteController {
  private final QuoteService service;

  public QuoteController(final QuoteService service) {
    this.service = service;
  }

  @Operation(summary = "Get the number of saved quotes",
    responses = {
      @ApiResponse(responseCode = "200", description = "Quotes found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Quote.class))}),
      @ApiResponse(responseCode = "404", description = "Quotes not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))})})
  @GetMapping("count")
  public Integer getQuotesCount() {
    return service.count();
  }
}
