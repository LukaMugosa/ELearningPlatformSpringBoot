package me.amplitudo.elearning.service;

import me.amplitudo.elearning.domain.Year;
import me.amplitudo.elearning.repository.YearRepository;
import me.amplitudo.elearning.service.dto.YearDTO;
import me.amplitudo.elearning.service.mapper.YearMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Year}.
 */
@Service
@Transactional
public class YearService {

    private final Logger log = LoggerFactory.getLogger(YearService.class);

    private final YearRepository yearRepository;

    private final YearMapper yearMapper;

    public YearService(YearRepository yearRepository, YearMapper yearMapper) {
        this.yearRepository = yearRepository;
        this.yearMapper = yearMapper;
    }

    /**
     * Save a year.
     *
     * @param yearDTO the entity to save.
     * @return the persisted entity.
     */
    public YearDTO save(YearDTO yearDTO) {
        log.debug("Request to save Year : {}", yearDTO);
        Year year = yearMapper.toEntity(yearDTO);
        year = yearRepository.save(year);
        return yearMapper.toDto(year);
    }

    /**
     * Get all the years.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<YearDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Years");
        return yearRepository.findAll(pageable)
            .map(yearMapper::toDto);
    }


    /**
     * Get one year by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<YearDTO> findOne(Long id) {
        log.debug("Request to get Year : {}", id);
        return yearRepository.findById(id)
            .map(yearMapper::toDto);
    }

    /**
     * Delete the year by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Year : {}", id);
        yearRepository.deleteById(id);
    }
}
