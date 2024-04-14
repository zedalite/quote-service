package de.zedalite.quotes.service;

import de.zedalite.quotes.data.model.*;
import de.zedalite.quotes.exceptions.ResourceAlreadyExitsException;
import de.zedalite.quotes.exceptions.ResourceNotFoundException;
import de.zedalite.quotes.exceptions.UserNotFoundException;
import de.zedalite.quotes.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

  public static final String USER_ALREADY_EXITS = "User already exits";

  private final UserRepository repository;

  private final PasswordEncoder passwordEncoder;

  private final AuthenticationManager authenticationManager;

  private final JwtTokenService tokenService;

  public UserService(final UserRepository repository,
                     final PasswordEncoder passwordEncoder,
                     final AuthenticationManager authenticationManager,
                     final JwtTokenService tokenService) {
    this.repository = repository;
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;
    this.tokenService = tokenService;
  }

  public AuthResponse authenticate(final AuthRequest request) throws AuthenticationException {
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.name(), request.password()));
    final String token = tokenService.generateToken(request.name());
    return new AuthResponse(token);
  }

  public AuthResponse refreshToken(final String username) {
    final String token = tokenService.generateToken(username);
    return new AuthResponse(token);
  }

  public User create(final UserRequest request) throws ResourceAlreadyExitsException {
    try {
      if (repository.isUsernameTaken(request.name())) {
        throw new ResourceAlreadyExitsException(USER_ALREADY_EXITS);
      } else {
        return repository.save(request);
      }
    } catch (UserNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }

  public List<User> findAll() {
    try {
      return repository.findAll();
    } catch (UserNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }

  public User find(final Integer id) throws ResourceNotFoundException {
    try {
      return repository.findById(id);
    } catch (UserNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }

  public User findByName(final String name) throws ResourceNotFoundException {
    try {
      return repository.findByName(name);
    } catch (UserNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }

  public void updatePassword(final Integer id, final PasswordRequest request) {
    try {
      final User user = find(id);
      final UserRequest userRequest = new UserRequest(user.name(), passwordEncoder.encode(request.password()), user.displayName());
      repository.update(id, userRequest);
    } catch (UserNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }

  public void updateDisplayName(final Integer id, final DisplayNameRequest request) {
    try {
      final User user = find(id);
      final UserRequest userRequest = new UserRequest(user.name(), user.email(), request.displayName());
      repository.update(id, userRequest);
    } catch (UserNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }
}
