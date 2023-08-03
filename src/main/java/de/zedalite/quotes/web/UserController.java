package de.zedalite.quotes.web;

import de.zedalite.quotes.data.model.DisplayNameRequest;
import de.zedalite.quotes.data.model.ErrorDetails;
import de.zedalite.quotes.data.model.PasswordRequest;
import de.zedalite.quotes.data.model.User;
import de.zedalite.quotes.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "UserController", description = "Operations related to users")
@RequestMapping("users")
@CrossOrigin(origins = "*")
public class UserController {

  private final UserService service;

  public UserController(final UserService service) {
    this.service = service;
  }

  @Operation(summary = "Get a user by its name",
    responses = {
      @ApiResponse(responseCode = "200", description = "User found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),
      @ApiResponse(responseCode = "404", description = "User not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))})})
  @GetMapping("{name}")
  public User getUser(@PathVariable("name") final String name) {
    return service.find(name);
  }

  @Operation(summary = "Patch user's password",
    responses = {
      @ApiResponse(responseCode = "200", description = "Password patched"),
      @ApiResponse(responseCode = "404", description = "User not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))})})
  @PatchMapping("password")
  public void patchPassword(@RequestBody @Valid final PasswordRequest request) {
    final var username = SecurityContextHolder.getContext().getAuthentication().getName();
    service.updatePassword(username, request);
  }

  @Operation(summary = "Patch user's display name",
    responses = {
      @ApiResponse(responseCode = "200", description = "Display name patched"),
      @ApiResponse(responseCode = "404", description = "User not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))})})
  @PatchMapping("displayname")
  public void patchDisplayName(@RequestBody @Valid final DisplayNameRequest request) {
    final var username = SecurityContextHolder.getContext().getAuthentication().getName();
    service.updateDisplayName(username, request);
  }
}
