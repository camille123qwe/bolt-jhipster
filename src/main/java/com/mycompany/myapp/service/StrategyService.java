package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Strategy;
import com.mycompany.myapp.repository.StrategyRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Strategy}.
 */
@Service
@Transactional
public class StrategyService {

    private static final Logger LOG = LoggerFactory.getLogger(StrategyService.class);

    private final StrategyRepository strategyRepository;

    public StrategyService(StrategyRepository strategyRepository) {
        this.strategyRepository = strategyRepository;
    }

    /**
     * Save a strategy.
     *
     * @param strategy the entity to save.
     * @return the persisted entity.
     */
    public Strategy save(Strategy strategy) {
        LOG.debug("Request to save Strategy : {}", strategy);
        return strategyRepository.save(strategy);
    }

    /**
     * Update a strategy.
     *
     * @param strategy the entity to save.
     * @return the persisted entity.
     */
    public Strategy update(Strategy strategy) {
        LOG.debug("Request to update Strategy : {}", strategy);
        return strategyRepository.save(strategy);
    }

    /**
     * Partially update a strategy.
     *
     * @param strategy the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Strategy> partialUpdate(Strategy strategy) {
        LOG.debug("Request to partially update Strategy : {}", strategy);

        return strategyRepository
            .findById(strategy.getId())
            .map(existingStrategy -> {
                if (strategy.getUuid() != null) {
                    existingStrategy.setUuid(strategy.getUuid());
                }
                if (strategy.getLabel() != null) {
                    existingStrategy.setLabel(strategy.getLabel());
                }
                if (strategy.getDescription() != null) {
                    existingStrategy.setDescription(strategy.getDescription());
                }
                if (strategy.getCreatedAt() != null) {
                    existingStrategy.setCreatedAt(strategy.getCreatedAt());
                }
                if (strategy.getExecutionRule() != null) {
                    existingStrategy.setExecutionRule(strategy.getExecutionRule());
                }
                if (strategy.getStatus() != null) {
                    existingStrategy.setStatus(strategy.getStatus());
                }

                return existingStrategy;
            })
            .map(strategyRepository::save);
    }

    /**
     * Get all the strategies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Strategy> findAll(Pageable pageable) {
        LOG.debug("Request to get all Strategies");
        return strategyRepository.findAll(pageable);
    }

    /**
     * Get one strategy by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Strategy> findOne(Long id) {
        LOG.debug("Request to get Strategy : {}", id);
        return strategyRepository.findById(id);
    }

    /**
     * Delete the strategy by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Strategy : {}", id);
        strategyRepository.deleteById(id);
    }
}
