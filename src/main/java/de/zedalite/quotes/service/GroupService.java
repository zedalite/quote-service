package de.zedalite.quotes.service;

import de.zedalite.quotes.data.mapper.UserMapper;
import de.zedalite.quotes.data.model.*;
import de.zedalite.quotes.exception.GroupNotFoundException;
import de.zedalite.quotes.exception.ResourceNotFoundException;
import de.zedalite.quotes.exception.UserNotFoundException;
import de.zedalite.quotes.repository.GroupRepository;
import de.zedalite.quotes.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class GroupService {

  private static final UserMapper USER_MAPPER = UserMapper.INSTANCE;

  private final GroupRepository repository;

  private final UserRepository userRepository;

  public GroupService(final GroupRepository repository, final UserRepository userRepository) {
    this.repository = repository;
    this.userRepository = userRepository;
  }

  public GroupResponse create(final GroupRequest request, final Integer creatorId) {
    try {
      final Group group = repository.save(request, creatorId);
      return new GroupResponse(group, getUser(creatorId));
    } catch (final GroupNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }

  public GroupResponse find(final Integer id) {
    try {
      final Group group = repository.findById(id);
      return new GroupResponse(group, getUser(group.creatorId().orElse(null)));
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

  private Optional<UserResponse> getUser(final Integer creatorId) {
    if (creatorId == null) {
      return Optional.empty();
    }

    Optional<UserResponse> creator;
    try {
      creator = Optional.of(USER_MAPPER.mapToResponse(userRepository.findById(creatorId)));
    } catch (final UserNotFoundException ex) {
      creator = Optional.empty();
    }
    return creator;
  }
}
