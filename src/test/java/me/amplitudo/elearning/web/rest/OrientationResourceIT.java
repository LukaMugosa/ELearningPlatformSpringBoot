package me.amplitudo.elearning.web.rest;

import me.amplitudo.elearning.ElearningApp;
import me.amplitudo.elearning.domain.Orientation;
import me.amplitudo.elearning.domain.Profile;
import me.amplitudo.elearning.domain.Faculty;
import me.amplitudo.elearning.domain.Course;
import me.amplitudo.elearning.repository.OrientationRepository;
import me.amplitudo.elearning.service.OrientationService;
import me.amplitudo.elearning.service.dto.OrientationDTO;
import me.amplitudo.elearning.service.mapper.OrientationMapper;
import me.amplitudo.elearning.service.dto.OrientationCriteria;
import me.amplitudo.elearning.service.OrientationQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link OrientationResource} REST controller.
 */
@SpringBootTest(classes = ElearningApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class OrientationResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private OrientationRepository orientationRepository;

    @Autowired
    private OrientationMapper orientationMapper;

    @Autowired
    private OrientationService orientationService;

    @Autowired
    private OrientationQueryService orientationQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrientationMockMvc;

    private Orientation orientation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Orientation createEntity(EntityManager em) {
        Orientation orientation = new Orientation()
            .name(DEFAULT_NAME)
            .dateCreated(DEFAULT_DATE_CREATED)
            .dateUpdated(DEFAULT_DATE_UPDATED);
        return orientation;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Orientation createUpdatedEntity(EntityManager em) {
        Orientation orientation = new Orientation()
            .name(UPDATED_NAME)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateUpdated(UPDATED_DATE_UPDATED);
        return orientation;
    }

    @BeforeEach
    public void initTest() {
        orientation = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrientation() throws Exception {
        int databaseSizeBeforeCreate = orientationRepository.findAll().size();
        // Create the Orientation
        OrientationDTO orientationDTO = orientationMapper.toDto(orientation);
        restOrientationMockMvc.perform(post("/api/orientations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(orientationDTO)))
            .andExpect(status().isCreated());

        // Validate the Orientation in the database
        List<Orientation> orientationList = orientationRepository.findAll();
        assertThat(orientationList).hasSize(databaseSizeBeforeCreate + 1);
        Orientation testOrientation = orientationList.get(orientationList.size() - 1);
        assertThat(testOrientation.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOrientation.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testOrientation.getDateUpdated()).isEqualTo(DEFAULT_DATE_UPDATED);
    }

    @Test
    @Transactional
    public void createOrientationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = orientationRepository.findAll().size();

        // Create the Orientation with an existing ID
        orientation.setId(1L);
        OrientationDTO orientationDTO = orientationMapper.toDto(orientation);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrientationMockMvc.perform(post("/api/orientations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(orientationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Orientation in the database
        List<Orientation> orientationList = orientationRepository.findAll();
        assertThat(orientationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = orientationRepository.findAll().size();
        // set the field null
        orientation.setName(null);

        // Create the Orientation, which fails.
        OrientationDTO orientationDTO = orientationMapper.toDto(orientation);


        restOrientationMockMvc.perform(post("/api/orientations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(orientationDTO)))
            .andExpect(status().isBadRequest());

        List<Orientation> orientationList = orientationRepository.findAll();
        assertThat(orientationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOrientations() throws Exception {
        // Initialize the database
        orientationRepository.saveAndFlush(orientation);

        // Get all the orientationList
        restOrientationMockMvc.perform(get("/api/orientations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orientation.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].dateUpdated").value(hasItem(DEFAULT_DATE_UPDATED.toString())));
    }
    
    @Test
    @Transactional
    public void getOrientation() throws Exception {
        // Initialize the database
        orientationRepository.saveAndFlush(orientation);

        // Get the orientation
        restOrientationMockMvc.perform(get("/api/orientations/{id}", orientation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orientation.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.dateUpdated").value(DEFAULT_DATE_UPDATED.toString()));
    }


    @Test
    @Transactional
    public void getOrientationsByIdFiltering() throws Exception {
        // Initialize the database
        orientationRepository.saveAndFlush(orientation);

        Long id = orientation.getId();

        defaultOrientationShouldBeFound("id.equals=" + id);
        defaultOrientationShouldNotBeFound("id.notEquals=" + id);

        defaultOrientationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOrientationShouldNotBeFound("id.greaterThan=" + id);

        defaultOrientationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOrientationShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllOrientationsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        orientationRepository.saveAndFlush(orientation);

        // Get all the orientationList where name equals to DEFAULT_NAME
        defaultOrientationShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the orientationList where name equals to UPDATED_NAME
        defaultOrientationShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOrientationsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orientationRepository.saveAndFlush(orientation);

        // Get all the orientationList where name not equals to DEFAULT_NAME
        defaultOrientationShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the orientationList where name not equals to UPDATED_NAME
        defaultOrientationShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOrientationsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        orientationRepository.saveAndFlush(orientation);

        // Get all the orientationList where name in DEFAULT_NAME or UPDATED_NAME
        defaultOrientationShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the orientationList where name equals to UPDATED_NAME
        defaultOrientationShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOrientationsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        orientationRepository.saveAndFlush(orientation);

        // Get all the orientationList where name is not null
        defaultOrientationShouldBeFound("name.specified=true");

        // Get all the orientationList where name is null
        defaultOrientationShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllOrientationsByNameContainsSomething() throws Exception {
        // Initialize the database
        orientationRepository.saveAndFlush(orientation);

        // Get all the orientationList where name contains DEFAULT_NAME
        defaultOrientationShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the orientationList where name contains UPDATED_NAME
        defaultOrientationShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOrientationsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        orientationRepository.saveAndFlush(orientation);

        // Get all the orientationList where name does not contain DEFAULT_NAME
        defaultOrientationShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the orientationList where name does not contain UPDATED_NAME
        defaultOrientationShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllOrientationsByDateCreatedIsEqualToSomething() throws Exception {
        // Initialize the database
        orientationRepository.saveAndFlush(orientation);

        // Get all the orientationList where dateCreated equals to DEFAULT_DATE_CREATED
        defaultOrientationShouldBeFound("dateCreated.equals=" + DEFAULT_DATE_CREATED);

        // Get all the orientationList where dateCreated equals to UPDATED_DATE_CREATED
        defaultOrientationShouldNotBeFound("dateCreated.equals=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    public void getAllOrientationsByDateCreatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orientationRepository.saveAndFlush(orientation);

        // Get all the orientationList where dateCreated not equals to DEFAULT_DATE_CREATED
        defaultOrientationShouldNotBeFound("dateCreated.notEquals=" + DEFAULT_DATE_CREATED);

        // Get all the orientationList where dateCreated not equals to UPDATED_DATE_CREATED
        defaultOrientationShouldBeFound("dateCreated.notEquals=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    public void getAllOrientationsByDateCreatedIsInShouldWork() throws Exception {
        // Initialize the database
        orientationRepository.saveAndFlush(orientation);

        // Get all the orientationList where dateCreated in DEFAULT_DATE_CREATED or UPDATED_DATE_CREATED
        defaultOrientationShouldBeFound("dateCreated.in=" + DEFAULT_DATE_CREATED + "," + UPDATED_DATE_CREATED);

        // Get all the orientationList where dateCreated equals to UPDATED_DATE_CREATED
        defaultOrientationShouldNotBeFound("dateCreated.in=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    public void getAllOrientationsByDateCreatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        orientationRepository.saveAndFlush(orientation);

        // Get all the orientationList where dateCreated is not null
        defaultOrientationShouldBeFound("dateCreated.specified=true");

        // Get all the orientationList where dateCreated is null
        defaultOrientationShouldNotBeFound("dateCreated.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrientationsByDateUpdatedIsEqualToSomething() throws Exception {
        // Initialize the database
        orientationRepository.saveAndFlush(orientation);

        // Get all the orientationList where dateUpdated equals to DEFAULT_DATE_UPDATED
        defaultOrientationShouldBeFound("dateUpdated.equals=" + DEFAULT_DATE_UPDATED);

        // Get all the orientationList where dateUpdated equals to UPDATED_DATE_UPDATED
        defaultOrientationShouldNotBeFound("dateUpdated.equals=" + UPDATED_DATE_UPDATED);
    }

    @Test
    @Transactional
    public void getAllOrientationsByDateUpdatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orientationRepository.saveAndFlush(orientation);

        // Get all the orientationList where dateUpdated not equals to DEFAULT_DATE_UPDATED
        defaultOrientationShouldNotBeFound("dateUpdated.notEquals=" + DEFAULT_DATE_UPDATED);

        // Get all the orientationList where dateUpdated not equals to UPDATED_DATE_UPDATED
        defaultOrientationShouldBeFound("dateUpdated.notEquals=" + UPDATED_DATE_UPDATED);
    }

    @Test
    @Transactional
    public void getAllOrientationsByDateUpdatedIsInShouldWork() throws Exception {
        // Initialize the database
        orientationRepository.saveAndFlush(orientation);

        // Get all the orientationList where dateUpdated in DEFAULT_DATE_UPDATED or UPDATED_DATE_UPDATED
        defaultOrientationShouldBeFound("dateUpdated.in=" + DEFAULT_DATE_UPDATED + "," + UPDATED_DATE_UPDATED);

        // Get all the orientationList where dateUpdated equals to UPDATED_DATE_UPDATED
        defaultOrientationShouldNotBeFound("dateUpdated.in=" + UPDATED_DATE_UPDATED);
    }

    @Test
    @Transactional
    public void getAllOrientationsByDateUpdatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        orientationRepository.saveAndFlush(orientation);

        // Get all the orientationList where dateUpdated is not null
        defaultOrientationShouldBeFound("dateUpdated.specified=true");

        // Get all the orientationList where dateUpdated is null
        defaultOrientationShouldNotBeFound("dateUpdated.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrientationsByUsersIsEqualToSomething() throws Exception {
        // Initialize the database
        orientationRepository.saveAndFlush(orientation);
        Profile users = ProfileResourceIT.createEntity(em);
        em.persist(users);
        em.flush();
        orientation.addUsers(users);
        orientationRepository.saveAndFlush(orientation);
        Long usersId = users.getId();

        // Get all the orientationList where users equals to usersId
        defaultOrientationShouldBeFound("usersId.equals=" + usersId);

        // Get all the orientationList where users equals to usersId + 1
        defaultOrientationShouldNotBeFound("usersId.equals=" + (usersId + 1));
    }


    @Test
    @Transactional
    public void getAllOrientationsByFacultyIsEqualToSomething() throws Exception {
        // Initialize the database
        orientationRepository.saveAndFlush(orientation);
        Faculty faculty = FacultyResourceIT.createEntity(em);
        em.persist(faculty);
        em.flush();
        orientation.setFaculty(faculty);
        orientationRepository.saveAndFlush(orientation);
        Long facultyId = faculty.getId();

        // Get all the orientationList where faculty equals to facultyId
        defaultOrientationShouldBeFound("facultyId.equals=" + facultyId);

        // Get all the orientationList where faculty equals to facultyId + 1
        defaultOrientationShouldNotBeFound("facultyId.equals=" + (facultyId + 1));
    }


    @Test
    @Transactional
    public void getAllOrientationsByFacultiesIsEqualToSomething() throws Exception {
        // Initialize the database
        orientationRepository.saveAndFlush(orientation);
        Faculty faculties = FacultyResourceIT.createEntity(em);
        em.persist(faculties);
        em.flush();
        orientation.addFaculties(faculties);
        orientationRepository.saveAndFlush(orientation);
        Long facultiesId = faculties.getId();

        // Get all the orientationList where faculties equals to facultiesId
        defaultOrientationShouldBeFound("facultiesId.equals=" + facultiesId);

        // Get all the orientationList where faculties equals to facultiesId + 1
        defaultOrientationShouldNotBeFound("facultiesId.equals=" + (facultiesId + 1));
    }


    @Test
    @Transactional
    public void getAllOrientationsByCoursesIsEqualToSomething() throws Exception {
        // Initialize the database
        orientationRepository.saveAndFlush(orientation);
        Course courses = CourseResourceIT.createEntity(em);
        em.persist(courses);
        em.flush();
        orientation.addCourses(courses);
        orientationRepository.saveAndFlush(orientation);
        Long coursesId = courses.getId();

        // Get all the orientationList where courses equals to coursesId
        defaultOrientationShouldBeFound("coursesId.equals=" + coursesId);

        // Get all the orientationList where courses equals to coursesId + 1
        defaultOrientationShouldNotBeFound("coursesId.equals=" + (coursesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrientationShouldBeFound(String filter) throws Exception {
        restOrientationMockMvc.perform(get("/api/orientations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orientation.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].dateUpdated").value(hasItem(DEFAULT_DATE_UPDATED.toString())));

        // Check, that the count call also returns 1
        restOrientationMockMvc.perform(get("/api/orientations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrientationShouldNotBeFound(String filter) throws Exception {
        restOrientationMockMvc.perform(get("/api/orientations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrientationMockMvc.perform(get("/api/orientations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingOrientation() throws Exception {
        // Get the orientation
        restOrientationMockMvc.perform(get("/api/orientations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrientation() throws Exception {
        // Initialize the database
        orientationRepository.saveAndFlush(orientation);

        int databaseSizeBeforeUpdate = orientationRepository.findAll().size();

        // Update the orientation
        Orientation updatedOrientation = orientationRepository.findById(orientation.getId()).get();
        // Disconnect from session so that the updates on updatedOrientation are not directly saved in db
        em.detach(updatedOrientation);
        updatedOrientation
            .name(UPDATED_NAME)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateUpdated(UPDATED_DATE_UPDATED);
        OrientationDTO orientationDTO = orientationMapper.toDto(updatedOrientation);

        restOrientationMockMvc.perform(put("/api/orientations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(orientationDTO)))
            .andExpect(status().isOk());

        // Validate the Orientation in the database
        List<Orientation> orientationList = orientationRepository.findAll();
        assertThat(orientationList).hasSize(databaseSizeBeforeUpdate);
        Orientation testOrientation = orientationList.get(orientationList.size() - 1);
        assertThat(testOrientation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrientation.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testOrientation.getDateUpdated()).isEqualTo(UPDATED_DATE_UPDATED);
    }

    @Test
    @Transactional
    public void updateNonExistingOrientation() throws Exception {
        int databaseSizeBeforeUpdate = orientationRepository.findAll().size();

        // Create the Orientation
        OrientationDTO orientationDTO = orientationMapper.toDto(orientation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrientationMockMvc.perform(put("/api/orientations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(orientationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Orientation in the database
        List<Orientation> orientationList = orientationRepository.findAll();
        assertThat(orientationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOrientation() throws Exception {
        // Initialize the database
        orientationRepository.saveAndFlush(orientation);

        int databaseSizeBeforeDelete = orientationRepository.findAll().size();

        // Delete the orientation
        restOrientationMockMvc.perform(delete("/api/orientations/{id}", orientation.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Orientation> orientationList = orientationRepository.findAll();
        assertThat(orientationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
