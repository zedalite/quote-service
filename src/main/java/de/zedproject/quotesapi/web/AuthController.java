package de.zedproject.quotesapi.web;

import de.zedproject.quotesapi.data.model.UserRequest;
import de.zedproject.quotesapi.data.model.UserResponse;
import de.zedproject.quotesapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "AuthController", description = "Operations related to authentication")
@RequestMapping("auth")
public class AuthController {

  private final UserService service;

  public AuthController(final UserService service) {
    this.service = service;
  }

  @Operation(summary = "Signup a new account",
    responses = {
      @ApiResponse(responseCode = "200", description = "Account created"),
      @ApiResponse(responseCode = "400", description = "Invalid request"),
      @ApiResponse(responseCode = "403", description = "Username already exists")
    })
  @PostMapping("signup")
  public void signup(@RequestBody @Valid final UserRequest request) {
    service.create(request);
  }

  @Operation(summary = "Login an account",
    responses = {
      @ApiResponse(responseCode = "200", description = "Login successful"),
      @ApiResponse(responseCode = "401", description = "Login failed"),
      @ApiResponse(responseCode = "403", description = "Login failed")
    })
  @PostMapping("login")
  public UserResponse login(@RequestBody @Valid final UserRequest request) {
    return service.authenticate(request);
  }

  @Operation(summary = "Refresh a valid token",
    responses = {
      @ApiResponse(responseCode = "200", description = "Refresh successful"),
      @ApiResponse(responseCode = "401", description = "Refresh failed")
    })
  @GetMapping("refresh")
  public UserResponse refresh() {
    final var username = SecurityContextHolder.getContext().getAuthentication().getName();
    return service.refreshToken(username);
  }
}
