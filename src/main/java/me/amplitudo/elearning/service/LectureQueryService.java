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

import me.amplitudo.elearning.domain.Lecture;
import me.amplitudo.elearning.domain.*; // for static metamodels
import me.amplitudo.elearning.repository.LectureRepository;
import me.amplitudo.elearning.service.dto.LectureCriteria;
import me.amplitudo.elearning.service.dto.LectureDTO;
import me.amplitudo.elearning.service.mapper.LectureMapper;

/**
 * Service for executing complex queries for {@link Lecture} entities in the database.
 * The main input is a {@link LectureCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LectureDTO} or a {@link Page} of {@link LectureDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LectureQueryService extends QueryService<Lecture> {

    private final Logger log = LoggerFactory.getLogger(LectureQueryService.class);

    private final LectureRepository lectureRepository;

    private final LectureMapper lectureMapper;

    public LectureQueryService(LectureRepository lectureRepository, LectureMapper lectureMapper) {
        this.lectureRepository = lectureRepository;
        this.lectureMapper = lectureMapper;
    }

    /**
     * Return a {@link List} of {@link LectureDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LectureDTO> findByCriteria(LectureCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Lecture> specification = createSpecification(criteria);
        return lectureMapper.toDto(lectureRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LectureDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LectureDTO> findByCriteria(LectureCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Lecture> specification = createSpecification(criteria);
        return lectureRepository.findAll(specification, page)
            .map(lectureMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LectureCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Lecture> specification = createSpecification(criteria);
        return lectureRepository.count(specification);
    }

    /**
     * Function to convert {@link LectureCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Lecture> createSpecification(LectureCriteria criteria) {
        Specification<Lecture> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Lecture_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Lecture_.title));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Lecture_.description));
            }
            if (criteria.getMaterialFilePath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMaterialFilePath(), Lecture_.materialFilePath));
            }
            if (criteria.getFileTypeIconPath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFileTypeIconPath(), Lecture_.fileTypeIconPath));
            }
            if (criteria.getDateCreated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateCreated(), Lecture_.dateCreated));
            }
            if (criteria.getDateUpdated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateUpdated(), Lecture_.dateUpdated));
            }
            if (criteria.getCourseId() != null) {
                specification = specification.and(buildSpecification(criteria.getCourseId(),
                    root -> root.join(Lecture_.course, JoinType.LEFT).get(Course_.id)));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(Lecture_.user, JoinType.LEFT).get(Profile_.id)));
            }
        }
        return specification;
    }
}
