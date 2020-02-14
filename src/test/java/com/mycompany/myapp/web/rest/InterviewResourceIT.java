package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.BugtrackerjhipsterApp;
import com.mycompany.myapp.domain.Interview;
import com.mycompany.myapp.repository.InterviewRepository;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.domain.enumeration.InterviewTypeEnum;
/**
 * Integration tests for the {@link InterviewResource} REST controller.
 */
@SpringBootTest(classes = BugtrackerjhipsterApp.class)
public class InterviewResourceIT {

    private static final LocalDate DEFAULT_OCCURED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_OCCURED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_JOB_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_JOB_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Float DEFAULT_SALARY = 1F;
    private static final Float UPDATED_SALARY = 2F;

    private static final String DEFAULT_CONTACT = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT = "BBBBBBBBBB";

    private static final InterviewTypeEnum DEFAULT_TYPE = InterviewTypeEnum.REMOTE;
    private static final InterviewTypeEnum UPDATED_TYPE = InterviewTypeEnum.CALL;

    @Autowired
    private InterviewRepository interviewRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restInterviewMockMvc;

    private Interview interview;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InterviewResource interviewResource = new InterviewResource(interviewRepository);
        this.restInterviewMockMvc = MockMvcBuilders.standaloneSetup(interviewResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Interview createEntity(EntityManager em) {
        Interview interview = new Interview()
            .occuredDate(DEFAULT_OCCURED_DATE)
            .jobTitle(DEFAULT_JOB_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .salary(DEFAULT_SALARY)
            .contact(DEFAULT_CONTACT)
            .type(DEFAULT_TYPE);
        return interview;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Interview createUpdatedEntity(EntityManager em) {
        Interview interview = new Interview()
            .occuredDate(UPDATED_OCCURED_DATE)
            .jobTitle(UPDATED_JOB_TITLE)
            .description(UPDATED_DESCRIPTION)
            .salary(UPDATED_SALARY)
            .contact(UPDATED_CONTACT)
            .type(UPDATED_TYPE);
        return interview;
    }

    @BeforeEach
    public void initTest() {
        interview = createEntity(em);
    }

    @Test
    @Transactional
    public void createInterview() throws Exception {
        int databaseSizeBeforeCreate = interviewRepository.findAll().size();

        // Create the Interview
        restInterviewMockMvc.perform(post("/api/interviews")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(interview)))
            .andExpect(status().isCreated());

        // Validate the Interview in the database
        List<Interview> interviewList = interviewRepository.findAll();
        assertThat(interviewList).hasSize(databaseSizeBeforeCreate + 1);
        Interview testInterview = interviewList.get(interviewList.size() - 1);
        assertThat(testInterview.getOccuredDate()).isEqualTo(DEFAULT_OCCURED_DATE);
        assertThat(testInterview.getJobTitle()).isEqualTo(DEFAULT_JOB_TITLE);
        assertThat(testInterview.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testInterview.getSalary()).isEqualTo(DEFAULT_SALARY);
        assertThat(testInterview.getContact()).isEqualTo(DEFAULT_CONTACT);
        assertThat(testInterview.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void createInterviewWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = interviewRepository.findAll().size();

        // Create the Interview with an existing ID
        interview.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInterviewMockMvc.perform(post("/api/interviews")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(interview)))
            .andExpect(status().isBadRequest());

        // Validate the Interview in the database
        List<Interview> interviewList = interviewRepository.findAll();
        assertThat(interviewList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllInterviews() throws Exception {
        // Initialize the database
        interviewRepository.saveAndFlush(interview);

        // Get all the interviewList
        restInterviewMockMvc.perform(get("/api/interviews?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(interview.getId().intValue())))
            .andExpect(jsonPath("$.[*].occuredDate").value(hasItem(DEFAULT_OCCURED_DATE.toString())))
            .andExpect(jsonPath("$.[*].jobTitle").value(hasItem(DEFAULT_JOB_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].salary").value(hasItem(DEFAULT_SALARY.doubleValue())))
            .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }
    
    @Test
    @Transactional
    public void getInterview() throws Exception {
        // Initialize the database
        interviewRepository.saveAndFlush(interview);

        // Get the interview
        restInterviewMockMvc.perform(get("/api/interviews/{id}", interview.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(interview.getId().intValue()))
            .andExpect(jsonPath("$.occuredDate").value(DEFAULT_OCCURED_DATE.toString()))
            .andExpect(jsonPath("$.jobTitle").value(DEFAULT_JOB_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.salary").value(DEFAULT_SALARY.doubleValue()))
            .andExpect(jsonPath("$.contact").value(DEFAULT_CONTACT))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingInterview() throws Exception {
        // Get the interview
        restInterviewMockMvc.perform(get("/api/interviews/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInterview() throws Exception {
        // Initialize the database
        interviewRepository.saveAndFlush(interview);

        int databaseSizeBeforeUpdate = interviewRepository.findAll().size();

        // Update the interview
        Interview updatedInterview = interviewRepository.findById(interview.getId()).get();
        // Disconnect from session so that the updates on updatedInterview are not directly saved in db
        em.detach(updatedInterview);
        updatedInterview
            .occuredDate(UPDATED_OCCURED_DATE)
            .jobTitle(UPDATED_JOB_TITLE)
            .description(UPDATED_DESCRIPTION)
            .salary(UPDATED_SALARY)
            .contact(UPDATED_CONTACT)
            .type(UPDATED_TYPE);

        restInterviewMockMvc.perform(put("/api/interviews")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedInterview)))
            .andExpect(status().isOk());

        // Validate the Interview in the database
        List<Interview> interviewList = interviewRepository.findAll();
        assertThat(interviewList).hasSize(databaseSizeBeforeUpdate);
        Interview testInterview = interviewList.get(interviewList.size() - 1);
        assertThat(testInterview.getOccuredDate()).isEqualTo(UPDATED_OCCURED_DATE);
        assertThat(testInterview.getJobTitle()).isEqualTo(UPDATED_JOB_TITLE);
        assertThat(testInterview.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testInterview.getSalary()).isEqualTo(UPDATED_SALARY);
        assertThat(testInterview.getContact()).isEqualTo(UPDATED_CONTACT);
        assertThat(testInterview.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingInterview() throws Exception {
        int databaseSizeBeforeUpdate = interviewRepository.findAll().size();

        // Create the Interview

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInterviewMockMvc.perform(put("/api/interviews")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(interview)))
            .andExpect(status().isBadRequest());

        // Validate the Interview in the database
        List<Interview> interviewList = interviewRepository.findAll();
        assertThat(interviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteInterview() throws Exception {
        // Initialize the database
        interviewRepository.saveAndFlush(interview);

        int databaseSizeBeforeDelete = interviewRepository.findAll().size();

        // Delete the interview
        restInterviewMockMvc.perform(delete("/api/interviews/{id}", interview.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Interview> interviewList = interviewRepository.findAll();
        assertThat(interviewList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
