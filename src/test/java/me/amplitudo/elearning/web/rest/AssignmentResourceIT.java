package me.amplitudo.elearning.web.rest;

import me.amplitudo.elearning.ElearningApp;
import me.amplitudo.elearning.domain.Assignment;
import me.amplitudo.elearning.domain.AssignmentProfile;
import me.amplitudo.elearning.domain.Profile;
import me.amplitudo.elearning.domain.Course;
import me.amplitudo.elearning.repository.AssignmentRepository;
import me.amplitudo.elearning.service.AssignmentService;
import me.amplitudo.elearning.service.dto.AssignmentDTO;
import me.amplitudo.elearning.service.mapper.AssignmentMapper;
import me.amplitudo.elearning.service.dto.AssignmentCriteria;
import me.amplitudo.elearning.service.AssignmentQueryService;

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
 * Integration tests for the {@link AssignmentResource} REST controller.
 */
@SpringBootTest(classes = ElearningApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class AssignmentResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_MAX_POINTS = 1;
    private static final Integer UPDATED_MAX_POINTS = 2;
    private static final Integer SMALLER_MAX_POINTS = 1 - 1;

    private static final Instant DEFAULT_DEAD_LINE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DEAD_LINE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_FILE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_FILE_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_TYPE_ICON_PATH = "AAAAAAAAAA";
    private static final String UPDATED_FILE_TYPE_ICON_PATH = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private AssignmentMapper assignmentMapper;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private AssignmentQueryService assignmentQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAssignmentMockMvc;

    private Assignment assignment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Assignment createEntity(EntityManager em) {
        Assignment assignment = new Assignment()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .maxPoints(DEFAULT_MAX_POINTS)
            .deadLine(DEFAULT_DEAD_LINE)
            .filePath(DEFAULT_FILE_PATH)
            .fileTypeIconPath(DEFAULT_FILE_TYPE_ICON_PATH)
            .dateCreated(DEFAULT_DATE_CREATED)
            .dateUpdated(DEFAULT_DATE_UPDATED);
        return assignment;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Assignment createUpdatedEntity(EntityManager em) {
        Assignment assignment = new Assignment()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .maxPoints(UPDATED_MAX_POINTS)
            .deadLine(UPDATED_DEAD_LINE)
            .filePath(UPDATED_FILE_PATH)
            .fileTypeIconPath(UPDATED_FILE_TYPE_ICON_PATH)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateUpdated(UPDATED_DATE_UPDATED);
        return assignment;
    }

    @BeforeEach
    public void initTest() {
        assignment = createEntity(em);
    }

    @Test
    @Transactional
    public void createAssignment() throws Exception {
        int databaseSizeBeforeCreate = assignmentRepository.findAll().size();
        // Create the Assignment
        AssignmentDTO assignmentDTO = assignmentMapper.toDto(assignment);
        restAssignmentMockMvc.perform(post("/api/assignments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(assignmentDTO)))
            .andExpect(status().isCreated());

        // Validate the Assignment in the database
        List<Assignment> assignmentList = assignmentRepository.findAll();
        assertThat(assignmentList).hasSize(databaseSizeBeforeCreate + 1);
        Assignment testAssignment = assignmentList.get(assignmentList.size() - 1);
        assertThat(testAssignment.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testAssignment.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAssignment.getMaxPoints()).isEqualTo(DEFAULT_MAX_POINTS);
        assertThat(testAssignment.getDeadLine()).isEqualTo(DEFAULT_DEAD_LINE);
        assertThat(testAssignment.getFilePath()).isEqualTo(DEFAULT_FILE_PATH);
        assertThat(testAssignment.getFileTypeIconPath()).isEqualTo(DEFAULT_FILE_TYPE_ICON_PATH);
        assertThat(testAssignment.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testAssignment.getDateUpdated()).isEqualTo(DEFAULT_DATE_UPDATED);
    }

    @Test
    @Transactional
    public void createAssignmentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = assignmentRepository.findAll().size();

        // Create the Assignment with an existing ID
        assignment.setId(1L);
        AssignmentDTO assignmentDTO = assignmentMapper.toDto(assignment);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssignmentMockMvc.perform(post("/api/assignments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(assignmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Assignment in the database
        List<Assignment> assignmentList = assignmentRepository.findAll();
        assertThat(assignmentList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = assignmentRepository.findAll().size();
        // set the field null
        assignment.setTitle(null);

        // Create the Assignment, which fails.
        AssignmentDTO assignmentDTO = assignmentMapper.toDto(assignment);


        restAssignmentMockMvc.perform(post("/api/assignments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(assignmentDTO)))
            .andExpect(status().isBadRequest());

        List<Assignment> assignmentList = assignmentRepository.findAll();
        assertThat(assignmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = assignmentRepository.findAll().size();
        // set the field null
        assignment.setDescription(null);

        // Create the Assignment, which fails.
        AssignmentDTO assignmentDTO = assignmentMapper.toDto(assignment);


        restAssignmentMockMvc.perform(post("/api/assignments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(assignmentDTO)))
            .andExpect(status().isBadRequest());

        List<Assignment> assignmentList = assignmentRepository.findAll();
        assertThat(assignmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMaxPointsIsRequired() throws Exception {
        int databaseSizeBeforeTest = assignmentRepository.findAll().size();
        // set the field null
        assignment.setMaxPoints(null);

        // Create the Assignment, which fails.
        AssignmentDTO assignmentDTO = assignmentMapper.toDto(assignment);


        restAssignmentMockMvc.perform(post("/api/assignments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(assignmentDTO)))
            .andExpect(status().isBadRequest());

        List<Assignment> assignmentList = assignmentRepository.findAll();
        assertThat(assignmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDeadLineIsRequired() throws Exception {
        int databaseSizeBeforeTest = assignmentRepository.findAll().size();
        // set the field null
        assignment.setDeadLine(null);

        // Create the Assignment, which fails.
        AssignmentDTO assignmentDTO = assignmentMapper.toDto(assignment);


        restAssignmentMockMvc.perform(post("/api/assignments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(assignmentDTO)))
            .andExpect(status().isBadRequest());

        List<Assignment> assignmentList = assignmentRepository.findAll();
        assertThat(assignmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFilePathIsRequired() throws Exception {
        int databaseSizeBeforeTest = assignmentRepository.findAll().size();
        // set the field null
        assignment.setFilePath(null);

        // Create the Assignment, which fails.
        AssignmentDTO assignmentDTO = assignmentMapper.toDto(assignment);


        restAssignmentMockMvc.perform(post("/api/assignments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(assignmentDTO)))
            .andExpect(status().isBadRequest());

        List<Assignment> assignmentList = assignmentRepository.findAll();
        assertThat(assignmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFileTypeIconPathIsRequired() throws Exception {
        int databaseSizeBeforeTest = assignmentRepository.findAll().size();
        // set the field null
        assignment.setFileTypeIconPath(null);

        // Create the Assignment, which fails.
        AssignmentDTO assignmentDTO = assignmentMapper.toDto(assignment);


        restAssignmentMockMvc.perform(post("/api/assignments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(assignmentDTO)))
            .andExpect(status().isBadRequest());

        List<Assignment> assignmentList = assignmentRepository.findAll();
        assertThat(assignmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAssignments() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList
        restAssignmentMockMvc.perform(get("/api/assignments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assignment.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].maxPoints").value(hasItem(DEFAULT_MAX_POINTS)))
            .andExpect(jsonPath("$.[*].deadLine").value(hasItem(DEFAULT_DEAD_LINE.toString())))
            .andExpect(jsonPath("$.[*].filePath").value(hasItem(DEFAULT_FILE_PATH)))
            .andExpect(jsonPath("$.[*].fileTypeIconPath").value(hasItem(DEFAULT_FILE_TYPE_ICON_PATH)))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].dateUpdated").value(hasItem(DEFAULT_DATE_UPDATED.toString())));
    }
    
    @Test
    @Transactional
    public void getAssignment() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        // Get the assignment
        restAssignmentMockMvc.perform(get("/api/assignments/{id}", assignment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(assignment.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.maxPoints").value(DEFAULT_MAX_POINTS))
            .andExpect(jsonPath("$.deadLine").value(DEFAULT_DEAD_LINE.toString()))
            .andExpect(jsonPath("$.filePath").value(DEFAULT_FILE_PATH))
            .andExpect(jsonPath("$.fileTypeIconPath").value(DEFAULT_FILE_TYPE_ICON_PATH))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.dateUpdated").value(DEFAULT_DATE_UPDATED.toString()));
    }


    @Test
    @Transactional
    public void getAssignmentsByIdFiltering() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        Long id = assignment.getId();

        defaultAssignmentShouldBeFound("id.equals=" + id);
        defaultAssignmentShouldNotBeFound("id.notEquals=" + id);

        defaultAssignmentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAssignmentShouldNotBeFound("id.greaterThan=" + id);

        defaultAssignmentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAssignmentShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllAssignmentsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where title equals to DEFAULT_TITLE
        defaultAssignmentShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the assignmentList where title equals to UPDATED_TITLE
        defaultAssignmentShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllAssignmentsByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where title not equals to DEFAULT_TITLE
        defaultAssignmentShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the assignmentList where title not equals to UPDATED_TITLE
        defaultAssignmentShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllAssignmentsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultAssignmentShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the assignmentList where title equals to UPDATED_TITLE
        defaultAssignmentShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllAssignmentsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where title is not null
        defaultAssignmentShouldBeFound("title.specified=true");

        // Get all the assignmentList where title is null
        defaultAssignmentShouldNotBeFound("title.specified=false");
    }
                @Test
    @Transactional
    public void getAllAssignmentsByTitleContainsSomething() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where title contains DEFAULT_TITLE
        defaultAssignmentShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the assignmentList where title contains UPDATED_TITLE
        defaultAssignmentShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllAssignmentsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where title does not contain DEFAULT_TITLE
        defaultAssignmentShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the assignmentList where title does not contain UPDATED_TITLE
        defaultAssignmentShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }


    @Test
    @Transactional
    public void getAllAssignmentsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where description equals to DEFAULT_DESCRIPTION
        defaultAssignmentShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the assignmentList where description equals to UPDATED_DESCRIPTION
        defaultAssignmentShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllAssignmentsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where description not equals to DEFAULT_DESCRIPTION
        defaultAssignmentShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the assignmentList where description not equals to UPDATED_DESCRIPTION
        defaultAssignmentShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllAssignmentsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultAssignmentShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the assignmentList where description equals to UPDATED_DESCRIPTION
        defaultAssignmentShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllAssignmentsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where description is not null
        defaultAssignmentShouldBeFound("description.specified=true");

        // Get all the assignmentList where description is null
        defaultAssignmentShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllAssignmentsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where description contains DEFAULT_DESCRIPTION
        defaultAssignmentShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the assignmentList where description contains UPDATED_DESCRIPTION
        defaultAssignmentShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllAssignmentsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where description does not contain DEFAULT_DESCRIPTION
        defaultAssignmentShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the assignmentList where description does not contain UPDATED_DESCRIPTION
        defaultAssignmentShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllAssignmentsByMaxPointsIsEqualToSomething() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where maxPoints equals to DEFAULT_MAX_POINTS
        defaultAssignmentShouldBeFound("maxPoints.equals=" + DEFAULT_MAX_POINTS);

        // Get all the assignmentList where maxPoints equals to UPDATED_MAX_POINTS
        defaultAssignmentShouldNotBeFound("maxPoints.equals=" + UPDATED_MAX_POINTS);
    }

    @Test
    @Transactional
    public void getAllAssignmentsByMaxPointsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where maxPoints not equals to DEFAULT_MAX_POINTS
        defaultAssignmentShouldNotBeFound("maxPoints.notEquals=" + DEFAULT_MAX_POINTS);

        // Get all the assignmentList where maxPoints not equals to UPDATED_MAX_POINTS
        defaultAssignmentShouldBeFound("maxPoints.notEquals=" + UPDATED_MAX_POINTS);
    }

    @Test
    @Transactional
    public void getAllAssignmentsByMaxPointsIsInShouldWork() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where maxPoints in DEFAULT_MAX_POINTS or UPDATED_MAX_POINTS
        defaultAssignmentShouldBeFound("maxPoints.in=" + DEFAULT_MAX_POINTS + "," + UPDATED_MAX_POINTS);

        // Get all the assignmentList where maxPoints equals to UPDATED_MAX_POINTS
        defaultAssignmentShouldNotBeFound("maxPoints.in=" + UPDATED_MAX_POINTS);
    }

    @Test
    @Transactional
    public void getAllAssignmentsByMaxPointsIsNullOrNotNull() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where maxPoints is not null
        defaultAssignmentShouldBeFound("maxPoints.specified=true");

        // Get all the assignmentList where maxPoints is null
        defaultAssignmentShouldNotBeFound("maxPoints.specified=false");
    }

    @Test
    @Transactional
    public void getAllAssignmentsByMaxPointsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where maxPoints is greater than or equal to DEFAULT_MAX_POINTS
        defaultAssignmentShouldBeFound("maxPoints.greaterThanOrEqual=" + DEFAULT_MAX_POINTS);

        // Get all the assignmentList where maxPoints is greater than or equal to UPDATED_MAX_POINTS
        defaultAssignmentShouldNotBeFound("maxPoints.greaterThanOrEqual=" + UPDATED_MAX_POINTS);
    }

    @Test
    @Transactional
    public void getAllAssignmentsByMaxPointsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where maxPoints is less than or equal to DEFAULT_MAX_POINTS
        defaultAssignmentShouldBeFound("maxPoints.lessThanOrEqual=" + DEFAULT_MAX_POINTS);

        // Get all the assignmentList where maxPoints is less than or equal to SMALLER_MAX_POINTS
        defaultAssignmentShouldNotBeFound("maxPoints.lessThanOrEqual=" + SMALLER_MAX_POINTS);
    }

    @Test
    @Transactional
    public void getAllAssignmentsByMaxPointsIsLessThanSomething() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where maxPoints is less than DEFAULT_MAX_POINTS
        defaultAssignmentShouldNotBeFound("maxPoints.lessThan=" + DEFAULT_MAX_POINTS);

        // Get all the assignmentList where maxPoints is less than UPDATED_MAX_POINTS
        defaultAssignmentShouldBeFound("maxPoints.lessThan=" + UPDATED_MAX_POINTS);
    }

    @Test
    @Transactional
    public void getAllAssignmentsByMaxPointsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where maxPoints is greater than DEFAULT_MAX_POINTS
        defaultAssignmentShouldNotBeFound("maxPoints.greaterThan=" + DEFAULT_MAX_POINTS);

        // Get all the assignmentList where maxPoints is greater than SMALLER_MAX_POINTS
        defaultAssignmentShouldBeFound("maxPoints.greaterThan=" + SMALLER_MAX_POINTS);
    }


    @Test
    @Transactional
    public void getAllAssignmentsByDeadLineIsEqualToSomething() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where deadLine equals to DEFAULT_DEAD_LINE
        defaultAssignmentShouldBeFound("deadLine.equals=" + DEFAULT_DEAD_LINE);

        // Get all the assignmentList where deadLine equals to UPDATED_DEAD_LINE
        defaultAssignmentShouldNotBeFound("deadLine.equals=" + UPDATED_DEAD_LINE);
    }

    @Test
    @Transactional
    public void getAllAssignmentsByDeadLineIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where deadLine not equals to DEFAULT_DEAD_LINE
        defaultAssignmentShouldNotBeFound("deadLine.notEquals=" + DEFAULT_DEAD_LINE);

        // Get all the assignmentList where deadLine not equals to UPDATED_DEAD_LINE
        defaultAssignmentShouldBeFound("deadLine.notEquals=" + UPDATED_DEAD_LINE);
    }

    @Test
    @Transactional
    public void getAllAssignmentsByDeadLineIsInShouldWork() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where deadLine in DEFAULT_DEAD_LINE or UPDATED_DEAD_LINE
        defaultAssignmentShouldBeFound("deadLine.in=" + DEFAULT_DEAD_LINE + "," + UPDATED_DEAD_LINE);

        // Get all the assignmentList where deadLine equals to UPDATED_DEAD_LINE
        defaultAssignmentShouldNotBeFound("deadLine.in=" + UPDATED_DEAD_LINE);
    }

    @Test
    @Transactional
    public void getAllAssignmentsByDeadLineIsNullOrNotNull() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where deadLine is not null
        defaultAssignmentShouldBeFound("deadLine.specified=true");

        // Get all the assignmentList where deadLine is null
        defaultAssignmentShouldNotBeFound("deadLine.specified=false");
    }

    @Test
    @Transactional
    public void getAllAssignmentsByFilePathIsEqualToSomething() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where filePath equals to DEFAULT_FILE_PATH
        defaultAssignmentShouldBeFound("filePath.equals=" + DEFAULT_FILE_PATH);

        // Get all the assignmentList where filePath equals to UPDATED_FILE_PATH
        defaultAssignmentShouldNotBeFound("filePath.equals=" + UPDATED_FILE_PATH);
    }

    @Test
    @Transactional
    public void getAllAssignmentsByFilePathIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where filePath not equals to DEFAULT_FILE_PATH
        defaultAssignmentShouldNotBeFound("filePath.notEquals=" + DEFAULT_FILE_PATH);

        // Get all the assignmentList where filePath not equals to UPDATED_FILE_PATH
        defaultAssignmentShouldBeFound("filePath.notEquals=" + UPDATED_FILE_PATH);
    }

    @Test
    @Transactional
    public void getAllAssignmentsByFilePathIsInShouldWork() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where filePath in DEFAULT_FILE_PATH or UPDATED_FILE_PATH
        defaultAssignmentShouldBeFound("filePath.in=" + DEFAULT_FILE_PATH + "," + UPDATED_FILE_PATH);

        // Get all the assignmentList where filePath equals to UPDATED_FILE_PATH
        defaultAssignmentShouldNotBeFound("filePath.in=" + UPDATED_FILE_PATH);
    }

    @Test
    @Transactional
    public void getAllAssignmentsByFilePathIsNullOrNotNull() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where filePath is not null
        defaultAssignmentShouldBeFound("filePath.specified=true");

        // Get all the assignmentList where filePath is null
        defaultAssignmentShouldNotBeFound("filePath.specified=false");
    }
                @Test
    @Transactional
    public void getAllAssignmentsByFilePathContainsSomething() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where filePath contains DEFAULT_FILE_PATH
        defaultAssignmentShouldBeFound("filePath.contains=" + DEFAULT_FILE_PATH);

        // Get all the assignmentList where filePath contains UPDATED_FILE_PATH
        defaultAssignmentShouldNotBeFound("filePath.contains=" + UPDATED_FILE_PATH);
    }

    @Test
    @Transactional
    public void getAllAssignmentsByFilePathNotContainsSomething() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where filePath does not contain DEFAULT_FILE_PATH
        defaultAssignmentShouldNotBeFound("filePath.doesNotContain=" + DEFAULT_FILE_PATH);

        // Get all the assignmentList where filePath does not contain UPDATED_FILE_PATH
        defaultAssignmentShouldBeFound("filePath.doesNotContain=" + UPDATED_FILE_PATH);
    }


    @Test
    @Transactional
    public void getAllAssignmentsByFileTypeIconPathIsEqualToSomething() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where fileTypeIconPath equals to DEFAULT_FILE_TYPE_ICON_PATH
        defaultAssignmentShouldBeFound("fileTypeIconPath.equals=" + DEFAULT_FILE_TYPE_ICON_PATH);

        // Get all the assignmentList where fileTypeIconPath equals to UPDATED_FILE_TYPE_ICON_PATH
        defaultAssignmentShouldNotBeFound("fileTypeIconPath.equals=" + UPDATED_FILE_TYPE_ICON_PATH);
    }

    @Test
    @Transactional
    public void getAllAssignmentsByFileTypeIconPathIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where fileTypeIconPath not equals to DEFAULT_FILE_TYPE_ICON_PATH
        defaultAssignmentShouldNotBeFound("fileTypeIconPath.notEquals=" + DEFAULT_FILE_TYPE_ICON_PATH);

        // Get all the assignmentList where fileTypeIconPath not equals to UPDATED_FILE_TYPE_ICON_PATH
        defaultAssignmentShouldBeFound("fileTypeIconPath.notEquals=" + UPDATED_FILE_TYPE_ICON_PATH);
    }

    @Test
    @Transactional
    public void getAllAssignmentsByFileTypeIconPathIsInShouldWork() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where fileTypeIconPath in DEFAULT_FILE_TYPE_ICON_PATH or UPDATED_FILE_TYPE_ICON_PATH
        defaultAssignmentShouldBeFound("fileTypeIconPath.in=" + DEFAULT_FILE_TYPE_ICON_PATH + "," + UPDATED_FILE_TYPE_ICON_PATH);

        // Get all the assignmentList where fileTypeIconPath equals to UPDATED_FILE_TYPE_ICON_PATH
        defaultAssignmentShouldNotBeFound("fileTypeIconPath.in=" + UPDATED_FILE_TYPE_ICON_PATH);
    }

    @Test
    @Transactional
    public void getAllAssignmentsByFileTypeIconPathIsNullOrNotNull() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where fileTypeIconPath is not null
        defaultAssignmentShouldBeFound("fileTypeIconPath.specified=true");

        // Get all the assignmentList where fileTypeIconPath is null
        defaultAssignmentShouldNotBeFound("fileTypeIconPath.specified=false");
    }
                @Test
    @Transactional
    public void getAllAssignmentsByFileTypeIconPathContainsSomething() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where fileTypeIconPath contains DEFAULT_FILE_TYPE_ICON_PATH
        defaultAssignmentShouldBeFound("fileTypeIconPath.contains=" + DEFAULT_FILE_TYPE_ICON_PATH);

        // Get all the assignmentList where fileTypeIconPath contains UPDATED_FILE_TYPE_ICON_PATH
        defaultAssignmentShouldNotBeFound("fileTypeIconPath.contains=" + UPDATED_FILE_TYPE_ICON_PATH);
    }

    @Test
    @Transactional
    public void getAllAssignmentsByFileTypeIconPathNotContainsSomething() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where fileTypeIconPath does not contain DEFAULT_FILE_TYPE_ICON_PATH
        defaultAssignmentShouldNotBeFound("fileTypeIconPath.doesNotContain=" + DEFAULT_FILE_TYPE_ICON_PATH);

        // Get all the assignmentList where fileTypeIconPath does not contain UPDATED_FILE_TYPE_ICON_PATH
        defaultAssignmentShouldBeFound("fileTypeIconPath.doesNotContain=" + UPDATED_FILE_TYPE_ICON_PATH);
    }


    @Test
    @Transactional
    public void getAllAssignmentsByDateCreatedIsEqualToSomething() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where dateCreated equals to DEFAULT_DATE_CREATED
        defaultAssignmentShouldBeFound("dateCreated.equals=" + DEFAULT_DATE_CREATED);

        // Get all the assignmentList where dateCreated equals to UPDATED_DATE_CREATED
        defaultAssignmentShouldNotBeFound("dateCreated.equals=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    public void getAllAssignmentsByDateCreatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where dateCreated not equals to DEFAULT_DATE_CREATED
        defaultAssignmentShouldNotBeFound("dateCreated.notEquals=" + DEFAULT_DATE_CREATED);

        // Get all the assignmentList where dateCreated not equals to UPDATED_DATE_CREATED
        defaultAssignmentShouldBeFound("dateCreated.notEquals=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    public void getAllAssignmentsByDateCreatedIsInShouldWork() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where dateCreated in DEFAULT_DATE_CREATED or UPDATED_DATE_CREATED
        defaultAssignmentShouldBeFound("dateCreated.in=" + DEFAULT_DATE_CREATED + "," + UPDATED_DATE_CREATED);

        // Get all the assignmentList where dateCreated equals to UPDATED_DATE_CREATED
        defaultAssignmentShouldNotBeFound("dateCreated.in=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    public void getAllAssignmentsByDateCreatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where dateCreated is not null
        defaultAssignmentShouldBeFound("dateCreated.specified=true");

        // Get all the assignmentList where dateCreated is null
        defaultAssignmentShouldNotBeFound("dateCreated.specified=false");
    }

    @Test
    @Transactional
    public void getAllAssignmentsByDateUpdatedIsEqualToSomething() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where dateUpdated equals to DEFAULT_DATE_UPDATED
        defaultAssignmentShouldBeFound("dateUpdated.equals=" + DEFAULT_DATE_UPDATED);

        // Get all the assignmentList where dateUpdated equals to UPDATED_DATE_UPDATED
        defaultAssignmentShouldNotBeFound("dateUpdated.equals=" + UPDATED_DATE_UPDATED);
    }

    @Test
    @Transactional
    public void getAllAssignmentsByDateUpdatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where dateUpdated not equals to DEFAULT_DATE_UPDATED
        defaultAssignmentShouldNotBeFound("dateUpdated.notEquals=" + DEFAULT_DATE_UPDATED);

        // Get all the assignmentList where dateUpdated not equals to UPDATED_DATE_UPDATED
        defaultAssignmentShouldBeFound("dateUpdated.notEquals=" + UPDATED_DATE_UPDATED);
    }

    @Test
    @Transactional
    public void getAllAssignmentsByDateUpdatedIsInShouldWork() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where dateUpdated in DEFAULT_DATE_UPDATED or UPDATED_DATE_UPDATED
        defaultAssignmentShouldBeFound("dateUpdated.in=" + DEFAULT_DATE_UPDATED + "," + UPDATED_DATE_UPDATED);

        // Get all the assignmentList where dateUpdated equals to UPDATED_DATE_UPDATED
        defaultAssignmentShouldNotBeFound("dateUpdated.in=" + UPDATED_DATE_UPDATED);
    }

    @Test
    @Transactional
    public void getAllAssignmentsByDateUpdatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where dateUpdated is not null
        defaultAssignmentShouldBeFound("dateUpdated.specified=true");

        // Get all the assignmentList where dateUpdated is null
        defaultAssignmentShouldNotBeFound("dateUpdated.specified=false");
    }

    @Test
    @Transactional
    public void getAllAssignmentsByAssignmentProfilesIsEqualToSomething() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);
        AssignmentProfile assignmentProfiles = AssignmentProfileResourceIT.createEntity(em);
        em.persist(assignmentProfiles);
        em.flush();
        assignment.addAssignmentProfiles(assignmentProfiles);
        assignmentRepository.saveAndFlush(assignment);
        Long assignmentProfilesId = assignmentProfiles.getId();

        // Get all the assignmentList where assignmentProfiles equals to assignmentProfilesId
        defaultAssignmentShouldBeFound("assignmentProfilesId.equals=" + assignmentProfilesId);

        // Get all the assignmentList where assignmentProfiles equals to assignmentProfilesId + 1
        defaultAssignmentShouldNotBeFound("assignmentProfilesId.equals=" + (assignmentProfilesId + 1));
    }


    @Test
    @Transactional
    public void getAllAssignmentsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);
        Profile user = ProfileResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        assignment.setUser(user);
        assignmentRepository.saveAndFlush(assignment);
        Long userId = user.getId();

        // Get all the assignmentList where user equals to userId
        defaultAssignmentShouldBeFound("userId.equals=" + userId);

        // Get all the assignmentList where user equals to userId + 1
        defaultAssignmentShouldNotBeFound("userId.equals=" + (userId + 1));
    }


    @Test
    @Transactional
    public void getAllAssignmentsByCourseIsEqualToSomething() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);
        Course course = CourseResourceIT.createEntity(em);
        em.persist(course);
        em.flush();
        assignment.setCourse(course);
        assignmentRepository.saveAndFlush(assignment);
        Long courseId = course.getId();

        // Get all the assignmentList where course equals to courseId
        defaultAssignmentShouldBeFound("courseId.equals=" + courseId);

        // Get all the assignmentList where course equals to courseId + 1
        defaultAssignmentShouldNotBeFound("courseId.equals=" + (courseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAssignmentShouldBeFound(String filter) throws Exception {
        restAssignmentMockMvc.perform(get("/api/assignments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assignment.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].maxPoints").value(hasItem(DEFAULT_MAX_POINTS)))
            .andExpect(jsonPath("$.[*].deadLine").value(hasItem(DEFAULT_DEAD_LINE.toString())))
            .andExpect(jsonPath("$.[*].filePath").value(hasItem(DEFAULT_FILE_PATH)))
            .andExpect(jsonPath("$.[*].fileTypeIconPath").value(hasItem(DEFAULT_FILE_TYPE_ICON_PATH)))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].dateUpdated").value(hasItem(DEFAULT_DATE_UPDATED.toString())));

        // Check, that the count call also returns 1
        restAssignmentMockMvc.perform(get("/api/assignments/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAssignmentShouldNotBeFound(String filter) throws Exception {
        restAssignmentMockMvc.perform(get("/api/assignments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAssignmentMockMvc.perform(get("/api/assignments/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingAssignment() throws Exception {
        // Get the assignment
        restAssignmentMockMvc.perform(get("/api/assignments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAssignment() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        int databaseSizeBeforeUpdate = assignmentRepository.findAll().size();

        // Update the assignment
        Assignment updatedAssignment = assignmentRepository.findById(assignment.getId()).get();
        // Disconnect from session so that the updates on updatedAssignment are not directly saved in db
        em.detach(updatedAssignment);
        updatedAssignment
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .maxPoints(UPDATED_MAX_POINTS)
            .deadLine(UPDATED_DEAD_LINE)
            .filePath(UPDATED_FILE_PATH)
            .fileTypeIconPath(UPDATED_FILE_TYPE_ICON_PATH)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateUpdated(UPDATED_DATE_UPDATED);
        AssignmentDTO assignmentDTO = assignmentMapper.toDto(updatedAssignment);

        restAssignmentMockMvc.perform(put("/api/assignments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(assignmentDTO)))
            .andExpect(status().isOk());

        // Validate the Assignment in the database
        List<Assignment> assignmentList = assignmentRepository.findAll();
        assertThat(assignmentList).hasSize(databaseSizeBeforeUpdate);
        Assignment testAssignment = assignmentList.get(assignmentList.size() - 1);
        assertThat(testAssignment.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testAssignment.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAssignment.getMaxPoints()).isEqualTo(UPDATED_MAX_POINTS);
        assertThat(testAssignment.getDeadLine()).isEqualTo(UPDATED_DEAD_LINE);
        assertThat(testAssignment.getFilePath()).isEqualTo(UPDATED_FILE_PATH);
        assertThat(testAssignment.getFileTypeIconPath()).isEqualTo(UPDATED_FILE_TYPE_ICON_PATH);
        assertThat(testAssignment.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testAssignment.getDateUpdated()).isEqualTo(UPDATED_DATE_UPDATED);
    }

    @Test
    @Transactional
    public void updateNonExistingAssignment() throws Exception {
        int databaseSizeBeforeUpdate = assignmentRepository.findAll().size();

        // Create the Assignment
        AssignmentDTO assignmentDTO = assignmentMapper.toDto(assignment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssignmentMockMvc.perform(put("/api/assignments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(assignmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Assignment in the database
        List<Assignment> assignmentList = assignmentRepository.findAll();
        assertThat(assignmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAssignment() throws Exception {
        // Initialize the database
        assignmentRepository.saveAndFlush(assignment);

        int databaseSizeBeforeDelete = assignmentRepository.findAll().size();

        // Delete the assignment
        restAssignmentMockMvc.perform(delete("/api/assignments/{id}", assignment.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Assignment> assignmentList = assignmentRepository.findAll();
        assertThat(assignmentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
