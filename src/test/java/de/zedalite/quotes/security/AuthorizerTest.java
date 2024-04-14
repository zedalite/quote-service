package de.zedalite.quotes.security;

import de.zedalite.quotes.data.model.UserPrincipal;
import de.zedalite.quotes.fixtures.UserGenerator;
import de.zedalite.quotes.service.GroupUserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willReturn;

@ExtendWith(MockitoExtension.class)
class AuthorizerTest {
  @InjectMocks
  private Authorizer instance;

  @Mock
  private GroupUserService service;

  @ParameterizedTest(name = "Allow User: {0}")
  @ValueSource(booleans = {true,false})
  @DisplayName("Should decide on user")
  void shouldDecideUser(final boolean expected) {
    final UserPrincipal principal = UserGenerator.getUserPrincipal();
    willReturn(expected).given(service).isUserInGroup(anyInt(), anyInt());

    final boolean result = instance.isUserInGroup(principal, 1);

    then(service).should().isUserInGroup(1, 1);
    assertThat(result).isEqualTo(expected);
  }


  @Test
  @DisplayName("Should allow user")
  void shouldAllowUser() {
    final UserPrincipal principal = UserGenerator.getUserPrincipal();
    willReturn(true).given(service).isUserInGroup(anyInt(), anyInt());

    final boolean result = instance.isUserInGroup(principal, 1);

    then(service).should().isUserInGroup(1, 1);
    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("Should disallow user")
  void shouldDisallowUser() {
    final UserPrincipal principal = UserGenerator.getUserPrincipal();
    willReturn(false).given(service).isUserInGroup(anyInt(), anyInt());

    final boolean result = instance.isUserInGroup(principal, 999);

    then(service).should().isUserInGroup(999, 1);
    assertThat(result).isFalse();
  }
}
