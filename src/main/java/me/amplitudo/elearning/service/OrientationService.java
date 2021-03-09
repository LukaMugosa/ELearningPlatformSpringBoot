package me.amplitudo.elearning.service;

import me.amplitudo.elearning.domain.Faculty;
import me.amplitudo.elearning.domain.Orientation;
import me.amplitudo.elearning.repository.CourseRepository;
import me.amplitudo.elearning.repository.FacultyRepository;
import me.amplitudo.elearning.repository.OrientationRepository;
import me.amplitudo.elearning.service.dto.OrientationDTO;
import me.amplitudo.elearning.service.mapper.OrientationMapper;
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
import java.util.Optional;

/**
 * Service Implementation for managing {@link Orientation}.
 */
@Service
@Transactional
public class OrientationService {

    private final Logger log = LoggerFactory.getLogger(OrientationService.class);

    private final OrientationRepository orientationRepository;

    private final OrientationMapper orientationMapper;

    private final CourseRepository courseRepository;

    private final FacultyRepository facultyRepository;

    public OrientationService(OrientationRepository orientationRepository,
                              OrientationMapper orientationMapper,
                              FacultyRepository facultyRepository,
                              CourseRepository courseRepository) {
        this.orientationRepository = orientationRepository;
        this.orientationMapper = orientationMapper;
        this.courseRepository = courseRepository;
        this.facultyRepository = facultyRepository;
    }

    /**
     * Save a orientation.
     *
     * @param orientationDTO the entity to save.
     * @return the persisted entity.
     */
    public OrientationDTO save(OrientationDTO orientationDTO) {
        log.debug("Request to save Orientation : {}", orientationDTO);

        if(
            orientationDTO.getId() == null && orientationRepository.existsByName(orientationDTO.getName()) ||
            orientationDTO.getId() != null && orientationRepository.existsByNameAndIdNot(orientationDTO.getName(), orientationDTO.getId())
        ){
            throw new BadActionException(
                ExceptionErrors.ORIENTATION_EXISTS.getErrorCode(),
                ExceptionErrors.ORIENTATION_EXISTS.getErrorDescription()
            );
        }

        Faculty faculty = facultyRepository.findById(orientationDTO.getFacultyId())
            .orElseThrow(() -> new EntityNotFoundException(
                "Faculty with id: " + orientationDTO.getFacultyId() + " does not exist."
            ));

        Orientation orientation = orientationMapper.toEntity(orientationDTO);

        if(orientation.getId() == null){
            orientation.setDateCreated(Instant.now());
        }else{
            orientation.setDateUpdated(Instant.now());
        }

        orientation = orientationRepository.save(orientation);
        return orientationMapper.toDto(orientation);
    }

    /**
     * Get all the orientations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<OrientationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Orientations");
        return orientationRepository.findAll(pageable)
            .map(orientationMapper::toDto);
    }


    /**
     * Get one orientation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrientationDTO> findOne(Long id) {
        log.debug("Request to get Orientation : {}", id);
        return orientationRepository.findById(id)
            .map(orientationMapper::toDto);
    }

    /**
     * Delete the orientation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Orientation : {}", id);

        if(!orientationRepository.existsById(id)){
            throw new EntityNotFoundException(
                "Orientation with id: " + id + "does not exist."
            );
        }

        orientationRepository.deleteById(id);
    }

    public Page<OrientationDTO> findAllByCourse(Pageable pageable, Long courseId) {

        if(!courseRepository.existsById(courseId)){
            throw new EntityNotFoundException(
                "Course with id " + courseId + " does not exist."
            );
        }

        return orientationRepository.findAllByCoursesId(pageable, courseId)
            .map(orientationMapper::toDto);
    }
}
