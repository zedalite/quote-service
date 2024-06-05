package de.zedalite.quotes.service;

import de.zedalite.quotes.data.mapper.GroupMapper;
import de.zedalite.quotes.data.mapper.UserMapper;
import de.zedalite.quotes.data.model.Group;
import de.zedalite.quotes.data.model.GroupRequest;
import de.zedalite.quotes.data.model.GroupResponse;
import de.zedalite.quotes.data.model.User;
import de.zedalite.quotes.exception.GroupNotFoundException;
import de.zedalite.quotes.exception.ResourceNotFoundException;
import de.zedalite.quotes.exception.UserNotFoundException;
import de.zedalite.quotes.repository.GroupRepository;
import de.zedalite.quotes.repository.GroupUserRepository;
import de.zedalite.quotes.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class GroupService {

  private static final GroupMapper GROUP_MAPPER = GroupMapper.INSTANCE;

  private static final UserMapper USER_MAPPER = UserMapper.INSTANCE;

  private final GroupRepository repository;

  private final UserRepository userRepository;

  private final GroupUserRepository groupUserRepository;

  public GroupService(
    final GroupRepository repository,
    final UserRepository userRepository,
    final GroupUserRepository groupUserRepository
  ) {
    this.repository = repository;
    this.userRepository = userRepository;
    this.groupUserRepository = groupUserRepository;
  }

  public GroupResponse create(final GroupRequest request, final Integer creatorId) {
    try {
      final Group group = repository.save(request, creatorId);
      return getResponse(group, getUser(group.creatorId()));
    } catch (final GroupNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }

  public GroupResponse find(final Integer id) {
    try {
      final Group group = repository.findById(id);
      return getResponse(group, getUser(group.creatorId()));
    } catch (final GroupNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }

  public List<GroupResponse> findAllByUser(final Integer userId) {
    try {
      final List<Group> groups = groupUserRepository.findGroups(userId);
      return getResponses(groups);
    } catch (final GroupNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }

  public List<Integer> findAllIds() {
    try {
      return repository.findAllIds();
    } catch (final GroupNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }

  private Optional<User> getUser(final Optional<Integer> creatorId) {
    if (creatorId.isEmpty()) {
      return Optional.empty();
    }

    Optional<User> creator;
    try {
      creator = Optional.of(userRepository.findById(creatorId.get()));
    } catch (final UserNotFoundException ex) {
      creator = Optional.empty();
    }
    return creator;
  }

  private GroupResponse getResponse(final Group group, final Optional<User> creator) {
    return GROUP_MAPPER.mapToResponse(group, creator.orElse(null));
  }

  private List<GroupResponse> getResponses(final List<Group> groups) {
    return groups.stream().map(group -> getResponse(group, getUser(group.creatorId()))).toList();
  }
}
