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
    final var token = tokenService.generateToken(request.name());
    return new AuthResponse(token);
  }

  public AuthResponse refreshToken(final String username) {
    final var token = tokenService.generateToken(username);
    return new AuthResponse(token);
  }

  public User create(final UserRequest request) throws ResourceAlreadyExitsException {
    try {
      repository.findByName(request.name());
      throw new ResourceAlreadyExitsException(USER_ALREADY_EXITS);
    } catch (UserNotFoundException ex) {
      final var encodedRequest = request.withPassword(passwordEncoder.encode(request.password()));
      return repository.save(encodedRequest);
    }
  }

  public User find(final String name) throws ResourceNotFoundException {
    try {
      return repository.findByName(name);
    } catch (UserNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }

  public void updatePassword(final String username, final PasswordRequest request) {
    try {
      final var user = find(username);
      final var userRequest = new UserRequest(user.name(), passwordEncoder.encode(request.password()), user.displayName());
      repository.update(username, userRequest);
    } catch (UserNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }

  public void updateDisplayName(final String username, final DisplayNameRequest request) {
    try {
      final var user = find(username);
      final var userRequest = new UserRequest(user.name(), user.password(), request.displayName());
      repository.update(username, userRequest);
    } catch (UserNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }
}
