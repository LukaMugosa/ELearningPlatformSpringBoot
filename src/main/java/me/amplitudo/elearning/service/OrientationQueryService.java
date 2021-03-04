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

import me.amplitudo.elearning.domain.Orientation;
import me.amplitudo.elearning.domain.*; // for static metamodels
import me.amplitudo.elearning.repository.OrientationRepository;
import me.amplitudo.elearning.service.dto.OrientationCriteria;
import me.amplitudo.elearning.service.dto.OrientationDTO;
import me.amplitudo.elearning.service.mapper.OrientationMapper;

/**
 * Service for executing complex queries for {@link Orientation} entities in the database.
 * The main input is a {@link OrientationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OrientationDTO} or a {@link Page} of {@link OrientationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrientationQueryService extends QueryService<Orientation> {

    private final Logger log = LoggerFactory.getLogger(OrientationQueryService.class);

    private final OrientationRepository orientationRepository;

    private final OrientationMapper orientationMapper;

    public OrientationQueryService(OrientationRepository orientationRepository, OrientationMapper orientationMapper) {
        this.orientationRepository = orientationRepository;
        this.orientationMapper = orientationMapper;
    }

    /**
     * Return a {@link List} of {@link OrientationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OrientationDTO> findByCriteria(OrientationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Orientation> specification = createSpecification(criteria);
        return orientationMapper.toDto(orientationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link OrientationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrientationDTO> findByCriteria(OrientationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Orientation> specification = createSpecification(criteria);
        return orientationRepository.findAll(specification, page)
            .map(orientationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrientationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Orientation> specification = createSpecification(criteria);
        return orientationRepository.count(specification);
    }

    /**
     * Function to convert {@link OrientationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Orientation> createSpecification(OrientationCriteria criteria) {
        Specification<Orientation> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Orientation_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Orientation_.name));
            }
            if (criteria.getDateCreated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateCreated(), Orientation_.dateCreated));
            }
            if (criteria.getDateUpdated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateUpdated(), Orientation_.dateUpdated));
            }
            if (criteria.getUsersId() != null) {
                specification = specification.and(buildSpecification(criteria.getUsersId(),
                    root -> root.join(Orientation_.users, JoinType.LEFT).get(Profile_.id)));
            }
            if (criteria.getFacultyId() != null) {
                specification = specification.and(buildSpecification(criteria.getFacultyId(),
                    root -> root.join(Orientation_.faculty, JoinType.LEFT).get(Faculty_.id)));
            }
            if (criteria.getFacultiesId() != null) {
                specification = specification.and(buildSpecification(criteria.getFacultiesId(),
                    root -> root.join(Orientation_.faculties, JoinType.LEFT).get(Faculty_.id)));
            }
            if (criteria.getCoursesId() != null) {
                specification = specification.and(buildSpecification(criteria.getCoursesId(),
                    root -> root.join(Orientation_.courses, JoinType.LEFT).get(Course_.id)));
            }
        }
        return specification;
    }
}
