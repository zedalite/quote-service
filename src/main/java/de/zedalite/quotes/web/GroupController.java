package de.zedalite.quotes.web;

import de.zedalite.quotes.data.model.*;
import de.zedalite.quotes.service.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Groups", description = "Operations related to groups")
@RequestMapping("groups")
public class GroupController {

  private final GroupService service;

  public GroupController(final GroupService service) {
    this.service = service;
  }

  @Operation(
    summary = "Get a group by its id",
    description = "Get a group by its id",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Group found",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = GroupResponse.class))
      ),
      @ApiResponse(
        responseCode = "403",
        description = "Group retrieval not allowed",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
      ),
      @ApiResponse(
        responseCode = "404",
        description = "Group not found",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
      ),
    }
  )
  @GetMapping("{id}")
  public ResponseEntity<GroupResponse> getGroup(@PathVariable("id") final Integer id) {
    return ResponseEntity.ok(service.find(id));
  }

  @Operation(
    summary = "Create a new group",
    description = "Create a new group",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Group created",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = GroupResponse.class))
      ),
      @ApiResponse(
        responseCode = "400",
        description = "Group not created",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
      ),
      @ApiResponse(
        responseCode = "403",
        description = "Group creation not allowed",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
      ),
      @ApiResponse(
        responseCode = "404",
        description = "Group not found",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
      ),
    }
  )
  @PostMapping
  public ResponseEntity<GroupResponse> postGroup(
    @RequestBody @Valid final GroupRequest request,
    @AuthenticationPrincipal final UserPrincipal principal
  ) {
    return ResponseEntity.ok(service.create(request, principal.getId()));
  }
}
