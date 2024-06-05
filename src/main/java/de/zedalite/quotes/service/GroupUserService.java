package de.zedalite.quotes.service;

import de.zedalite.quotes.data.mapper.UserMapper;
import de.zedalite.quotes.data.model.User;
import de.zedalite.quotes.data.model.UserResponse;
import de.zedalite.quotes.exception.ResourceAlreadyExitsException;
import de.zedalite.quotes.exception.ResourceNotFoundException;
import de.zedalite.quotes.exception.UserNotFoundException;
import de.zedalite.quotes.repository.GroupUserRepository;
import de.zedalite.quotes.repository.UserRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GroupUserService {

  private static final UserMapper USER_MAPPER = UserMapper.INSTANCE;

  private static final String GROUP_USER_ALREADY_EXITS = "Group user already exits";

  private final GroupUserRepository repository;

  private final UserRepository userRepository;

  public GroupUserService(final GroupUserRepository repository, final UserRepository userRepository) {
    this.repository = repository;
    this.userRepository = userRepository;
  }

  public Boolean create(final Integer id, final Integer userId) {
    if (userRepository.doesUserNonExist(userId)) throw new ResourceNotFoundException("USER_NOT_FOUND");

    // TODO add push notifcation: send to specific group topic or user notification token
    try {
      if (repository.isUserInGroup(id, userId)) {
        throw new ResourceAlreadyExitsException(GROUP_USER_ALREADY_EXITS);
      } else {
        return repository.save(id, userId);
      }
    } catch (final UserNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }

  public UserResponse find(final Integer id, final Integer userId) {
    try {
      return getResponse(repository.findById(id, userId));
    } catch (final UserNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }

  public List<UserResponse> findAll(final Integer id) {
    try {
      return getResponses(repository.findUsers(id));
    } catch (final UserNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }

  public boolean isUserInGroup(final Integer id, final Integer userId) {
    return repository.isUserInGroup(id, userId);
  }

  private List<UserResponse> getResponses(final List<User> users) {
    return USER_MAPPER.mapToResponses(users);
  }

  private UserResponse getResponse(final User user) {
    return USER_MAPPER.mapToResponse(user);
  }
}
