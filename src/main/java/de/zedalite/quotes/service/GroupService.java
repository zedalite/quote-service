package de.zedalite.quotes.service;

import de.zedalite.quotes.data.model.Group;
import de.zedalite.quotes.data.model.GroupRequest;
import de.zedalite.quotes.exceptions.GroupNotFoundException;
import de.zedalite.quotes.exceptions.ResourceNotFoundException;
import de.zedalite.quotes.repository.GroupRepository;
import de.zedalite.quotes.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class GroupService {

  private final GroupRepository repository;

  private final UserRepository userRepository;

  public GroupService(final GroupRepository repository, final UserRepository userRepository) {
    this.repository = repository;
    this.userRepository = userRepository;
  }

  public Group create(final GroupRequest request) {
    try {
      return repository.save(request);
    } catch (GroupNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }

  public Group create(final GroupRequest request, final String creator) {
    final var creatorId = request.creatorId() == null ? userRepository.findByName(creator).id() : request.creatorId();

    return create(request.withCreatorId(creatorId));
  }

  public Group find(final Integer id) {
    try {
      return repository.findById(id);
    } catch (GroupNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }
}
