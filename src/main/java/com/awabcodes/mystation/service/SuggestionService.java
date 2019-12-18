package com.awabcodes.mystation.service;

import com.awabcodes.mystation.domain.Suggestion;
import com.awabcodes.mystation.repository.SuggestionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Suggestion}.
 */
@Service
@Transactional
public class SuggestionService {

    private final Logger log = LoggerFactory.getLogger(SuggestionService.class);

    private final SuggestionRepository suggestionRepository;

    public SuggestionService(SuggestionRepository suggestionRepository) {
        this.suggestionRepository = suggestionRepository;
    }

    /**
     * Save a suggestion.
     *
     * @param suggestion the entity to save.
     * @return the persisted entity.
     */
    public Suggestion save(Suggestion suggestion) {
        log.debug("Request to save Suggestion : {}", suggestion);
        return suggestionRepository.save(suggestion);
    }

    /**
     * Get all the suggestions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Suggestion> findAll(Pageable pageable) {
        log.debug("Request to get all Suggestions");
        return suggestionRepository.findAll(pageable);
    }


    /**
     * Get one suggestion by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Suggestion> findOne(Long id) {
        log.debug("Request to get Suggestion : {}", id);
        return suggestionRepository.findById(id);
    }

    /**
     * Delete the suggestion by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Suggestion : {}", id);
        suggestionRepository.deleteById(id);
    }
}
