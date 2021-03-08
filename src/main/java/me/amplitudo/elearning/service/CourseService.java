package me.amplitudo.elearning.service;

import me.amplitudo.elearning.domain.Course;
import me.amplitudo.elearning.domain.Orientation;
import me.amplitudo.elearning.domain.User;
import me.amplitudo.elearning.domain.Year;
import me.amplitudo.elearning.repository.CourseRepository;
import me.amplitudo.elearning.repository.OrientationRepository;
import me.amplitudo.elearning.repository.UserRepository;
import me.amplitudo.elearning.repository.YearRepository;
import me.amplitudo.elearning.service.dto.CourseDTO;
import me.amplitudo.elearning.service.dto.CourseOrientationDTO;
import me.amplitudo.elearning.service.mapper.CourseMapper;
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
 * Service Implementation for managing {@link Course}.
 */
@Service
@Transactional
public class CourseService {

    private final Logger log = LoggerFactory.getLogger(CourseService.class);

    private final CourseRepository courseRepository;

    private final CourseMapper courseMapper;

    private final UserRepository userRepository;
    private final YearRepository yearRepository;

    private final OrientationRepository orientationRepository;

    public CourseService(CourseRepository courseRepository,
                         CourseMapper courseMapper,
                         UserRepository userRepository,
                         OrientationRepository orientationRepository,
                         YearRepository yearRepository) {
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
        this.userRepository = userRepository;
        this.orientationRepository = orientationRepository;
        this.yearRepository = yearRepository;
    }

    /**
     * Save a course.
     *
     * @param courseDTO the entity to save.
     * @return the persisted entity.
     */
    public CourseDTO save(CourseDTO courseDTO) {
        log.debug("Request to save Course : {}", courseDTO);

        Optional<User> professor = Optional.ofNullable(userRepository.findById(courseDTO.getProfessorId())
            .orElseThrow(() -> new EntityNotFoundException(
                "Professor with id " + courseDTO.getProfessorId() + " does not exist."
            )));

        Optional<User> assistant = Optional.ofNullable(userRepository.findById(courseDTO.getAssistantId())
            .orElseThrow(() -> new EntityNotFoundException(
                "Assistant with id " + courseDTO.getAssistantId() + " does not exist."
            )));

        Optional<Year> year = Optional.ofNullable(yearRepository.findById(courseDTO.getYearId())
            .orElseThrow(() -> new EntityNotFoundException(
                "Year with id " + courseDTO.getYearId() + " does not exist."
            )));

        if(courseDTO.getId() != null && !courseRepository.existsById(courseDTO.getId())){
            throw new EntityNotFoundException(
                "Course with id " + courseDTO.getId() + " does not exist."
            );
        }

        if(
            courseDTO.getId() == null && courseRepository.countAllByName(courseDTO.getName()) > 0 ||
            courseDTO.getId() != null && courseRepository.countAllByNameAndIdNot(courseDTO.getName(), courseDTO.getId()) > 0
        )
        {
            throw new BadActionException(
                ExceptionErrors.COURSE_EXISTS.getErrorCode(),
                ExceptionErrors.COURSE_EXISTS.getErrorDescription()
            );
        }

        Course course = courseMapper.toEntity(courseDTO);

        if(course.getId() == null) {
            course.dateCreated(Instant.now());
        }else{
            course.dateUpdated(Instant.now());
        }

        course = courseRepository.save(course);

        return courseMapper.toDto(course);
    }

    /**
     * Get all the courses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CourseDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Courses");
        return courseRepository.findAll(pageable)
            .map(courseMapper::toDto);
    }


    /**
     * Get all the courses with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CourseDTO> findAllWithEagerRelationships(Pageable pageable) {
        return courseRepository.findAllWithEagerRelationships(pageable).map(courseMapper::toDto);
    }

    /**
     * Get one course by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CourseDTO> findOne(Long id) {
        log.debug("Request to get Course : {}", id);
        return courseRepository.findOneWithEagerRelationships(id)
            .map(courseMapper::toDto);
    }

    /**
     * Delete the course by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Course : {}", id);
        Optional<Course> courseOptional = courseRepository.findById(id);
        if(!courseOptional.isPresent()){
            throw new EntityNotFoundException("Course with id " + id + " does not exist.");
        }
        courseRepository.deleteById(id);
    }

    public CourseDTO addOrientationToCourse(CourseOrientationDTO courseOrientationDTO){

        Course course = courseRepository.findById(courseOrientationDTO.getCourseId())
            .orElseThrow(() -> new EntityNotFoundException(
                "Course with id " + courseOrientationDTO.getCourseId() + " does not exist."
            ));

        Orientation orientation = orientationRepository.findById(courseOrientationDTO.getOrientationId())
            .orElseThrow(() -> new EntityNotFoundException(
                "Orientation with id " + courseOrientationDTO.getOrientationId() + " does not exist."
            ));

        if(courseRepository.countAllByOrientationsIdAndCourseId(courseOrientationDTO.getOrientationId(), courseOrientationDTO.getCourseId()) > 0){
            throw new BadActionException(
                  ExceptionErrors.COURSE_ORIENTATION_EXISTS.getErrorCode(),
                  ExceptionErrors.COURSE_ORIENTATION_EXISTS.getErrorDescription()
            );
        }

        course.addOrientations(orientation);

        return courseMapper.toDto(course);

    }

}
