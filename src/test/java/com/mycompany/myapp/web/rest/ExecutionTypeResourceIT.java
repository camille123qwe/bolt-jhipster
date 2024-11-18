package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ExecutionTypeAsserts.assertExecutionTypeAllPropertiesEquals;
import static com.mycompany.myapp.domain.ExecutionTypeAsserts.assertExecutionTypeAllUpdatablePropertiesEquals;
import static com.mycompany.myapp.domain.ExecutionTypeAsserts.assertExecutionTypeUpdatableFieldsEquals;
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
import com.mycompany.myapp.domain.ExecutionType;
import com.mycompany.myapp.repository.ExecutionTypeRepository;
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
 * Integration tests for the {@link ExecutionTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExecutionTypeResourceIT {

    private static final UUID DEFAULT_UUID = UUID.randomUUID();
    private static final UUID UPDATED_UUID = UUID.randomUUID();

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/execution-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ExecutionTypeRepository executionTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExecutionTypeMockMvc;

    private ExecutionType executionType;

    private ExecutionType insertedExecutionType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExecutionType createEntity() {
        return new ExecutionType()
            .uuid(DEFAULT_UUID)
            .name(DEFAULT_NAME)
            .label(DEFAULT_LABEL)
            .description(DEFAULT_DESCRIPTION)
            .createdAt(DEFAULT_CREATED_AT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExecutionType createUpdatedEntity() {
        return new ExecutionType()
            .uuid(UPDATED_UUID)
            .name(UPDATED_NAME)
            .label(UPDATED_LABEL)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT);
    }

    @BeforeEach
    public void initTest() {
        executionType = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedExecutionType != null) {
            executionTypeRepository.delete(insertedExecutionType);
            insertedExecutionType = null;
        }
    }

    @Test
    @Transactional
    void createExecutionType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ExecutionType
        var returnedExecutionType = om.readValue(
            restExecutionTypeMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(executionType))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ExecutionType.class
        );

        // Validate the ExecutionType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertExecutionTypeUpdatableFieldsEquals(returnedExecutionType, getPersistedExecutionType(returnedExecutionType));

        insertedExecutionType = returnedExecutionType;
    }

    @Test
    @Transactional
    void createExecutionTypeWithExistingId() throws Exception {
        // Create the ExecutionType with an existing ID
        executionType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExecutionTypeMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(executionType)))
            .andExpect(status().isBadRequest());

        // Validate the ExecutionType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUuidIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        executionType.setUuid(null);

        // Create the ExecutionType, which fails.

        restExecutionTypeMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(executionType)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        executionType.setName(null);

        // Create the ExecutionType, which fails.

        restExecutionTypeMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(executionType)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        executionType.setCreatedAt(null);

        // Create the ExecutionType, which fails.

        restExecutionTypeMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(executionType)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllExecutionTypes() throws Exception {
        // Initialize the database
        insertedExecutionType = executionTypeRepository.saveAndFlush(executionType);

        // Get all the executionTypeList
        restExecutionTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(executionType.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))));
    }

    @Test
    @Transactional
    void getExecutionType() throws Exception {
        // Initialize the database
        insertedExecutionType = executionTypeRepository.saveAndFlush(executionType);

        // Get the executionType
        restExecutionTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, executionType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(executionType.getId().intValue()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)));
    }

    @Test
    @Transactional
    void getNonExistingExecutionType() throws Exception {
        // Get the executionType
        restExecutionTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingExecutionType() throws Exception {
        // Initialize the database
        insertedExecutionType = executionTypeRepository.saveAndFlush(executionType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the executionType
        ExecutionType updatedExecutionType = executionTypeRepository.findById(executionType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedExecutionType are not directly saved in db
        em.detach(updatedExecutionType);
        updatedExecutionType
            .uuid(UPDATED_UUID)
            .name(UPDATED_NAME)
            .label(UPDATED_LABEL)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT);

        restExecutionTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedExecutionType.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedExecutionType))
            )
            .andExpect(status().isOk());

        // Validate the ExecutionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedExecutionTypeToMatchAllProperties(updatedExecutionType);
    }

    @Test
    @Transactional
    void putNonExistingExecutionType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        executionType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExecutionTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, executionType.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(executionType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExecutionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExecutionType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        executionType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExecutionTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(executionType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExecutionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExecutionType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        executionType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExecutionTypeMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(executionType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExecutionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExecutionTypeWithPatch() throws Exception {
        // Initialize the database
        insertedExecutionType = executionTypeRepository.saveAndFlush(executionType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the executionType using partial update
        ExecutionType partialUpdatedExecutionType = new ExecutionType();
        partialUpdatedExecutionType.setId(executionType.getId());

        partialUpdatedExecutionType.name(UPDATED_NAME).label(UPDATED_LABEL).description(UPDATED_DESCRIPTION).createdAt(UPDATED_CREATED_AT);

        restExecutionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExecutionType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExecutionType))
            )
            .andExpect(status().isOk());

        // Validate the ExecutionType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExecutionTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedExecutionType, executionType),
            getPersistedExecutionType(executionType)
        );
    }

    @Test
    @Transactional
    void fullUpdateExecutionTypeWithPatch() throws Exception {
        // Initialize the database
        insertedExecutionType = executionTypeRepository.saveAndFlush(executionType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the executionType using partial update
        ExecutionType partialUpdatedExecutionType = new ExecutionType();
        partialUpdatedExecutionType.setId(executionType.getId());

        partialUpdatedExecutionType
            .uuid(UPDATED_UUID)
            .name(UPDATED_NAME)
            .label(UPDATED_LABEL)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT);

        restExecutionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExecutionType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExecutionType))
            )
            .andExpect(status().isOk());

        // Validate the ExecutionType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExecutionTypeUpdatableFieldsEquals(partialUpdatedExecutionType, getPersistedExecutionType(partialUpdatedExecutionType));
    }

    @Test
    @Transactional
    void patchNonExistingExecutionType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        executionType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExecutionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, executionType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(executionType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExecutionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExecutionType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        executionType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExecutionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(executionType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExecutionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExecutionType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        executionType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExecutionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(executionType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExecutionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExecutionType() throws Exception {
        // Initialize the database
        insertedExecutionType = executionTypeRepository.saveAndFlush(executionType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the executionType
        restExecutionTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, executionType.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return executionTypeRepository.count();
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

    protected ExecutionType getPersistedExecutionType(ExecutionType executionType) {
        return executionTypeRepository.findById(executionType.getId()).orElseThrow();
    }

    protected void assertPersistedExecutionTypeToMatchAllProperties(ExecutionType expectedExecutionType) {
        assertExecutionTypeAllPropertiesEquals(expectedExecutionType, getPersistedExecutionType(expectedExecutionType));
    }

    protected void assertPersistedExecutionTypeToMatchUpdatableProperties(ExecutionType expectedExecutionType) {
        assertExecutionTypeAllUpdatablePropertiesEquals(expectedExecutionType, getPersistedExecutionType(expectedExecutionType));
    }
}
