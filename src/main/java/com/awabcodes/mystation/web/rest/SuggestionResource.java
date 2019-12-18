package com.awabcodes.mystation.web.rest;

import com.awabcodes.mystation.domain.Suggestion;
import com.awabcodes.mystation.service.SuggestionService;
import com.awabcodes.mystation.web.rest.errors.BadRequestAlertException;
import com.awabcodes.mystation.service.dto.SuggestionCriteria;
import com.awabcodes.mystation.service.SuggestionQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.awabcodes.mystation.domain.Suggestion}.
 */
@RestController
@RequestMapping("/api")
public class SuggestionResource {

    private final Logger log = LoggerFactory.getLogger(SuggestionResource.class);

    private static final String ENTITY_NAME = "suggestion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SuggestionService suggestionService;

    private final SuggestionQueryService suggestionQueryService;

    public SuggestionResource(SuggestionService suggestionService, SuggestionQueryService suggestionQueryService) {
        this.suggestionService = suggestionService;
        this.suggestionQueryService = suggestionQueryService;
    }

    /**
     * {@code POST  /suggestions} : Create a new suggestion.
     *
     * @param suggestion the suggestion to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new suggestion, or with status {@code 400 (Bad Request)} if the suggestion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/suggestions")
    public ResponseEntity<Suggestion> createSuggestion(@Valid @RequestBody Suggestion suggestion) throws URISyntaxException {
        log.debug("REST request to save Suggestion : {}", suggestion);
        if (suggestion.getId() != null) {
            throw new BadRequestAlertException("A new suggestion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Suggestion result = suggestionService.save(suggestion);
        return ResponseEntity.created(new URI("/api/suggestions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /suggestions} : Updates an existing suggestion.
     *
     * @param suggestion the suggestion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated suggestion,
     * or with status {@code 400 (Bad Request)} if the suggestion is not valid,
     * or with status {@code 500 (Internal Server Error)} if the suggestion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/suggestions")
    public ResponseEntity<Suggestion> updateSuggestion(@Valid @RequestBody Suggestion suggestion) throws URISyntaxException {
        log.debug("REST request to update Suggestion : {}", suggestion);
        if (suggestion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Suggestion result = suggestionService.save(suggestion);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, suggestion.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /suggestions} : get all the suggestions.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of suggestions in body.
     */
    @GetMapping("/suggestions")
    public ResponseEntity<List<Suggestion>> getAllSuggestions(SuggestionCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Suggestions by criteria: {}", criteria);
        Page<Suggestion> page = suggestionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /suggestions/count} : count all the suggestions.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/suggestions/count")
    public ResponseEntity<Long> countSuggestions(SuggestionCriteria criteria) {
        log.debug("REST request to count Suggestions by criteria: {}", criteria);
        return ResponseEntity.ok().body(suggestionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /suggestions/:id} : get the "id" suggestion.
     *
     * @param id the id of the suggestion to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the suggestion, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/suggestions/{id}")
    public ResponseEntity<Suggestion> getSuggestion(@PathVariable Long id) {
        log.debug("REST request to get Suggestion : {}", id);
        Optional<Suggestion> suggestion = suggestionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(suggestion);
    }

    /**
     * {@code DELETE  /suggestions/:id} : delete the "id" suggestion.
     *
     * @param id the id of the suggestion to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/suggestions/{id}")
    public ResponseEntity<Void> deleteSuggestion(@PathVariable Long id) {
        log.debug("REST request to delete Suggestion : {}", id);
        suggestionService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
