package me.amplitudo.elearning.service;

import me.amplitudo.elearning.domain.Building;
import me.amplitudo.elearning.domain.Faculty;
import me.amplitudo.elearning.repository.BuildingRepository;
import me.amplitudo.elearning.repository.FacultyRepository;
import me.amplitudo.elearning.service.dto.FacultyDTO;
import me.amplitudo.elearning.service.mapper.FacultyMapper;
import me.amplitudo.elearning.web.rest.errors.BadActionException;
import me.amplitudo.elearning.web.rest.errors.ExceptionErrors;
import org.checkerframework.checker.nullness.Opt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Faculty}.
 */
@Service
@Transactional
public class FacultyService {

    private final Logger log = LoggerFactory.getLogger(FacultyService.class);

    private final FacultyRepository facultyRepository;

    private final BuildingRepository buildingRepository;

    private final FacultyMapper facultyMapper;

    public FacultyService(FacultyRepository facultyRepository,
                          FacultyMapper facultyMapper,
                          BuildingRepository buildingRepository) {
        this.facultyRepository = facultyRepository;
        this.facultyMapper = facultyMapper;
        this.buildingRepository = buildingRepository;
    }

    /**
     * Save a faculty.
     *
     * @param facultyDTO the entity to save.
     * @return the persisted entity.
     */
    public FacultyDTO save(FacultyDTO facultyDTO) {
        log.debug("Request to save Faculty : {}", facultyDTO);

        if(
            facultyDTO.getId() == null && facultyRepository.countAllByName(facultyDTO.getName()) > 0 ||
            facultyDTO.getId() != null && facultyRepository.countAllByNameAndIdNot(facultyDTO.getName(), facultyDTO.getId()) > 0
        )
        {
            throw new BadActionException(
                ExceptionErrors.FACULTY_EXISTS.getErrorCode(),
                ExceptionErrors.FACULTY_EXISTS.getErrorDescription()
            );
        }

        Optional<Building> building = Optional.ofNullable(
            buildingRepository.findById(facultyDTO.getBuildingId())
                .orElseThrow(() -> new BadActionException(
                    ExceptionErrors.FACULTY_EXISTS.getErrorCode(),
                    ExceptionErrors.FACULTY_EXISTS.getErrorDescription()
                ))
        );

        Faculty faculty = facultyMapper.toEntity(facultyDTO);

        if(faculty.getId() == null){
            faculty.setDateCreated(Instant.now());
        }else{
            faculty.setDateUpdated(Instant.now());
        }

        faculty = facultyRepository.save(faculty);

        return facultyMapper.toDto(faculty);
    }

    /**
     * Get all the faculties.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FacultyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Faculties");
        return facultyRepository.findAll(pageable)
            .map(facultyMapper::toDto);
    }


    /**
     * Get all the faculties with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<FacultyDTO> findAllWithEagerRelationships(Pageable pageable) {
        return facultyRepository.findAllWithEagerRelationships(pageable).map(facultyMapper::toDto);
    }

    /**
     * Get one faculty by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FacultyDTO> findOne(Long id) {
        log.debug("Request to get Faculty : {}", id);
        return facultyRepository.findOneWithEagerRelationships(id)
            .map(facultyMapper::toDto);
    }

    /**
     * Delete the faculty by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Faculty : {}", id);
        facultyRepository.deleteById(id);
    }
}
