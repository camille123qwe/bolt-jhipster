package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Strategy;
import com.mycompany.myapp.repository.StrategyRepository;
import com.mycompany.myapp.service.StrategyService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Strategy}.
 */
@RestController
@RequestMapping("/api/strategies")
public class StrategyResource {

    private static final Logger LOG = LoggerFactory.getLogger(StrategyResource.class);

    private static final String ENTITY_NAME = "app1Strategy";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StrategyService strategyService;

    private final StrategyRepository strategyRepository;

    public StrategyResource(StrategyService strategyService, StrategyRepository strategyRepository) {
        this.strategyService = strategyService;
        this.strategyRepository = strategyRepository;
    }

    /**
     * {@code POST  /strategies} : Create a new strategy.
     *
     * @param strategy the strategy to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new strategy, or with status {@code 400 (Bad Request)} if the strategy has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Strategy> createStrategy(@Valid @RequestBody Strategy strategy) throws URISyntaxException {
        LOG.debug("REST request to save Strategy : {}", strategy);
        if (strategy.getId() != null) {
            throw new BadRequestAlertException("A new strategy cannot already have an ID", ENTITY_NAME, "idexists");
        }
        strategy = strategyService.save(strategy);
        return ResponseEntity.created(new URI("/api/strategies/" + strategy.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, strategy.getId().toString()))
            .body(strategy);
    }

    /**
     * {@code PUT  /strategies/:id} : Updates an existing strategy.
     *
     * @param id the id of the strategy to save.
     * @param strategy the strategy to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated strategy,
     * or with status {@code 400 (Bad Request)} if the strategy is not valid,
     * or with status {@code 500 (Internal Server Error)} if the strategy couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Strategy> updateStrategy(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Strategy strategy
    ) throws URISyntaxException {
        LOG.debug("REST request to update Strategy : {}, {}", id, strategy);
        if (strategy.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, strategy.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!strategyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        strategy = strategyService.update(strategy);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, strategy.getId().toString()))
            .body(strategy);
    }

    /**
     * {@code PATCH  /strategies/:id} : Partial updates given fields of an existing strategy, field will ignore if it is null
     *
     * @param id the id of the strategy to save.
     * @param strategy the strategy to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated strategy,
     * or with status {@code 400 (Bad Request)} if the strategy is not valid,
     * or with status {@code 404 (Not Found)} if the strategy is not found,
     * or with status {@code 500 (Internal Server Error)} if the strategy couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Strategy> partialUpdateStrategy(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Strategy strategy
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Strategy partially : {}, {}", id, strategy);
        if (strategy.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, strategy.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!strategyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Strategy> result = strategyService.partialUpdate(strategy);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, strategy.getId().toString())
        );
    }

    /**
     * {@code GET  /strategies} : get all the strategies.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of strategies in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Strategy>> getAllStrategies(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Strategies");
        Page<Strategy> page = strategyService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /strategies/:id} : get the "id" strategy.
     *
     * @param id the id of the strategy to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the strategy, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Strategy> getStrategy(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Strategy : {}", id);
        Optional<Strategy> strategy = strategyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(strategy);
    }

    /**
     * {@code DELETE  /strategies/:id} : delete the "id" strategy.
     *
     * @param id the id of the strategy to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStrategy(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Strategy : {}", id);
        strategyService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
