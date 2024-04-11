package de.zedalite.quotes.repository;

import de.zedalite.quotes.TestEnvironmentProvider;
import de.zedalite.quotes.data.jooq.tables.GroupQuotes;
import de.zedalite.quotes.data.model.GroupRequest;
import de.zedalite.quotes.data.model.Quote;
import de.zedalite.quotes.data.model.UserRequest;
import de.zedalite.quotes.fixtures.QuoteGenerator;
import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.Comparator;

import static de.zedalite.quotes.data.model.SortField.*;
import static de.zedalite.quotes.data.model.SortOrder.ASC;
import static de.zedalite.quotes.data.model.SortOrder.DESC;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(value = "classpath:test-no-cache.properties")
class GroupQuoteRepositoryTest extends TestEnvironmentProvider {

  private static final GroupQuotes GROUP_QUOTES = GroupQuotes.GROUP_QUOTES.as("group_quotes");

  @Autowired
  private GroupQuoteRepository instance;

  @Autowired
  private GroupRepository groupRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private DSLContext dsl;

  private Integer groupId;

  private Quote quote;


  @BeforeAll
  void setup() {
    final var userId = userRepository.save(new UserRequest("qg", "test")).id();
    groupId = groupRepository.save(new GroupRequest("quoter-group", "Quoter Group", LocalDateTime.now(), userId)).id();
    quote = instance.save(groupId, QuoteGenerator.getQuoteRequest());
  }

  @Test
  @DisplayName("Should save group quote")
  void shouldSaveGroupQuote() {
    final var quoteRequest = QuoteGenerator.getQuoteRequest();

    final var quote = instance.save(groupId, quoteRequest);

    assertThat(quote).isNotNull();
    assertThat(quote.id()).isNotNegative();
    assertThat(quote.author()).isEqualTo(quoteRequest.author());
    assertThat(quote.text()).isEqualTo(quoteRequest.text());
    assertThat(quote.context()).isEqualTo(quoteRequest.context());
    assertThat(quote.creatorId()).isEqualTo(quoteRequest.creatorId());

    final var isInserted = dsl.fetchExists(dsl.selectFrom(GROUP_QUOTES).where(GROUP_QUOTES.GROUP_ID.eq(groupId).and(GROUP_QUOTES.QUOTE_ID.eq(quote.id()))));

    assertThat(isInserted).isTrue();
  }

  @Test
  @DisplayName("Should find group quote by id")
  void shouldFindGroupQuoteById() {
    final var quoteRequest = QuoteGenerator.getQuoteRequest();
    final var savedQuote = instance.save(groupId, quoteRequest);

    final var quote = instance.findById(groupId, savedQuote.id());

    assertThat(quote).isNotNull();
    assertThat(quote.id()).isNotNegative();
    assertThat(quote.author()).isEqualTo(quoteRequest.author());
    assertThat(quote.text()).isEqualTo(quoteRequest.text());
    assertThat(quote.context()).isEqualTo(quoteRequest.context());
    assertThat(quote.creatorId()).isEqualTo(quoteRequest.creatorId());
  }

  @Test
  @DisplayName("Should find all group quotes")
  void shouldFindAllGroupQuotes() {
    final var sortedAuthorAsc = instance.findAll(groupId, AUTHOR, ASC);
    final var sortedAuthorDesc = instance.findAll(groupId, AUTHOR, DESC);

    final var sortedCreationAsc = instance.findAll(groupId, CREATION_DATE, ASC);
    final var sortedCreationDesc = instance.findAll(groupId, CREATION_DATE, DESC);

    final var sortedTextAsc = instance.findAll(groupId, TEXT, ASC);
    final var sortedTextDesc = instance.findAll(groupId, TEXT, DESC);

    assertThat(sortedAuthorAsc).map(Quote::author).isSortedAccordingTo(Comparator.naturalOrder());
    assertThat(sortedAuthorDesc).map(Quote::author).isSortedAccordingTo(Comparator.reverseOrder());

    assertThat(sortedCreationAsc).map(Quote::author).isSortedAccordingTo(Comparator.naturalOrder());
    assertThat(sortedCreationDesc).map(Quote::author).isSortedAccordingTo(Comparator.reverseOrder());

    assertThat(sortedTextAsc).map(Quote::author).isSortedAccordingTo(Comparator.naturalOrder());
    assertThat(sortedTextDesc).map(Quote::author).isSortedAccordingTo(Comparator.reverseOrder());
  }

  @Test
  @DisplayName("Should find random group quotes")
  void shouldFindRandomGroupQuotes() {
    final var quotes = instance.findRandoms(groupId, 8);

    assertThat(quotes).contains(quote);
  }

  @Test
  @DisplayName("Should find group quote count")
  void shouldFindGroupQuoteCount() {
    final var count = instance.count(groupId);

    assertThat(count).isGreaterThan(1);
  }
}
