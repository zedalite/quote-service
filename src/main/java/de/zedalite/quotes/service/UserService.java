package de.zedalite.quotes.service;

import de.zedalite.quotes.data.model.DisplayNameRequest;
import de.zedalite.quotes.data.model.User;
import de.zedalite.quotes.data.model.UserRequest;
import de.zedalite.quotes.exception.ResourceAlreadyExitsException;
import de.zedalite.quotes.exception.ResourceNotFoundException;
import de.zedalite.quotes.exception.UserNotFoundException;
import de.zedalite.quotes.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

  public static final String USER_ALREADY_EXITS = "User already exits";

  private final UserRepository repository;


  public UserService(final UserRepository repository) {
    this.repository = repository;
  }

  public User create(final UserRequest request) throws ResourceAlreadyExitsException {
    try {
      if (repository.isUsernameTaken(request.name())) {
        throw new ResourceAlreadyExitsException(USER_ALREADY_EXITS);
      } else {
        return repository.save(request);
      }
    } catch (final UserNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }

  public List<User> findAll() {
    try {
      return repository.findAll();
    } catch (final UserNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }

  public User find(final Integer id) throws ResourceNotFoundException {
    try {
      return repository.findById(id);
    } catch (final UserNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }

  public void updateDisplayName(final Integer id, final DisplayNameRequest request) {
    try {
      final User user = find(id);
      final UserRequest userRequest = new UserRequest(user.name(), user.email(), request.displayName());
      repository.update(id, userRequest);
    } catch (final UserNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }
}
