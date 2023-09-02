package de.zedalite.quotes.service;

import de.zedalite.quotes.data.model.Group;
import de.zedalite.quotes.data.model.GroupRequest;
import de.zedalite.quotes.exceptions.GroupNotFoundException;
import de.zedalite.quotes.exceptions.ResourceNotFoundException;
import de.zedalite.quotes.repository.GroupRepository;
import org.springframework.stereotype.Service;

@Service
public class GroupService {

  private final GroupRepository repository;

  public GroupService(final GroupRepository repository) {
    this.repository = repository;
  }

  public Group create(final GroupRequest request) {
    try {
      return repository.save(request);
    } catch (GroupNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }

  public Group create(final GroupRequest request, final Integer creatorId) {
    final var creatorIdOrDefault = request.creatorId() == null ? creatorId : request.creatorId();

    return create(request.withCreatorId(creatorIdOrDefault));
  }

  public Group find(final Integer id) {
    try {
      return repository.findById(id);
    } catch (GroupNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    }
  }
}
