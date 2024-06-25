package de.zedalite.quotes.service;

import de.zedalite.quotes.data.mapper.UserMapper;
import de.zedalite.quotes.data.model.*;
import de.zedalite.quotes.exception.ResourceAlreadyExitsException;
import de.zedalite.quotes.exception.ResourceNotFoundException;
import de.zedalite.quotes.exception.UserNotFoundException;
import de.zedalite.quotes.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private static final UserMapper USER_MAPPER = UserMapper.INSTANCE;

  private static final String USER_ALREADY_EXITS = "User already exits";

  private final UserRepository repository;

  public UserService(final UserRepository repository) {
    this.repository = repository;
  }

  public UserResponse create(final UserRequest request) throws ResourceAlreadyExitsException {
    try {
      if (repository.isUsernameTaken(request.name())) {
        throw new ResourceAlreadyExitsException(USER_ALREADY_EXITS);
      } else {
        return getResponse(repository.save(request));
      }
    } catch (final UserNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }

  public UserResponse find(final Integer id) throws ResourceNotFoundException {
    try {
      return getResponse(repository.findById(id));
    } catch (final UserNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }

  public void update(final Integer id, final UserRequest request) {
    try {
      repository.update(id, request);
    } catch (final UserNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }

  public void updateName(final Integer id, final UserNameRequest request) {
    if (repository.isUsernameTaken(request.name())) throw new ResourceAlreadyExitsException(USER_ALREADY_EXITS);

    try {
      final User user = repository.findById(id);
      update(id, new UserRequest(request.name(), user.email(), user.displayName()));
    } catch (final UserNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }

  public void updateDisplayName(final Integer id, final UserDisplayNameRequest request) {
    try {
      final User user = repository.findById(id);
      update(1, new UserRequest(user.name(), user.email(), request.displayName()));
    } catch (final UserNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }

  public void updateEmail(final Integer id, final UserEmailRequest request) {
    try {
      final User user = repository.findById(id);
      update(1, new UserRequest(user.name(), request.email(), user.displayName()));
    } catch (final UserNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }

  private UserResponse getResponse(final User user) {
    return USER_MAPPER.mapToResponse(user);
  }
}
