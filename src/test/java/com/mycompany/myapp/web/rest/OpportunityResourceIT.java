package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.BugtrackerjhipsterApp;
import com.mycompany.myapp.domain.Opportunity;
import com.mycompany.myapp.repository.OpportunityRepository;
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
import java.util.List;

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.domain.enumeration.NetworkEnum;
/**
 * Integration tests for the {@link OpportunityResource} REST controller.
 */
@SpringBootTest(classes = BugtrackerjhipsterApp.class)
public class OpportunityResourceIT {

    private static final String DEFAULT_COMPANY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PLACE = "AAAAAAAAAA";
    private static final String UPDATED_PLACE = "BBBBBBBBBB";

    private static final NetworkEnum DEFAULT_CONTACT_FROM = NetworkEnum.LINKEDIN;
    private static final NetworkEnum UPDATED_CONTACT_FROM = NetworkEnum.PERSONNAL;

    @Autowired
    private OpportunityRepository opportunityRepository;

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

    private MockMvc restOpportunityMockMvc;

    private Opportunity opportunity;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OpportunityResource opportunityResource = new OpportunityResource(opportunityRepository);
        this.restOpportunityMockMvc = MockMvcBuilders.standaloneSetup(opportunityResource)
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
    public static Opportunity createEntity(EntityManager em) {
        Opportunity opportunity = new Opportunity()
            .companyName(DEFAULT_COMPANY_NAME)
            .place(DEFAULT_PLACE)
            .contactFrom(DEFAULT_CONTACT_FROM);
        return opportunity;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Opportunity createUpdatedEntity(EntityManager em) {
        Opportunity opportunity = new Opportunity()
            .companyName(UPDATED_COMPANY_NAME)
            .place(UPDATED_PLACE)
            .contactFrom(UPDATED_CONTACT_FROM);
        return opportunity;
    }

    @BeforeEach
    public void initTest() {
        opportunity = createEntity(em);
    }

    @Test
    @Transactional
    public void createOpportunity() throws Exception {
        int databaseSizeBeforeCreate = opportunityRepository.findAll().size();

        // Create the Opportunity
        restOpportunityMockMvc.perform(post("/api/opportunities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(opportunity)))
            .andExpect(status().isCreated());

        // Validate the Opportunity in the database
        List<Opportunity> opportunityList = opportunityRepository.findAll();
        assertThat(opportunityList).hasSize(databaseSizeBeforeCreate + 1);
        Opportunity testOpportunity = opportunityList.get(opportunityList.size() - 1);
        assertThat(testOpportunity.getCompanyName()).isEqualTo(DEFAULT_COMPANY_NAME);
        assertThat(testOpportunity.getPlace()).isEqualTo(DEFAULT_PLACE);
        assertThat(testOpportunity.getContactFrom()).isEqualTo(DEFAULT_CONTACT_FROM);
    }

    @Test
    @Transactional
    public void createOpportunityWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = opportunityRepository.findAll().size();

        // Create the Opportunity with an existing ID
        opportunity.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOpportunityMockMvc.perform(post("/api/opportunities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(opportunity)))
            .andExpect(status().isBadRequest());

        // Validate the Opportunity in the database
        List<Opportunity> opportunityList = opportunityRepository.findAll();
        assertThat(opportunityList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllOpportunities() throws Exception {
        // Initialize the database
        opportunityRepository.saveAndFlush(opportunity);

        // Get all the opportunityList
        restOpportunityMockMvc.perform(get("/api/opportunities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(opportunity.getId().intValue())))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME)))
            .andExpect(jsonPath("$.[*].place").value(hasItem(DEFAULT_PLACE)))
            .andExpect(jsonPath("$.[*].contactFrom").value(hasItem(DEFAULT_CONTACT_FROM.toString())));
    }
    
    @Test
    @Transactional
    public void getOpportunity() throws Exception {
        // Initialize the database
        opportunityRepository.saveAndFlush(opportunity);

        // Get the opportunity
        restOpportunityMockMvc.perform(get("/api/opportunities/{id}", opportunity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(opportunity.getId().intValue()))
            .andExpect(jsonPath("$.companyName").value(DEFAULT_COMPANY_NAME))
            .andExpect(jsonPath("$.place").value(DEFAULT_PLACE))
            .andExpect(jsonPath("$.contactFrom").value(DEFAULT_CONTACT_FROM.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOpportunity() throws Exception {
        // Get the opportunity
        restOpportunityMockMvc.perform(get("/api/opportunities/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOpportunity() throws Exception {
        // Initialize the database
        opportunityRepository.saveAndFlush(opportunity);

        int databaseSizeBeforeUpdate = opportunityRepository.findAll().size();

        // Update the opportunity
        Opportunity updatedOpportunity = opportunityRepository.findById(opportunity.getId()).get();
        // Disconnect from session so that the updates on updatedOpportunity are not directly saved in db
        em.detach(updatedOpportunity);
        updatedOpportunity
            .companyName(UPDATED_COMPANY_NAME)
            .place(UPDATED_PLACE)
            .contactFrom(UPDATED_CONTACT_FROM);

        restOpportunityMockMvc.perform(put("/api/opportunities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedOpportunity)))
            .andExpect(status().isOk());

        // Validate the Opportunity in the database
        List<Opportunity> opportunityList = opportunityRepository.findAll();
        assertThat(opportunityList).hasSize(databaseSizeBeforeUpdate);
        Opportunity testOpportunity = opportunityList.get(opportunityList.size() - 1);
        assertThat(testOpportunity.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testOpportunity.getPlace()).isEqualTo(UPDATED_PLACE);
        assertThat(testOpportunity.getContactFrom()).isEqualTo(UPDATED_CONTACT_FROM);
    }

    @Test
    @Transactional
    public void updateNonExistingOpportunity() throws Exception {
        int databaseSizeBeforeUpdate = opportunityRepository.findAll().size();

        // Create the Opportunity

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOpportunityMockMvc.perform(put("/api/opportunities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(opportunity)))
            .andExpect(status().isBadRequest());

        // Validate the Opportunity in the database
        List<Opportunity> opportunityList = opportunityRepository.findAll();
        assertThat(opportunityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOpportunity() throws Exception {
        // Initialize the database
        opportunityRepository.saveAndFlush(opportunity);

        int databaseSizeBeforeDelete = opportunityRepository.findAll().size();

        // Delete the opportunity
        restOpportunityMockMvc.perform(delete("/api/opportunities/{id}", opportunity.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Opportunity> opportunityList = opportunityRepository.findAll();
        assertThat(opportunityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
