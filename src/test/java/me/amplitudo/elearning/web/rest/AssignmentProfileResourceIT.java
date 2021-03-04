package me.amplitudo.elearning.web.rest;

import me.amplitudo.elearning.ElearningApp;
import me.amplitudo.elearning.domain.AssignmentProfile;
import me.amplitudo.elearning.domain.Assignment;
import me.amplitudo.elearning.domain.Profile;
import me.amplitudo.elearning.repository.AssignmentProfileRepository;
import me.amplitudo.elearning.service.AssignmentProfileService;
import me.amplitudo.elearning.service.dto.AssignmentProfileDTO;
import me.amplitudo.elearning.service.mapper.AssignmentProfileMapper;
import me.amplitudo.elearning.service.dto.AssignmentProfileCriteria;
import me.amplitudo.elearning.service.AssignmentProfileQueryService;

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
 * Integration tests for the {@link AssignmentProfileResource} REST controller.
 */
@SpringBootTest(classes = ElearningApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class AssignmentProfileResourceIT {

    private static final Integer DEFAULT_POINTS = 1;
    private static final Integer UPDATED_POINTS = 2;
    private static final Integer SMALLER_POINTS = 1 - 1;

    private static final String DEFAULT_FILE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_FILE_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_TYPE_ICON_PATH = "AAAAAAAAAA";
    private static final String UPDATED_FILE_TYPE_ICON_PATH = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private AssignmentProfileRepository assignmentProfileRepository;

    @Autowired
    private AssignmentProfileMapper assignmentProfileMapper;

    @Autowired
    private AssignmentProfileService assignmentProfileService;

    @Autowired
    private AssignmentProfileQueryService assignmentProfileQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAssignmentProfileMockMvc;

    private AssignmentProfile assignmentProfile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssignmentProfile createEntity(EntityManager em) {
        AssignmentProfile assignmentProfile = new AssignmentProfile()
            .points(DEFAULT_POINTS)
            .filePath(DEFAULT_FILE_PATH)
            .fileTypeIconPath(DEFAULT_FILE_TYPE_ICON_PATH)
            .dateCreated(DEFAULT_DATE_CREATED)
            .dateUpdated(DEFAULT_DATE_UPDATED);
        return assignmentProfile;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssignmentProfile createUpdatedEntity(EntityManager em) {
        AssignmentProfile assignmentProfile = new AssignmentProfile()
            .points(UPDATED_POINTS)
            .filePath(UPDATED_FILE_PATH)
            .fileTypeIconPath(UPDATED_FILE_TYPE_ICON_PATH)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateUpdated(UPDATED_DATE_UPDATED);
        return assignmentProfile;
    }

    @BeforeEach
    public void initTest() {
        assignmentProfile = createEntity(em);
    }

    @Test
    @Transactional
    public void createAssignmentProfile() throws Exception {
        int databaseSizeBeforeCreate = assignmentProfileRepository.findAll().size();
        // Create the AssignmentProfile
        AssignmentProfileDTO assignmentProfileDTO = assignmentProfileMapper.toDto(assignmentProfile);
        restAssignmentProfileMockMvc.perform(post("/api/assignment-profiles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(assignmentProfileDTO)))
            .andExpect(status().isCreated());

        // Validate the AssignmentProfile in the database
        List<AssignmentProfile> assignmentProfileList = assignmentProfileRepository.findAll();
        assertThat(assignmentProfileList).hasSize(databaseSizeBeforeCreate + 1);
        AssignmentProfile testAssignmentProfile = assignmentProfileList.get(assignmentProfileList.size() - 1);
        assertThat(testAssignmentProfile.getPoints()).isEqualTo(DEFAULT_POINTS);
        assertThat(testAssignmentProfile.getFilePath()).isEqualTo(DEFAULT_FILE_PATH);
        assertThat(testAssignmentProfile.getFileTypeIconPath()).isEqualTo(DEFAULT_FILE_TYPE_ICON_PATH);
        assertThat(testAssignmentProfile.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testAssignmentProfile.getDateUpdated()).isEqualTo(DEFAULT_DATE_UPDATED);
    }

    @Test
    @Transactional
    public void createAssignmentProfileWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = assignmentProfileRepository.findAll().size();

        // Create the AssignmentProfile with an existing ID
        assignmentProfile.setId(1L);
        AssignmentProfileDTO assignmentProfileDTO = assignmentProfileMapper.toDto(assignmentProfile);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssignmentProfileMockMvc.perform(post("/api/assignment-profiles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(assignmentProfileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AssignmentProfile in the database
        List<AssignmentProfile> assignmentProfileList = assignmentProfileRepository.findAll();
        assertThat(assignmentProfileList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllAssignmentProfiles() throws Exception {
        // Initialize the database
        assignmentProfileRepository.saveAndFlush(assignmentProfile);

        // Get all the assignmentProfileList
        restAssignmentProfileMockMvc.perform(get("/api/assignment-profiles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assignmentProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS)))
            .andExpect(jsonPath("$.[*].filePath").value(hasItem(DEFAULT_FILE_PATH)))
            .andExpect(jsonPath("$.[*].fileTypeIconPath").value(hasItem(DEFAULT_FILE_TYPE_ICON_PATH)))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].dateUpdated").value(hasItem(DEFAULT_DATE_UPDATED.toString())));
    }
    
    @Test
    @Transactional
    public void getAssignmentProfile() throws Exception {
        // Initialize the database
        assignmentProfileRepository.saveAndFlush(assignmentProfile);

        // Get the assignmentProfile
        restAssignmentProfileMockMvc.perform(get("/api/assignment-profiles/{id}", assignmentProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(assignmentProfile.getId().intValue()))
            .andExpect(jsonPath("$.points").value(DEFAULT_POINTS))
            .andExpect(jsonPath("$.filePath").value(DEFAULT_FILE_PATH))
            .andExpect(jsonPath("$.fileTypeIconPath").value(DEFAULT_FILE_TYPE_ICON_PATH))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.dateUpdated").value(DEFAULT_DATE_UPDATED.toString()));
    }


    @Test
    @Transactional
    public void getAssignmentProfilesByIdFiltering() throws Exception {
        // Initialize the database
        assignmentProfileRepository.saveAndFlush(assignmentProfile);

        Long id = assignmentProfile.getId();

        defaultAssignmentProfileShouldBeFound("id.equals=" + id);
        defaultAssignmentProfileShouldNotBeFound("id.notEquals=" + id);

        defaultAssignmentProfileShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAssignmentProfileShouldNotBeFound("id.greaterThan=" + id);

        defaultAssignmentProfileShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAssignmentProfileShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllAssignmentProfilesByPointsIsEqualToSomething() throws Exception {
        // Initialize the database
        assignmentProfileRepository.saveAndFlush(assignmentProfile);

        // Get all the assignmentProfileList where points equals to DEFAULT_POINTS
        defaultAssignmentProfileShouldBeFound("points.equals=" + DEFAULT_POINTS);

        // Get all the assignmentProfileList where points equals to UPDATED_POINTS
        defaultAssignmentProfileShouldNotBeFound("points.equals=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    public void getAllAssignmentProfilesByPointsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assignmentProfileRepository.saveAndFlush(assignmentProfile);

        // Get all the assignmentProfileList where points not equals to DEFAULT_POINTS
        defaultAssignmentProfileShouldNotBeFound("points.notEquals=" + DEFAULT_POINTS);

        // Get all the assignmentProfileList where points not equals to UPDATED_POINTS
        defaultAssignmentProfileShouldBeFound("points.notEquals=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    public void getAllAssignmentProfilesByPointsIsInShouldWork() throws Exception {
        // Initialize the database
        assignmentProfileRepository.saveAndFlush(assignmentProfile);

        // Get all the assignmentProfileList where points in DEFAULT_POINTS or UPDATED_POINTS
        defaultAssignmentProfileShouldBeFound("points.in=" + DEFAULT_POINTS + "," + UPDATED_POINTS);

        // Get all the assignmentProfileList where points equals to UPDATED_POINTS
        defaultAssignmentProfileShouldNotBeFound("points.in=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    public void getAllAssignmentProfilesByPointsIsNullOrNotNull() throws Exception {
        // Initialize the database
        assignmentProfileRepository.saveAndFlush(assignmentProfile);

        // Get all the assignmentProfileList where points is not null
        defaultAssignmentProfileShouldBeFound("points.specified=true");

        // Get all the assignmentProfileList where points is null
        defaultAssignmentProfileShouldNotBeFound("points.specified=false");
    }

    @Test
    @Transactional
    public void getAllAssignmentProfilesByPointsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assignmentProfileRepository.saveAndFlush(assignmentProfile);

        // Get all the assignmentProfileList where points is greater than or equal to DEFAULT_POINTS
        defaultAssignmentProfileShouldBeFound("points.greaterThanOrEqual=" + DEFAULT_POINTS);

        // Get all the assignmentProfileList where points is greater than or equal to UPDATED_POINTS
        defaultAssignmentProfileShouldNotBeFound("points.greaterThanOrEqual=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    public void getAllAssignmentProfilesByPointsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assignmentProfileRepository.saveAndFlush(assignmentProfile);

        // Get all the assignmentProfileList where points is less than or equal to DEFAULT_POINTS
        defaultAssignmentProfileShouldBeFound("points.lessThanOrEqual=" + DEFAULT_POINTS);

        // Get all the assignmentProfileList where points is less than or equal to SMALLER_POINTS
        defaultAssignmentProfileShouldNotBeFound("points.lessThanOrEqual=" + SMALLER_POINTS);
    }

    @Test
    @Transactional
    public void getAllAssignmentProfilesByPointsIsLessThanSomething() throws Exception {
        // Initialize the database
        assignmentProfileRepository.saveAndFlush(assignmentProfile);

        // Get all the assignmentProfileList where points is less than DEFAULT_POINTS
        defaultAssignmentProfileShouldNotBeFound("points.lessThan=" + DEFAULT_POINTS);

        // Get all the assignmentProfileList where points is less than UPDATED_POINTS
        defaultAssignmentProfileShouldBeFound("points.lessThan=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    public void getAllAssignmentProfilesByPointsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assignmentProfileRepository.saveAndFlush(assignmentProfile);

        // Get all the assignmentProfileList where points is greater than DEFAULT_POINTS
        defaultAssignmentProfileShouldNotBeFound("points.greaterThan=" + DEFAULT_POINTS);

        // Get all the assignmentProfileList where points is greater than SMALLER_POINTS
        defaultAssignmentProfileShouldBeFound("points.greaterThan=" + SMALLER_POINTS);
    }


    @Test
    @Transactional
    public void getAllAssignmentProfilesByFilePathIsEqualToSomething() throws Exception {
        // Initialize the database
        assignmentProfileRepository.saveAndFlush(assignmentProfile);

        // Get all the assignmentProfileList where filePath equals to DEFAULT_FILE_PATH
        defaultAssignmentProfileShouldBeFound("filePath.equals=" + DEFAULT_FILE_PATH);

        // Get all the assignmentProfileList where filePath equals to UPDATED_FILE_PATH
        defaultAssignmentProfileShouldNotBeFound("filePath.equals=" + UPDATED_FILE_PATH);
    }

    @Test
    @Transactional
    public void getAllAssignmentProfilesByFilePathIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assignmentProfileRepository.saveAndFlush(assignmentProfile);

        // Get all the assignmentProfileList where filePath not equals to DEFAULT_FILE_PATH
        defaultAssignmentProfileShouldNotBeFound("filePath.notEquals=" + DEFAULT_FILE_PATH);

        // Get all the assignmentProfileList where filePath not equals to UPDATED_FILE_PATH
        defaultAssignmentProfileShouldBeFound("filePath.notEquals=" + UPDATED_FILE_PATH);
    }

    @Test
    @Transactional
    public void getAllAssignmentProfilesByFilePathIsInShouldWork() throws Exception {
        // Initialize the database
        assignmentProfileRepository.saveAndFlush(assignmentProfile);

        // Get all the assignmentProfileList where filePath in DEFAULT_FILE_PATH or UPDATED_FILE_PATH
        defaultAssignmentProfileShouldBeFound("filePath.in=" + DEFAULT_FILE_PATH + "," + UPDATED_FILE_PATH);

        // Get all the assignmentProfileList where filePath equals to UPDATED_FILE_PATH
        defaultAssignmentProfileShouldNotBeFound("filePath.in=" + UPDATED_FILE_PATH);
    }

    @Test
    @Transactional
    public void getAllAssignmentProfilesByFilePathIsNullOrNotNull() throws Exception {
        // Initialize the database
        assignmentProfileRepository.saveAndFlush(assignmentProfile);

        // Get all the assignmentProfileList where filePath is not null
        defaultAssignmentProfileShouldBeFound("filePath.specified=true");

        // Get all the assignmentProfileList where filePath is null
        defaultAssignmentProfileShouldNotBeFound("filePath.specified=false");
    }
                @Test
    @Transactional
    public void getAllAssignmentProfilesByFilePathContainsSomething() throws Exception {
        // Initialize the database
        assignmentProfileRepository.saveAndFlush(assignmentProfile);

        // Get all the assignmentProfileList where filePath contains DEFAULT_FILE_PATH
        defaultAssignmentProfileShouldBeFound("filePath.contains=" + DEFAULT_FILE_PATH);

        // Get all the assignmentProfileList where filePath contains UPDATED_FILE_PATH
        defaultAssignmentProfileShouldNotBeFound("filePath.contains=" + UPDATED_FILE_PATH);
    }

    @Test
    @Transactional
    public void getAllAssignmentProfilesByFilePathNotContainsSomething() throws Exception {
        // Initialize the database
        assignmentProfileRepository.saveAndFlush(assignmentProfile);

        // Get all the assignmentProfileList where filePath does not contain DEFAULT_FILE_PATH
        defaultAssignmentProfileShouldNotBeFound("filePath.doesNotContain=" + DEFAULT_FILE_PATH);

        // Get all the assignmentProfileList where filePath does not contain UPDATED_FILE_PATH
        defaultAssignmentProfileShouldBeFound("filePath.doesNotContain=" + UPDATED_FILE_PATH);
    }


    @Test
    @Transactional
    public void getAllAssignmentProfilesByFileTypeIconPathIsEqualToSomething() throws Exception {
        // Initialize the database
        assignmentProfileRepository.saveAndFlush(assignmentProfile);

        // Get all the assignmentProfileList where fileTypeIconPath equals to DEFAULT_FILE_TYPE_ICON_PATH
        defaultAssignmentProfileShouldBeFound("fileTypeIconPath.equals=" + DEFAULT_FILE_TYPE_ICON_PATH);

        // Get all the assignmentProfileList where fileTypeIconPath equals to UPDATED_FILE_TYPE_ICON_PATH
        defaultAssignmentProfileShouldNotBeFound("fileTypeIconPath.equals=" + UPDATED_FILE_TYPE_ICON_PATH);
    }

    @Test
    @Transactional
    public void getAllAssignmentProfilesByFileTypeIconPathIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assignmentProfileRepository.saveAndFlush(assignmentProfile);

        // Get all the assignmentProfileList where fileTypeIconPath not equals to DEFAULT_FILE_TYPE_ICON_PATH
        defaultAssignmentProfileShouldNotBeFound("fileTypeIconPath.notEquals=" + DEFAULT_FILE_TYPE_ICON_PATH);

        // Get all the assignmentProfileList where fileTypeIconPath not equals to UPDATED_FILE_TYPE_ICON_PATH
        defaultAssignmentProfileShouldBeFound("fileTypeIconPath.notEquals=" + UPDATED_FILE_TYPE_ICON_PATH);
    }

    @Test
    @Transactional
    public void getAllAssignmentProfilesByFileTypeIconPathIsInShouldWork() throws Exception {
        // Initialize the database
        assignmentProfileRepository.saveAndFlush(assignmentProfile);

        // Get all the assignmentProfileList where fileTypeIconPath in DEFAULT_FILE_TYPE_ICON_PATH or UPDATED_FILE_TYPE_ICON_PATH
        defaultAssignmentProfileShouldBeFound("fileTypeIconPath.in=" + DEFAULT_FILE_TYPE_ICON_PATH + "," + UPDATED_FILE_TYPE_ICON_PATH);

        // Get all the assignmentProfileList where fileTypeIconPath equals to UPDATED_FILE_TYPE_ICON_PATH
        defaultAssignmentProfileShouldNotBeFound("fileTypeIconPath.in=" + UPDATED_FILE_TYPE_ICON_PATH);
    }

    @Test
    @Transactional
    public void getAllAssignmentProfilesByFileTypeIconPathIsNullOrNotNull() throws Exception {
        // Initialize the database
        assignmentProfileRepository.saveAndFlush(assignmentProfile);

        // Get all the assignmentProfileList where fileTypeIconPath is not null
        defaultAssignmentProfileShouldBeFound("fileTypeIconPath.specified=true");

        // Get all the assignmentProfileList where fileTypeIconPath is null
        defaultAssignmentProfileShouldNotBeFound("fileTypeIconPath.specified=false");
    }
                @Test
    @Transactional
    public void getAllAssignmentProfilesByFileTypeIconPathContainsSomething() throws Exception {
        // Initialize the database
        assignmentProfileRepository.saveAndFlush(assignmentProfile);

        // Get all the assignmentProfileList where fileTypeIconPath contains DEFAULT_FILE_TYPE_ICON_PATH
        defaultAssignmentProfileShouldBeFound("fileTypeIconPath.contains=" + DEFAULT_FILE_TYPE_ICON_PATH);

        // Get all the assignmentProfileList where fileTypeIconPath contains UPDATED_FILE_TYPE_ICON_PATH
        defaultAssignmentProfileShouldNotBeFound("fileTypeIconPath.contains=" + UPDATED_FILE_TYPE_ICON_PATH);
    }

    @Test
    @Transactional
    public void getAllAssignmentProfilesByFileTypeIconPathNotContainsSomething() throws Exception {
        // Initialize the database
        assignmentProfileRepository.saveAndFlush(assignmentProfile);

        // Get all the assignmentProfileList where fileTypeIconPath does not contain DEFAULT_FILE_TYPE_ICON_PATH
        defaultAssignmentProfileShouldNotBeFound("fileTypeIconPath.doesNotContain=" + DEFAULT_FILE_TYPE_ICON_PATH);

        // Get all the assignmentProfileList where fileTypeIconPath does not contain UPDATED_FILE_TYPE_ICON_PATH
        defaultAssignmentProfileShouldBeFound("fileTypeIconPath.doesNotContain=" + UPDATED_FILE_TYPE_ICON_PATH);
    }


    @Test
    @Transactional
    public void getAllAssignmentProfilesByDateCreatedIsEqualToSomething() throws Exception {
        // Initialize the database
        assignmentProfileRepository.saveAndFlush(assignmentProfile);

        // Get all the assignmentProfileList where dateCreated equals to DEFAULT_DATE_CREATED
        defaultAssignmentProfileShouldBeFound("dateCreated.equals=" + DEFAULT_DATE_CREATED);

        // Get all the assignmentProfileList where dateCreated equals to UPDATED_DATE_CREATED
        defaultAssignmentProfileShouldNotBeFound("dateCreated.equals=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    public void getAllAssignmentProfilesByDateCreatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assignmentProfileRepository.saveAndFlush(assignmentProfile);

        // Get all the assignmentProfileList where dateCreated not equals to DEFAULT_DATE_CREATED
        defaultAssignmentProfileShouldNotBeFound("dateCreated.notEquals=" + DEFAULT_DATE_CREATED);

        // Get all the assignmentProfileList where dateCreated not equals to UPDATED_DATE_CREATED
        defaultAssignmentProfileShouldBeFound("dateCreated.notEquals=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    public void getAllAssignmentProfilesByDateCreatedIsInShouldWork() throws Exception {
        // Initialize the database
        assignmentProfileRepository.saveAndFlush(assignmentProfile);

        // Get all the assignmentProfileList where dateCreated in DEFAULT_DATE_CREATED or UPDATED_DATE_CREATED
        defaultAssignmentProfileShouldBeFound("dateCreated.in=" + DEFAULT_DATE_CREATED + "," + UPDATED_DATE_CREATED);

        // Get all the assignmentProfileList where dateCreated equals to UPDATED_DATE_CREATED
        defaultAssignmentProfileShouldNotBeFound("dateCreated.in=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    public void getAllAssignmentProfilesByDateCreatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        assignmentProfileRepository.saveAndFlush(assignmentProfile);

        // Get all the assignmentProfileList where dateCreated is not null
        defaultAssignmentProfileShouldBeFound("dateCreated.specified=true");

        // Get all the assignmentProfileList where dateCreated is null
        defaultAssignmentProfileShouldNotBeFound("dateCreated.specified=false");
    }

    @Test
    @Transactional
    public void getAllAssignmentProfilesByDateUpdatedIsEqualToSomething() throws Exception {
        // Initialize the database
        assignmentProfileRepository.saveAndFlush(assignmentProfile);

        // Get all the assignmentProfileList where dateUpdated equals to DEFAULT_DATE_UPDATED
        defaultAssignmentProfileShouldBeFound("dateUpdated.equals=" + DEFAULT_DATE_UPDATED);

        // Get all the assignmentProfileList where dateUpdated equals to UPDATED_DATE_UPDATED
        defaultAssignmentProfileShouldNotBeFound("dateUpdated.equals=" + UPDATED_DATE_UPDATED);
    }

    @Test
    @Transactional
    public void getAllAssignmentProfilesByDateUpdatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assignmentProfileRepository.saveAndFlush(assignmentProfile);

        // Get all the assignmentProfileList where dateUpdated not equals to DEFAULT_DATE_UPDATED
        defaultAssignmentProfileShouldNotBeFound("dateUpdated.notEquals=" + DEFAULT_DATE_UPDATED);

        // Get all the assignmentProfileList where dateUpdated not equals to UPDATED_DATE_UPDATED
        defaultAssignmentProfileShouldBeFound("dateUpdated.notEquals=" + UPDATED_DATE_UPDATED);
    }

    @Test
    @Transactional
    public void getAllAssignmentProfilesByDateUpdatedIsInShouldWork() throws Exception {
        // Initialize the database
        assignmentProfileRepository.saveAndFlush(assignmentProfile);

        // Get all the assignmentProfileList where dateUpdated in DEFAULT_DATE_UPDATED or UPDATED_DATE_UPDATED
        defaultAssignmentProfileShouldBeFound("dateUpdated.in=" + DEFAULT_DATE_UPDATED + "," + UPDATED_DATE_UPDATED);

        // Get all the assignmentProfileList where dateUpdated equals to UPDATED_DATE_UPDATED
        defaultAssignmentProfileShouldNotBeFound("dateUpdated.in=" + UPDATED_DATE_UPDATED);
    }

    @Test
    @Transactional
    public void getAllAssignmentProfilesByDateUpdatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        assignmentProfileRepository.saveAndFlush(assignmentProfile);

        // Get all the assignmentProfileList where dateUpdated is not null
        defaultAssignmentProfileShouldBeFound("dateUpdated.specified=true");

        // Get all the assignmentProfileList where dateUpdated is null
        defaultAssignmentProfileShouldNotBeFound("dateUpdated.specified=false");
    }

    @Test
    @Transactional
    public void getAllAssignmentProfilesByAssignmentIsEqualToSomething() throws Exception {
        // Initialize the database
        assignmentProfileRepository.saveAndFlush(assignmentProfile);
        Assignment assignment = AssignmentResourceIT.createEntity(em);
        em.persist(assignment);
        em.flush();
        assignmentProfile.setAssignment(assignment);
        assignmentProfileRepository.saveAndFlush(assignmentProfile);
        Long assignmentId = assignment.getId();

        // Get all the assignmentProfileList where assignment equals to assignmentId
        defaultAssignmentProfileShouldBeFound("assignmentId.equals=" + assignmentId);

        // Get all the assignmentProfileList where assignment equals to assignmentId + 1
        defaultAssignmentProfileShouldNotBeFound("assignmentId.equals=" + (assignmentId + 1));
    }


    @Test
    @Transactional
    public void getAllAssignmentProfilesByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        assignmentProfileRepository.saveAndFlush(assignmentProfile);
        Profile user = ProfileResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        assignmentProfile.setUser(user);
        assignmentProfileRepository.saveAndFlush(assignmentProfile);
        Long userId = user.getId();

        // Get all the assignmentProfileList where user equals to userId
        defaultAssignmentProfileShouldBeFound("userId.equals=" + userId);

        // Get all the assignmentProfileList where user equals to userId + 1
        defaultAssignmentProfileShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAssignmentProfileShouldBeFound(String filter) throws Exception {
        restAssignmentProfileMockMvc.perform(get("/api/assignment-profiles?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assignmentProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS)))
            .andExpect(jsonPath("$.[*].filePath").value(hasItem(DEFAULT_FILE_PATH)))
            .andExpect(jsonPath("$.[*].fileTypeIconPath").value(hasItem(DEFAULT_FILE_TYPE_ICON_PATH)))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].dateUpdated").value(hasItem(DEFAULT_DATE_UPDATED.toString())));

        // Check, that the count call also returns 1
        restAssignmentProfileMockMvc.perform(get("/api/assignment-profiles/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAssignmentProfileShouldNotBeFound(String filter) throws Exception {
        restAssignmentProfileMockMvc.perform(get("/api/assignment-profiles?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAssignmentProfileMockMvc.perform(get("/api/assignment-profiles/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingAssignmentProfile() throws Exception {
        // Get the assignmentProfile
        restAssignmentProfileMockMvc.perform(get("/api/assignment-profiles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAssignmentProfile() throws Exception {
        // Initialize the database
        assignmentProfileRepository.saveAndFlush(assignmentProfile);

        int databaseSizeBeforeUpdate = assignmentProfileRepository.findAll().size();

        // Update the assignmentProfile
        AssignmentProfile updatedAssignmentProfile = assignmentProfileRepository.findById(assignmentProfile.getId()).get();
        // Disconnect from session so that the updates on updatedAssignmentProfile are not directly saved in db
        em.detach(updatedAssignmentProfile);
        updatedAssignmentProfile
            .points(UPDATED_POINTS)
            .filePath(UPDATED_FILE_PATH)
            .fileTypeIconPath(UPDATED_FILE_TYPE_ICON_PATH)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateUpdated(UPDATED_DATE_UPDATED);
        AssignmentProfileDTO assignmentProfileDTO = assignmentProfileMapper.toDto(updatedAssignmentProfile);

        restAssignmentProfileMockMvc.perform(put("/api/assignment-profiles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(assignmentProfileDTO)))
            .andExpect(status().isOk());

        // Validate the AssignmentProfile in the database
        List<AssignmentProfile> assignmentProfileList = assignmentProfileRepository.findAll();
        assertThat(assignmentProfileList).hasSize(databaseSizeBeforeUpdate);
        AssignmentProfile testAssignmentProfile = assignmentProfileList.get(assignmentProfileList.size() - 1);
        assertThat(testAssignmentProfile.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testAssignmentProfile.getFilePath()).isEqualTo(UPDATED_FILE_PATH);
        assertThat(testAssignmentProfile.getFileTypeIconPath()).isEqualTo(UPDATED_FILE_TYPE_ICON_PATH);
        assertThat(testAssignmentProfile.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testAssignmentProfile.getDateUpdated()).isEqualTo(UPDATED_DATE_UPDATED);
    }

    @Test
    @Transactional
    public void updateNonExistingAssignmentProfile() throws Exception {
        int databaseSizeBeforeUpdate = assignmentProfileRepository.findAll().size();

        // Create the AssignmentProfile
        AssignmentProfileDTO assignmentProfileDTO = assignmentProfileMapper.toDto(assignmentProfile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssignmentProfileMockMvc.perform(put("/api/assignment-profiles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(assignmentProfileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AssignmentProfile in the database
        List<AssignmentProfile> assignmentProfileList = assignmentProfileRepository.findAll();
        assertThat(assignmentProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAssignmentProfile() throws Exception {
        // Initialize the database
        assignmentProfileRepository.saveAndFlush(assignmentProfile);

        int databaseSizeBeforeDelete = assignmentProfileRepository.findAll().size();

        // Delete the assignmentProfile
        restAssignmentProfileMockMvc.perform(delete("/api/assignment-profiles/{id}", assignmentProfile.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AssignmentProfile> assignmentProfileList = assignmentProfileRepository.findAll();
        assertThat(assignmentProfileList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
