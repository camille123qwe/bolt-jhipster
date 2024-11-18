package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Source;
import com.mycompany.myapp.repository.SourceRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Source}.
 */
@Service
@Transactional
public class SourceService {

    private static final Logger LOG = LoggerFactory.getLogger(SourceService.class);

    private final SourceRepository sourceRepository;

    public SourceService(SourceRepository sourceRepository) {
        this.sourceRepository = sourceRepository;
    }

    /**
     * Save a source.
     *
     * @param source the entity to save.
     * @return the persisted entity.
     */
    public Source save(Source source) {
        LOG.debug("Request to save Source : {}", source);
        return sourceRepository.save(source);
    }

    /**
     * Update a source.
     *
     * @param source the entity to save.
     * @return the persisted entity.
     */
    public Source update(Source source) {
        LOG.debug("Request to update Source : {}", source);
        return sourceRepository.save(source);
    }

    /**
     * Partially update a source.
     *
     * @param source the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Source> partialUpdate(Source source) {
        LOG.debug("Request to partially update Source : {}", source);

        return sourceRepository
            .findById(source.getId())
            .map(existingSource -> {
                if (source.getUuid() != null) {
                    existingSource.setUuid(source.getUuid());
                }
                if (source.getName() != null) {
                    existingSource.setName(source.getName());
                }
                if (source.getLabel() != null) {
                    existingSource.setLabel(source.getLabel());
                }
                if (source.getDescription() != null) {
                    existingSource.setDescription(source.getDescription());
                }
                if (source.getCreatedAt() != null) {
                    existingSource.setCreatedAt(source.getCreatedAt());
                }

                return existingSource;
            })
            .map(sourceRepository::save);
    }

    /**
     * Get all the sources.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Source> findAll() {
        LOG.debug("Request to get all Sources");
        return sourceRepository.findAll();
    }

    /**
     * Get one source by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Source> findOne(Long id) {
        LOG.debug("Request to get Source : {}", id);
        return sourceRepository.findById(id);
    }

    /**
     * Delete the source by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Source : {}", id);
        sourceRepository.deleteById(id);
    }
}
