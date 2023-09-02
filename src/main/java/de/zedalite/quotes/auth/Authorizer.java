package de.zedalite.quotes.auth;

import de.zedalite.quotes.service.GroupUserService;
import org.springframework.stereotype.Component;

@Component
public class Authorizer {

  private final GroupUserService groupUserService;

  public Authorizer(final GroupUserService groupUserService) {
    this.groupUserService = groupUserService;
  }

  /**
   * Checks if a user is in a specified group.
   *
   * @param principal the UserPrincipal
   * @param groupId   the ID of the group to check if the user is in
   * @return true if the user is in the group, false otherwise
   */
  public boolean isUserInGroup(final UserPrincipal principal, final Integer groupId) {
    return groupUserService.isUserInGroup(groupId, principal.getId());
  }
}
