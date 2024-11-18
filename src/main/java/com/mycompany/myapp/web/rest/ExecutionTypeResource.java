package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.ExecutionType;
import com.mycompany.myapp.repository.ExecutionTypeRepository;
import com.mycompany.myapp.service.ExecutionTypeService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.ExecutionType}.
 */
@RestController
@RequestMapping("/api/execution-types")
public class ExecutionTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(ExecutionTypeResource.class);

    private static final String ENTITY_NAME = "app1ExecutionType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExecutionTypeService executionTypeService;

    private final ExecutionTypeRepository executionTypeRepository;

    public ExecutionTypeResource(ExecutionTypeService executionTypeService, ExecutionTypeRepository executionTypeRepository) {
        this.executionTypeService = executionTypeService;
        this.executionTypeRepository = executionTypeRepository;
    }

    /**
     * {@code POST  /execution-types} : Create a new executionType.
     *
     * @param executionType the executionType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new executionType, or with status {@code 400 (Bad Request)} if the executionType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ExecutionType> createExecutionType(@Valid @RequestBody ExecutionType executionType) throws URISyntaxException {
        LOG.debug("REST request to save ExecutionType : {}", executionType);
        if (executionType.getId() != null) {
            throw new BadRequestAlertException("A new executionType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        executionType = executionTypeService.save(executionType);
        return ResponseEntity.created(new URI("/api/execution-types/" + executionType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, executionType.getId().toString()))
            .body(executionType);
    }

    /**
     * {@code PUT  /execution-types/:id} : Updates an existing executionType.
     *
     * @param id the id of the executionType to save.
     * @param executionType the executionType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated executionType,
     * or with status {@code 400 (Bad Request)} if the executionType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the executionType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ExecutionType> updateExecutionType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ExecutionType executionType
    ) throws URISyntaxException {
        LOG.debug("REST request to update ExecutionType : {}, {}", id, executionType);
        if (executionType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, executionType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!executionTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        executionType = executionTypeService.update(executionType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, executionType.getId().toString()))
            .body(executionType);
    }

    /**
     * {@code PATCH  /execution-types/:id} : Partial updates given fields of an existing executionType, field will ignore if it is null
     *
     * @param id the id of the executionType to save.
     * @param executionType the executionType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated executionType,
     * or with status {@code 400 (Bad Request)} if the executionType is not valid,
     * or with status {@code 404 (Not Found)} if the executionType is not found,
     * or with status {@code 500 (Internal Server Error)} if the executionType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ExecutionType> partialUpdateExecutionType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ExecutionType executionType
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ExecutionType partially : {}, {}", id, executionType);
        if (executionType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, executionType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!executionTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExecutionType> result = executionTypeService.partialUpdate(executionType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, executionType.getId().toString())
        );
    }

    /**
     * {@code GET  /execution-types} : get all the executionTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of executionTypes in body.
     */
    @GetMapping("")
    public List<ExecutionType> getAllExecutionTypes() {
        LOG.debug("REST request to get all ExecutionTypes");
        return executionTypeService.findAll();
    }

    /**
     * {@code GET  /execution-types/:id} : get the "id" executionType.
     *
     * @param id the id of the executionType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the executionType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ExecutionType> getExecutionType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ExecutionType : {}", id);
        Optional<ExecutionType> executionType = executionTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(executionType);
    }

    /**
     * {@code DELETE  /execution-types/:id} : delete the "id" executionType.
     *
     * @param id the id of the executionType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExecutionType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ExecutionType : {}", id);
        executionTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
