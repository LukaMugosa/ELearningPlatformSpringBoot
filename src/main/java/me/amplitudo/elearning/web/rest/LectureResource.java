package me.amplitudo.elearning.web.rest;

import me.amplitudo.elearning.service.LectureService;
import me.amplitudo.elearning.web.rest.errors.BadRequestAlertException;
import me.amplitudo.elearning.service.dto.LectureDTO;
import me.amplitudo.elearning.service.dto.LectureCriteria;
import me.amplitudo.elearning.service.LectureQueryService;

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
 * REST controller for managing {@link me.amplitudo.elearning.domain.Lecture}.
 */
@RestController
@RequestMapping("/api")
public class LectureResource {

    private final Logger log = LoggerFactory.getLogger(LectureResource.class);

    private static final String ENTITY_NAME = "lecture";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LectureService lectureService;

    private final LectureQueryService lectureQueryService;

    public LectureResource(LectureService lectureService, LectureQueryService lectureQueryService) {
        this.lectureService = lectureService;
        this.lectureQueryService = lectureQueryService;
    }

    /**
     * {@code POST  /lectures} : Create a new lecture.
     *
     * @param lectureDTO the lectureDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lectureDTO, or with status {@code 400 (Bad Request)} if the lecture has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/lectures")
    public ResponseEntity<LectureDTO> createLecture(@Valid @RequestBody LectureDTO lectureDTO) throws URISyntaxException {
        log.debug("REST request to save Lecture : {}", lectureDTO);
        if (lectureDTO.getId() != null) {
            throw new BadRequestAlertException("A new lecture cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LectureDTO result = lectureService.save(lectureDTO);
        return ResponseEntity.created(new URI("/api/lectures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /lectures} : Updates an existing lecture.
     *
     * @param lectureDTO the lectureDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lectureDTO,
     * or with status {@code 400 (Bad Request)} if the lectureDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lectureDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lectures")
    public ResponseEntity<LectureDTO> updateLecture(@Valid @RequestBody LectureDTO lectureDTO) throws URISyntaxException {
        log.debug("REST request to update Lecture : {}", lectureDTO);
        if (lectureDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LectureDTO result = lectureService.save(lectureDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lectureDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /lectures} : get all the lectures.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lectures in body.
     */
    @GetMapping("/lectures")
    public ResponseEntity<List<LectureDTO>> getAllLectures(LectureCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Lectures by criteria: {}", criteria);
        Page<LectureDTO> page = lectureQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /lectures/count} : count all the lectures.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/lectures/count")
    public ResponseEntity<Long> countLectures(LectureCriteria criteria) {
        log.debug("REST request to count Lectures by criteria: {}", criteria);
        return ResponseEntity.ok().body(lectureQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /lectures/:id} : get the "id" lecture.
     *
     * @param id the id of the lectureDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lectureDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lectures/{id}")
    public ResponseEntity<LectureDTO> getLecture(@PathVariable Long id) {
        log.debug("REST request to get Lecture : {}", id);
        Optional<LectureDTO> lectureDTO = lectureService.findOne(id);
        return ResponseUtil.wrapOrNotFound(lectureDTO);
    }

    /**
     * {@code DELETE  /lectures/:id} : delete the "id" lecture.
     *
     * @param id the id of the lectureDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/lectures/{id}")
    public ResponseEntity<Void> deleteLecture(@PathVariable Long id) {
        log.debug("REST request to delete Lecture : {}", id);
        lectureService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
