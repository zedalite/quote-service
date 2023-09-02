package de.zedalite.quotes.web;

import de.zedalite.quotes.auth.UserPrincipal;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Users", description = "Operations related to users")
@RequestMapping("users")
@CrossOrigin(origins = "*")
public class UserController {

  private final UserService service;

  public UserController(final UserService service) {
    this.service = service;
  }

  @Operation(summary = "Get all users",
    responses = {
      @ApiResponse(responseCode = "200", description = "Users found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),
      @ApiResponse(responseCode = "404", description = "Users not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))})})
  @GetMapping()
  public List<User> getUsers() {
    // TODO decide if necessary for non-admins?
    return service.findAll();
  }

  @Operation(summary = "Get a user by its name",
    responses = {
      @ApiResponse(responseCode = "200", description = "User found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),
      @ApiResponse(responseCode = "404", description = "User not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))})})
  @GetMapping("{id}")
  public User getUser(@PathVariable("id") final Integer id) {
    return service.find(id);
  }

  @Operation(summary = "Patch user's password",
    responses = {
      @ApiResponse(responseCode = "200", description = "Password patched"),
      @ApiResponse(responseCode = "404", description = "User not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))})})
  @PatchMapping("password")
  public void patchPassword(@RequestBody @Valid final PasswordRequest request, @AuthenticationPrincipal UserPrincipal principal) {
    service.updatePassword(principal.getId(), request);
  }

  @Operation(summary = "Patch user's display name",
    responses = {
      @ApiResponse(responseCode = "200", description = "Display name patched"),
      @ApiResponse(responseCode = "404", description = "User not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))})})
  @PatchMapping("displayname")
  public void patchDisplayName(@RequestBody @Valid final DisplayNameRequest request, @AuthenticationPrincipal UserPrincipal principal) {
    service.updateDisplayName(principal.getId(), request);
  }
}
