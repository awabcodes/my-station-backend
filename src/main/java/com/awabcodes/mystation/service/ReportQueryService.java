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

import com.awabcodes.mystation.domain.Report;
import com.awabcodes.mystation.domain.*; // for static metamodels
import com.awabcodes.mystation.repository.ReportRepository;
import com.awabcodes.mystation.service.dto.ReportCriteria;

/**
 * Service for executing complex queries for {@link Report} entities in the database.
 * The main input is a {@link ReportCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Report} or a {@link Page} of {@link Report} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReportQueryService extends QueryService<Report> {

    private final Logger log = LoggerFactory.getLogger(ReportQueryService.class);

    private final ReportRepository reportRepository;

    public ReportQueryService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    /**
     * Return a {@link List} of {@link Report} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Report> findByCriteria(ReportCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Report> specification = createSpecification(criteria);
        return reportRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Report} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Report> findByCriteria(ReportCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Report> specification = createSpecification(criteria);
        return reportRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ReportCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Report> specification = createSpecification(criteria);
        return reportRepository.count(specification);
    }

    /**
     * Function to convert {@link ReportCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Report> createSpecification(ReportCriteria criteria) {
        Specification<Report> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Report_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Report_.title));
            }
            if (criteria.getWeeklyConsumption() != null) {
                specification = specification.and(buildStringSpecification(criteria.getWeeklyConsumption(), Report_.weeklyConsumption));
            }
            if (criteria.getMonthlyConsumption() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMonthlyConsumption(), Report_.monthlyConsumption));
            }
            if (criteria.getStationId() != null) {
                specification = specification.and(buildSpecification(criteria.getStationId(),
                    root -> root.join(Report_.station, JoinType.LEFT).get(Station_.id)));
            }
        }
        return specification;
    }
}
