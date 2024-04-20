package de.zedalite.quotes.web;

import de.zedalite.quotes.data.model.ErrorResponse;
import de.zedalite.quotes.data.model.UserResponse;
import de.zedalite.quotes.service.GroupUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Groups", description = "Operations related to groups")
@RequestMapping("groups")
public class GroupUserController {

  private final GroupUserService service;

  public GroupUserController(final GroupUserService service) {
    this.service = service;
  }

  @Operation(
    summary = "Get all group users",
    description = "Get all group users",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Group user found",
        content = { @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class)) }
      ),
      @ApiResponse(
        responseCode = "403",
        description = "Principal is no group member",
        content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)) }
      ),
      @ApiResponse(
        responseCode = "404",
        description = "Group user not found",
        content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)) }
      ),
    }
  )
  @PreAuthorize("@authorizer.isUserInGroup(principal,#id)")
  @GetMapping("{id}/users")
  public List<UserResponse> getUsers(@PathVariable("id") final Integer id) {
    return service.findAll(id);
  }

  @Operation(
    summary = "Get a group user by its id",
    description = "Get a group user by its id",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Group user found",
        content = { @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class)) }
      ),
      @ApiResponse(
        responseCode = "403",
        description = "Principal is no group member",
        content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)) }
      ),
      @ApiResponse(
        responseCode = "404",
        description = "Group user not found",
        content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)) }
      ),
    }
  )
  @PreAuthorize("@authorizer.isUserInGroup(principal,#id)")
  @GetMapping("{id}/users/{userId}")
  public UserResponse getUser(@PathVariable("id") final Integer id, @PathVariable("userId") final Integer userId) {
    return service.find(id, userId);
  }

  @Operation(
    summary = "Create a new group user",
    description = "Create a new group user",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Group user created",
        content = @Content(mediaType = "application/json")
      ),
      @ApiResponse(
        responseCode = "400",
        description = "Group user not created",
        content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)) }
      ),
      @ApiResponse(
        responseCode = "403",
        description = "Principal is no group member",
        content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)) }
      ),
      @ApiResponse(
        responseCode = "404",
        description = "Group/User not found",
        content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)) }
      ),
    }
  )
  @PreAuthorize("@authorizer.isUserInGroup(principal,#id)")
  @PostMapping("{id}/users")
  public void postUser(@PathVariable("id") final Integer id, @RequestBody final Integer userId) {
    service.create(id, userId);
  }
}
