package de.zedalite.quotes.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.never;

import de.zedalite.quotes.data.model.User;
import de.zedalite.quotes.data.model.UserResponse;
import de.zedalite.quotes.exception.ResourceAlreadyExitsException;
import de.zedalite.quotes.exception.ResourceNotFoundException;
import de.zedalite.quotes.exception.UserNotFoundException;
import de.zedalite.quotes.fixtures.UserGenerator;
import de.zedalite.quotes.repository.GroupUserRepository;
import de.zedalite.quotes.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GroupUserServiceTest {

  @InjectMocks
  private GroupUserService instance;

  @Mock
  private GroupUserRepository repository;

  @Mock
  private UserRepository userRepository;

  @ParameterizedTest
  @ValueSource(booleans = { true, false })
  @DisplayName("Should create group user")
  void shouldCreateGroupUser(final boolean isUserSaved) {
    willReturn(false).given(userRepository).doesUserNonExist(2);
    willReturn(false).given(repository).isUserInGroup(1, 2);
    willReturn(isUserSaved).given(repository).save(anyInt(), anyInt());

    final Boolean result = instance.create(1, 2);

    then(repository).should().save(1, 2);
    assertThat(result).isEqualTo(isUserSaved);
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
    final User expectedUser = UserGenerator.getUser();
    willReturn(expectedUser).given(repository).findById(1, 2);

    final UserResponse result = instance.find(1, 2);

    then(repository).should().findById(1, 2);
    assertThat(result).isNotNull();
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
    final List<User> expectedUsers = UserGenerator.getUsers();
    willReturn(expectedUsers).given(repository).findAll(1);

    final List<UserResponse> result = instance.findAll(1);

    then(repository).should().findAll(1);
    assertThat(result).hasSizeGreaterThanOrEqualTo(1);
  }

  @Test
  @DisplayName("Should not find group users when user non exist")
  void shouldNotFindGroupUsersWhenUserNonExist() {
    willThrow(UserNotFoundException.class).given(repository).findAll(1);

    assertThatCode(() -> instance.findAll(1)).isInstanceOf(ResourceNotFoundException.class);
  }

  @ParameterizedTest(name = "User in Group: {0}")
  @ValueSource(booleans = { true, false })
  @DisplayName("Should determine if user is in group")
  void shouldDetermineIfUserIsInGroup(final Boolean isInGroup) {
    willReturn(isInGroup).given(repository).isUserInGroup(1, 2);

    final boolean result = instance.isUserInGroup(1, 2);

    assertThat(result).isEqualTo(isInGroup);
  }
}
