package me.amplitudo.elearning.web.rest;

import me.amplitudo.elearning.service.AssignmentProfileService;
import me.amplitudo.elearning.web.rest.errors.BadRequestAlertException;
import me.amplitudo.elearning.service.dto.AssignmentProfileDTO;
import me.amplitudo.elearning.service.dto.AssignmentProfileCriteria;
import me.amplitudo.elearning.service.AssignmentProfileQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link me.amplitudo.elearning.domain.AssignmentProfile}.
 */
@RestController
@RequestMapping("/api")
public class AssignmentProfileResource {

    private final Logger log = LoggerFactory.getLogger(AssignmentProfileResource.class);

    private static final String ENTITY_NAME = "assignmentProfile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AssignmentProfileService assignmentProfileService;

    private final AssignmentProfileQueryService assignmentProfileQueryService;

    public AssignmentProfileResource(AssignmentProfileService assignmentProfileService, AssignmentProfileQueryService assignmentProfileQueryService) {
        this.assignmentProfileService = assignmentProfileService;
        this.assignmentProfileQueryService = assignmentProfileQueryService;
    }

    /**
     * {@code POST  /assignment-profiles} : Create a new assignmentProfile.
     *
     * @param assignmentProfileDTO the assignmentProfileDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new assignmentProfileDTO, or with status {@code 400 (Bad Request)} if the assignmentProfile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/assignment-profiles")
    public ResponseEntity<AssignmentProfileDTO> createAssignmentProfile(@RequestBody AssignmentProfileDTO assignmentProfileDTO) throws URISyntaxException {
        log.debug("REST request to save AssignmentProfile : {}", assignmentProfileDTO);
        if (assignmentProfileDTO.getId() != null) {
            throw new BadRequestAlertException("A new assignmentProfile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AssignmentProfileDTO result = assignmentProfileService.save(assignmentProfileDTO);
        return ResponseEntity.created(new URI("/api/assignment-profiles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /assignment-profiles} : Updates an existing assignmentProfile.
     *
     * @param assignmentProfileDTO the assignmentProfileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assignmentProfileDTO,
     * or with status {@code 400 (Bad Request)} if the assignmentProfileDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the assignmentProfileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/assignment-profiles")
    public ResponseEntity<AssignmentProfileDTO> updateAssignmentProfile(@RequestBody AssignmentProfileDTO assignmentProfileDTO) throws URISyntaxException {
        log.debug("REST request to update AssignmentProfile : {}", assignmentProfileDTO);
        if (assignmentProfileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AssignmentProfileDTO result = assignmentProfileService.save(assignmentProfileDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, assignmentProfileDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /assignment-profiles} : get all the assignmentProfiles.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of assignmentProfiles in body.
     */
    @GetMapping("/assignment-profiles")
    public ResponseEntity<List<AssignmentProfileDTO>> getAllAssignmentProfiles(AssignmentProfileCriteria criteria, Pageable pageable) {
        log.debug("REST request to get AssignmentProfiles by criteria: {}", criteria);
        Page<AssignmentProfileDTO> page = assignmentProfileQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /assignment-profiles/count} : count all the assignmentProfiles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/assignment-profiles/count")
    public ResponseEntity<Long> countAssignmentProfiles(AssignmentProfileCriteria criteria) {
        log.debug("REST request to count AssignmentProfiles by criteria: {}", criteria);
        return ResponseEntity.ok().body(assignmentProfileQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /assignment-profiles/:id} : get the "id" assignmentProfile.
     *
     * @param id the id of the assignmentProfileDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the assignmentProfileDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/assignment-profiles/{id}")
    public ResponseEntity<AssignmentProfileDTO> getAssignmentProfile(@PathVariable Long id) {
        log.debug("REST request to get AssignmentProfile : {}", id);
        Optional<AssignmentProfileDTO> assignmentProfileDTO = assignmentProfileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(assignmentProfileDTO);
    }

    /**
     * {@code DELETE  /assignment-profiles/:id} : delete the "id" assignmentProfile.
     *
     * @param id the id of the assignmentProfileDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/assignment-profiles/{id}")
    public ResponseEntity<Void> deleteAssignmentProfile(@PathVariable Long id) {
        log.debug("REST request to delete AssignmentProfile : {}", id);
        assignmentProfileService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
