package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.StrategyAsserts.assertStrategyAllPropertiesEquals;
import static com.mycompany.myapp.domain.StrategyAsserts.assertStrategyAllUpdatablePropertiesEquals;
import static com.mycompany.myapp.domain.StrategyAsserts.assertStrategyUpdatableFieldsEquals;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.mycompany.myapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Strategy;
import com.mycompany.myapp.domain.enumeration.Status;
import com.mycompany.myapp.repository.StrategyRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link StrategyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StrategyResourceIT {

    private static final UUID DEFAULT_UUID = UUID.randomUUID();
    private static final UUID UPDATED_UUID = UUID.randomUUID();

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_EXECUTION_RULE = "AAAAAAAAAA";
    private static final String UPDATED_EXECUTION_RULE = "BBBBBBBBBB";

    private static final Status DEFAULT_STATUS = Status.Active;
    private static final Status UPDATED_STATUS = Status.Inactive;

    private static final String ENTITY_API_URL = "/api/strategies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private StrategyRepository strategyRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStrategyMockMvc;

    private Strategy strategy;

    private Strategy insertedStrategy;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Strategy createEntity() {
        return new Strategy()
            .uuid(DEFAULT_UUID)
            .label(DEFAULT_LABEL)
            .description(DEFAULT_DESCRIPTION)
            .createdAt(DEFAULT_CREATED_AT)
            .executionRule(DEFAULT_EXECUTION_RULE)
            .status(DEFAULT_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Strategy createUpdatedEntity() {
        return new Strategy()
            .uuid(UPDATED_UUID)
            .label(UPDATED_LABEL)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .executionRule(UPDATED_EXECUTION_RULE)
            .status(UPDATED_STATUS);
    }

    @BeforeEach
    public void initTest() {
        strategy = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedStrategy != null) {
            strategyRepository.delete(insertedStrategy);
            insertedStrategy = null;
        }
    }

    @Test
    @Transactional
    void createStrategy() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Strategy
        var returnedStrategy = om.readValue(
            restStrategyMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(strategy)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Strategy.class
        );

        // Validate the Strategy in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertStrategyUpdatableFieldsEquals(returnedStrategy, getPersistedStrategy(returnedStrategy));

        insertedStrategy = returnedStrategy;
    }

    @Test
    @Transactional
    void createStrategyWithExistingId() throws Exception {
        // Create the Strategy with an existing ID
        strategy.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStrategyMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(strategy)))
            .andExpect(status().isBadRequest());

        // Validate the Strategy in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUuidIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        strategy.setUuid(null);

        // Create the Strategy, which fails.

        restStrategyMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(strategy)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLabelIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        strategy.setLabel(null);

        // Create the Strategy, which fails.

        restStrategyMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(strategy)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        strategy.setCreatedAt(null);

        // Create the Strategy, which fails.

        restStrategyMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(strategy)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkExecutionRuleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        strategy.setExecutionRule(null);

        // Create the Strategy, which fails.

        restStrategyMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(strategy)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        strategy.setStatus(null);

        // Create the Strategy, which fails.

        restStrategyMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(strategy)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllStrategies() throws Exception {
        // Initialize the database
        insertedStrategy = strategyRepository.saveAndFlush(strategy);

        // Get all the strategyList
        restStrategyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(strategy.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].executionRule").value(hasItem(DEFAULT_EXECUTION_RULE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getStrategy() throws Exception {
        // Initialize the database
        insertedStrategy = strategyRepository.saveAndFlush(strategy);

        // Get the strategy
        restStrategyMockMvc
            .perform(get(ENTITY_API_URL_ID, strategy.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(strategy.getId().intValue()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.executionRule").value(DEFAULT_EXECUTION_RULE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingStrategy() throws Exception {
        // Get the strategy
        restStrategyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStrategy() throws Exception {
        // Initialize the database
        insertedStrategy = strategyRepository.saveAndFlush(strategy);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the strategy
        Strategy updatedStrategy = strategyRepository.findById(strategy.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedStrategy are not directly saved in db
        em.detach(updatedStrategy);
        updatedStrategy
            .uuid(UPDATED_UUID)
            .label(UPDATED_LABEL)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .executionRule(UPDATED_EXECUTION_RULE)
            .status(UPDATED_STATUS);

        restStrategyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedStrategy.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedStrategy))
            )
            .andExpect(status().isOk());

        // Validate the Strategy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedStrategyToMatchAllProperties(updatedStrategy);
    }

    @Test
    @Transactional
    void putNonExistingStrategy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        strategy.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStrategyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, strategy.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(strategy))
            )
            .andExpect(status().isBadRequest());

        // Validate the Strategy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStrategy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        strategy.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStrategyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(strategy))
            )
            .andExpect(status().isBadRequest());

        // Validate the Strategy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStrategy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        strategy.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStrategyMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(strategy)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Strategy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStrategyWithPatch() throws Exception {
        // Initialize the database
        insertedStrategy = strategyRepository.saveAndFlush(strategy);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the strategy using partial update
        Strategy partialUpdatedStrategy = new Strategy();
        partialUpdatedStrategy.setId(strategy.getId());

        partialUpdatedStrategy.uuid(UPDATED_UUID).description(UPDATED_DESCRIPTION).createdAt(UPDATED_CREATED_AT);

        restStrategyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStrategy.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStrategy))
            )
            .andExpect(status().isOk());

        // Validate the Strategy in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStrategyUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedStrategy, strategy), getPersistedStrategy(strategy));
    }

    @Test
    @Transactional
    void fullUpdateStrategyWithPatch() throws Exception {
        // Initialize the database
        insertedStrategy = strategyRepository.saveAndFlush(strategy);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the strategy using partial update
        Strategy partialUpdatedStrategy = new Strategy();
        partialUpdatedStrategy.setId(strategy.getId());

        partialUpdatedStrategy
            .uuid(UPDATED_UUID)
            .label(UPDATED_LABEL)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .executionRule(UPDATED_EXECUTION_RULE)
            .status(UPDATED_STATUS);

        restStrategyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStrategy.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStrategy))
            )
            .andExpect(status().isOk());

        // Validate the Strategy in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStrategyUpdatableFieldsEquals(partialUpdatedStrategy, getPersistedStrategy(partialUpdatedStrategy));
    }

    @Test
    @Transactional
    void patchNonExistingStrategy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        strategy.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStrategyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, strategy.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(strategy))
            )
            .andExpect(status().isBadRequest());

        // Validate the Strategy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStrategy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        strategy.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStrategyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(strategy))
            )
            .andExpect(status().isBadRequest());

        // Validate the Strategy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStrategy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        strategy.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStrategyMockMvc
            .perform(patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(strategy)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Strategy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStrategy() throws Exception {
        // Initialize the database
        insertedStrategy = strategyRepository.saveAndFlush(strategy);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the strategy
        restStrategyMockMvc
            .perform(delete(ENTITY_API_URL_ID, strategy.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return strategyRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Strategy getPersistedStrategy(Strategy strategy) {
        return strategyRepository.findById(strategy.getId()).orElseThrow();
    }

    protected void assertPersistedStrategyToMatchAllProperties(Strategy expectedStrategy) {
        assertStrategyAllPropertiesEquals(expectedStrategy, getPersistedStrategy(expectedStrategy));
    }

    protected void assertPersistedStrategyToMatchUpdatableProperties(Strategy expectedStrategy) {
        assertStrategyAllUpdatablePropertiesEquals(expectedStrategy, getPersistedStrategy(expectedStrategy));
    }
}
