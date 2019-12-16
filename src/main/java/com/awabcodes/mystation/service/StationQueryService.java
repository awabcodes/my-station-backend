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

import com.awabcodes.mystation.domain.Station;
import com.awabcodes.mystation.domain.*; // for static metamodels
import com.awabcodes.mystation.repository.StationRepository;
import com.awabcodes.mystation.service.dto.StationCriteria;

/**
 * Service for executing complex queries for {@link Station} entities in the database.
 * The main input is a {@link StationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Station} or a {@link Page} of {@link Station} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StationQueryService extends QueryService<Station> {

    private final Logger log = LoggerFactory.getLogger(StationQueryService.class);

    private final StationRepository stationRepository;

    public StationQueryService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    /**
     * Return a {@link List} of {@link Station} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Station> findByCriteria(StationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Station> specification = createSpecification(criteria);
        return stationRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Station} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Station> findByCriteria(StationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Station> specification = createSpecification(criteria);
        return stationRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Station> specification = createSpecification(criteria);
        return stationRepository.count(specification);
    }

    /**
     * Function to convert {@link StationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Station> createSpecification(StationCriteria criteria) {
        Specification<Station> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Station_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Station_.name));
            }
            if (criteria.getGasLevel() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGasLevel(), Station_.gasLevel));
            }
            if (criteria.getBenzeneLevel() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBenzeneLevel(), Station_.benzeneLevel));
            }
            if (criteria.getLastTankFill() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastTankFill(), Station_.lastTankFill));
            }
            if (criteria.getCity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCity(), Station_.city));
            }
            if (criteria.getLocation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLocation(), Station_.location));
            }
            if (criteria.getMapUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMapUrl(), Station_.mapUrl));
            }
        }
        return specification;
    }
}
