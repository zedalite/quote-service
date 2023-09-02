package de.zedalite.quotes.service;

import de.zedalite.quotes.exceptions.ResourceAlreadyExitsException;
import de.zedalite.quotes.exceptions.ResourceNotFoundException;
import de.zedalite.quotes.exceptions.UserNotFoundException;
import de.zedalite.quotes.fixtures.UserGenerator;
import de.zedalite.quotes.repository.GroupUserRepository;
import de.zedalite.quotes.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class GroupUserServiceTest {

  @InjectMocks
  private GroupUserService instance;

  @Mock
  private GroupUserRepository repository;

  @Mock
  private UserRepository userRepository;

  @Test
  @DisplayName("Should create group user")
  void shouldCreateGroupUser() {
    willReturn(false).given(userRepository).doesUserNonExist(2);
    willReturn(false).given(repository).isUserInGroup(1, 2);

    instance.create(1, 2);

    then(repository).should().save(1,2);
  }

  @Test
  @DisplayName("Should not create group user when user non exist")
  void shouldNotCreateGroupUserWhenUserNonExist() {
    willReturn(true).given(userRepository).doesUserNonExist(2);

    assertThatCode(() -> instance.create(1, 2)).isInstanceOf(ResourceNotFoundException.class);
    then(repository).shouldHaveNoInteractions();
  }

  @Test
  @DisplayName("Should not create group user when user already in group")
  void shouldNotCreateGroupUserWhenUserAlreadyInGroup() {
    willReturn(false).given(userRepository).doesUserNonExist(2);
    willReturn(true).given(repository).isUserInGroup(1, 2);

    assertThatCode(() -> instance.create(1, 2)).isInstanceOf(ResourceAlreadyExitsException.class);
    then(repository).should(never()).save(1, 2);
  }

  @Test
  @DisplayName("Should not create group user when saving failed")
  void shouldNotCreateGroupUserWhenSavingFailed() {
    willReturn(false).given(userRepository).doesUserNonExist(2);
    willReturn(false).given(repository).isUserInGroup(1, 2);
    willThrow(UserNotFoundException.class).given(repository).save(1, 2);

    assertThatCode(() -> instance.create(1, 2)).isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  @DisplayName("Should find group user")
  void shouldFindGroupUser() {
    final var expectedUser = UserGenerator.getUser();
    willReturn(expectedUser).given(repository).findById(1, 2);

    instance.find(1, 2);

    then(repository).should().findById(1, 2);
  }

  @Test
  @DisplayName("Should not find group user when user non exist")
  void shouldNotFindGroupUserWhenUserNonExist() {
    willThrow(UserNotFoundException.class).given(repository).findById(1, 2);

    assertThatCode(() -> instance.find(1, 2)).isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  @DisplayName("Should find group users")
  void shouldFindGroupUsers() {
    final var expectedUsers = UserGenerator.getUsers();
    willReturn(expectedUsers).given(repository).findAll(1);

    instance.findAll(1);

    then(repository).should().findAll(1);
  }

  @Test
  @DisplayName("Should not find group users when user non exist")
  void shouldNotFindGroupUsersWhenUserNonExist() {
    willThrow(UserNotFoundException.class).given(repository).findAll(1);

    assertThatCode(() -> instance.findAll(1)).isInstanceOf(ResourceNotFoundException.class);
  }

  @ParameterizedTest(name = "User in Group: {0}")
  @ValueSource(booleans = {true,false})
  @DisplayName("Should determine if user is in group")
  void shouldDetermineIfUserIsInGroup(final Boolean isInGroup) {
    willReturn(isInGroup).given(repository).isUserInGroup(1, 2);

    final var result = instance.isUserInGroup(1, 2);

    assertThat(result).isEqualTo(isInGroup);
  }

}
