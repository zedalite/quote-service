package de.zedalite.quotes.web;

import de.zedalite.quotes.data.model.UserPrincipal;
import de.zedalite.quotes.data.model.*;
import de.zedalite.quotes.service.GroupQuoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Groups", description = "Operations related to groups")
@RequestMapping("groups")
public class GroupQuoteController {

  private final GroupQuoteService service;

  public GroupQuoteController(final GroupQuoteService service) {
    this.service = service;
  }

  @Operation(summary = "Get all group quotes",
    responses = {
      @ApiResponse(responseCode = "200", description = "Group quote found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = QuoteMessage.class))}),
      @ApiResponse(responseCode = "403", description = "Principal is no group member", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))}),
      @ApiResponse(responseCode = "404", description = "Group quotes not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))})})
  @PreAuthorize("@authorizer.isUserInGroup(principal,#id)")
  @GetMapping("{id}/quotes")
  public List<QuoteMessage> getQuotes(@PathVariable("id") final Integer id,
                                      @RequestParam(defaultValue = "CREATION_DATE") @Valid final SortField field,
                                      @RequestParam(defaultValue = "DESC") @Valid final SortOrder order) {
    return service.findAll(id, field, order);
  }

  @Operation(summary = "Get a group quote by its id",
    responses = {
      @ApiResponse(responseCode = "200", description = "Group quote found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = QuoteMessage.class))}),
      @ApiResponse(responseCode = "403", description = "Principal is no group member", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))}),
      @ApiResponse(responseCode = "404", description = "Group quote not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))})})
  @PreAuthorize("@authorizer.isUserInGroup(principal,#id)")
  @GetMapping("{id}/quotes/{quoteId}")
  public QuoteMessage getQuote(@PathVariable("id") final Integer id, @PathVariable("quoteId") final Integer quoteId) {
    return service.find(id, quoteId);
  }

  @Operation(summary = "Get the number of saved quotes",
    responses = {
      @ApiResponse(responseCode = "200", description = "Group quotes count found"),
      @ApiResponse(responseCode = "403", description = "Principal is no group member", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))}),
      @ApiResponse(responseCode = "404", description = "Group quotes not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))})})
  @PreAuthorize("@authorizer.isUserInGroup(principal,#id)")
  @GetMapping("{id}/quotes/count")
  public Integer getQuotesCount(@PathVariable("id") final Integer id) {
    return service.count(id);
  }

  @Operation(summary = "Create a new group quote",
    responses = {
      @ApiResponse(responseCode = "200", description = "Group quote created", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Quote.class))}),
      @ApiResponse(responseCode = "400", description = "Group quote not created", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrorDetails.class))}),
      @ApiResponse(responseCode = "403", description = "Principal is no group member", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))})})
  @PreAuthorize("@authorizer.isUserInGroup(principal,#id)")
  @PostMapping("{id}/quotes")
  public QuoteMessage postQuote(@PathVariable("id") final Integer id, @RequestBody @Valid final QuoteRequest request, @AuthenticationPrincipal final UserPrincipal principal) {
    return service.create(id, request, principal.getId());
  }

  @Operation(summary = "Get random group quotes",
    responses = {
      @ApiResponse(responseCode = "200", description = "Group quotes found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Quote.class))}),
      @ApiResponse(responseCode = "403", description = "Principal is no group member", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))}),
      @ApiResponse(responseCode = "404", description = "Group quotes not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))})})
  @PreAuthorize("@authorizer.isUserInGroup(principal,#id)")
  @GetMapping("{id}/quotes/randoms")
  public List<QuoteMessage> getRandomQuotes(@PathVariable("id") final Integer id,
                                            @RequestParam(defaultValue = "8") @PositiveOrZero final Integer quantity) {
    return service.findRandoms(id, quantity);
  }
}
