package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.ExecutionType;
import com.mycompany.myapp.repository.ExecutionTypeRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.ExecutionType}.
 */
@Service
@Transactional
public class ExecutionTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(ExecutionTypeService.class);

    private final ExecutionTypeRepository executionTypeRepository;

    public ExecutionTypeService(ExecutionTypeRepository executionTypeRepository) {
        this.executionTypeRepository = executionTypeRepository;
    }

    /**
     * Save a executionType.
     *
     * @param executionType the entity to save.
     * @return the persisted entity.
     */
    public ExecutionType save(ExecutionType executionType) {
        LOG.debug("Request to save ExecutionType : {}", executionType);
        return executionTypeRepository.save(executionType);
    }

    /**
     * Update a executionType.
     *
     * @param executionType the entity to save.
     * @return the persisted entity.
     */
    public ExecutionType update(ExecutionType executionType) {
        LOG.debug("Request to update ExecutionType : {}", executionType);
        return executionTypeRepository.save(executionType);
    }

    /**
     * Partially update a executionType.
     *
     * @param executionType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ExecutionType> partialUpdate(ExecutionType executionType) {
        LOG.debug("Request to partially update ExecutionType : {}", executionType);

        return executionTypeRepository
            .findById(executionType.getId())
            .map(existingExecutionType -> {
                if (executionType.getUuid() != null) {
                    existingExecutionType.setUuid(executionType.getUuid());
                }
                if (executionType.getName() != null) {
                    existingExecutionType.setName(executionType.getName());
                }
                if (executionType.getLabel() != null) {
                    existingExecutionType.setLabel(executionType.getLabel());
                }
                if (executionType.getDescription() != null) {
                    existingExecutionType.setDescription(executionType.getDescription());
                }
                if (executionType.getCreatedAt() != null) {
                    existingExecutionType.setCreatedAt(executionType.getCreatedAt());
                }

                return existingExecutionType;
            })
            .map(executionTypeRepository::save);
    }

    /**
     * Get all the executionTypes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ExecutionType> findAll() {
        LOG.debug("Request to get all ExecutionTypes");
        return executionTypeRepository.findAll();
    }

    /**
     * Get one executionType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ExecutionType> findOne(Long id) {
        LOG.debug("Request to get ExecutionType : {}", id);
        return executionTypeRepository.findById(id);
    }

    /**
     * Delete the executionType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ExecutionType : {}", id);
        executionTypeRepository.deleteById(id);
    }
}
