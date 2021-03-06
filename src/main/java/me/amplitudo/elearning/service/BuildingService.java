package me.amplitudo.elearning.service;

import me.amplitudo.elearning.domain.Building;
import me.amplitudo.elearning.domain.Faculty;
import me.amplitudo.elearning.repository.BuildingRepository;
import me.amplitudo.elearning.repository.FacultyRepository;
import me.amplitudo.elearning.service.dto.BuildingDTO;
import me.amplitudo.elearning.service.mapper.BuildingMapper;
import me.amplitudo.elearning.web.rest.errors.BadActionException;
import me.amplitudo.elearning.web.rest.errors.EntityNotFoundException;
import me.amplitudo.elearning.web.rest.errors.ExceptionErrors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Building}.
 */
@Service
@Transactional
public class BuildingService {

    private final Logger log = LoggerFactory.getLogger(BuildingService.class);

    private final BuildingRepository buildingRepository;

    private final BuildingMapper buildingMapper;

    private final FacultyRepository facultyRepository;

    public final String ENTITY = "Building";

    public BuildingService(BuildingRepository buildingRepository,
                           BuildingMapper buildingMapper,
                           FacultyRepository facultyRepository) {
        this.buildingRepository = buildingRepository;
        this.buildingMapper = buildingMapper;
        this.facultyRepository = facultyRepository;
    }

    /**
     * Save a building.
     *
     * @param buildingDTO the entity to save.
     * @return the persisted entity.
     */
    public BuildingDTO save(BuildingDTO buildingDTO) {

        log.debug("Request to save Building : {}", buildingDTO);

        if(
            buildingDTO.getId() == null && buildingRepository.countAllByName(buildingDTO.getName()) > 0 ||
            buildingDTO.getId() != null && buildingRepository.countAllByNameAndIdNot(buildingDTO.getName(), buildingDTO.getId()) > 0
        )
        {
            throw new BadActionException(
                ExceptionErrors.BUILDING_EXISTS.getErrorCode(),
                ExceptionErrors.BUILDING_EXISTS.getErrorDescription()
            );
        }
        Building building = buildingMapper.toEntity(buildingDTO);

        if (building.getId() == null){
            building.setDateCreated(Instant.now());
        }else{
            building.setDateUpdated(Instant.now());
        }

        building = buildingRepository.save(building);

        return buildingMapper.toDto(building);
    }

    /**
     * Get all the buildings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BuildingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Buildings");
        return buildingRepository.findAll(pageable)
            .map(buildingMapper::toDto);
    }


    /**
     * Get one building by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BuildingDTO> findOne(Long id) {
        log.debug("Request to get Building : {}", id);
        return buildingRepository.findById(id)
            .map(buildingMapper::toDto);
    }

    /**
     * Delete the building by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Building : {}", id);

        Building building = buildingRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(this.ENTITY + " with id "+ id + " was not found."));

        List<Faculty> facultyList = facultyRepository.findAllByBuildingId(id);
        if(!facultyList.isEmpty()){
            facultyList.forEach(faculty -> {
                facultyRepository.deleteById(faculty.getId());
            });
        }

        buildingRepository.deleteById(id);
    }
}
