package me.amplitudo.elearning.web.rest;

import me.amplitudo.elearning.ElearningApp;
import me.amplitudo.elearning.domain.Lecture;
import me.amplitudo.elearning.domain.Course;
import me.amplitudo.elearning.domain.Profile;
import me.amplitudo.elearning.repository.LectureRepository;
import me.amplitudo.elearning.service.LectureService;
import me.amplitudo.elearning.service.dto.LectureDTO;
import me.amplitudo.elearning.service.mapper.LectureMapper;
import me.amplitudo.elearning.service.dto.LectureCriteria;
import me.amplitudo.elearning.service.LectureQueryService;

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
 * Integration tests for the {@link LectureResource} REST controller.
 */
@SpringBootTest(classes = ElearningApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class LectureResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_MATERIAL_FILE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_MATERIAL_FILE_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_TYPE_ICON_PATH = "AAAAAAAAAA";
    private static final String UPDATED_FILE_TYPE_ICON_PATH = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private LectureMapper lectureMapper;

    @Autowired
    private LectureService lectureService;

    @Autowired
    private LectureQueryService lectureQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLectureMockMvc;

    private Lecture lecture;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lecture createEntity(EntityManager em) {
        Lecture lecture = new Lecture()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .materialFilePath(DEFAULT_MATERIAL_FILE_PATH)
            .fileTypeIconPath(DEFAULT_FILE_TYPE_ICON_PATH)
            .dateCreated(DEFAULT_DATE_CREATED)
            .dateUpdated(DEFAULT_DATE_UPDATED);
        return lecture;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lecture createUpdatedEntity(EntityManager em) {
        Lecture lecture = new Lecture()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .materialFilePath(UPDATED_MATERIAL_FILE_PATH)
            .fileTypeIconPath(UPDATED_FILE_TYPE_ICON_PATH)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateUpdated(UPDATED_DATE_UPDATED);
        return lecture;
    }

    @BeforeEach
    public void initTest() {
        lecture = createEntity(em);
    }

    @Test
    @Transactional
    public void createLecture() throws Exception {
        int databaseSizeBeforeCreate = lectureRepository.findAll().size();
        // Create the Lecture
        LectureDTO lectureDTO = lectureMapper.toDto(lecture);
        restLectureMockMvc.perform(post("/api/lectures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lectureDTO)))
            .andExpect(status().isCreated());

        // Validate the Lecture in the database
        List<Lecture> lectureList = lectureRepository.findAll();
        assertThat(lectureList).hasSize(databaseSizeBeforeCreate + 1);
        Lecture testLecture = lectureList.get(lectureList.size() - 1);
        assertThat(testLecture.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testLecture.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testLecture.getMaterialFilePath()).isEqualTo(DEFAULT_MATERIAL_FILE_PATH);
        assertThat(testLecture.getFileTypeIconPath()).isEqualTo(DEFAULT_FILE_TYPE_ICON_PATH);
        assertThat(testLecture.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testLecture.getDateUpdated()).isEqualTo(DEFAULT_DATE_UPDATED);
    }

    @Test
    @Transactional
    public void createLectureWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = lectureRepository.findAll().size();

        // Create the Lecture with an existing ID
        lecture.setId(1L);
        LectureDTO lectureDTO = lectureMapper.toDto(lecture);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLectureMockMvc.perform(post("/api/lectures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lectureDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Lecture in the database
        List<Lecture> lectureList = lectureRepository.findAll();
        assertThat(lectureList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = lectureRepository.findAll().size();
        // set the field null
        lecture.setTitle(null);

        // Create the Lecture, which fails.
        LectureDTO lectureDTO = lectureMapper.toDto(lecture);


        restLectureMockMvc.perform(post("/api/lectures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lectureDTO)))
            .andExpect(status().isBadRequest());

        List<Lecture> lectureList = lectureRepository.findAll();
        assertThat(lectureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = lectureRepository.findAll().size();
        // set the field null
        lecture.setDescription(null);

        // Create the Lecture, which fails.
        LectureDTO lectureDTO = lectureMapper.toDto(lecture);


        restLectureMockMvc.perform(post("/api/lectures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lectureDTO)))
            .andExpect(status().isBadRequest());

        List<Lecture> lectureList = lectureRepository.findAll();
        assertThat(lectureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMaterialFilePathIsRequired() throws Exception {
        int databaseSizeBeforeTest = lectureRepository.findAll().size();
        // set the field null
        lecture.setMaterialFilePath(null);

        // Create the Lecture, which fails.
        LectureDTO lectureDTO = lectureMapper.toDto(lecture);


        restLectureMockMvc.perform(post("/api/lectures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lectureDTO)))
            .andExpect(status().isBadRequest());

        List<Lecture> lectureList = lectureRepository.findAll();
        assertThat(lectureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFileTypeIconPathIsRequired() throws Exception {
        int databaseSizeBeforeTest = lectureRepository.findAll().size();
        // set the field null
        lecture.setFileTypeIconPath(null);

        // Create the Lecture, which fails.
        LectureDTO lectureDTO = lectureMapper.toDto(lecture);


        restLectureMockMvc.perform(post("/api/lectures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lectureDTO)))
            .andExpect(status().isBadRequest());

        List<Lecture> lectureList = lectureRepository.findAll();
        assertThat(lectureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLectures() throws Exception {
        // Initialize the database
        lectureRepository.saveAndFlush(lecture);

        // Get all the lectureList
        restLectureMockMvc.perform(get("/api/lectures?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lecture.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].materialFilePath").value(hasItem(DEFAULT_MATERIAL_FILE_PATH)))
            .andExpect(jsonPath("$.[*].fileTypeIconPath").value(hasItem(DEFAULT_FILE_TYPE_ICON_PATH)))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].dateUpdated").value(hasItem(DEFAULT_DATE_UPDATED.toString())));
    }
    
    @Test
    @Transactional
    public void getLecture() throws Exception {
        // Initialize the database
        lectureRepository.saveAndFlush(lecture);

        // Get the lecture
        restLectureMockMvc.perform(get("/api/lectures/{id}", lecture.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lecture.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.materialFilePath").value(DEFAULT_MATERIAL_FILE_PATH))
            .andExpect(jsonPath("$.fileTypeIconPath").value(DEFAULT_FILE_TYPE_ICON_PATH))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.dateUpdated").value(DEFAULT_DATE_UPDATED.toString()));
    }


    @Test
    @Transactional
    public void getLecturesByIdFiltering() throws Exception {
        // Initialize the database
        lectureRepository.saveAndFlush(lecture);

        Long id = lecture.getId();

        defaultLectureShouldBeFound("id.equals=" + id);
        defaultLectureShouldNotBeFound("id.notEquals=" + id);

        defaultLectureShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLectureShouldNotBeFound("id.greaterThan=" + id);

        defaultLectureShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLectureShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllLecturesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        lectureRepository.saveAndFlush(lecture);

        // Get all the lectureList where title equals to DEFAULT_TITLE
        defaultLectureShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the lectureList where title equals to UPDATED_TITLE
        defaultLectureShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllLecturesByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        lectureRepository.saveAndFlush(lecture);

        // Get all the lectureList where title not equals to DEFAULT_TITLE
        defaultLectureShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the lectureList where title not equals to UPDATED_TITLE
        defaultLectureShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllLecturesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        lectureRepository.saveAndFlush(lecture);

        // Get all the lectureList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultLectureShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the lectureList where title equals to UPDATED_TITLE
        defaultLectureShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllLecturesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        lectureRepository.saveAndFlush(lecture);

        // Get all the lectureList where title is not null
        defaultLectureShouldBeFound("title.specified=true");

        // Get all the lectureList where title is null
        defaultLectureShouldNotBeFound("title.specified=false");
    }
                @Test
    @Transactional
    public void getAllLecturesByTitleContainsSomething() throws Exception {
        // Initialize the database
        lectureRepository.saveAndFlush(lecture);

        // Get all the lectureList where title contains DEFAULT_TITLE
        defaultLectureShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the lectureList where title contains UPDATED_TITLE
        defaultLectureShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllLecturesByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        lectureRepository.saveAndFlush(lecture);

        // Get all the lectureList where title does not contain DEFAULT_TITLE
        defaultLectureShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the lectureList where title does not contain UPDATED_TITLE
        defaultLectureShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }


    @Test
    @Transactional
    public void getAllLecturesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        lectureRepository.saveAndFlush(lecture);

        // Get all the lectureList where description equals to DEFAULT_DESCRIPTION
        defaultLectureShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the lectureList where description equals to UPDATED_DESCRIPTION
        defaultLectureShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllLecturesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        lectureRepository.saveAndFlush(lecture);

        // Get all the lectureList where description not equals to DEFAULT_DESCRIPTION
        defaultLectureShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the lectureList where description not equals to UPDATED_DESCRIPTION
        defaultLectureShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllLecturesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        lectureRepository.saveAndFlush(lecture);

        // Get all the lectureList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultLectureShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the lectureList where description equals to UPDATED_DESCRIPTION
        defaultLectureShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllLecturesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        lectureRepository.saveAndFlush(lecture);

        // Get all the lectureList where description is not null
        defaultLectureShouldBeFound("description.specified=true");

        // Get all the lectureList where description is null
        defaultLectureShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllLecturesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        lectureRepository.saveAndFlush(lecture);

        // Get all the lectureList where description contains DEFAULT_DESCRIPTION
        defaultLectureShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the lectureList where description contains UPDATED_DESCRIPTION
        defaultLectureShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllLecturesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        lectureRepository.saveAndFlush(lecture);

        // Get all the lectureList where description does not contain DEFAULT_DESCRIPTION
        defaultLectureShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the lectureList where description does not contain UPDATED_DESCRIPTION
        defaultLectureShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllLecturesByMaterialFilePathIsEqualToSomething() throws Exception {
        // Initialize the database
        lectureRepository.saveAndFlush(lecture);

        // Get all the lectureList where materialFilePath equals to DEFAULT_MATERIAL_FILE_PATH
        defaultLectureShouldBeFound("materialFilePath.equals=" + DEFAULT_MATERIAL_FILE_PATH);

        // Get all the lectureList where materialFilePath equals to UPDATED_MATERIAL_FILE_PATH
        defaultLectureShouldNotBeFound("materialFilePath.equals=" + UPDATED_MATERIAL_FILE_PATH);
    }

    @Test
    @Transactional
    public void getAllLecturesByMaterialFilePathIsNotEqualToSomething() throws Exception {
        // Initialize the database
        lectureRepository.saveAndFlush(lecture);

        // Get all the lectureList where materialFilePath not equals to DEFAULT_MATERIAL_FILE_PATH
        defaultLectureShouldNotBeFound("materialFilePath.notEquals=" + DEFAULT_MATERIAL_FILE_PATH);

        // Get all the lectureList where materialFilePath not equals to UPDATED_MATERIAL_FILE_PATH
        defaultLectureShouldBeFound("materialFilePath.notEquals=" + UPDATED_MATERIAL_FILE_PATH);
    }

    @Test
    @Transactional
    public void getAllLecturesByMaterialFilePathIsInShouldWork() throws Exception {
        // Initialize the database
        lectureRepository.saveAndFlush(lecture);

        // Get all the lectureList where materialFilePath in DEFAULT_MATERIAL_FILE_PATH or UPDATED_MATERIAL_FILE_PATH
        defaultLectureShouldBeFound("materialFilePath.in=" + DEFAULT_MATERIAL_FILE_PATH + "," + UPDATED_MATERIAL_FILE_PATH);

        // Get all the lectureList where materialFilePath equals to UPDATED_MATERIAL_FILE_PATH
        defaultLectureShouldNotBeFound("materialFilePath.in=" + UPDATED_MATERIAL_FILE_PATH);
    }

    @Test
    @Transactional
    public void getAllLecturesByMaterialFilePathIsNullOrNotNull() throws Exception {
        // Initialize the database
        lectureRepository.saveAndFlush(lecture);

        // Get all the lectureList where materialFilePath is not null
        defaultLectureShouldBeFound("materialFilePath.specified=true");

        // Get all the lectureList where materialFilePath is null
        defaultLectureShouldNotBeFound("materialFilePath.specified=false");
    }
                @Test
    @Transactional
    public void getAllLecturesByMaterialFilePathContainsSomething() throws Exception {
        // Initialize the database
        lectureRepository.saveAndFlush(lecture);

        // Get all the lectureList where materialFilePath contains DEFAULT_MATERIAL_FILE_PATH
        defaultLectureShouldBeFound("materialFilePath.contains=" + DEFAULT_MATERIAL_FILE_PATH);

        // Get all the lectureList where materialFilePath contains UPDATED_MATERIAL_FILE_PATH
        defaultLectureShouldNotBeFound("materialFilePath.contains=" + UPDATED_MATERIAL_FILE_PATH);
    }

    @Test
    @Transactional
    public void getAllLecturesByMaterialFilePathNotContainsSomething() throws Exception {
        // Initialize the database
        lectureRepository.saveAndFlush(lecture);

        // Get all the lectureList where materialFilePath does not contain DEFAULT_MATERIAL_FILE_PATH
        defaultLectureShouldNotBeFound("materialFilePath.doesNotContain=" + DEFAULT_MATERIAL_FILE_PATH);

        // Get all the lectureList where materialFilePath does not contain UPDATED_MATERIAL_FILE_PATH
        defaultLectureShouldBeFound("materialFilePath.doesNotContain=" + UPDATED_MATERIAL_FILE_PATH);
    }


    @Test
    @Transactional
    public void getAllLecturesByFileTypeIconPathIsEqualToSomething() throws Exception {
        // Initialize the database
        lectureRepository.saveAndFlush(lecture);

        // Get all the lectureList where fileTypeIconPath equals to DEFAULT_FILE_TYPE_ICON_PATH
        defaultLectureShouldBeFound("fileTypeIconPath.equals=" + DEFAULT_FILE_TYPE_ICON_PATH);

        // Get all the lectureList where fileTypeIconPath equals to UPDATED_FILE_TYPE_ICON_PATH
        defaultLectureShouldNotBeFound("fileTypeIconPath.equals=" + UPDATED_FILE_TYPE_ICON_PATH);
    }

    @Test
    @Transactional
    public void getAllLecturesByFileTypeIconPathIsNotEqualToSomething() throws Exception {
        // Initialize the database
        lectureRepository.saveAndFlush(lecture);

        // Get all the lectureList where fileTypeIconPath not equals to DEFAULT_FILE_TYPE_ICON_PATH
        defaultLectureShouldNotBeFound("fileTypeIconPath.notEquals=" + DEFAULT_FILE_TYPE_ICON_PATH);

        // Get all the lectureList where fileTypeIconPath not equals to UPDATED_FILE_TYPE_ICON_PATH
        defaultLectureShouldBeFound("fileTypeIconPath.notEquals=" + UPDATED_FILE_TYPE_ICON_PATH);
    }

    @Test
    @Transactional
    public void getAllLecturesByFileTypeIconPathIsInShouldWork() throws Exception {
        // Initialize the database
        lectureRepository.saveAndFlush(lecture);

        // Get all the lectureList where fileTypeIconPath in DEFAULT_FILE_TYPE_ICON_PATH or UPDATED_FILE_TYPE_ICON_PATH
        defaultLectureShouldBeFound("fileTypeIconPath.in=" + DEFAULT_FILE_TYPE_ICON_PATH + "," + UPDATED_FILE_TYPE_ICON_PATH);

        // Get all the lectureList where fileTypeIconPath equals to UPDATED_FILE_TYPE_ICON_PATH
        defaultLectureShouldNotBeFound("fileTypeIconPath.in=" + UPDATED_FILE_TYPE_ICON_PATH);
    }

    @Test
    @Transactional
    public void getAllLecturesByFileTypeIconPathIsNullOrNotNull() throws Exception {
        // Initialize the database
        lectureRepository.saveAndFlush(lecture);

        // Get all the lectureList where fileTypeIconPath is not null
        defaultLectureShouldBeFound("fileTypeIconPath.specified=true");

        // Get all the lectureList where fileTypeIconPath is null
        defaultLectureShouldNotBeFound("fileTypeIconPath.specified=false");
    }
                @Test
    @Transactional
    public void getAllLecturesByFileTypeIconPathContainsSomething() throws Exception {
        // Initialize the database
        lectureRepository.saveAndFlush(lecture);

        // Get all the lectureList where fileTypeIconPath contains DEFAULT_FILE_TYPE_ICON_PATH
        defaultLectureShouldBeFound("fileTypeIconPath.contains=" + DEFAULT_FILE_TYPE_ICON_PATH);

        // Get all the lectureList where fileTypeIconPath contains UPDATED_FILE_TYPE_ICON_PATH
        defaultLectureShouldNotBeFound("fileTypeIconPath.contains=" + UPDATED_FILE_TYPE_ICON_PATH);
    }

    @Test
    @Transactional
    public void getAllLecturesByFileTypeIconPathNotContainsSomething() throws Exception {
        // Initialize the database
        lectureRepository.saveAndFlush(lecture);

        // Get all the lectureList where fileTypeIconPath does not contain DEFAULT_FILE_TYPE_ICON_PATH
        defaultLectureShouldNotBeFound("fileTypeIconPath.doesNotContain=" + DEFAULT_FILE_TYPE_ICON_PATH);

        // Get all the lectureList where fileTypeIconPath does not contain UPDATED_FILE_TYPE_ICON_PATH
        defaultLectureShouldBeFound("fileTypeIconPath.doesNotContain=" + UPDATED_FILE_TYPE_ICON_PATH);
    }


    @Test
    @Transactional
    public void getAllLecturesByDateCreatedIsEqualToSomething() throws Exception {
        // Initialize the database
        lectureRepository.saveAndFlush(lecture);

        // Get all the lectureList where dateCreated equals to DEFAULT_DATE_CREATED
        defaultLectureShouldBeFound("dateCreated.equals=" + DEFAULT_DATE_CREATED);

        // Get all the lectureList where dateCreated equals to UPDATED_DATE_CREATED
        defaultLectureShouldNotBeFound("dateCreated.equals=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    public void getAllLecturesByDateCreatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        lectureRepository.saveAndFlush(lecture);

        // Get all the lectureList where dateCreated not equals to DEFAULT_DATE_CREATED
        defaultLectureShouldNotBeFound("dateCreated.notEquals=" + DEFAULT_DATE_CREATED);

        // Get all the lectureList where dateCreated not equals to UPDATED_DATE_CREATED
        defaultLectureShouldBeFound("dateCreated.notEquals=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    public void getAllLecturesByDateCreatedIsInShouldWork() throws Exception {
        // Initialize the database
        lectureRepository.saveAndFlush(lecture);

        // Get all the lectureList where dateCreated in DEFAULT_DATE_CREATED or UPDATED_DATE_CREATED
        defaultLectureShouldBeFound("dateCreated.in=" + DEFAULT_DATE_CREATED + "," + UPDATED_DATE_CREATED);

        // Get all the lectureList where dateCreated equals to UPDATED_DATE_CREATED
        defaultLectureShouldNotBeFound("dateCreated.in=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    public void getAllLecturesByDateCreatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        lectureRepository.saveAndFlush(lecture);

        // Get all the lectureList where dateCreated is not null
        defaultLectureShouldBeFound("dateCreated.specified=true");

        // Get all the lectureList where dateCreated is null
        defaultLectureShouldNotBeFound("dateCreated.specified=false");
    }

    @Test
    @Transactional
    public void getAllLecturesByDateUpdatedIsEqualToSomething() throws Exception {
        // Initialize the database
        lectureRepository.saveAndFlush(lecture);

        // Get all the lectureList where dateUpdated equals to DEFAULT_DATE_UPDATED
        defaultLectureShouldBeFound("dateUpdated.equals=" + DEFAULT_DATE_UPDATED);

        // Get all the lectureList where dateUpdated equals to UPDATED_DATE_UPDATED
        defaultLectureShouldNotBeFound("dateUpdated.equals=" + UPDATED_DATE_UPDATED);
    }

    @Test
    @Transactional
    public void getAllLecturesByDateUpdatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        lectureRepository.saveAndFlush(lecture);

        // Get all the lectureList where dateUpdated not equals to DEFAULT_DATE_UPDATED
        defaultLectureShouldNotBeFound("dateUpdated.notEquals=" + DEFAULT_DATE_UPDATED);

        // Get all the lectureList where dateUpdated not equals to UPDATED_DATE_UPDATED
        defaultLectureShouldBeFound("dateUpdated.notEquals=" + UPDATED_DATE_UPDATED);
    }

    @Test
    @Transactional
    public void getAllLecturesByDateUpdatedIsInShouldWork() throws Exception {
        // Initialize the database
        lectureRepository.saveAndFlush(lecture);

        // Get all the lectureList where dateUpdated in DEFAULT_DATE_UPDATED or UPDATED_DATE_UPDATED
        defaultLectureShouldBeFound("dateUpdated.in=" + DEFAULT_DATE_UPDATED + "," + UPDATED_DATE_UPDATED);

        // Get all the lectureList where dateUpdated equals to UPDATED_DATE_UPDATED
        defaultLectureShouldNotBeFound("dateUpdated.in=" + UPDATED_DATE_UPDATED);
    }

    @Test
    @Transactional
    public void getAllLecturesByDateUpdatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        lectureRepository.saveAndFlush(lecture);

        // Get all the lectureList where dateUpdated is not null
        defaultLectureShouldBeFound("dateUpdated.specified=true");

        // Get all the lectureList where dateUpdated is null
        defaultLectureShouldNotBeFound("dateUpdated.specified=false");
    }

    @Test
    @Transactional
    public void getAllLecturesByCourseIsEqualToSomething() throws Exception {
        // Initialize the database
        lectureRepository.saveAndFlush(lecture);
        Course course = CourseResourceIT.createEntity(em);
        em.persist(course);
        em.flush();
        lecture.addCourse(course);
        lectureRepository.saveAndFlush(lecture);
        Long courseId = course.getId();

        // Get all the lectureList where course equals to courseId
        defaultLectureShouldBeFound("courseId.equals=" + courseId);

        // Get all the lectureList where course equals to courseId + 1
        defaultLectureShouldNotBeFound("courseId.equals=" + (courseId + 1));
    }


    @Test
    @Transactional
    public void getAllLecturesByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        lectureRepository.saveAndFlush(lecture);
        Profile user = ProfileResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        lecture.setUser(user);
        lectureRepository.saveAndFlush(lecture);
        Long userId = user.getId();

        // Get all the lectureList where user equals to userId
        defaultLectureShouldBeFound("userId.equals=" + userId);

        // Get all the lectureList where user equals to userId + 1
        defaultLectureShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLectureShouldBeFound(String filter) throws Exception {
        restLectureMockMvc.perform(get("/api/lectures?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lecture.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].materialFilePath").value(hasItem(DEFAULT_MATERIAL_FILE_PATH)))
            .andExpect(jsonPath("$.[*].fileTypeIconPath").value(hasItem(DEFAULT_FILE_TYPE_ICON_PATH)))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].dateUpdated").value(hasItem(DEFAULT_DATE_UPDATED.toString())));

        // Check, that the count call also returns 1
        restLectureMockMvc.perform(get("/api/lectures/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLectureShouldNotBeFound(String filter) throws Exception {
        restLectureMockMvc.perform(get("/api/lectures?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLectureMockMvc.perform(get("/api/lectures/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingLecture() throws Exception {
        // Get the lecture
        restLectureMockMvc.perform(get("/api/lectures/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLecture() throws Exception {
        // Initialize the database
        lectureRepository.saveAndFlush(lecture);

        int databaseSizeBeforeUpdate = lectureRepository.findAll().size();

        // Update the lecture
        Lecture updatedLecture = lectureRepository.findById(lecture.getId()).get();
        // Disconnect from session so that the updates on updatedLecture are not directly saved in db
        em.detach(updatedLecture);
        updatedLecture
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .materialFilePath(UPDATED_MATERIAL_FILE_PATH)
            .fileTypeIconPath(UPDATED_FILE_TYPE_ICON_PATH)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateUpdated(UPDATED_DATE_UPDATED);
        LectureDTO lectureDTO = lectureMapper.toDto(updatedLecture);

        restLectureMockMvc.perform(put("/api/lectures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lectureDTO)))
            .andExpect(status().isOk());

        // Validate the Lecture in the database
        List<Lecture> lectureList = lectureRepository.findAll();
        assertThat(lectureList).hasSize(databaseSizeBeforeUpdate);
        Lecture testLecture = lectureList.get(lectureList.size() - 1);
        assertThat(testLecture.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testLecture.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testLecture.getMaterialFilePath()).isEqualTo(UPDATED_MATERIAL_FILE_PATH);
        assertThat(testLecture.getFileTypeIconPath()).isEqualTo(UPDATED_FILE_TYPE_ICON_PATH);
        assertThat(testLecture.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testLecture.getDateUpdated()).isEqualTo(UPDATED_DATE_UPDATED);
    }

    @Test
    @Transactional
    public void updateNonExistingLecture() throws Exception {
        int databaseSizeBeforeUpdate = lectureRepository.findAll().size();

        // Create the Lecture
        LectureDTO lectureDTO = lectureMapper.toDto(lecture);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLectureMockMvc.perform(put("/api/lectures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lectureDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Lecture in the database
        List<Lecture> lectureList = lectureRepository.findAll();
        assertThat(lectureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLecture() throws Exception {
        // Initialize the database
        lectureRepository.saveAndFlush(lecture);

        int databaseSizeBeforeDelete = lectureRepository.findAll().size();

        // Delete the lecture
        restLectureMockMvc.perform(delete("/api/lectures/{id}", lecture.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Lecture> lectureList = lectureRepository.findAll();
        assertThat(lectureList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
