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

import me.amplitudo.elearning.domain.Building;
import me.amplitudo.elearning.domain.*; // for static metamodels
import me.amplitudo.elearning.repository.BuildingRepository;
import me.amplitudo.elearning.service.dto.BuildingCriteria;
import me.amplitudo.elearning.service.dto.BuildingDTO;
import me.amplitudo.elearning.service.mapper.BuildingMapper;

/**
 * Service for executing complex queries for {@link Building} entities in the database.
 * The main input is a {@link BuildingCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BuildingDTO} or a {@link Page} of {@link BuildingDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BuildingQueryService extends QueryService<Building> {

    private final Logger log = LoggerFactory.getLogger(BuildingQueryService.class);

    private final BuildingRepository buildingRepository;

    private final BuildingMapper buildingMapper;

    public BuildingQueryService(BuildingRepository buildingRepository, BuildingMapper buildingMapper) {
        this.buildingRepository = buildingRepository;
        this.buildingMapper = buildingMapper;
    }

    /**
     * Return a {@link List} of {@link BuildingDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BuildingDTO> findByCriteria(BuildingCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Building> specification = createSpecification(criteria);
        return buildingMapper.toDto(buildingRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link BuildingDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BuildingDTO> findByCriteria(BuildingCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Building> specification = createSpecification(criteria);
        return buildingRepository.findAll(specification, page)
            .map(buildingMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BuildingCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Building> specification = createSpecification(criteria);
        return buildingRepository.count(specification);
    }

    /**
     * Function to convert {@link BuildingCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Building> createSpecification(BuildingCriteria criteria) {
        Specification<Building> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Building_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Building_.name));
            }
            if (criteria.getLocation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLocation(), Building_.location));
            }
            if (criteria.getLatitude() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLatitude(), Building_.latitude));
            }
            if (criteria.getLongitude() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLongitude(), Building_.longitude));
            }
            if (criteria.getDateCreated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateCreated(), Building_.dateCreated));
            }
            if (criteria.getDateUpdated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateUpdated(), Building_.dateUpdated));
            }
            if (criteria.getFacultiesId() != null) {
                specification = specification.and(buildSpecification(criteria.getFacultiesId(),
                    root -> root.join(Building_.faculties, JoinType.LEFT).get(Faculty_.id)));
            }
        }
        return specification;
    }
}
