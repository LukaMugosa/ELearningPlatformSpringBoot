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

import me.amplitudo.elearning.domain.Profile;
import me.amplitudo.elearning.domain.*; // for static metamodels
import me.amplitudo.elearning.repository.ProfileRepository;
import me.amplitudo.elearning.service.dto.ProfileCriteria;
import me.amplitudo.elearning.service.dto.ProfileDTO;
import me.amplitudo.elearning.service.mapper.ProfileMapper;

/**
 * Service for executing complex queries for {@link Profile} entities in the database.
 * The main input is a {@link ProfileCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProfileDTO} or a {@link Page} of {@link ProfileDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProfileQueryService extends QueryService<Profile> {

    private final Logger log = LoggerFactory.getLogger(ProfileQueryService.class);

    private final ProfileRepository profileRepository;

    private final ProfileMapper profileMapper;

    public ProfileQueryService(ProfileRepository profileRepository, ProfileMapper profileMapper) {
        this.profileRepository = profileRepository;
        this.profileMapper = profileMapper;
    }

    /**
     * Return a {@link List} of {@link ProfileDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProfileDTO> findByCriteria(ProfileCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Profile> specification = createSpecification(criteria);
        return profileMapper.toDto(profileRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProfileDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProfileDTO> findByCriteria(ProfileCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Profile> specification = createSpecification(criteria);
        return profileRepository.findAll(specification, page)
            .map(profileMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProfileCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Profile> specification = createSpecification(criteria);
        return profileRepository.count(specification);
    }

    /**
     * Function to convert {@link ProfileCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Profile> createSpecification(ProfileCriteria criteria) {
        Specification<Profile> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Profile_.id));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), Profile_.phoneNumber));
            }
            if (criteria.getDateOfBirth() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateOfBirth(), Profile_.dateOfBirth));
            }
            if (criteria.getIndex() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIndex(), Profile_.index));
            }
            if (criteria.getYearOfEnrollment() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getYearOfEnrollment(), Profile_.yearOfEnrollment));
            }
            if (criteria.getVerificationNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVerificationNumber(), Profile_.verificationNumber));
            }
            if (criteria.getIsApproved() != null) {
                specification = specification.and(buildSpecification(criteria.getIsApproved(), Profile_.isApproved));
            }
            if (criteria.getDateCreated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateCreated(), Profile_.dateCreated));
            }
            if (criteria.getDateUpdated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateUpdated(), Profile_.dateUpdated));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(Profile_.user, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getAssignmentProfilesId() != null) {
                specification = specification.and(buildSpecification(criteria.getAssignmentProfilesId(),
                    root -> root.join(Profile_.assignmentProfiles, JoinType.LEFT).get(AssignmentProfile_.id)));
            }
            if (criteria.getLecturesId() != null) {
                specification = specification.and(buildSpecification(criteria.getLecturesId(),
                    root -> root.join(Profile_.lectures, JoinType.LEFT).get(Lecture_.id)));
            }
            if (criteria.getNotificationsId() != null) {
                specification = specification.and(buildSpecification(criteria.getNotificationsId(),
                    root -> root.join(Profile_.notifications, JoinType.LEFT).get(Notification_.id)));
            }
            if (criteria.getAssignmentsId() != null) {
                specification = specification.and(buildSpecification(criteria.getAssignmentsId(),
                    root -> root.join(Profile_.assignments, JoinType.LEFT).get(Assignment_.id)));
            }
            if (criteria.getYearId() != null) {
                specification = specification.and(buildSpecification(criteria.getYearId(),
                    root -> root.join(Profile_.year, JoinType.LEFT).get(Year_.id)));
            }
            if (criteria.getFacultyId() != null) {
                specification = specification.and(buildSpecification(criteria.getFacultyId(),
                    root -> root.join(Profile_.faculty, JoinType.LEFT).get(Orientation_.id)));
            }
            if (criteria.getFacultiesId() != null) {
                specification = specification.and(buildSpecification(criteria.getFacultiesId(),
                    root -> root.join(Profile_.faculties, JoinType.LEFT).get(Faculty_.id)));
            }
            if (criteria.getCoursesId() != null) {
                specification = specification.and(buildSpecification(criteria.getCoursesId(),
                    root -> root.join(Profile_.courses, JoinType.LEFT).get(Course_.id)));
            }
        }
        return specification;
    }
}
