package me.amplitudo.elearning.web.rest;

import me.amplitudo.elearning.ElearningApp;
import me.amplitudo.elearning.domain.Building;
import me.amplitudo.elearning.domain.Faculty;
import me.amplitudo.elearning.repository.BuildingRepository;
import me.amplitudo.elearning.service.BuildingService;
import me.amplitudo.elearning.service.dto.BuildingDTO;
import me.amplitudo.elearning.service.mapper.BuildingMapper;
import me.amplitudo.elearning.service.dto.BuildingCriteria;
import me.amplitudo.elearning.service.BuildingQueryService;

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
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link BuildingResource} REST controller.
 */
@SpringBootTest(classes = ElearningApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class BuildingResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_LATITUDE = new BigDecimal(1);
    private static final BigDecimal UPDATED_LATITUDE = new BigDecimal(2);
    private static final BigDecimal SMALLER_LATITUDE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_LONGITUDE = new BigDecimal(1);
    private static final BigDecimal UPDATED_LONGITUDE = new BigDecimal(2);
    private static final BigDecimal SMALLER_LONGITUDE = new BigDecimal(1 - 1);

    private static final Instant DEFAULT_DATE_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private BuildingMapper buildingMapper;

    @Autowired
    private BuildingService buildingService;

    @Autowired
    private BuildingQueryService buildingQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBuildingMockMvc;

    private Building building;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Building createEntity(EntityManager em) {
        Building building = new Building()
            .name(DEFAULT_NAME)
            .location(DEFAULT_LOCATION)
            .latitude(DEFAULT_LATITUDE)
            .longitude(DEFAULT_LONGITUDE)
            .dateCreated(DEFAULT_DATE_CREATED)
            .dateUpdated(DEFAULT_DATE_UPDATED);
        return building;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Building createUpdatedEntity(EntityManager em) {
        Building building = new Building()
            .name(UPDATED_NAME)
            .location(UPDATED_LOCATION)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateUpdated(UPDATED_DATE_UPDATED);
        return building;
    }

    @BeforeEach
    public void initTest() {
        building = createEntity(em);
    }

    @Test
    @Transactional
    public void createBuilding() throws Exception {
        int databaseSizeBeforeCreate = buildingRepository.findAll().size();
        // Create the Building
        BuildingDTO buildingDTO = buildingMapper.toDto(building);
        restBuildingMockMvc.perform(post("/api/buildings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(buildingDTO)))
            .andExpect(status().isCreated());

        // Validate the Building in the database
        List<Building> buildingList = buildingRepository.findAll();
        assertThat(buildingList).hasSize(databaseSizeBeforeCreate + 1);
        Building testBuilding = buildingList.get(buildingList.size() - 1);
        assertThat(testBuilding.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBuilding.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testBuilding.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testBuilding.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testBuilding.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testBuilding.getDateUpdated()).isEqualTo(DEFAULT_DATE_UPDATED);
    }

    @Test
    @Transactional
    public void createBuildingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = buildingRepository.findAll().size();

        // Create the Building with an existing ID
        building.setId(1L);
        BuildingDTO buildingDTO = buildingMapper.toDto(building);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBuildingMockMvc.perform(post("/api/buildings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(buildingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Building in the database
        List<Building> buildingList = buildingRepository.findAll();
        assertThat(buildingList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = buildingRepository.findAll().size();
        // set the field null
        building.setName(null);

        // Create the Building, which fails.
        BuildingDTO buildingDTO = buildingMapper.toDto(building);


        restBuildingMockMvc.perform(post("/api/buildings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(buildingDTO)))
            .andExpect(status().isBadRequest());

        List<Building> buildingList = buildingRepository.findAll();
        assertThat(buildingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLocationIsRequired() throws Exception {
        int databaseSizeBeforeTest = buildingRepository.findAll().size();
        // set the field null
        building.setLocation(null);

        // Create the Building, which fails.
        BuildingDTO buildingDTO = buildingMapper.toDto(building);


        restBuildingMockMvc.perform(post("/api/buildings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(buildingDTO)))
            .andExpect(status().isBadRequest());

        List<Building> buildingList = buildingRepository.findAll();
        assertThat(buildingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLatitudeIsRequired() throws Exception {
        int databaseSizeBeforeTest = buildingRepository.findAll().size();
        // set the field null
        building.setLatitude(null);

        // Create the Building, which fails.
        BuildingDTO buildingDTO = buildingMapper.toDto(building);


        restBuildingMockMvc.perform(post("/api/buildings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(buildingDTO)))
            .andExpect(status().isBadRequest());

        List<Building> buildingList = buildingRepository.findAll();
        assertThat(buildingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLongitudeIsRequired() throws Exception {
        int databaseSizeBeforeTest = buildingRepository.findAll().size();
        // set the field null
        building.setLongitude(null);

        // Create the Building, which fails.
        BuildingDTO buildingDTO = buildingMapper.toDto(building);


        restBuildingMockMvc.perform(post("/api/buildings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(buildingDTO)))
            .andExpect(status().isBadRequest());

        List<Building> buildingList = buildingRepository.findAll();
        assertThat(buildingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBuildings() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList
        restBuildingMockMvc.perform(get("/api/buildings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(building.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.intValue())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.intValue())))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].dateUpdated").value(hasItem(DEFAULT_DATE_UPDATED.toString())));
    }
    
    @Test
    @Transactional
    public void getBuilding() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get the building
        restBuildingMockMvc.perform(get("/api/buildings/{id}", building.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(building.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.intValue()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.intValue()))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.dateUpdated").value(DEFAULT_DATE_UPDATED.toString()));
    }


    @Test
    @Transactional
    public void getBuildingsByIdFiltering() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        Long id = building.getId();

        defaultBuildingShouldBeFound("id.equals=" + id);
        defaultBuildingShouldNotBeFound("id.notEquals=" + id);

        defaultBuildingShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBuildingShouldNotBeFound("id.greaterThan=" + id);

        defaultBuildingShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBuildingShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllBuildingsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where name equals to DEFAULT_NAME
        defaultBuildingShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the buildingList where name equals to UPDATED_NAME
        defaultBuildingShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllBuildingsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where name not equals to DEFAULT_NAME
        defaultBuildingShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the buildingList where name not equals to UPDATED_NAME
        defaultBuildingShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllBuildingsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where name in DEFAULT_NAME or UPDATED_NAME
        defaultBuildingShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the buildingList where name equals to UPDATED_NAME
        defaultBuildingShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllBuildingsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where name is not null
        defaultBuildingShouldBeFound("name.specified=true");

        // Get all the buildingList where name is null
        defaultBuildingShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllBuildingsByNameContainsSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where name contains DEFAULT_NAME
        defaultBuildingShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the buildingList where name contains UPDATED_NAME
        defaultBuildingShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllBuildingsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where name does not contain DEFAULT_NAME
        defaultBuildingShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the buildingList where name does not contain UPDATED_NAME
        defaultBuildingShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllBuildingsByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where location equals to DEFAULT_LOCATION
        defaultBuildingShouldBeFound("location.equals=" + DEFAULT_LOCATION);

        // Get all the buildingList where location equals to UPDATED_LOCATION
        defaultBuildingShouldNotBeFound("location.equals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void getAllBuildingsByLocationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where location not equals to DEFAULT_LOCATION
        defaultBuildingShouldNotBeFound("location.notEquals=" + DEFAULT_LOCATION);

        // Get all the buildingList where location not equals to UPDATED_LOCATION
        defaultBuildingShouldBeFound("location.notEquals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void getAllBuildingsByLocationIsInShouldWork() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where location in DEFAULT_LOCATION or UPDATED_LOCATION
        defaultBuildingShouldBeFound("location.in=" + DEFAULT_LOCATION + "," + UPDATED_LOCATION);

        // Get all the buildingList where location equals to UPDATED_LOCATION
        defaultBuildingShouldNotBeFound("location.in=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void getAllBuildingsByLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where location is not null
        defaultBuildingShouldBeFound("location.specified=true");

        // Get all the buildingList where location is null
        defaultBuildingShouldNotBeFound("location.specified=false");
    }
                @Test
    @Transactional
    public void getAllBuildingsByLocationContainsSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where location contains DEFAULT_LOCATION
        defaultBuildingShouldBeFound("location.contains=" + DEFAULT_LOCATION);

        // Get all the buildingList where location contains UPDATED_LOCATION
        defaultBuildingShouldNotBeFound("location.contains=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void getAllBuildingsByLocationNotContainsSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where location does not contain DEFAULT_LOCATION
        defaultBuildingShouldNotBeFound("location.doesNotContain=" + DEFAULT_LOCATION);

        // Get all the buildingList where location does not contain UPDATED_LOCATION
        defaultBuildingShouldBeFound("location.doesNotContain=" + UPDATED_LOCATION);
    }


    @Test
    @Transactional
    public void getAllBuildingsByLatitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where latitude equals to DEFAULT_LATITUDE
        defaultBuildingShouldBeFound("latitude.equals=" + DEFAULT_LATITUDE);

        // Get all the buildingList where latitude equals to UPDATED_LATITUDE
        defaultBuildingShouldNotBeFound("latitude.equals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllBuildingsByLatitudeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where latitude not equals to DEFAULT_LATITUDE
        defaultBuildingShouldNotBeFound("latitude.notEquals=" + DEFAULT_LATITUDE);

        // Get all the buildingList where latitude not equals to UPDATED_LATITUDE
        defaultBuildingShouldBeFound("latitude.notEquals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllBuildingsByLatitudeIsInShouldWork() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where latitude in DEFAULT_LATITUDE or UPDATED_LATITUDE
        defaultBuildingShouldBeFound("latitude.in=" + DEFAULT_LATITUDE + "," + UPDATED_LATITUDE);

        // Get all the buildingList where latitude equals to UPDATED_LATITUDE
        defaultBuildingShouldNotBeFound("latitude.in=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllBuildingsByLatitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where latitude is not null
        defaultBuildingShouldBeFound("latitude.specified=true");

        // Get all the buildingList where latitude is null
        defaultBuildingShouldNotBeFound("latitude.specified=false");
    }

    @Test
    @Transactional
    public void getAllBuildingsByLatitudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where latitude is greater than or equal to DEFAULT_LATITUDE
        defaultBuildingShouldBeFound("latitude.greaterThanOrEqual=" + DEFAULT_LATITUDE);

        // Get all the buildingList where latitude is greater than or equal to UPDATED_LATITUDE
        defaultBuildingShouldNotBeFound("latitude.greaterThanOrEqual=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllBuildingsByLatitudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where latitude is less than or equal to DEFAULT_LATITUDE
        defaultBuildingShouldBeFound("latitude.lessThanOrEqual=" + DEFAULT_LATITUDE);

        // Get all the buildingList where latitude is less than or equal to SMALLER_LATITUDE
        defaultBuildingShouldNotBeFound("latitude.lessThanOrEqual=" + SMALLER_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllBuildingsByLatitudeIsLessThanSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where latitude is less than DEFAULT_LATITUDE
        defaultBuildingShouldNotBeFound("latitude.lessThan=" + DEFAULT_LATITUDE);

        // Get all the buildingList where latitude is less than UPDATED_LATITUDE
        defaultBuildingShouldBeFound("latitude.lessThan=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllBuildingsByLatitudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where latitude is greater than DEFAULT_LATITUDE
        defaultBuildingShouldNotBeFound("latitude.greaterThan=" + DEFAULT_LATITUDE);

        // Get all the buildingList where latitude is greater than SMALLER_LATITUDE
        defaultBuildingShouldBeFound("latitude.greaterThan=" + SMALLER_LATITUDE);
    }


    @Test
    @Transactional
    public void getAllBuildingsByLongitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where longitude equals to DEFAULT_LONGITUDE
        defaultBuildingShouldBeFound("longitude.equals=" + DEFAULT_LONGITUDE);

        // Get all the buildingList where longitude equals to UPDATED_LONGITUDE
        defaultBuildingShouldNotBeFound("longitude.equals=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    public void getAllBuildingsByLongitudeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where longitude not equals to DEFAULT_LONGITUDE
        defaultBuildingShouldNotBeFound("longitude.notEquals=" + DEFAULT_LONGITUDE);

        // Get all the buildingList where longitude not equals to UPDATED_LONGITUDE
        defaultBuildingShouldBeFound("longitude.notEquals=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    public void getAllBuildingsByLongitudeIsInShouldWork() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where longitude in DEFAULT_LONGITUDE or UPDATED_LONGITUDE
        defaultBuildingShouldBeFound("longitude.in=" + DEFAULT_LONGITUDE + "," + UPDATED_LONGITUDE);

        // Get all the buildingList where longitude equals to UPDATED_LONGITUDE
        defaultBuildingShouldNotBeFound("longitude.in=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    public void getAllBuildingsByLongitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where longitude is not null
        defaultBuildingShouldBeFound("longitude.specified=true");

        // Get all the buildingList where longitude is null
        defaultBuildingShouldNotBeFound("longitude.specified=false");
    }

    @Test
    @Transactional
    public void getAllBuildingsByLongitudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where longitude is greater than or equal to DEFAULT_LONGITUDE
        defaultBuildingShouldBeFound("longitude.greaterThanOrEqual=" + DEFAULT_LONGITUDE);

        // Get all the buildingList where longitude is greater than or equal to UPDATED_LONGITUDE
        defaultBuildingShouldNotBeFound("longitude.greaterThanOrEqual=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    public void getAllBuildingsByLongitudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where longitude is less than or equal to DEFAULT_LONGITUDE
        defaultBuildingShouldBeFound("longitude.lessThanOrEqual=" + DEFAULT_LONGITUDE);

        // Get all the buildingList where longitude is less than or equal to SMALLER_LONGITUDE
        defaultBuildingShouldNotBeFound("longitude.lessThanOrEqual=" + SMALLER_LONGITUDE);
    }

    @Test
    @Transactional
    public void getAllBuildingsByLongitudeIsLessThanSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where longitude is less than DEFAULT_LONGITUDE
        defaultBuildingShouldNotBeFound("longitude.lessThan=" + DEFAULT_LONGITUDE);

        // Get all the buildingList where longitude is less than UPDATED_LONGITUDE
        defaultBuildingShouldBeFound("longitude.lessThan=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    public void getAllBuildingsByLongitudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where longitude is greater than DEFAULT_LONGITUDE
        defaultBuildingShouldNotBeFound("longitude.greaterThan=" + DEFAULT_LONGITUDE);

        // Get all the buildingList where longitude is greater than SMALLER_LONGITUDE
        defaultBuildingShouldBeFound("longitude.greaterThan=" + SMALLER_LONGITUDE);
    }


    @Test
    @Transactional
    public void getAllBuildingsByDateCreatedIsEqualToSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where dateCreated equals to DEFAULT_DATE_CREATED
        defaultBuildingShouldBeFound("dateCreated.equals=" + DEFAULT_DATE_CREATED);

        // Get all the buildingList where dateCreated equals to UPDATED_DATE_CREATED
        defaultBuildingShouldNotBeFound("dateCreated.equals=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    public void getAllBuildingsByDateCreatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where dateCreated not equals to DEFAULT_DATE_CREATED
        defaultBuildingShouldNotBeFound("dateCreated.notEquals=" + DEFAULT_DATE_CREATED);

        // Get all the buildingList where dateCreated not equals to UPDATED_DATE_CREATED
        defaultBuildingShouldBeFound("dateCreated.notEquals=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    public void getAllBuildingsByDateCreatedIsInShouldWork() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where dateCreated in DEFAULT_DATE_CREATED or UPDATED_DATE_CREATED
        defaultBuildingShouldBeFound("dateCreated.in=" + DEFAULT_DATE_CREATED + "," + UPDATED_DATE_CREATED);

        // Get all the buildingList where dateCreated equals to UPDATED_DATE_CREATED
        defaultBuildingShouldNotBeFound("dateCreated.in=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    public void getAllBuildingsByDateCreatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where dateCreated is not null
        defaultBuildingShouldBeFound("dateCreated.specified=true");

        // Get all the buildingList where dateCreated is null
        defaultBuildingShouldNotBeFound("dateCreated.specified=false");
    }

    @Test
    @Transactional
    public void getAllBuildingsByDateUpdatedIsEqualToSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where dateUpdated equals to DEFAULT_DATE_UPDATED
        defaultBuildingShouldBeFound("dateUpdated.equals=" + DEFAULT_DATE_UPDATED);

        // Get all the buildingList where dateUpdated equals to UPDATED_DATE_UPDATED
        defaultBuildingShouldNotBeFound("dateUpdated.equals=" + UPDATED_DATE_UPDATED);
    }

    @Test
    @Transactional
    public void getAllBuildingsByDateUpdatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where dateUpdated not equals to DEFAULT_DATE_UPDATED
        defaultBuildingShouldNotBeFound("dateUpdated.notEquals=" + DEFAULT_DATE_UPDATED);

        // Get all the buildingList where dateUpdated not equals to UPDATED_DATE_UPDATED
        defaultBuildingShouldBeFound("dateUpdated.notEquals=" + UPDATED_DATE_UPDATED);
    }

    @Test
    @Transactional
    public void getAllBuildingsByDateUpdatedIsInShouldWork() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where dateUpdated in DEFAULT_DATE_UPDATED or UPDATED_DATE_UPDATED
        defaultBuildingShouldBeFound("dateUpdated.in=" + DEFAULT_DATE_UPDATED + "," + UPDATED_DATE_UPDATED);

        // Get all the buildingList where dateUpdated equals to UPDATED_DATE_UPDATED
        defaultBuildingShouldNotBeFound("dateUpdated.in=" + UPDATED_DATE_UPDATED);
    }

    @Test
    @Transactional
    public void getAllBuildingsByDateUpdatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList where dateUpdated is not null
        defaultBuildingShouldBeFound("dateUpdated.specified=true");

        // Get all the buildingList where dateUpdated is null
        defaultBuildingShouldNotBeFound("dateUpdated.specified=false");
    }

    @Test
    @Transactional
    public void getAllBuildingsByFacultiesIsEqualToSomething() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);
        Faculty faculties = FacultyResourceIT.createEntity(em);
        em.persist(faculties);
        em.flush();
        building.addFaculties(faculties);
        buildingRepository.saveAndFlush(building);
        Long facultiesId = faculties.getId();

        // Get all the buildingList where faculties equals to facultiesId
        defaultBuildingShouldBeFound("facultiesId.equals=" + facultiesId);

        // Get all the buildingList where faculties equals to facultiesId + 1
        defaultBuildingShouldNotBeFound("facultiesId.equals=" + (facultiesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBuildingShouldBeFound(String filter) throws Exception {
        restBuildingMockMvc.perform(get("/api/buildings?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(building.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.intValue())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.intValue())))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].dateUpdated").value(hasItem(DEFAULT_DATE_UPDATED.toString())));

        // Check, that the count call also returns 1
        restBuildingMockMvc.perform(get("/api/buildings/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBuildingShouldNotBeFound(String filter) throws Exception {
        restBuildingMockMvc.perform(get("/api/buildings?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBuildingMockMvc.perform(get("/api/buildings/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingBuilding() throws Exception {
        // Get the building
        restBuildingMockMvc.perform(get("/api/buildings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBuilding() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        int databaseSizeBeforeUpdate = buildingRepository.findAll().size();

        // Update the building
        Building updatedBuilding = buildingRepository.findById(building.getId()).get();
        // Disconnect from session so that the updates on updatedBuilding are not directly saved in db
        em.detach(updatedBuilding);
        updatedBuilding
            .name(UPDATED_NAME)
            .location(UPDATED_LOCATION)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateUpdated(UPDATED_DATE_UPDATED);
        BuildingDTO buildingDTO = buildingMapper.toDto(updatedBuilding);

        restBuildingMockMvc.perform(put("/api/buildings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(buildingDTO)))
            .andExpect(status().isOk());

        // Validate the Building in the database
        List<Building> buildingList = buildingRepository.findAll();
        assertThat(buildingList).hasSize(databaseSizeBeforeUpdate);
        Building testBuilding = buildingList.get(buildingList.size() - 1);
        assertThat(testBuilding.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBuilding.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testBuilding.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testBuilding.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testBuilding.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testBuilding.getDateUpdated()).isEqualTo(UPDATED_DATE_UPDATED);
    }

    @Test
    @Transactional
    public void updateNonExistingBuilding() throws Exception {
        int databaseSizeBeforeUpdate = buildingRepository.findAll().size();

        // Create the Building
        BuildingDTO buildingDTO = buildingMapper.toDto(building);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBuildingMockMvc.perform(put("/api/buildings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(buildingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Building in the database
        List<Building> buildingList = buildingRepository.findAll();
        assertThat(buildingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBuilding() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        int databaseSizeBeforeDelete = buildingRepository.findAll().size();

        // Delete the building
        restBuildingMockMvc.perform(delete("/api/buildings/{id}", building.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Building> buildingList = buildingRepository.findAll();
        assertThat(buildingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
