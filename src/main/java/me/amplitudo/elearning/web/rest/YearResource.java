package me.amplitudo.elearning.web.rest;

import me.amplitudo.elearning.service.YearService;
import me.amplitudo.elearning.web.rest.errors.BadRequestAlertException;
import me.amplitudo.elearning.service.dto.YearDTO;
import me.amplitudo.elearning.service.dto.YearCriteria;
import me.amplitudo.elearning.service.YearQueryService;

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
 * REST controller for managing {@link me.amplitudo.elearning.domain.Year}.
 */
@RestController
@RequestMapping("/api")
public class YearResource {

    private final Logger log = LoggerFactory.getLogger(YearResource.class);

    private static final String ENTITY_NAME = "year";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final YearService yearService;

    private final YearQueryService yearQueryService;

    public YearResource(YearService yearService, YearQueryService yearQueryService) {
        this.yearService = yearService;
        this.yearQueryService = yearQueryService;
    }

    /**
     * {@code POST  /years} : Create a new year.
     *
     * @param yearDTO the yearDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new yearDTO, or with status {@code 400 (Bad Request)} if the year has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/years")
    public ResponseEntity<YearDTO> createYear(@Valid @RequestBody YearDTO yearDTO) throws URISyntaxException {
        log.debug("REST request to save Year : {}", yearDTO);
        if (yearDTO.getId() != null) {
            throw new BadRequestAlertException("A new year cannot already have an ID", ENTITY_NAME, "idexists");
        }
        YearDTO result = yearService.save(yearDTO);
        return ResponseEntity.created(new URI("/api/years/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /years} : Updates an existing year.
     *
     * @param yearDTO the yearDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated yearDTO,
     * or with status {@code 400 (Bad Request)} if the yearDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the yearDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/years")
    public ResponseEntity<YearDTO> updateYear(@Valid @RequestBody YearDTO yearDTO) throws URISyntaxException {
        log.debug("REST request to update Year : {}", yearDTO);
        if (yearDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        YearDTO result = yearService.save(yearDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, yearDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /years} : get all the years.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of years in body.
     */
    @GetMapping("/years")
    public ResponseEntity<List<YearDTO>> getAllYears(YearCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Years by criteria: {}", criteria);
        Page<YearDTO> page = yearQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /years/count} : count all the years.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/years/count")
    public ResponseEntity<Long> countYears(YearCriteria criteria) {
        log.debug("REST request to count Years by criteria: {}", criteria);
        return ResponseEntity.ok().body(yearQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /years/:id} : get the "id" year.
     *
     * @param id the id of the yearDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the yearDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/years/{id}")
    public ResponseEntity<YearDTO> getYear(@PathVariable Long id) {
        log.debug("REST request to get Year : {}", id);
        Optional<YearDTO> yearDTO = yearService.findOne(id);
        return ResponseUtil.wrapOrNotFound(yearDTO);
    }

    /**
     * {@code DELETE  /years/:id} : delete the "id" year.
     *
     * @param id the id of the yearDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/years/{id}")
    public ResponseEntity<Void> deleteYear(@PathVariable Long id) {
        log.debug("REST request to delete Year : {}", id);
        yearService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
