package me.amplitudo.elearning.service;

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

import me.amplitudo.elearning.domain.Year;
import me.amplitudo.elearning.domain.*; // for static metamodels
import me.amplitudo.elearning.repository.YearRepository;
import me.amplitudo.elearning.service.dto.YearCriteria;
import me.amplitudo.elearning.service.dto.YearDTO;
import me.amplitudo.elearning.service.mapper.YearMapper;

/**
 * Service for executing complex queries for {@link Year} entities in the database.
 * The main input is a {@link YearCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link YearDTO} or a {@link Page} of {@link YearDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class YearQueryService extends QueryService<Year> {

    private final Logger log = LoggerFactory.getLogger(YearQueryService.class);

    private final YearRepository yearRepository;

    private final YearMapper yearMapper;

    public YearQueryService(YearRepository yearRepository, YearMapper yearMapper) {
        this.yearRepository = yearRepository;
        this.yearMapper = yearMapper;
    }

    /**
     * Return a {@link List} of {@link YearDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<YearDTO> findByCriteria(YearCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Year> specification = createSpecification(criteria);
        return yearMapper.toDto(yearRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link YearDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<YearDTO> findByCriteria(YearCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Year> specification = createSpecification(criteria);
        return yearRepository.findAll(specification, page)
            .map(yearMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(YearCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Year> specification = createSpecification(criteria);
        return yearRepository.count(specification);
    }

    /**
     * Function to convert {@link YearCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Year> createSpecification(YearCriteria criteria) {
        Specification<Year> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Year_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Year_.name));
            }
            if (criteria.getDateCreated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateCreated(), Year_.dateCreated));
            }
            if (criteria.getDateUpdated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateUpdated(), Year_.dateUpdated));
            }
            if (criteria.getUsersId() != null) {
                specification = specification.and(buildSpecification(criteria.getUsersId(),
                    root -> root.join(Year_.users, JoinType.LEFT).get(Profile_.id)));
            }
        }
        return specification;
    }
}
