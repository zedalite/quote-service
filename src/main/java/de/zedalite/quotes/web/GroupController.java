package de.zedalite.quotes.web;

import de.zedalite.quotes.data.model.UserPrincipal;
import de.zedalite.quotes.data.model.ErrorDetails;
import de.zedalite.quotes.data.model.Group;
import de.zedalite.quotes.data.model.GroupRequest;
import de.zedalite.quotes.data.model.ValidationErrorDetails;
import de.zedalite.quotes.service.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

  @Operation(summary = "Get a group by its id",
    responses = {
      @ApiResponse(responseCode = "200", description = "Group found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Group.class))}),
      @ApiResponse(responseCode = "404", description = "Group not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))})})
  @GetMapping("{id}")
  public Group getGroup(@PathVariable("id") final Integer id) {
    return service.find(id);
  }

  @Operation(summary = "Create a new group",
    responses = {
      @ApiResponse(responseCode = "200", description = "Group created", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Group.class))}),
      @ApiResponse(responseCode = "400", description = "Group not created", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrorDetails.class))})})
  @PostMapping()
  public Group postGroup(@RequestBody @Valid final GroupRequest request, @AuthenticationPrincipal final UserPrincipal principal) {
    return service.create(request, principal.getId());
  }
}
