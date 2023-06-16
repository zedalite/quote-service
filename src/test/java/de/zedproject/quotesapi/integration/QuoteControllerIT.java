package de.zedproject.quotesapi.integration;

import de.zedproject.quotesapi.DatabaseContainerBaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
class QuoteControllerIT extends DatabaseContainerBaseTest {
  @Autowired
  private MockMvc mockMvc;

  @Test
  @DisplayName("Should return success on valid input")
  void shouldReturnSuccessOnValidInput() throws Exception {
    final var result = mockMvc.perform(get("/quotes/count"))
      .andExpect(status().isOk())
      .andReturn();

    assertThat(Integer.parseInt(result.getResponse().getContentAsString())).isNotNegative();
  }
}
