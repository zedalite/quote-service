package de.zedalite.quotes.web;

import de.zedalite.quotes.data.model.*;
import de.zedalite.quotes.service.QuoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "QuoteController", description = "Operations related to quotes")
@RequestMapping("quotes")
@CrossOrigin(origins = "*")
public class QuoteController {
  private final QuoteService service;

  public QuoteController(final QuoteService service) {
    this.service = service;
  }

  @Operation(summary = "Get all quotes",
    responses = {
      @ApiResponse(responseCode = "200", description = "Quotes found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Quote.class))}),
      @ApiResponse(responseCode = "404", description = "Quotes not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))})})
  @GetMapping()
  public List<Quote> getQuotes(@RequestParam(defaultValue = "DATETIME") @Valid final SortField field, @RequestParam(defaultValue = "DESC") @Valid final SortOrder order) {
    return service.findAll(field, order);
  }

  @Operation(summary = "Get a quote by its id",
    responses = {
      @ApiResponse(responseCode = "200", description = "Quote found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Quote.class))}),
      @ApiResponse(responseCode = "404", description = "Quote not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))})})
  @GetMapping("{id}")
  public Quote getQuote(@PathVariable("id") final Integer id) {
    return service.find(id);
  }

  @Operation(summary = "Get a random quotes",
    responses = {
      @ApiResponse(responseCode = "200", description = "Quotes found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Quote.class))}),
      @ApiResponse(responseCode = "404", description = "Quotes not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))})})
  @GetMapping("randoms")
  public List<Quote> getRandomQuotes(@RequestParam(defaultValue = "8") final Integer quantity) {
    return service.findRandoms(quantity);
  }

  @Operation(summary = "Get a random quote",
    responses = {
      @ApiResponse(responseCode = "200", description = "Quote found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Quote.class))}),
      @ApiResponse(responseCode = "404", description = "Quote not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))})})
  @GetMapping("random")
  public Quote getRandomQuote() {
    return service.findRandom();
  }

  @Operation(summary = "Get quote of the day",
    responses = {
      @ApiResponse(responseCode = "200", description = "Quote found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Quote.class))}),
      @ApiResponse(responseCode = "404", description = "Quote not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))})})
  @GetMapping("qotd")
  public Quote getQuoteOfTheDay() {
    return service.findQuoteOfTheDay();
  }

  @Operation(summary = "Get the number of saved quotes",
    responses = {
      @ApiResponse(responseCode = "200", description = "Quotes found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Quote.class))}),
      @ApiResponse(responseCode = "404", description = "Quotes not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))})})
  @GetMapping("count")
  public Integer getQuotesCount() {
    return service.count();
  }

  @Operation(summary = "Create a new quote",
    responses = {
      @ApiResponse(responseCode = "200", description = "Quote created", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Quote.class))}),
      @ApiResponse(responseCode = "400", description = "Quote not created", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrorDetails.class))})})
  @PostMapping()
  public Quote postQuote(@RequestBody @Valid final QuoteRequest request) {
    final var creator = SecurityContextHolder.getContext().getAuthentication().getName();
    return service.create(request, creator);
  }

  @Operation(summary = "Edit a existing quote",
    responses = {
      @ApiResponse(responseCode = "200", description = "Quote edited", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Quote.class))}),
      @ApiResponse(responseCode = "400", description = "Quote not edited", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrorDetails.class))}),
      @ApiResponse(responseCode = "404", description = "Quote not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))})})
  @PutMapping("{id}")
  public Quote putQuote(@PathVariable("id") final Integer id, @RequestBody @Valid final QuoteRequest request) {
    final var creator = SecurityContextHolder.getContext().getAuthentication().getName();
    return service.update(id, request, creator);
  }

  @Operation(summary = "Delete a existing quote",
    responses = {
      @ApiResponse(responseCode = "200", description = "Quote deleted", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Quote.class))}),
      @ApiResponse(responseCode = "404", description = "Quote not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))})})
  @DeleteMapping("{id}")
  public Quote deleteQuote(@PathVariable("id") final Integer id) {
    return service.delete(id);
  }
}
