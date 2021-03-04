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

import me.amplitudo.elearning.domain.Assignment;
import me.amplitudo.elearning.domain.*; // for static metamodels
import me.amplitudo.elearning.repository.AssignmentRepository;
import me.amplitudo.elearning.service.dto.AssignmentCriteria;
import me.amplitudo.elearning.service.dto.AssignmentDTO;
import me.amplitudo.elearning.service.mapper.AssignmentMapper;

/**
 * Service for executing complex queries for {@link Assignment} entities in the database.
 * The main input is a {@link AssignmentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AssignmentDTO} or a {@link Page} of {@link AssignmentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AssignmentQueryService extends QueryService<Assignment> {

    private final Logger log = LoggerFactory.getLogger(AssignmentQueryService.class);

    private final AssignmentRepository assignmentRepository;

    private final AssignmentMapper assignmentMapper;

    public AssignmentQueryService(AssignmentRepository assignmentRepository, AssignmentMapper assignmentMapper) {
        this.assignmentRepository = assignmentRepository;
        this.assignmentMapper = assignmentMapper;
    }

    /**
     * Return a {@link List} of {@link AssignmentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AssignmentDTO> findByCriteria(AssignmentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Assignment> specification = createSpecification(criteria);
        return assignmentMapper.toDto(assignmentRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AssignmentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AssignmentDTO> findByCriteria(AssignmentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Assignment> specification = createSpecification(criteria);
        return assignmentRepository.findAll(specification, page)
            .map(assignmentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AssignmentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Assignment> specification = createSpecification(criteria);
        return assignmentRepository.count(specification);
    }

    /**
     * Function to convert {@link AssignmentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Assignment> createSpecification(AssignmentCriteria criteria) {
        Specification<Assignment> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Assignment_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Assignment_.title));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Assignment_.description));
            }
            if (criteria.getMaxPoints() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMaxPoints(), Assignment_.maxPoints));
            }
            if (criteria.getDeadLine() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDeadLine(), Assignment_.deadLine));
            }
            if (criteria.getFilePath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFilePath(), Assignment_.filePath));
            }
            if (criteria.getFileTypeIconPath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFileTypeIconPath(), Assignment_.fileTypeIconPath));
            }
            if (criteria.getDateCreated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateCreated(), Assignment_.dateCreated));
            }
            if (criteria.getDateUpdated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateUpdated(), Assignment_.dateUpdated));
            }
            if (criteria.getAssignmentProfilesId() != null) {
                specification = specification.and(buildSpecification(criteria.getAssignmentProfilesId(),
                    root -> root.join(Assignment_.assignmentProfiles, JoinType.LEFT).get(AssignmentProfile_.id)));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(Assignment_.user, JoinType.LEFT).get(Profile_.id)));
            }
            if (criteria.getCourseId() != null) {
                specification = specification.and(buildSpecification(criteria.getCourseId(),
                    root -> root.join(Assignment_.course, JoinType.LEFT).get(Course_.id)));
            }
        }
        return specification;
    }
}
