package pl.kopacz.web.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import pl.kopacz.CompanyApp;
import pl.kopacz.domain.Production;
import pl.kopacz.repository.ProductionRepository;
import pl.kopacz.service.ProductionService;
import pl.kopacz.service.dto.ProductionDTO;
import pl.kopacz.service.mapper.ProductionMapper;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ProductionResource REST controller.
 *
 * @see ProductionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CompanyApp.class)
public class ProductionResourceIntTest {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private ProductionRepository productionRepository;

    @Inject
    private ProductionMapper productionMapper;

    @Inject
    private ProductionService productionService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restProductionMockMvc;

    private Production production;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProductionResource productionResource = new ProductionResource();
        ReflectionTestUtils.setField(productionResource, "productionService", productionService);
        this.restProductionMockMvc = MockMvcBuilders.standaloneSetup(productionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Production createEntity(EntityManager em) {
        Production production = new Production()
                .date(DEFAULT_DATE);
        return production;
    }

    @Before
    public void initTest() {
        production = createEntity(em);
    }

    @Test
    @Transactional
    public void createProduction() throws Exception {
        int databaseSizeBeforeCreate = productionRepository.findAll().size();

        // Create the Production
        ProductionDTO productionDTO = productionMapper.productionToProductionDTO(production);

        restProductionMockMvc.perform(post("/api/productions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(productionDTO)))
                .andExpect(status().isCreated());

        // Validate the Production in the database
        List<Production> productions = productionRepository.findAll();
        assertThat(productions).hasSize(databaseSizeBeforeCreate + 1);
        Production testProduction = productions.get(productions.size() - 1);
        assertThat(testProduction.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = productionRepository.findAll().size();
        // set the field null
        production.setDate(null);

        // Create the Production, which fails.
        ProductionDTO productionDTO = productionMapper.productionToProductionDTO(production);

        restProductionMockMvc.perform(post("/api/productions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(productionDTO)))
                .andExpect(status().isBadRequest());

        List<Production> productions = productionRepository.findAll();
        assertThat(productions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProductions() throws Exception {
        // Initialize the database
        productionRepository.saveAndFlush(production);

        // Get all the productions
        restProductionMockMvc.perform(get("/api/productions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(production.getId().intValue())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    public void getProduction() throws Exception {
        // Initialize the database
        productionRepository.saveAndFlush(production);

        // Get the production
        restProductionMockMvc.perform(get("/api/productions/{id}", production.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(production.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProduction() throws Exception {
        // Get the production
        restProductionMockMvc.perform(get("/api/productions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProduction() throws Exception {
        // Initialize the database
        productionRepository.saveAndFlush(production);
        int databaseSizeBeforeUpdate = productionRepository.findAll().size();

        // Update the production
        Production updatedProduction = productionRepository.findOne(production.getId());
        updatedProduction
                .date(UPDATED_DATE);
        ProductionDTO productionDTO = productionMapper.productionToProductionDTO(updatedProduction);

        restProductionMockMvc.perform(put("/api/productions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(productionDTO)))
                .andExpect(status().isOk());

        // Validate the Production in the database
        List<Production> productions = productionRepository.findAll();
        assertThat(productions).hasSize(databaseSizeBeforeUpdate);
        Production testProduction = productions.get(productions.size() - 1);
        assertThat(testProduction.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    public void deleteProduction() throws Exception {
        // Initialize the database
        productionRepository.saveAndFlush(production);
        int databaseSizeBeforeDelete = productionRepository.findAll().size();

        // Get the production
        restProductionMockMvc.perform(delete("/api/productions/{id}", production.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Production> productions = productionRepository.findAll();
        assertThat(productions).hasSize(databaseSizeBeforeDelete - 1);
    }
}
