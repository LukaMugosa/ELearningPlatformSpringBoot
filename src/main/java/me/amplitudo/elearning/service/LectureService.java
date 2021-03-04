package me.amplitudo.elearning.service;

import me.amplitudo.elearning.domain.Lecture;
import me.amplitudo.elearning.repository.LectureRepository;
import me.amplitudo.elearning.service.dto.LectureDTO;
import me.amplitudo.elearning.service.mapper.LectureMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Lecture}.
 */
@Service
@Transactional
public class LectureService {

    private final Logger log = LoggerFactory.getLogger(LectureService.class);

    private final LectureRepository lectureRepository;

    private final LectureMapper lectureMapper;

    public LectureService(LectureRepository lectureRepository, LectureMapper lectureMapper) {
        this.lectureRepository = lectureRepository;
        this.lectureMapper = lectureMapper;
    }

    /**
     * Save a lecture.
     *
     * @param lectureDTO the entity to save.
     * @return the persisted entity.
     */
    public LectureDTO save(LectureDTO lectureDTO) {
        log.debug("Request to save Lecture : {}", lectureDTO);
        Lecture lecture = lectureMapper.toEntity(lectureDTO);
        lecture = lectureRepository.save(lecture);
        return lectureMapper.toDto(lecture);
    }

    /**
     * Get all the lectures.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<LectureDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Lectures");
        return lectureRepository.findAll(pageable)
            .map(lectureMapper::toDto);
    }


    /**
     * Get one lecture by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LectureDTO> findOne(Long id) {
        log.debug("Request to get Lecture : {}", id);
        return lectureRepository.findById(id)
            .map(lectureMapper::toDto);
    }

    /**
     * Delete the lecture by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Lecture : {}", id);
        lectureRepository.deleteById(id);
    }
}
