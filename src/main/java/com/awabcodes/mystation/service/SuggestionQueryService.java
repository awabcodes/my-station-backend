package com.awabcodes.mystation.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.awabcodes.mystation.domain.Suggestion;
import com.awabcodes.mystation.domain.*; // for static metamodels
import com.awabcodes.mystation.repository.SuggestionRepository;
import com.awabcodes.mystation.service.dto.SuggestionCriteria;

/**
 * Service for executing complex queries for {@link Suggestion} entities in the database.
 * The main input is a {@link SuggestionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Suggestion} or a {@link Page} of {@link Suggestion} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SuggestionQueryService extends QueryService<Suggestion> {

    private final Logger log = LoggerFactory.getLogger(SuggestionQueryService.class);

    private final SuggestionRepository suggestionRepository;

    public SuggestionQueryService(SuggestionRepository suggestionRepository) {
        this.suggestionRepository = suggestionRepository;
    }

    /**
     * Return a {@link List} of {@link Suggestion} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Suggestion> findByCriteria(SuggestionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Suggestion> specification = createSpecification(criteria);
        return suggestionRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Suggestion} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Suggestion> findByCriteria(SuggestionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Suggestion> specification = createSpecification(criteria);
        return suggestionRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SuggestionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Suggestion> specification = createSpecification(criteria);
        return suggestionRepository.count(specification);
    }

    /**
     * Function to convert {@link SuggestionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Suggestion> createSpecification(SuggestionCriteria criteria) {
        Specification<Suggestion> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Suggestion_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Suggestion_.title));
            }
            if (criteria.getMessage() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMessage(), Suggestion_.message));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), Suggestion_.date));
            }
        }
        return specification;
    }
}
