package me.amplitudo.elearning.service;

import me.amplitudo.elearning.domain.AssignmentProfile;
import me.amplitudo.elearning.repository.AssignmentProfileRepository;
import me.amplitudo.elearning.service.dto.AssignmentProfileDTO;
import me.amplitudo.elearning.service.mapper.AssignmentProfileMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link AssignmentProfile}.
 */
@Service
@Transactional
public class AssignmentProfileService {

    private final Logger log = LoggerFactory.getLogger(AssignmentProfileService.class);

    private final AssignmentProfileRepository assignmentProfileRepository;

    private final AssignmentProfileMapper assignmentProfileMapper;

    public AssignmentProfileService(AssignmentProfileRepository assignmentProfileRepository, AssignmentProfileMapper assignmentProfileMapper) {
        this.assignmentProfileRepository = assignmentProfileRepository;
        this.assignmentProfileMapper = assignmentProfileMapper;
    }

    /**
     * Save a assignmentProfile.
     *
     * @param assignmentProfileDTO the entity to save.
     * @return the persisted entity.
     */
    public AssignmentProfileDTO save(AssignmentProfileDTO assignmentProfileDTO) {
        log.debug("Request to save AssignmentProfile : {}", assignmentProfileDTO);
        AssignmentProfile assignmentProfile = assignmentProfileMapper.toEntity(assignmentProfileDTO);
        assignmentProfile = assignmentProfileRepository.save(assignmentProfile);
        return assignmentProfileMapper.toDto(assignmentProfile);
    }

    /**
     * Get all the assignmentProfiles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AssignmentProfileDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AssignmentProfiles");
        return assignmentProfileRepository.findAll(pageable)
            .map(assignmentProfileMapper::toDto);
    }


    /**
     * Get one assignmentProfile by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AssignmentProfileDTO> findOne(Long id) {
        log.debug("Request to get AssignmentProfile : {}", id);
        return assignmentProfileRepository.findById(id)
            .map(assignmentProfileMapper::toDto);
    }

    /**
     * Delete the assignmentProfile by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AssignmentProfile : {}", id);
        assignmentProfileRepository.deleteById(id);
    }
}
