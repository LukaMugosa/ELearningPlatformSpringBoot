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

import me.amplitudo.elearning.domain.Course;
import me.amplitudo.elearning.domain.*; // for static metamodels
import me.amplitudo.elearning.repository.CourseRepository;
import me.amplitudo.elearning.service.dto.CourseCriteria;
import me.amplitudo.elearning.service.dto.CourseDTO;
import me.amplitudo.elearning.service.mapper.CourseMapper;

/**
 * Service for executing complex queries for {@link Course} entities in the database.
 * The main input is a {@link CourseCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CourseDTO} or a {@link Page} of {@link CourseDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CourseQueryService extends QueryService<Course> {

    private final Logger log = LoggerFactory.getLogger(CourseQueryService.class);

    private final CourseRepository courseRepository;

    private final CourseMapper courseMapper;

    public CourseQueryService(CourseRepository courseRepository, CourseMapper courseMapper) {
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
    }

    /**
     * Return a {@link List} of {@link CourseDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CourseDTO> findByCriteria(CourseCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Course> specification = createSpecification(criteria);
        return courseMapper.toDto(courseRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CourseDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CourseDTO> findByCriteria(CourseCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Course> specification = createSpecification(criteria);
        return courseRepository.findAll(specification, page)
            .map(courseMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CourseCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Course> specification = createSpecification(criteria);
        return courseRepository.count(specification);
    }

    /**
     * Function to convert {@link CourseCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Course> createSpecification(CourseCriteria criteria) {
        Specification<Course> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Course_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Course_.name));
            }
            if (criteria.getDateCreated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateCreated(), Course_.dateCreated));
            }
            if (criteria.getDateUpdated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateUpdated(), Course_.dateUpdated));
            }
            if (criteria.getNotificationsId() != null) {
                specification = specification.and(buildSpecification(criteria.getNotificationsId(),
                    root -> root.join(Course_.notifications, JoinType.LEFT).get(Notification_.id)));
            }
            if (criteria.getAssignmentsId() != null) {
                specification = specification.and(buildSpecification(criteria.getAssignmentsId(),
                    root -> root.join(Course_.assignments, JoinType.LEFT).get(Assignment_.id)));
            }
            if (criteria.getProfessorId() != null) {
                specification = specification.and(buildSpecification(criteria.getProfessorId(),
                    root -> root.join(Course_.professor, JoinType.LEFT).get(Profile_.id)));
            }
            if (criteria.getAssistantId() != null) {
                specification = specification.and(buildSpecification(criteria.getAssistantId(),
                    root -> root.join(Course_.assistant, JoinType.LEFT).get(Profile_.id)));
            }
            if (criteria.getYearId() != null) {
                specification = specification.and(buildSpecification(criteria.getYearId(),
                    root -> root.join(Course_.year, JoinType.LEFT).get(Year_.id)));
            }
            if (criteria.getOrientationsId() != null) {
                specification = specification.and(buildSpecification(criteria.getOrientationsId(),
                    root -> root.join(Course_.orientations, JoinType.LEFT).get(Orientation_.id)));
            }
            if (criteria.getUsersId() != null) {
                specification = specification.and(buildSpecification(criteria.getUsersId(),
                    root -> root.join(Course_.users, JoinType.LEFT).get(Profile_.id)));
            }
            if (criteria.getLecturesId() != null) {
                specification = specification.and(buildSpecification(criteria.getLecturesId(),
                    root -> root.join(Course_.lectures, JoinType.LEFT).get(Lecture_.id)));
            }
        }
        return specification;
    }
}
