package me.amplitudo.elearning.web.rest;

import me.amplitudo.elearning.ElearningApp;
import me.amplitudo.elearning.domain.Profile;
import me.amplitudo.elearning.domain.User;
import me.amplitudo.elearning.domain.AssignmentProfile;
import me.amplitudo.elearning.domain.Lecture;
import me.amplitudo.elearning.domain.Notification;
import me.amplitudo.elearning.domain.Assignment;
import me.amplitudo.elearning.domain.Year;
import me.amplitudo.elearning.domain.Orientation;
import me.amplitudo.elearning.domain.Faculty;
import me.amplitudo.elearning.domain.Course;
import me.amplitudo.elearning.repository.ProfileRepository;
import me.amplitudo.elearning.service.ProfileService;
import me.amplitudo.elearning.service.dto.ProfileDTO;
import me.amplitudo.elearning.service.mapper.ProfileMapper;
import me.amplitudo.elearning.service.dto.ProfileCriteria;
import me.amplitudo.elearning.service.ProfileQueryService;

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
import java.time.LocalDate;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ProfileResource} REST controller.
 */
@SpringBootTest(classes = ElearningApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ProfileResourceIT {

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_OF_BIRTH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_BIRTH = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_OF_BIRTH = LocalDate.ofEpochDay(-1L);

    private static final Integer DEFAULT_INDEX = 1;
    private static final Integer UPDATED_INDEX = 2;
    private static final Integer SMALLER_INDEX = 1 - 1;

    private static final Integer DEFAULT_YEAR_OF_ENROLLMENT = 1;
    private static final Integer UPDATED_YEAR_OF_ENROLLMENT = 2;
    private static final Integer SMALLER_YEAR_OF_ENROLLMENT = 1 - 1;

    private static final String DEFAULT_VERIFICATION_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_VERIFICATION_NUMBER = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_APPROVED = false;
    private static final Boolean UPDATED_IS_APPROVED = true;

    private static final Instant DEFAULT_DATE_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ProfileMapper profileMapper;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ProfileQueryService profileQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProfileMockMvc;

    private Profile profile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Profile createEntity(EntityManager em) {
        Profile profile = new Profile()
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .dateOfBirth(DEFAULT_DATE_OF_BIRTH)
            .index(DEFAULT_INDEX)
            .yearOfEnrollment(DEFAULT_YEAR_OF_ENROLLMENT)
            .verificationNumber(DEFAULT_VERIFICATION_NUMBER)
            .isApproved(DEFAULT_IS_APPROVED)
            .dateCreated(DEFAULT_DATE_CREATED)
            .dateUpdated(DEFAULT_DATE_UPDATED);
        return profile;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Profile createUpdatedEntity(EntityManager em) {
        Profile profile = new Profile()
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .index(UPDATED_INDEX)
            .yearOfEnrollment(UPDATED_YEAR_OF_ENROLLMENT)
            .verificationNumber(UPDATED_VERIFICATION_NUMBER)
            .isApproved(UPDATED_IS_APPROVED)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateUpdated(UPDATED_DATE_UPDATED);
        return profile;
    }

    @BeforeEach
    public void initTest() {
        profile = createEntity(em);
    }

    @Test
    @Transactional
    public void createProfile() throws Exception {
        int databaseSizeBeforeCreate = profileRepository.findAll().size();
        // Create the Profile
        ProfileDTO profileDTO = profileMapper.toDto(profile);
        restProfileMockMvc.perform(post("/api/profiles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(profileDTO)))
            .andExpect(status().isCreated());

        // Validate the Profile in the database
        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeCreate + 1);
        Profile testProfile = profileList.get(profileList.size() - 1);
        assertThat(testProfile.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testProfile.getDateOfBirth()).isEqualTo(DEFAULT_DATE_OF_BIRTH);
        assertThat(testProfile.getIndex()).isEqualTo(DEFAULT_INDEX);
        assertThat(testProfile.getYearOfEnrollment()).isEqualTo(DEFAULT_YEAR_OF_ENROLLMENT);
        assertThat(testProfile.getVerificationNumber()).isEqualTo(DEFAULT_VERIFICATION_NUMBER);
        assertThat(testProfile.isIsApproved()).isEqualTo(DEFAULT_IS_APPROVED);
        assertThat(testProfile.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testProfile.getDateUpdated()).isEqualTo(DEFAULT_DATE_UPDATED);
    }

    @Test
    @Transactional
    public void createProfileWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = profileRepository.findAll().size();

        // Create the Profile with an existing ID
        profile.setId(1L);
        ProfileDTO profileDTO = profileMapper.toDto(profile);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfileMockMvc.perform(post("/api/profiles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(profileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Profile in the database
        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllProfiles() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList
        restProfileMockMvc.perform(get("/api/profiles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profile.getId().intValue())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].index").value(hasItem(DEFAULT_INDEX)))
            .andExpect(jsonPath("$.[*].yearOfEnrollment").value(hasItem(DEFAULT_YEAR_OF_ENROLLMENT)))
            .andExpect(jsonPath("$.[*].verificationNumber").value(hasItem(DEFAULT_VERIFICATION_NUMBER)))
            .andExpect(jsonPath("$.[*].isApproved").value(hasItem(DEFAULT_IS_APPROVED.booleanValue())))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].dateUpdated").value(hasItem(DEFAULT_DATE_UPDATED.toString())));
    }
    
    @Test
    @Transactional
    public void getProfile() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get the profile
        restProfileMockMvc.perform(get("/api/profiles/{id}", profile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(profile.getId().intValue()))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.dateOfBirth").value(DEFAULT_DATE_OF_BIRTH.toString()))
            .andExpect(jsonPath("$.index").value(DEFAULT_INDEX))
            .andExpect(jsonPath("$.yearOfEnrollment").value(DEFAULT_YEAR_OF_ENROLLMENT))
            .andExpect(jsonPath("$.verificationNumber").value(DEFAULT_VERIFICATION_NUMBER))
            .andExpect(jsonPath("$.isApproved").value(DEFAULT_IS_APPROVED.booleanValue()))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.dateUpdated").value(DEFAULT_DATE_UPDATED.toString()));
    }


    @Test
    @Transactional
    public void getProfilesByIdFiltering() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        Long id = profile.getId();

        defaultProfileShouldBeFound("id.equals=" + id);
        defaultProfileShouldNotBeFound("id.notEquals=" + id);

        defaultProfileShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProfileShouldNotBeFound("id.greaterThan=" + id);

        defaultProfileShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProfileShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllProfilesByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where phoneNumber equals to DEFAULT_PHONE_NUMBER
        defaultProfileShouldBeFound("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER);

        // Get all the profileList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultProfileShouldNotBeFound("phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllProfilesByPhoneNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where phoneNumber not equals to DEFAULT_PHONE_NUMBER
        defaultProfileShouldNotBeFound("phoneNumber.notEquals=" + DEFAULT_PHONE_NUMBER);

        // Get all the profileList where phoneNumber not equals to UPDATED_PHONE_NUMBER
        defaultProfileShouldBeFound("phoneNumber.notEquals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllProfilesByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where phoneNumber in DEFAULT_PHONE_NUMBER or UPDATED_PHONE_NUMBER
        defaultProfileShouldBeFound("phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER);

        // Get all the profileList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultProfileShouldNotBeFound("phoneNumber.in=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllProfilesByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where phoneNumber is not null
        defaultProfileShouldBeFound("phoneNumber.specified=true");

        // Get all the profileList where phoneNumber is null
        defaultProfileShouldNotBeFound("phoneNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllProfilesByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where phoneNumber contains DEFAULT_PHONE_NUMBER
        defaultProfileShouldBeFound("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER);

        // Get all the profileList where phoneNumber contains UPDATED_PHONE_NUMBER
        defaultProfileShouldNotBeFound("phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllProfilesByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where phoneNumber does not contain DEFAULT_PHONE_NUMBER
        defaultProfileShouldNotBeFound("phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER);

        // Get all the profileList where phoneNumber does not contain UPDATED_PHONE_NUMBER
        defaultProfileShouldBeFound("phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER);
    }


    @Test
    @Transactional
    public void getAllProfilesByDateOfBirthIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where dateOfBirth equals to DEFAULT_DATE_OF_BIRTH
        defaultProfileShouldBeFound("dateOfBirth.equals=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the profileList where dateOfBirth equals to UPDATED_DATE_OF_BIRTH
        defaultProfileShouldNotBeFound("dateOfBirth.equals=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    public void getAllProfilesByDateOfBirthIsNotEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where dateOfBirth not equals to DEFAULT_DATE_OF_BIRTH
        defaultProfileShouldNotBeFound("dateOfBirth.notEquals=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the profileList where dateOfBirth not equals to UPDATED_DATE_OF_BIRTH
        defaultProfileShouldBeFound("dateOfBirth.notEquals=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    public void getAllProfilesByDateOfBirthIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where dateOfBirth in DEFAULT_DATE_OF_BIRTH or UPDATED_DATE_OF_BIRTH
        defaultProfileShouldBeFound("dateOfBirth.in=" + DEFAULT_DATE_OF_BIRTH + "," + UPDATED_DATE_OF_BIRTH);

        // Get all the profileList where dateOfBirth equals to UPDATED_DATE_OF_BIRTH
        defaultProfileShouldNotBeFound("dateOfBirth.in=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    public void getAllProfilesByDateOfBirthIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where dateOfBirth is not null
        defaultProfileShouldBeFound("dateOfBirth.specified=true");

        // Get all the profileList where dateOfBirth is null
        defaultProfileShouldNotBeFound("dateOfBirth.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByDateOfBirthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where dateOfBirth is greater than or equal to DEFAULT_DATE_OF_BIRTH
        defaultProfileShouldBeFound("dateOfBirth.greaterThanOrEqual=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the profileList where dateOfBirth is greater than or equal to UPDATED_DATE_OF_BIRTH
        defaultProfileShouldNotBeFound("dateOfBirth.greaterThanOrEqual=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    public void getAllProfilesByDateOfBirthIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where dateOfBirth is less than or equal to DEFAULT_DATE_OF_BIRTH
        defaultProfileShouldBeFound("dateOfBirth.lessThanOrEqual=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the profileList where dateOfBirth is less than or equal to SMALLER_DATE_OF_BIRTH
        defaultProfileShouldNotBeFound("dateOfBirth.lessThanOrEqual=" + SMALLER_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    public void getAllProfilesByDateOfBirthIsLessThanSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where dateOfBirth is less than DEFAULT_DATE_OF_BIRTH
        defaultProfileShouldNotBeFound("dateOfBirth.lessThan=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the profileList where dateOfBirth is less than UPDATED_DATE_OF_BIRTH
        defaultProfileShouldBeFound("dateOfBirth.lessThan=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    public void getAllProfilesByDateOfBirthIsGreaterThanSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where dateOfBirth is greater than DEFAULT_DATE_OF_BIRTH
        defaultProfileShouldNotBeFound("dateOfBirth.greaterThan=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the profileList where dateOfBirth is greater than SMALLER_DATE_OF_BIRTH
        defaultProfileShouldBeFound("dateOfBirth.greaterThan=" + SMALLER_DATE_OF_BIRTH);
    }


    @Test
    @Transactional
    public void getAllProfilesByIndexIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where index equals to DEFAULT_INDEX
        defaultProfileShouldBeFound("index.equals=" + DEFAULT_INDEX);

        // Get all the profileList where index equals to UPDATED_INDEX
        defaultProfileShouldNotBeFound("index.equals=" + UPDATED_INDEX);
    }

    @Test
    @Transactional
    public void getAllProfilesByIndexIsNotEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where index not equals to DEFAULT_INDEX
        defaultProfileShouldNotBeFound("index.notEquals=" + DEFAULT_INDEX);

        // Get all the profileList where index not equals to UPDATED_INDEX
        defaultProfileShouldBeFound("index.notEquals=" + UPDATED_INDEX);
    }

    @Test
    @Transactional
    public void getAllProfilesByIndexIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where index in DEFAULT_INDEX or UPDATED_INDEX
        defaultProfileShouldBeFound("index.in=" + DEFAULT_INDEX + "," + UPDATED_INDEX);

        // Get all the profileList where index equals to UPDATED_INDEX
        defaultProfileShouldNotBeFound("index.in=" + UPDATED_INDEX);
    }

    @Test
    @Transactional
    public void getAllProfilesByIndexIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where index is not null
        defaultProfileShouldBeFound("index.specified=true");

        // Get all the profileList where index is null
        defaultProfileShouldNotBeFound("index.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByIndexIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where index is greater than or equal to DEFAULT_INDEX
        defaultProfileShouldBeFound("index.greaterThanOrEqual=" + DEFAULT_INDEX);

        // Get all the profileList where index is greater than or equal to UPDATED_INDEX
        defaultProfileShouldNotBeFound("index.greaterThanOrEqual=" + UPDATED_INDEX);
    }

    @Test
    @Transactional
    public void getAllProfilesByIndexIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where index is less than or equal to DEFAULT_INDEX
        defaultProfileShouldBeFound("index.lessThanOrEqual=" + DEFAULT_INDEX);

        // Get all the profileList where index is less than or equal to SMALLER_INDEX
        defaultProfileShouldNotBeFound("index.lessThanOrEqual=" + SMALLER_INDEX);
    }

    @Test
    @Transactional
    public void getAllProfilesByIndexIsLessThanSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where index is less than DEFAULT_INDEX
        defaultProfileShouldNotBeFound("index.lessThan=" + DEFAULT_INDEX);

        // Get all the profileList where index is less than UPDATED_INDEX
        defaultProfileShouldBeFound("index.lessThan=" + UPDATED_INDEX);
    }

    @Test
    @Transactional
    public void getAllProfilesByIndexIsGreaterThanSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where index is greater than DEFAULT_INDEX
        defaultProfileShouldNotBeFound("index.greaterThan=" + DEFAULT_INDEX);

        // Get all the profileList where index is greater than SMALLER_INDEX
        defaultProfileShouldBeFound("index.greaterThan=" + SMALLER_INDEX);
    }


    @Test
    @Transactional
    public void getAllProfilesByYearOfEnrollmentIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where yearOfEnrollment equals to DEFAULT_YEAR_OF_ENROLLMENT
        defaultProfileShouldBeFound("yearOfEnrollment.equals=" + DEFAULT_YEAR_OF_ENROLLMENT);

        // Get all the profileList where yearOfEnrollment equals to UPDATED_YEAR_OF_ENROLLMENT
        defaultProfileShouldNotBeFound("yearOfEnrollment.equals=" + UPDATED_YEAR_OF_ENROLLMENT);
    }

    @Test
    @Transactional
    public void getAllProfilesByYearOfEnrollmentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where yearOfEnrollment not equals to DEFAULT_YEAR_OF_ENROLLMENT
        defaultProfileShouldNotBeFound("yearOfEnrollment.notEquals=" + DEFAULT_YEAR_OF_ENROLLMENT);

        // Get all the profileList where yearOfEnrollment not equals to UPDATED_YEAR_OF_ENROLLMENT
        defaultProfileShouldBeFound("yearOfEnrollment.notEquals=" + UPDATED_YEAR_OF_ENROLLMENT);
    }

    @Test
    @Transactional
    public void getAllProfilesByYearOfEnrollmentIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where yearOfEnrollment in DEFAULT_YEAR_OF_ENROLLMENT or UPDATED_YEAR_OF_ENROLLMENT
        defaultProfileShouldBeFound("yearOfEnrollment.in=" + DEFAULT_YEAR_OF_ENROLLMENT + "," + UPDATED_YEAR_OF_ENROLLMENT);

        // Get all the profileList where yearOfEnrollment equals to UPDATED_YEAR_OF_ENROLLMENT
        defaultProfileShouldNotBeFound("yearOfEnrollment.in=" + UPDATED_YEAR_OF_ENROLLMENT);
    }

    @Test
    @Transactional
    public void getAllProfilesByYearOfEnrollmentIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where yearOfEnrollment is not null
        defaultProfileShouldBeFound("yearOfEnrollment.specified=true");

        // Get all the profileList where yearOfEnrollment is null
        defaultProfileShouldNotBeFound("yearOfEnrollment.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByYearOfEnrollmentIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where yearOfEnrollment is greater than or equal to DEFAULT_YEAR_OF_ENROLLMENT
        defaultProfileShouldBeFound("yearOfEnrollment.greaterThanOrEqual=" + DEFAULT_YEAR_OF_ENROLLMENT);

        // Get all the profileList where yearOfEnrollment is greater than or equal to UPDATED_YEAR_OF_ENROLLMENT
        defaultProfileShouldNotBeFound("yearOfEnrollment.greaterThanOrEqual=" + UPDATED_YEAR_OF_ENROLLMENT);
    }

    @Test
    @Transactional
    public void getAllProfilesByYearOfEnrollmentIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where yearOfEnrollment is less than or equal to DEFAULT_YEAR_OF_ENROLLMENT
        defaultProfileShouldBeFound("yearOfEnrollment.lessThanOrEqual=" + DEFAULT_YEAR_OF_ENROLLMENT);

        // Get all the profileList where yearOfEnrollment is less than or equal to SMALLER_YEAR_OF_ENROLLMENT
        defaultProfileShouldNotBeFound("yearOfEnrollment.lessThanOrEqual=" + SMALLER_YEAR_OF_ENROLLMENT);
    }

    @Test
    @Transactional
    public void getAllProfilesByYearOfEnrollmentIsLessThanSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where yearOfEnrollment is less than DEFAULT_YEAR_OF_ENROLLMENT
        defaultProfileShouldNotBeFound("yearOfEnrollment.lessThan=" + DEFAULT_YEAR_OF_ENROLLMENT);

        // Get all the profileList where yearOfEnrollment is less than UPDATED_YEAR_OF_ENROLLMENT
        defaultProfileShouldBeFound("yearOfEnrollment.lessThan=" + UPDATED_YEAR_OF_ENROLLMENT);
    }

    @Test
    @Transactional
    public void getAllProfilesByYearOfEnrollmentIsGreaterThanSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where yearOfEnrollment is greater than DEFAULT_YEAR_OF_ENROLLMENT
        defaultProfileShouldNotBeFound("yearOfEnrollment.greaterThan=" + DEFAULT_YEAR_OF_ENROLLMENT);

        // Get all the profileList where yearOfEnrollment is greater than SMALLER_YEAR_OF_ENROLLMENT
        defaultProfileShouldBeFound("yearOfEnrollment.greaterThan=" + SMALLER_YEAR_OF_ENROLLMENT);
    }


    @Test
    @Transactional
    public void getAllProfilesByVerificationNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where verificationNumber equals to DEFAULT_VERIFICATION_NUMBER
        defaultProfileShouldBeFound("verificationNumber.equals=" + DEFAULT_VERIFICATION_NUMBER);

        // Get all the profileList where verificationNumber equals to UPDATED_VERIFICATION_NUMBER
        defaultProfileShouldNotBeFound("verificationNumber.equals=" + UPDATED_VERIFICATION_NUMBER);
    }

    @Test
    @Transactional
    public void getAllProfilesByVerificationNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where verificationNumber not equals to DEFAULT_VERIFICATION_NUMBER
        defaultProfileShouldNotBeFound("verificationNumber.notEquals=" + DEFAULT_VERIFICATION_NUMBER);

        // Get all the profileList where verificationNumber not equals to UPDATED_VERIFICATION_NUMBER
        defaultProfileShouldBeFound("verificationNumber.notEquals=" + UPDATED_VERIFICATION_NUMBER);
    }

    @Test
    @Transactional
    public void getAllProfilesByVerificationNumberIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where verificationNumber in DEFAULT_VERIFICATION_NUMBER or UPDATED_VERIFICATION_NUMBER
        defaultProfileShouldBeFound("verificationNumber.in=" + DEFAULT_VERIFICATION_NUMBER + "," + UPDATED_VERIFICATION_NUMBER);

        // Get all the profileList where verificationNumber equals to UPDATED_VERIFICATION_NUMBER
        defaultProfileShouldNotBeFound("verificationNumber.in=" + UPDATED_VERIFICATION_NUMBER);
    }

    @Test
    @Transactional
    public void getAllProfilesByVerificationNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where verificationNumber is not null
        defaultProfileShouldBeFound("verificationNumber.specified=true");

        // Get all the profileList where verificationNumber is null
        defaultProfileShouldNotBeFound("verificationNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllProfilesByVerificationNumberContainsSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where verificationNumber contains DEFAULT_VERIFICATION_NUMBER
        defaultProfileShouldBeFound("verificationNumber.contains=" + DEFAULT_VERIFICATION_NUMBER);

        // Get all the profileList where verificationNumber contains UPDATED_VERIFICATION_NUMBER
        defaultProfileShouldNotBeFound("verificationNumber.contains=" + UPDATED_VERIFICATION_NUMBER);
    }

    @Test
    @Transactional
    public void getAllProfilesByVerificationNumberNotContainsSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where verificationNumber does not contain DEFAULT_VERIFICATION_NUMBER
        defaultProfileShouldNotBeFound("verificationNumber.doesNotContain=" + DEFAULT_VERIFICATION_NUMBER);

        // Get all the profileList where verificationNumber does not contain UPDATED_VERIFICATION_NUMBER
        defaultProfileShouldBeFound("verificationNumber.doesNotContain=" + UPDATED_VERIFICATION_NUMBER);
    }


    @Test
    @Transactional
    public void getAllProfilesByIsApprovedIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where isApproved equals to DEFAULT_IS_APPROVED
        defaultProfileShouldBeFound("isApproved.equals=" + DEFAULT_IS_APPROVED);

        // Get all the profileList where isApproved equals to UPDATED_IS_APPROVED
        defaultProfileShouldNotBeFound("isApproved.equals=" + UPDATED_IS_APPROVED);
    }

    @Test
    @Transactional
    public void getAllProfilesByIsApprovedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where isApproved not equals to DEFAULT_IS_APPROVED
        defaultProfileShouldNotBeFound("isApproved.notEquals=" + DEFAULT_IS_APPROVED);

        // Get all the profileList where isApproved not equals to UPDATED_IS_APPROVED
        defaultProfileShouldBeFound("isApproved.notEquals=" + UPDATED_IS_APPROVED);
    }

    @Test
    @Transactional
    public void getAllProfilesByIsApprovedIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where isApproved in DEFAULT_IS_APPROVED or UPDATED_IS_APPROVED
        defaultProfileShouldBeFound("isApproved.in=" + DEFAULT_IS_APPROVED + "," + UPDATED_IS_APPROVED);

        // Get all the profileList where isApproved equals to UPDATED_IS_APPROVED
        defaultProfileShouldNotBeFound("isApproved.in=" + UPDATED_IS_APPROVED);
    }

    @Test
    @Transactional
    public void getAllProfilesByIsApprovedIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where isApproved is not null
        defaultProfileShouldBeFound("isApproved.specified=true");

        // Get all the profileList where isApproved is null
        defaultProfileShouldNotBeFound("isApproved.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByDateCreatedIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where dateCreated equals to DEFAULT_DATE_CREATED
        defaultProfileShouldBeFound("dateCreated.equals=" + DEFAULT_DATE_CREATED);

        // Get all the profileList where dateCreated equals to UPDATED_DATE_CREATED
        defaultProfileShouldNotBeFound("dateCreated.equals=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    public void getAllProfilesByDateCreatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where dateCreated not equals to DEFAULT_DATE_CREATED
        defaultProfileShouldNotBeFound("dateCreated.notEquals=" + DEFAULT_DATE_CREATED);

        // Get all the profileList where dateCreated not equals to UPDATED_DATE_CREATED
        defaultProfileShouldBeFound("dateCreated.notEquals=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    public void getAllProfilesByDateCreatedIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where dateCreated in DEFAULT_DATE_CREATED or UPDATED_DATE_CREATED
        defaultProfileShouldBeFound("dateCreated.in=" + DEFAULT_DATE_CREATED + "," + UPDATED_DATE_CREATED);

        // Get all the profileList where dateCreated equals to UPDATED_DATE_CREATED
        defaultProfileShouldNotBeFound("dateCreated.in=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    public void getAllProfilesByDateCreatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where dateCreated is not null
        defaultProfileShouldBeFound("dateCreated.specified=true");

        // Get all the profileList where dateCreated is null
        defaultProfileShouldNotBeFound("dateCreated.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByDateUpdatedIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where dateUpdated equals to DEFAULT_DATE_UPDATED
        defaultProfileShouldBeFound("dateUpdated.equals=" + DEFAULT_DATE_UPDATED);

        // Get all the profileList where dateUpdated equals to UPDATED_DATE_UPDATED
        defaultProfileShouldNotBeFound("dateUpdated.equals=" + UPDATED_DATE_UPDATED);
    }

    @Test
    @Transactional
    public void getAllProfilesByDateUpdatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where dateUpdated not equals to DEFAULT_DATE_UPDATED
        defaultProfileShouldNotBeFound("dateUpdated.notEquals=" + DEFAULT_DATE_UPDATED);

        // Get all the profileList where dateUpdated not equals to UPDATED_DATE_UPDATED
        defaultProfileShouldBeFound("dateUpdated.notEquals=" + UPDATED_DATE_UPDATED);
    }

    @Test
    @Transactional
    public void getAllProfilesByDateUpdatedIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where dateUpdated in DEFAULT_DATE_UPDATED or UPDATED_DATE_UPDATED
        defaultProfileShouldBeFound("dateUpdated.in=" + DEFAULT_DATE_UPDATED + "," + UPDATED_DATE_UPDATED);

        // Get all the profileList where dateUpdated equals to UPDATED_DATE_UPDATED
        defaultProfileShouldNotBeFound("dateUpdated.in=" + UPDATED_DATE_UPDATED);
    }

    @Test
    @Transactional
    public void getAllProfilesByDateUpdatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where dateUpdated is not null
        defaultProfileShouldBeFound("dateUpdated.specified=true");

        // Get all the profileList where dateUpdated is null
        defaultProfileShouldNotBeFound("dateUpdated.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        profile.setUser(user);
        profileRepository.saveAndFlush(profile);
        Long userId = user.getId();

        // Get all the profileList where user equals to userId
        defaultProfileShouldBeFound("userId.equals=" + userId);

        // Get all the profileList where user equals to userId + 1
        defaultProfileShouldNotBeFound("userId.equals=" + (userId + 1));
    }


    @Test
    @Transactional
    public void getAllProfilesByAssignmentProfilesIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);
        AssignmentProfile assignmentProfiles = AssignmentProfileResourceIT.createEntity(em);
        em.persist(assignmentProfiles);
        em.flush();
        profile.addAssignmentProfiles(assignmentProfiles);
        profileRepository.saveAndFlush(profile);
        Long assignmentProfilesId = assignmentProfiles.getId();

        // Get all the profileList where assignmentProfiles equals to assignmentProfilesId
        defaultProfileShouldBeFound("assignmentProfilesId.equals=" + assignmentProfilesId);

        // Get all the profileList where assignmentProfiles equals to assignmentProfilesId + 1
        defaultProfileShouldNotBeFound("assignmentProfilesId.equals=" + (assignmentProfilesId + 1));
    }


    @Test
    @Transactional
    public void getAllProfilesByLecturesIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);
        Lecture lectures = LectureResourceIT.createEntity(em);
        em.persist(lectures);
        em.flush();
        profile.addLectures(lectures);
        profileRepository.saveAndFlush(profile);
        Long lecturesId = lectures.getId();

        // Get all the profileList where lectures equals to lecturesId
        defaultProfileShouldBeFound("lecturesId.equals=" + lecturesId);

        // Get all the profileList where lectures equals to lecturesId + 1
        defaultProfileShouldNotBeFound("lecturesId.equals=" + (lecturesId + 1));
    }


    @Test
    @Transactional
    public void getAllProfilesByNotificationsIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);
        Notification notifications = NotificationResourceIT.createEntity(em);
        em.persist(notifications);
        em.flush();
        profile.addNotifications(notifications);
        profileRepository.saveAndFlush(profile);
        Long notificationsId = notifications.getId();

        // Get all the profileList where notifications equals to notificationsId
        defaultProfileShouldBeFound("notificationsId.equals=" + notificationsId);

        // Get all the profileList where notifications equals to notificationsId + 1
        defaultProfileShouldNotBeFound("notificationsId.equals=" + (notificationsId + 1));
    }


    @Test
    @Transactional
    public void getAllProfilesByAssignmentsIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);
        Assignment assignments = AssignmentResourceIT.createEntity(em);
        em.persist(assignments);
        em.flush();
        profile.addAssignments(assignments);
        profileRepository.saveAndFlush(profile);
        Long assignmentsId = assignments.getId();

        // Get all the profileList where assignments equals to assignmentsId
        defaultProfileShouldBeFound("assignmentsId.equals=" + assignmentsId);

        // Get all the profileList where assignments equals to assignmentsId + 1
        defaultProfileShouldNotBeFound("assignmentsId.equals=" + (assignmentsId + 1));
    }


    @Test
    @Transactional
    public void getAllProfilesByYearIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);
        Year year = YearResourceIT.createEntity(em);
        em.persist(year);
        em.flush();
        profile.setYear(year);
        profileRepository.saveAndFlush(profile);
        Long yearId = year.getId();

        // Get all the profileList where year equals to yearId
        defaultProfileShouldBeFound("yearId.equals=" + yearId);

        // Get all the profileList where year equals to yearId + 1
        defaultProfileShouldNotBeFound("yearId.equals=" + (yearId + 1));
    }


    @Test
    @Transactional
    public void getAllProfilesByFacultyIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);
        Orientation faculty = OrientationResourceIT.createEntity(em);
        em.persist(faculty);
        em.flush();
        profile.setFaculty(faculty);
        profileRepository.saveAndFlush(profile);
        Long facultyId = faculty.getId();

        // Get all the profileList where faculty equals to facultyId
        defaultProfileShouldBeFound("facultyId.equals=" + facultyId);

        // Get all the profileList where faculty equals to facultyId + 1
        defaultProfileShouldNotBeFound("facultyId.equals=" + (facultyId + 1));
    }


    @Test
    @Transactional
    public void getAllProfilesByFacultiesIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);
        Faculty faculties = FacultyResourceIT.createEntity(em);
        em.persist(faculties);
        em.flush();
        profile.addFaculties(faculties);
        profileRepository.saveAndFlush(profile);
        Long facultiesId = faculties.getId();

        // Get all the profileList where faculties equals to facultiesId
        defaultProfileShouldBeFound("facultiesId.equals=" + facultiesId);

        // Get all the profileList where faculties equals to facultiesId + 1
        defaultProfileShouldNotBeFound("facultiesId.equals=" + (facultiesId + 1));
    }


    @Test
    @Transactional
    public void getAllProfilesByCoursesIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);
        Course courses = CourseResourceIT.createEntity(em);
        em.persist(courses);
        em.flush();
        profile.addCourses(courses);
        profileRepository.saveAndFlush(profile);
        Long coursesId = courses.getId();

        // Get all the profileList where courses equals to coursesId
        defaultProfileShouldBeFound("coursesId.equals=" + coursesId);

        // Get all the profileList where courses equals to coursesId + 1
        defaultProfileShouldNotBeFound("coursesId.equals=" + (coursesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProfileShouldBeFound(String filter) throws Exception {
        restProfileMockMvc.perform(get("/api/profiles?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profile.getId().intValue())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].index").value(hasItem(DEFAULT_INDEX)))
            .andExpect(jsonPath("$.[*].yearOfEnrollment").value(hasItem(DEFAULT_YEAR_OF_ENROLLMENT)))
            .andExpect(jsonPath("$.[*].verificationNumber").value(hasItem(DEFAULT_VERIFICATION_NUMBER)))
            .andExpect(jsonPath("$.[*].isApproved").value(hasItem(DEFAULT_IS_APPROVED.booleanValue())))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].dateUpdated").value(hasItem(DEFAULT_DATE_UPDATED.toString())));

        // Check, that the count call also returns 1
        restProfileMockMvc.perform(get("/api/profiles/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProfileShouldNotBeFound(String filter) throws Exception {
        restProfileMockMvc.perform(get("/api/profiles?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProfileMockMvc.perform(get("/api/profiles/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingProfile() throws Exception {
        // Get the profile
        restProfileMockMvc.perform(get("/api/profiles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProfile() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        int databaseSizeBeforeUpdate = profileRepository.findAll().size();

        // Update the profile
        Profile updatedProfile = profileRepository.findById(profile.getId()).get();
        // Disconnect from session so that the updates on updatedProfile are not directly saved in db
        em.detach(updatedProfile);
        updatedProfile
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .index(UPDATED_INDEX)
            .yearOfEnrollment(UPDATED_YEAR_OF_ENROLLMENT)
            .verificationNumber(UPDATED_VERIFICATION_NUMBER)
            .isApproved(UPDATED_IS_APPROVED)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateUpdated(UPDATED_DATE_UPDATED);
        ProfileDTO profileDTO = profileMapper.toDto(updatedProfile);

        restProfileMockMvc.perform(put("/api/profiles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(profileDTO)))
            .andExpect(status().isOk());

        // Validate the Profile in the database
        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeUpdate);
        Profile testProfile = profileList.get(profileList.size() - 1);
        assertThat(testProfile.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testProfile.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testProfile.getIndex()).isEqualTo(UPDATED_INDEX);
        assertThat(testProfile.getYearOfEnrollment()).isEqualTo(UPDATED_YEAR_OF_ENROLLMENT);
        assertThat(testProfile.getVerificationNumber()).isEqualTo(UPDATED_VERIFICATION_NUMBER);
        assertThat(testProfile.isIsApproved()).isEqualTo(UPDATED_IS_APPROVED);
        assertThat(testProfile.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testProfile.getDateUpdated()).isEqualTo(UPDATED_DATE_UPDATED);
    }

    @Test
    @Transactional
    public void updateNonExistingProfile() throws Exception {
        int databaseSizeBeforeUpdate = profileRepository.findAll().size();

        // Create the Profile
        ProfileDTO profileDTO = profileMapper.toDto(profile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfileMockMvc.perform(put("/api/profiles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(profileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Profile in the database
        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProfile() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        int databaseSizeBeforeDelete = profileRepository.findAll().size();

        // Delete the profile
        restProfileMockMvc.perform(delete("/api/profiles/{id}", profile.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
