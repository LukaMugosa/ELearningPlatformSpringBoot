package me.amplitudo.elearning.web.rest;

import me.amplitudo.elearning.service.OrientationService;
import me.amplitudo.elearning.web.rest.errors.BadRequestAlertException;
import me.amplitudo.elearning.service.dto.OrientationDTO;
import me.amplitudo.elearning.service.dto.OrientationCriteria;
import me.amplitudo.elearning.service.OrientationQueryService;

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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link me.amplitudo.elearning.domain.Orientation}.
 */
@RestController
@RequestMapping("/api")
public class OrientationResource {

    private final Logger log = LoggerFactory.getLogger(OrientationResource.class);

    private static final String ENTITY_NAME = "orientation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrientationService orientationService;

    private final OrientationQueryService orientationQueryService;

    public OrientationResource(OrientationService orientationService, OrientationQueryService orientationQueryService) {
        this.orientationService = orientationService;
        this.orientationQueryService = orientationQueryService;
    }

    /**
     * {@code POST  /orientations} : Create a new orientation.
     *
     * @param orientationDTO the orientationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orientationDTO, or with status {@code 400 (Bad Request)} if the orientation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/orientations")
    public ResponseEntity<OrientationDTO> createOrientation(@Valid @RequestBody OrientationDTO orientationDTO) throws URISyntaxException {
        log.debug("REST request to save Orientation : {}", orientationDTO);
        if (orientationDTO.getId() != null) {
            throw new BadRequestAlertException("A new orientation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrientationDTO result = orientationService.save(orientationDTO);
        return ResponseEntity.created(new URI("/api/orientations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /orientations} : Updates an existing orientation.
     *
     * @param orientationDTO the orientationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orientationDTO,
     * or with status {@code 400 (Bad Request)} if the orientationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orientationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/orientations")
    public ResponseEntity<OrientationDTO> updateOrientation(@Valid @RequestBody OrientationDTO orientationDTO) throws URISyntaxException {
        log.debug("REST request to update Orientation : {}", orientationDTO);
        if (orientationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OrientationDTO result = orientationService.save(orientationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orientationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /orientations} : get all the orientations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orientations in body.
     */
    @GetMapping("/orientations")
    public ResponseEntity<List<OrientationDTO>> getAllOrientations(OrientationCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Orientations by criteria: {}", criteria);
        Page<OrientationDTO> page = orientationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /orientations/count} : count all the orientations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/orientations/count")
    public ResponseEntity<Long> countOrientations(OrientationCriteria criteria) {
        log.debug("REST request to count Orientations by criteria: {}", criteria);
        return ResponseEntity.ok().body(orientationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /orientations/:id} : get the "id" orientation.
     *
     * @param id the id of the orientationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orientationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/orientations/{id}")
    public ResponseEntity<OrientationDTO> getOrientation(@PathVariable Long id) {
        log.debug("REST request to get Orientation : {}", id);
        Optional<OrientationDTO> orientationDTO = orientationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orientationDTO);
    }

    /**
     * {@code DELETE  /orientations/:id} : delete the "id" orientation.
     *
     * @param id the id of the orientationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/orientations/{id}")
    public ResponseEntity<Void> deleteOrientation(@PathVariable Long id) {
        log.debug("REST request to delete Orientation : {}", id);
        orientationService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
