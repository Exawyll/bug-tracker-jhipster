package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.BugtrackerjhipsterApp;
import com.mycompany.myapp.domain.SellingProduct;
import com.mycompany.myapp.repository.SellingProductRepository;
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

/**
 * Integration tests for the {@link SellingProductResource} REST controller.
 */
@SpringBootTest(classes = BugtrackerjhipsterApp.class)
public class SellingProductResourceIT {

    private static final String DEFAULT_V_2_PRODUCT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_V_2_PRODUCT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_V_2_PRODUCT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_V_2_PRODUCT_CATEGORY = "BBBBBBBBBB";

    private static final Integer DEFAULT_UNITS_SOLD = 1;
    private static final Integer UPDATED_UNITS_SOLD = 2;

    private static final Float DEFAULT_REVENUE = 1F;
    private static final Float UPDATED_REVENUE = 2F;

    @Autowired
    private SellingProductRepository sellingProductRepository;

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

    private MockMvc restSellingProductMockMvc;

    private SellingProduct sellingProduct;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SellingProductResource sellingProductResource = new SellingProductResource(sellingProductRepository);
        this.restSellingProductMockMvc = MockMvcBuilders.standaloneSetup(sellingProductResource)
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
    public static SellingProduct createEntity(EntityManager em) {
        SellingProduct sellingProduct = new SellingProduct()
            .v2ProductName(DEFAULT_V_2_PRODUCT_NAME)
            .v2ProductCategory(DEFAULT_V_2_PRODUCT_CATEGORY)
            .unitsSold(DEFAULT_UNITS_SOLD)
            .revenue(DEFAULT_REVENUE);
        return sellingProduct;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SellingProduct createUpdatedEntity(EntityManager em) {
        SellingProduct sellingProduct = new SellingProduct()
            .v2ProductName(UPDATED_V_2_PRODUCT_NAME)
            .v2ProductCategory(UPDATED_V_2_PRODUCT_CATEGORY)
            .unitsSold(UPDATED_UNITS_SOLD)
            .revenue(UPDATED_REVENUE);
        return sellingProduct;
    }

    @BeforeEach
    public void initTest() {
        sellingProduct = createEntity(em);
    }

    @Test
    @Transactional
    public void createSellingProduct() throws Exception {
        int databaseSizeBeforeCreate = sellingProductRepository.findAll().size();

        // Create the SellingProduct
        restSellingProductMockMvc.perform(post("/api/selling-products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sellingProduct)))
            .andExpect(status().isCreated());

        // Validate the SellingProduct in the database
        List<SellingProduct> sellingProductList = sellingProductRepository.findAll();
        assertThat(sellingProductList).hasSize(databaseSizeBeforeCreate + 1);
        SellingProduct testSellingProduct = sellingProductList.get(sellingProductList.size() - 1);
        assertThat(testSellingProduct.getv2ProductName()).isEqualTo(DEFAULT_V_2_PRODUCT_NAME);
        assertThat(testSellingProduct.getv2ProductCategory()).isEqualTo(DEFAULT_V_2_PRODUCT_CATEGORY);
        assertThat(testSellingProduct.getUnitsSold()).isEqualTo(DEFAULT_UNITS_SOLD);
        assertThat(testSellingProduct.getRevenue()).isEqualTo(DEFAULT_REVENUE);
    }

    @Test
    @Transactional
    public void createSellingProductWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sellingProductRepository.findAll().size();

        // Create the SellingProduct with an existing ID
        sellingProduct.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSellingProductMockMvc.perform(post("/api/selling-products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sellingProduct)))
            .andExpect(status().isBadRequest());

        // Validate the SellingProduct in the database
        List<SellingProduct> sellingProductList = sellingProductRepository.findAll();
        assertThat(sellingProductList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllSellingProducts() throws Exception {
        // Initialize the database
        sellingProductRepository.saveAndFlush(sellingProduct);

        // Get all the sellingProductList
        restSellingProductMockMvc.perform(get("/api/selling-products?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sellingProduct.getId().intValue())))
            .andExpect(jsonPath("$.[*].v2ProductName").value(hasItem(DEFAULT_V_2_PRODUCT_NAME)))
            .andExpect(jsonPath("$.[*].v2ProductCategory").value(hasItem(DEFAULT_V_2_PRODUCT_CATEGORY)))
            .andExpect(jsonPath("$.[*].unitsSold").value(hasItem(DEFAULT_UNITS_SOLD)))
            .andExpect(jsonPath("$.[*].revenue").value(hasItem(DEFAULT_REVENUE.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getSellingProduct() throws Exception {
        // Initialize the database
        sellingProductRepository.saveAndFlush(sellingProduct);

        // Get the sellingProduct
        restSellingProductMockMvc.perform(get("/api/selling-products/{id}", sellingProduct.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sellingProduct.getId().intValue()))
            .andExpect(jsonPath("$.v2ProductName").value(DEFAULT_V_2_PRODUCT_NAME))
            .andExpect(jsonPath("$.v2ProductCategory").value(DEFAULT_V_2_PRODUCT_CATEGORY))
            .andExpect(jsonPath("$.unitsSold").value(DEFAULT_UNITS_SOLD))
            .andExpect(jsonPath("$.revenue").value(DEFAULT_REVENUE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSellingProduct() throws Exception {
        // Get the sellingProduct
        restSellingProductMockMvc.perform(get("/api/selling-products/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSellingProduct() throws Exception {
        // Initialize the database
        sellingProductRepository.saveAndFlush(sellingProduct);

        int databaseSizeBeforeUpdate = sellingProductRepository.findAll().size();

        // Update the sellingProduct
        SellingProduct updatedSellingProduct = sellingProductRepository.findById(sellingProduct.getId()).get();
        // Disconnect from session so that the updates on updatedSellingProduct are not directly saved in db
        em.detach(updatedSellingProduct);
        updatedSellingProduct
            .v2ProductName(UPDATED_V_2_PRODUCT_NAME)
            .v2ProductCategory(UPDATED_V_2_PRODUCT_CATEGORY)
            .unitsSold(UPDATED_UNITS_SOLD)
            .revenue(UPDATED_REVENUE);

        restSellingProductMockMvc.perform(put("/api/selling-products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSellingProduct)))
            .andExpect(status().isOk());

        // Validate the SellingProduct in the database
        List<SellingProduct> sellingProductList = sellingProductRepository.findAll();
        assertThat(sellingProductList).hasSize(databaseSizeBeforeUpdate);
        SellingProduct testSellingProduct = sellingProductList.get(sellingProductList.size() - 1);
        assertThat(testSellingProduct.getv2ProductName()).isEqualTo(UPDATED_V_2_PRODUCT_NAME);
        assertThat(testSellingProduct.getv2ProductCategory()).isEqualTo(UPDATED_V_2_PRODUCT_CATEGORY);
        assertThat(testSellingProduct.getUnitsSold()).isEqualTo(UPDATED_UNITS_SOLD);
        assertThat(testSellingProduct.getRevenue()).isEqualTo(UPDATED_REVENUE);
    }

    @Test
    @Transactional
    public void updateNonExistingSellingProduct() throws Exception {
        int databaseSizeBeforeUpdate = sellingProductRepository.findAll().size();

        // Create the SellingProduct

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSellingProductMockMvc.perform(put("/api/selling-products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sellingProduct)))
            .andExpect(status().isBadRequest());

        // Validate the SellingProduct in the database
        List<SellingProduct> sellingProductList = sellingProductRepository.findAll();
        assertThat(sellingProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSellingProduct() throws Exception {
        // Initialize the database
        sellingProductRepository.saveAndFlush(sellingProduct);

        int databaseSizeBeforeDelete = sellingProductRepository.findAll().size();

        // Delete the sellingProduct
        restSellingProductMockMvc.perform(delete("/api/selling-products/{id}", sellingProduct.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SellingProduct> sellingProductList = sellingProductRepository.findAll();
        assertThat(sellingProductList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
