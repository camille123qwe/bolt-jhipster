package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Source;
import com.mycompany.myapp.repository.SourceRepository;
import com.mycompany.myapp.service.SourceService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Source}.
 */
@RestController
@RequestMapping("/api/sources")
public class SourceResource {

    private static final Logger LOG = LoggerFactory.getLogger(SourceResource.class);

    private static final String ENTITY_NAME = "app1Source";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SourceService sourceService;

    private final SourceRepository sourceRepository;

    public SourceResource(SourceService sourceService, SourceRepository sourceRepository) {
        this.sourceService = sourceService;
        this.sourceRepository = sourceRepository;
    }

    /**
     * {@code POST  /sources} : Create a new source.
     *
     * @param source the source to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new source, or with status {@code 400 (Bad Request)} if the source has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Source> createSource(@Valid @RequestBody Source source) throws URISyntaxException {
        LOG.debug("REST request to save Source : {}", source);
        if (source.getId() != null) {
            throw new BadRequestAlertException("A new source cannot already have an ID", ENTITY_NAME, "idexists");
        }
        source = sourceService.save(source);
        return ResponseEntity.created(new URI("/api/sources/" + source.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, source.getId().toString()))
            .body(source);
    }

    /**
     * {@code PUT  /sources/:id} : Updates an existing source.
     *
     * @param id the id of the source to save.
     * @param source the source to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated source,
     * or with status {@code 400 (Bad Request)} if the source is not valid,
     * or with status {@code 500 (Internal Server Error)} if the source couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Source> updateSource(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Source source
    ) throws URISyntaxException {
        LOG.debug("REST request to update Source : {}, {}", id, source);
        if (source.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, source.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sourceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        source = sourceService.update(source);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, source.getId().toString()))
            .body(source);
    }

    /**
     * {@code PATCH  /sources/:id} : Partial updates given fields of an existing source, field will ignore if it is null
     *
     * @param id the id of the source to save.
     * @param source the source to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated source,
     * or with status {@code 400 (Bad Request)} if the source is not valid,
     * or with status {@code 404 (Not Found)} if the source is not found,
     * or with status {@code 500 (Internal Server Error)} if the source couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Source> partialUpdateSource(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Source source
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Source partially : {}, {}", id, source);
        if (source.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, source.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sourceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Source> result = sourceService.partialUpdate(source);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, source.getId().toString())
        );
    }

    /**
     * {@code GET  /sources} : get all the sources.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sources in body.
     */
    @GetMapping("")
    public List<Source> getAllSources() {
        LOG.debug("REST request to get all Sources");
        return sourceService.findAll();
    }

    /**
     * {@code GET  /sources/:id} : get the "id" source.
     *
     * @param id the id of the source to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the source, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Source> getSource(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Source : {}", id);
        Optional<Source> source = sourceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(source);
    }

    /**
     * {@code DELETE  /sources/:id} : delete the "id" source.
     *
     * @param id the id of the source to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSource(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Source : {}", id);
        sourceService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
