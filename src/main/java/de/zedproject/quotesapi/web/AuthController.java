package de.zedproject.quotesapi.web;

import de.zedproject.quotesapi.data.model.UserRequest;
import de.zedproject.quotesapi.data.model.UserResponse;
import de.zedproject.quotesapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    service.createUser(request);
  }

  @Operation(summary = "Login an account",
    responses = {
      @ApiResponse(responseCode = "200", description = "Login successful"),
      @ApiResponse(responseCode = "403", description = "Login failed")
    })
  @PostMapping("login")
  public UserResponse login(@RequestBody @Valid final UserRequest request) {
    //TODO handle invalid and expired token
    return service.authenticate(request);
  }
}
