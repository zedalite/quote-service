package de.zedproject.quotesapi.service;

import de.zedproject.quotesapi.data.model.User;
import de.zedproject.quotesapi.data.model.UserRequest;
import de.zedproject.quotesapi.data.model.UserResponse;
import de.zedproject.quotesapi.exceptions.ResourceAlreadyExitsException;
import de.zedproject.quotesapi.exceptions.UserNotFoundException;
import de.zedproject.quotesapi.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  public static final String USER_ALREADY_EXITS = "User already exits";

  private final UserRepository repository;

  private final PasswordEncoder passwordEncoder;

  private final AuthenticationManager authenticationManager;

  private final JwtTokenService tokenService;

  public UserService(final UserRepository repository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenService tokenService) {
    this.repository = repository;
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;
    this.tokenService = tokenService;
  }

  public UserResponse authenticate(final UserRequest request) {
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.name(), request.password()));
    final var token = tokenService.generateToken(request.name());
    return new UserResponse(token);
  }

  public User createUser(final UserRequest request) throws ResourceAlreadyExitsException {
    try {
      findByName(request.name());
      throw new ResourceAlreadyExitsException(USER_ALREADY_EXITS);
    } catch (UserNotFoundException ex) {
      final var encodedRequest = request.withPassword(passwordEncoder.encode(request.password()));
      return repository.save(encodedRequest);
    }
  }

  public User findByName(final String name) {
    return repository.findByName(name);
  }
}
