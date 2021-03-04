package me.amplitudo.elearning.web.rest;

import me.amplitudo.elearning.ElearningApp;
import me.amplitudo.elearning.domain.Year;
import me.amplitudo.elearning.domain.Profile;
import me.amplitudo.elearning.repository.YearRepository;
import me.amplitudo.elearning.service.YearService;
import me.amplitudo.elearning.service.dto.YearDTO;
import me.amplitudo.elearning.service.mapper.YearMapper;
import me.amplitudo.elearning.service.dto.YearCriteria;
import me.amplitudo.elearning.service.YearQueryService;

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
 * Integration tests for the {@link YearResource} REST controller.
 */
@SpringBootTest(classes = ElearningApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class YearResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private YearRepository yearRepository;

    @Autowired
    private YearMapper yearMapper;

    @Autowired
    private YearService yearService;

    @Autowired
    private YearQueryService yearQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restYearMockMvc;

    private Year year;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Year createEntity(EntityManager em) {
        Year year = new Year()
            .name(DEFAULT_NAME)
            .dateCreated(DEFAULT_DATE_CREATED)
            .dateUpdated(DEFAULT_DATE_UPDATED);
        return year;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Year createUpdatedEntity(EntityManager em) {
        Year year = new Year()
            .name(UPDATED_NAME)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateUpdated(UPDATED_DATE_UPDATED);
        return year;
    }

    @BeforeEach
    public void initTest() {
        year = createEntity(em);
    }

    @Test
    @Transactional
    public void createYear() throws Exception {
        int databaseSizeBeforeCreate = yearRepository.findAll().size();
        // Create the Year
        YearDTO yearDTO = yearMapper.toDto(year);
        restYearMockMvc.perform(post("/api/years")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(yearDTO)))
            .andExpect(status().isCreated());

        // Validate the Year in the database
        List<Year> yearList = yearRepository.findAll();
        assertThat(yearList).hasSize(databaseSizeBeforeCreate + 1);
        Year testYear = yearList.get(yearList.size() - 1);
        assertThat(testYear.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testYear.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testYear.getDateUpdated()).isEqualTo(DEFAULT_DATE_UPDATED);
    }

    @Test
    @Transactional
    public void createYearWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = yearRepository.findAll().size();

        // Create the Year with an existing ID
        year.setId(1L);
        YearDTO yearDTO = yearMapper.toDto(year);

        // An entity with an existing ID cannot be created, so this API call must fail
        restYearMockMvc.perform(post("/api/years")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(yearDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Year in the database
        List<Year> yearList = yearRepository.findAll();
        assertThat(yearList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = yearRepository.findAll().size();
        // set the field null
        year.setName(null);

        // Create the Year, which fails.
        YearDTO yearDTO = yearMapper.toDto(year);


        restYearMockMvc.perform(post("/api/years")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(yearDTO)))
            .andExpect(status().isBadRequest());

        List<Year> yearList = yearRepository.findAll();
        assertThat(yearList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllYears() throws Exception {
        // Initialize the database
        yearRepository.saveAndFlush(year);

        // Get all the yearList
        restYearMockMvc.perform(get("/api/years?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(year.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].dateUpdated").value(hasItem(DEFAULT_DATE_UPDATED.toString())));
    }
    
    @Test
    @Transactional
    public void getYear() throws Exception {
        // Initialize the database
        yearRepository.saveAndFlush(year);

        // Get the year
        restYearMockMvc.perform(get("/api/years/{id}", year.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(year.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.dateUpdated").value(DEFAULT_DATE_UPDATED.toString()));
    }


    @Test
    @Transactional
    public void getYearsByIdFiltering() throws Exception {
        // Initialize the database
        yearRepository.saveAndFlush(year);

        Long id = year.getId();

        defaultYearShouldBeFound("id.equals=" + id);
        defaultYearShouldNotBeFound("id.notEquals=" + id);

        defaultYearShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultYearShouldNotBeFound("id.greaterThan=" + id);

        defaultYearShouldBeFound("id.lessThanOrEqual=" + id);
        defaultYearShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllYearsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        yearRepository.saveAndFlush(year);

        // Get all the yearList where name equals to DEFAULT_NAME
        defaultYearShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the yearList where name equals to UPDATED_NAME
        defaultYearShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllYearsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        yearRepository.saveAndFlush(year);

        // Get all the yearList where name not equals to DEFAULT_NAME
        defaultYearShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the yearList where name not equals to UPDATED_NAME
        defaultYearShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllYearsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        yearRepository.saveAndFlush(year);

        // Get all the yearList where name in DEFAULT_NAME or UPDATED_NAME
        defaultYearShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the yearList where name equals to UPDATED_NAME
        defaultYearShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllYearsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        yearRepository.saveAndFlush(year);

        // Get all the yearList where name is not null
        defaultYearShouldBeFound("name.specified=true");

        // Get all the yearList where name is null
        defaultYearShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllYearsByNameContainsSomething() throws Exception {
        // Initialize the database
        yearRepository.saveAndFlush(year);

        // Get all the yearList where name contains DEFAULT_NAME
        defaultYearShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the yearList where name contains UPDATED_NAME
        defaultYearShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllYearsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        yearRepository.saveAndFlush(year);

        // Get all the yearList where name does not contain DEFAULT_NAME
        defaultYearShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the yearList where name does not contain UPDATED_NAME
        defaultYearShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllYearsByDateCreatedIsEqualToSomething() throws Exception {
        // Initialize the database
        yearRepository.saveAndFlush(year);

        // Get all the yearList where dateCreated equals to DEFAULT_DATE_CREATED
        defaultYearShouldBeFound("dateCreated.equals=" + DEFAULT_DATE_CREATED);

        // Get all the yearList where dateCreated equals to UPDATED_DATE_CREATED
        defaultYearShouldNotBeFound("dateCreated.equals=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    public void getAllYearsByDateCreatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        yearRepository.saveAndFlush(year);

        // Get all the yearList where dateCreated not equals to DEFAULT_DATE_CREATED
        defaultYearShouldNotBeFound("dateCreated.notEquals=" + DEFAULT_DATE_CREATED);

        // Get all the yearList where dateCreated not equals to UPDATED_DATE_CREATED
        defaultYearShouldBeFound("dateCreated.notEquals=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    public void getAllYearsByDateCreatedIsInShouldWork() throws Exception {
        // Initialize the database
        yearRepository.saveAndFlush(year);

        // Get all the yearList where dateCreated in DEFAULT_DATE_CREATED or UPDATED_DATE_CREATED
        defaultYearShouldBeFound("dateCreated.in=" + DEFAULT_DATE_CREATED + "," + UPDATED_DATE_CREATED);

        // Get all the yearList where dateCreated equals to UPDATED_DATE_CREATED
        defaultYearShouldNotBeFound("dateCreated.in=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    public void getAllYearsByDateCreatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        yearRepository.saveAndFlush(year);

        // Get all the yearList where dateCreated is not null
        defaultYearShouldBeFound("dateCreated.specified=true");

        // Get all the yearList where dateCreated is null
        defaultYearShouldNotBeFound("dateCreated.specified=false");
    }

    @Test
    @Transactional
    public void getAllYearsByDateUpdatedIsEqualToSomething() throws Exception {
        // Initialize the database
        yearRepository.saveAndFlush(year);

        // Get all the yearList where dateUpdated equals to DEFAULT_DATE_UPDATED
        defaultYearShouldBeFound("dateUpdated.equals=" + DEFAULT_DATE_UPDATED);

        // Get all the yearList where dateUpdated equals to UPDATED_DATE_UPDATED
        defaultYearShouldNotBeFound("dateUpdated.equals=" + UPDATED_DATE_UPDATED);
    }

    @Test
    @Transactional
    public void getAllYearsByDateUpdatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        yearRepository.saveAndFlush(year);

        // Get all the yearList where dateUpdated not equals to DEFAULT_DATE_UPDATED
        defaultYearShouldNotBeFound("dateUpdated.notEquals=" + DEFAULT_DATE_UPDATED);

        // Get all the yearList where dateUpdated not equals to UPDATED_DATE_UPDATED
        defaultYearShouldBeFound("dateUpdated.notEquals=" + UPDATED_DATE_UPDATED);
    }

    @Test
    @Transactional
    public void getAllYearsByDateUpdatedIsInShouldWork() throws Exception {
        // Initialize the database
        yearRepository.saveAndFlush(year);

        // Get all the yearList where dateUpdated in DEFAULT_DATE_UPDATED or UPDATED_DATE_UPDATED
        defaultYearShouldBeFound("dateUpdated.in=" + DEFAULT_DATE_UPDATED + "," + UPDATED_DATE_UPDATED);

        // Get all the yearList where dateUpdated equals to UPDATED_DATE_UPDATED
        defaultYearShouldNotBeFound("dateUpdated.in=" + UPDATED_DATE_UPDATED);
    }

    @Test
    @Transactional
    public void getAllYearsByDateUpdatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        yearRepository.saveAndFlush(year);

        // Get all the yearList where dateUpdated is not null
        defaultYearShouldBeFound("dateUpdated.specified=true");

        // Get all the yearList where dateUpdated is null
        defaultYearShouldNotBeFound("dateUpdated.specified=false");
    }

    @Test
    @Transactional
    public void getAllYearsByUsersIsEqualToSomething() throws Exception {
        // Initialize the database
        yearRepository.saveAndFlush(year);
        Profile users = ProfileResourceIT.createEntity(em);
        em.persist(users);
        em.flush();
        year.addUsers(users);
        yearRepository.saveAndFlush(year);
        Long usersId = users.getId();

        // Get all the yearList where users equals to usersId
        defaultYearShouldBeFound("usersId.equals=" + usersId);

        // Get all the yearList where users equals to usersId + 1
        defaultYearShouldNotBeFound("usersId.equals=" + (usersId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultYearShouldBeFound(String filter) throws Exception {
        restYearMockMvc.perform(get("/api/years?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(year.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].dateUpdated").value(hasItem(DEFAULT_DATE_UPDATED.toString())));

        // Check, that the count call also returns 1
        restYearMockMvc.perform(get("/api/years/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultYearShouldNotBeFound(String filter) throws Exception {
        restYearMockMvc.perform(get("/api/years?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restYearMockMvc.perform(get("/api/years/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingYear() throws Exception {
        // Get the year
        restYearMockMvc.perform(get("/api/years/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateYear() throws Exception {
        // Initialize the database
        yearRepository.saveAndFlush(year);

        int databaseSizeBeforeUpdate = yearRepository.findAll().size();

        // Update the year
        Year updatedYear = yearRepository.findById(year.getId()).get();
        // Disconnect from session so that the updates on updatedYear are not directly saved in db
        em.detach(updatedYear);
        updatedYear
            .name(UPDATED_NAME)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateUpdated(UPDATED_DATE_UPDATED);
        YearDTO yearDTO = yearMapper.toDto(updatedYear);

        restYearMockMvc.perform(put("/api/years")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(yearDTO)))
            .andExpect(status().isOk());

        // Validate the Year in the database
        List<Year> yearList = yearRepository.findAll();
        assertThat(yearList).hasSize(databaseSizeBeforeUpdate);
        Year testYear = yearList.get(yearList.size() - 1);
        assertThat(testYear.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testYear.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testYear.getDateUpdated()).isEqualTo(UPDATED_DATE_UPDATED);
    }

    @Test
    @Transactional
    public void updateNonExistingYear() throws Exception {
        int databaseSizeBeforeUpdate = yearRepository.findAll().size();

        // Create the Year
        YearDTO yearDTO = yearMapper.toDto(year);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restYearMockMvc.perform(put("/api/years")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(yearDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Year in the database
        List<Year> yearList = yearRepository.findAll();
        assertThat(yearList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteYear() throws Exception {
        // Initialize the database
        yearRepository.saveAndFlush(year);

        int databaseSizeBeforeDelete = yearRepository.findAll().size();

        // Delete the year
        restYearMockMvc.perform(delete("/api/years/{id}", year.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Year> yearList = yearRepository.findAll();
        assertThat(yearList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
