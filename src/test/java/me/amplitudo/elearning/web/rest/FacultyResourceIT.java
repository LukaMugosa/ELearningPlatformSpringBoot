package me.amplitudo.elearning.web.rest;

import me.amplitudo.elearning.ElearningApp;
import me.amplitudo.elearning.domain.Faculty;
import me.amplitudo.elearning.domain.Orientation;
import me.amplitudo.elearning.domain.Profile;
import me.amplitudo.elearning.domain.Building;
import me.amplitudo.elearning.repository.FacultyRepository;
import me.amplitudo.elearning.service.FacultyService;
import me.amplitudo.elearning.service.dto.FacultyDTO;
import me.amplitudo.elearning.service.mapper.FacultyMapper;
import me.amplitudo.elearning.service.dto.FacultyCriteria;
import me.amplitudo.elearning.service.FacultyQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link FacultyResource} REST controller.
 */
@SpringBootTest(classes = ElearningApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class FacultyResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private FacultyRepository facultyRepository;

    @Mock
    private FacultyRepository facultyRepositoryMock;

    @Autowired
    private FacultyMapper facultyMapper;

    @Mock
    private FacultyService facultyServiceMock;

    @Autowired
    private FacultyService facultyService;

    @Autowired
    private FacultyQueryService facultyQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFacultyMockMvc;

    private Faculty faculty;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Faculty createEntity(EntityManager em) {
        Faculty faculty = new Faculty()
            .name(DEFAULT_NAME)
            .dateCreated(DEFAULT_DATE_CREATED)
            .dateUpdated(DEFAULT_DATE_UPDATED);
        return faculty;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Faculty createUpdatedEntity(EntityManager em) {
        Faculty faculty = new Faculty()
            .name(UPDATED_NAME)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateUpdated(UPDATED_DATE_UPDATED);
        return faculty;
    }

    @BeforeEach
    public void initTest() {
        faculty = createEntity(em);
    }

    @Test
    @Transactional
    public void createFaculty() throws Exception {
        int databaseSizeBeforeCreate = facultyRepository.findAll().size();
        // Create the Faculty
        FacultyDTO facultyDTO = facultyMapper.toDto(faculty);
        restFacultyMockMvc.perform(post("/api/faculties")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(facultyDTO)))
            .andExpect(status().isCreated());

        // Validate the Faculty in the database
        List<Faculty> facultyList = facultyRepository.findAll();
        assertThat(facultyList).hasSize(databaseSizeBeforeCreate + 1);
        Faculty testFaculty = facultyList.get(facultyList.size() - 1);
        assertThat(testFaculty.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFaculty.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testFaculty.getDateUpdated()).isEqualTo(DEFAULT_DATE_UPDATED);
    }

    @Test
    @Transactional
    public void createFacultyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = facultyRepository.findAll().size();

        // Create the Faculty with an existing ID
        faculty.setId(1L);
        FacultyDTO facultyDTO = facultyMapper.toDto(faculty);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFacultyMockMvc.perform(post("/api/faculties")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(facultyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Faculty in the database
        List<Faculty> facultyList = facultyRepository.findAll();
        assertThat(facultyList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = facultyRepository.findAll().size();
        // set the field null
        faculty.setName(null);

        // Create the Faculty, which fails.
        FacultyDTO facultyDTO = facultyMapper.toDto(faculty);


        restFacultyMockMvc.perform(post("/api/faculties")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(facultyDTO)))
            .andExpect(status().isBadRequest());

        List<Faculty> facultyList = facultyRepository.findAll();
        assertThat(facultyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFaculties() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        // Get all the facultyList
        restFacultyMockMvc.perform(get("/api/faculties?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(faculty.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].dateUpdated").value(hasItem(DEFAULT_DATE_UPDATED.toString())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllFacultiesWithEagerRelationshipsIsEnabled() throws Exception {
        when(facultyServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFacultyMockMvc.perform(get("/api/faculties?eagerload=true"))
            .andExpect(status().isOk());

        verify(facultyServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllFacultiesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(facultyServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFacultyMockMvc.perform(get("/api/faculties?eagerload=true"))
            .andExpect(status().isOk());

        verify(facultyServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getFaculty() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        // Get the faculty
        restFacultyMockMvc.perform(get("/api/faculties/{id}", faculty.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(faculty.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.dateUpdated").value(DEFAULT_DATE_UPDATED.toString()));
    }


    @Test
    @Transactional
    public void getFacultiesByIdFiltering() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        Long id = faculty.getId();

        defaultFacultyShouldBeFound("id.equals=" + id);
        defaultFacultyShouldNotBeFound("id.notEquals=" + id);

        defaultFacultyShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFacultyShouldNotBeFound("id.greaterThan=" + id);

        defaultFacultyShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFacultyShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllFacultiesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        // Get all the facultyList where name equals to DEFAULT_NAME
        defaultFacultyShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the facultyList where name equals to UPDATED_NAME
        defaultFacultyShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllFacultiesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        // Get all the facultyList where name not equals to DEFAULT_NAME
        defaultFacultyShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the facultyList where name not equals to UPDATED_NAME
        defaultFacultyShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllFacultiesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        // Get all the facultyList where name in DEFAULT_NAME or UPDATED_NAME
        defaultFacultyShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the facultyList where name equals to UPDATED_NAME
        defaultFacultyShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllFacultiesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        // Get all the facultyList where name is not null
        defaultFacultyShouldBeFound("name.specified=true");

        // Get all the facultyList where name is null
        defaultFacultyShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllFacultiesByNameContainsSomething() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        // Get all the facultyList where name contains DEFAULT_NAME
        defaultFacultyShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the facultyList where name contains UPDATED_NAME
        defaultFacultyShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllFacultiesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        // Get all the facultyList where name does not contain DEFAULT_NAME
        defaultFacultyShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the facultyList where name does not contain UPDATED_NAME
        defaultFacultyShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllFacultiesByDateCreatedIsEqualToSomething() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        // Get all the facultyList where dateCreated equals to DEFAULT_DATE_CREATED
        defaultFacultyShouldBeFound("dateCreated.equals=" + DEFAULT_DATE_CREATED);

        // Get all the facultyList where dateCreated equals to UPDATED_DATE_CREATED
        defaultFacultyShouldNotBeFound("dateCreated.equals=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    public void getAllFacultiesByDateCreatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        // Get all the facultyList where dateCreated not equals to DEFAULT_DATE_CREATED
        defaultFacultyShouldNotBeFound("dateCreated.notEquals=" + DEFAULT_DATE_CREATED);

        // Get all the facultyList where dateCreated not equals to UPDATED_DATE_CREATED
        defaultFacultyShouldBeFound("dateCreated.notEquals=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    public void getAllFacultiesByDateCreatedIsInShouldWork() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        // Get all the facultyList where dateCreated in DEFAULT_DATE_CREATED or UPDATED_DATE_CREATED
        defaultFacultyShouldBeFound("dateCreated.in=" + DEFAULT_DATE_CREATED + "," + UPDATED_DATE_CREATED);

        // Get all the facultyList where dateCreated equals to UPDATED_DATE_CREATED
        defaultFacultyShouldNotBeFound("dateCreated.in=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    public void getAllFacultiesByDateCreatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        // Get all the facultyList where dateCreated is not null
        defaultFacultyShouldBeFound("dateCreated.specified=true");

        // Get all the facultyList where dateCreated is null
        defaultFacultyShouldNotBeFound("dateCreated.specified=false");
    }

    @Test
    @Transactional
    public void getAllFacultiesByDateUpdatedIsEqualToSomething() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        // Get all the facultyList where dateUpdated equals to DEFAULT_DATE_UPDATED
        defaultFacultyShouldBeFound("dateUpdated.equals=" + DEFAULT_DATE_UPDATED);

        // Get all the facultyList where dateUpdated equals to UPDATED_DATE_UPDATED
        defaultFacultyShouldNotBeFound("dateUpdated.equals=" + UPDATED_DATE_UPDATED);
    }

    @Test
    @Transactional
    public void getAllFacultiesByDateUpdatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        // Get all the facultyList where dateUpdated not equals to DEFAULT_DATE_UPDATED
        defaultFacultyShouldNotBeFound("dateUpdated.notEquals=" + DEFAULT_DATE_UPDATED);

        // Get all the facultyList where dateUpdated not equals to UPDATED_DATE_UPDATED
        defaultFacultyShouldBeFound("dateUpdated.notEquals=" + UPDATED_DATE_UPDATED);
    }

    @Test
    @Transactional
    public void getAllFacultiesByDateUpdatedIsInShouldWork() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        // Get all the facultyList where dateUpdated in DEFAULT_DATE_UPDATED or UPDATED_DATE_UPDATED
        defaultFacultyShouldBeFound("dateUpdated.in=" + DEFAULT_DATE_UPDATED + "," + UPDATED_DATE_UPDATED);

        // Get all the facultyList where dateUpdated equals to UPDATED_DATE_UPDATED
        defaultFacultyShouldNotBeFound("dateUpdated.in=" + UPDATED_DATE_UPDATED);
    }

    @Test
    @Transactional
    public void getAllFacultiesByDateUpdatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        // Get all the facultyList where dateUpdated is not null
        defaultFacultyShouldBeFound("dateUpdated.specified=true");

        // Get all the facultyList where dateUpdated is null
        defaultFacultyShouldNotBeFound("dateUpdated.specified=false");
    }

    @Test
    @Transactional
    public void getAllFacultiesByOrientationsIsEqualToSomething() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);
        Orientation orientations = OrientationResourceIT.createEntity(em);
        em.persist(orientations);
        em.flush();
        faculty.addOrientations(orientations);
        facultyRepository.saveAndFlush(faculty);
        Long orientationsId = orientations.getId();

        // Get all the facultyList where orientations equals to orientationsId
        defaultFacultyShouldBeFound("orientationsId.equals=" + orientationsId);

        // Get all the facultyList where orientations equals to orientationsId + 1
        defaultFacultyShouldNotBeFound("orientationsId.equals=" + (orientationsId + 1));
    }


    @Test
    @Transactional
    public void getAllFacultiesByOrientationFacultiesIsEqualToSomething() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);
        Orientation orientationFaculties = OrientationResourceIT.createEntity(em);
        em.persist(orientationFaculties);
        em.flush();
        faculty.addOrientationFaculties(orientationFaculties);
        facultyRepository.saveAndFlush(faculty);
        Long orientationFacultiesId = orientationFaculties.getId();

        // Get all the facultyList where orientationFaculties equals to orientationFacultiesId
        defaultFacultyShouldBeFound("orientationFacultiesId.equals=" + orientationFacultiesId);

        // Get all the facultyList where orientationFaculties equals to orientationFacultiesId + 1
        defaultFacultyShouldNotBeFound("orientationFacultiesId.equals=" + (orientationFacultiesId + 1));
    }


    @Test
    @Transactional
    public void getAllFacultiesByUsersIsEqualToSomething() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);
        Profile users = ProfileResourceIT.createEntity(em);
        em.persist(users);
        em.flush();
        faculty.addUsers(users);
        facultyRepository.saveAndFlush(faculty);
        Long usersId = users.getId();

        // Get all the facultyList where users equals to usersId
        defaultFacultyShouldBeFound("usersId.equals=" + usersId);

        // Get all the facultyList where users equals to usersId + 1
        defaultFacultyShouldNotBeFound("usersId.equals=" + (usersId + 1));
    }


    @Test
    @Transactional
    public void getAllFacultiesByBuildingIsEqualToSomething() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);
        Building building = BuildingResourceIT.createEntity(em);
        em.persist(building);
        em.flush();
        faculty.setBuilding(building);
        facultyRepository.saveAndFlush(faculty);
        Long buildingId = building.getId();

        // Get all the facultyList where building equals to buildingId
        defaultFacultyShouldBeFound("buildingId.equals=" + buildingId);

        // Get all the facultyList where building equals to buildingId + 1
        defaultFacultyShouldNotBeFound("buildingId.equals=" + (buildingId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFacultyShouldBeFound(String filter) throws Exception {
        restFacultyMockMvc.perform(get("/api/faculties?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(faculty.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].dateUpdated").value(hasItem(DEFAULT_DATE_UPDATED.toString())));

        // Check, that the count call also returns 1
        restFacultyMockMvc.perform(get("/api/faculties/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFacultyShouldNotBeFound(String filter) throws Exception {
        restFacultyMockMvc.perform(get("/api/faculties?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFacultyMockMvc.perform(get("/api/faculties/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingFaculty() throws Exception {
        // Get the faculty
        restFacultyMockMvc.perform(get("/api/faculties/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFaculty() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        int databaseSizeBeforeUpdate = facultyRepository.findAll().size();

        // Update the faculty
        Faculty updatedFaculty = facultyRepository.findById(faculty.getId()).get();
        // Disconnect from session so that the updates on updatedFaculty are not directly saved in db
        em.detach(updatedFaculty);
        updatedFaculty
            .name(UPDATED_NAME)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateUpdated(UPDATED_DATE_UPDATED);
        FacultyDTO facultyDTO = facultyMapper.toDto(updatedFaculty);

        restFacultyMockMvc.perform(put("/api/faculties")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(facultyDTO)))
            .andExpect(status().isOk());

        // Validate the Faculty in the database
        List<Faculty> facultyList = facultyRepository.findAll();
        assertThat(facultyList).hasSize(databaseSizeBeforeUpdate);
        Faculty testFaculty = facultyList.get(facultyList.size() - 1);
        assertThat(testFaculty.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFaculty.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testFaculty.getDateUpdated()).isEqualTo(UPDATED_DATE_UPDATED);
    }

    @Test
    @Transactional
    public void updateNonExistingFaculty() throws Exception {
        int databaseSizeBeforeUpdate = facultyRepository.findAll().size();

        // Create the Faculty
        FacultyDTO facultyDTO = facultyMapper.toDto(faculty);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacultyMockMvc.perform(put("/api/faculties")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(facultyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Faculty in the database
        List<Faculty> facultyList = facultyRepository.findAll();
        assertThat(facultyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFaculty() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        int databaseSizeBeforeDelete = facultyRepository.findAll().size();

        // Delete the faculty
        restFacultyMockMvc.perform(delete("/api/faculties/{id}", faculty.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Faculty> facultyList = facultyRepository.findAll();
        assertThat(facultyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
