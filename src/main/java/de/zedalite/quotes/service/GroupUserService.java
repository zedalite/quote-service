package de.zedalite.quotes.service;

import de.zedalite.quotes.data.model.User;
import de.zedalite.quotes.exceptions.ResourceAlreadyExitsException;
import de.zedalite.quotes.exceptions.ResourceNotFoundException;
import de.zedalite.quotes.exceptions.UserNotFoundException;
import de.zedalite.quotes.repository.GroupUserRepository;
import de.zedalite.quotes.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupUserService {

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
    } catch (UserNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }

  public User find(final Integer id, final Integer userId) {
    try {
      return repository.findById(id, userId);
    } catch (UserNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }

  public List<User> findAll(final Integer id) {
    try {
      return repository.findAll(id);
    } catch (UserNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }

  public boolean isUserInGroup(final Integer id, final Integer userId) {
    return repository.isUserInGroup(id, userId);
  }
}
