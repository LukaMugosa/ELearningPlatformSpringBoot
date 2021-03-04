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

import me.amplitudo.elearning.domain.AssignmentProfile;
import me.amplitudo.elearning.domain.*; // for static metamodels
import me.amplitudo.elearning.repository.AssignmentProfileRepository;
import me.amplitudo.elearning.service.dto.AssignmentProfileCriteria;
import me.amplitudo.elearning.service.dto.AssignmentProfileDTO;
import me.amplitudo.elearning.service.mapper.AssignmentProfileMapper;

/**
 * Service for executing complex queries for {@link AssignmentProfile} entities in the database.
 * The main input is a {@link AssignmentProfileCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AssignmentProfileDTO} or a {@link Page} of {@link AssignmentProfileDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AssignmentProfileQueryService extends QueryService<AssignmentProfile> {

    private final Logger log = LoggerFactory.getLogger(AssignmentProfileQueryService.class);

    private final AssignmentProfileRepository assignmentProfileRepository;

    private final AssignmentProfileMapper assignmentProfileMapper;

    public AssignmentProfileQueryService(AssignmentProfileRepository assignmentProfileRepository, AssignmentProfileMapper assignmentProfileMapper) {
        this.assignmentProfileRepository = assignmentProfileRepository;
        this.assignmentProfileMapper = assignmentProfileMapper;
    }

    /**
     * Return a {@link List} of {@link AssignmentProfileDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AssignmentProfileDTO> findByCriteria(AssignmentProfileCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AssignmentProfile> specification = createSpecification(criteria);
        return assignmentProfileMapper.toDto(assignmentProfileRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AssignmentProfileDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AssignmentProfileDTO> findByCriteria(AssignmentProfileCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AssignmentProfile> specification = createSpecification(criteria);
        return assignmentProfileRepository.findAll(specification, page)
            .map(assignmentProfileMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AssignmentProfileCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AssignmentProfile> specification = createSpecification(criteria);
        return assignmentProfileRepository.count(specification);
    }

    /**
     * Function to convert {@link AssignmentProfileCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AssignmentProfile> createSpecification(AssignmentProfileCriteria criteria) {
        Specification<AssignmentProfile> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AssignmentProfile_.id));
            }
            if (criteria.getPoints() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPoints(), AssignmentProfile_.points));
            }
            if (criteria.getFilePath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFilePath(), AssignmentProfile_.filePath));
            }
            if (criteria.getFileTypeIconPath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFileTypeIconPath(), AssignmentProfile_.fileTypeIconPath));
            }
            if (criteria.getDateCreated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateCreated(), AssignmentProfile_.dateCreated));
            }
            if (criteria.getDateUpdated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateUpdated(), AssignmentProfile_.dateUpdated));
            }
            if (criteria.getAssignmentId() != null) {
                specification = specification.and(buildSpecification(criteria.getAssignmentId(),
                    root -> root.join(AssignmentProfile_.assignment, JoinType.LEFT).get(Assignment_.id)));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(AssignmentProfile_.user, JoinType.LEFT).get(Profile_.id)));
            }
        }
        return specification;
    }
}
