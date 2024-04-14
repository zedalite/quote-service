package de.zedalite.quotes.integration;

import de.zedalite.quotes.TestEnvironmentProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
class QuoteControllerIT extends TestEnvironmentProvider {
  @Autowired
  private MockMvc mockMvc;

  @Test
  @DisplayName("Should return success on valid input")
  void shouldReturnSuccessOnValidInput() throws Exception {
    final MvcResult result = mockMvc.perform(get("/quotes/count"))
      .andExpect(status().isOk())
      .andReturn();

    assertThat(Integer.parseInt(result.getResponse().getContentAsString())).isNotNegative();
  }
}
