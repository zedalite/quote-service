package de.zedproject.quotesapi.web;

import de.zedproject.quotesapi.data.model.ErrorDetails;
import de.zedproject.quotesapi.data.model.Quote;
import de.zedproject.quotesapi.data.model.QuoteRequest;
import de.zedproject.quotesapi.data.model.ValidationErrorDetails;
import de.zedproject.quotesapi.service.QuoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "QuoteController", description = "Operation related to quotes")
@RequestMapping("/quotes")
@CrossOrigin(origins = "*")
public class QuoteController {
  private final QuoteService service;

  public QuoteController(final QuoteService service) {
    this.service = service;
  }

  @Operation(summary = "Get all quotes")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Quotes found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Quote.class))}),
          @ApiResponse(responseCode = "404", description = "Quotes not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))})})
  @GetMapping()
  public List<Quote> getQuotes() {
    return service.getEntities();
  }

  @Operation(summary = "Get a quote by its id")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Quote found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Quote.class))}),
          @ApiResponse(responseCode = "404", description = "Quote not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))})})
  @GetMapping("/{id}")
  public Quote getQuote(@PathVariable("id") final Integer id) {
    return service.getEntity(id);
  }

  @Operation(summary = "Get a random quote")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Quote found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Quote.class))}),
          @ApiResponse(responseCode = "404", description = "Quote not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))})})
  @GetMapping("/random")
  public Quote getRandomQuote() {
    return service.getRandomEntity();
  }

  @Operation(summary = "Get a random quotes")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Quotes found", content = {@Content(mediaType = "application/json",schema = @Schema(implementation = Quote.class))}),
          @ApiResponse(responseCode = "404", description = "Quotes not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))})})
  @GetMapping("/randoms")
  public List<Quote> getRandomQuotes(@RequestParam final Integer quantity) {
    return service.getRandomEntities(quantity);
  }

  @Operation(summary = "Get the number of saved quotes")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Quotes found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Quote.class))}),
          @ApiResponse(responseCode = "404", description = "Quotes not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))})})
  @GetMapping("/count")
  public Integer getQuotesCount() {
    return service.countEntities();
  }

  @Operation(summary = "Create a new quote")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Quote created", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Quote.class))}),
          @ApiResponse(responseCode = "400", description = "Quote not created", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrorDetails.class))})})
  @PostMapping()
  public Quote postQuote(@RequestBody @Valid final QuoteRequest request) {
    return service.createEntity(request);
  }

  @Operation(summary = "Edit a existing quote")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Quote edited", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Quote.class))}),
          @ApiResponse(responseCode = "400", description = "Quote not edited", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrorDetails.class))}),
          @ApiResponse(responseCode = "404", description = "Quote not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))})})
  @PutMapping("/{id}")
  public Quote putQuote(@PathVariable("id") final Integer id, @RequestBody @Valid final QuoteRequest request) {
    return service.updateEntity(id, request);
  }

  @Operation(summary = "Delete a existing quote")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Quote deleted", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Quote.class))}),
          @ApiResponse(responseCode = "404", description = "Quote not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))})})
  @DeleteMapping("/{id}")
  public Quote deleteQuote(@PathVariable("id") final Integer id) {
    return service.deleteEntity(id);
  }
}
