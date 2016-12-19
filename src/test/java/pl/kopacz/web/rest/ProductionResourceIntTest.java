package pl.kopacz.web.rest;

import pl.kopacz.CompanyApp;

import pl.kopacz.domain.Production;
import pl.kopacz.repository.ProductionRepository;
import pl.kopacz.service.ProductionService;
import pl.kopacz.service.dto.ProductionDTO;
import pl.kopacz.service.mapper.ProductionMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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

    private static final ZonedDateTime DEFAULT_DATETIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATETIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATETIME_STR = DateTimeFormatter.ISO_INSTANT.format(DEFAULT_DATETIME);

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
                .datetime(DEFAULT_DATETIME);
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
        assertThat(testProduction.getDatetime()).isEqualTo(DEFAULT_DATETIME);
    }

    @Test
    @Transactional
    public void checkDatetimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = productionRepository.findAll().size();
        // set the field null
        production.setDatetime(null);

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
                .andExpect(jsonPath("$.[*].datetime").value(hasItem(DEFAULT_DATETIME_STR)));
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
            .andExpect(jsonPath("$.datetime").value(DEFAULT_DATETIME_STR));
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
                .datetime(UPDATED_DATETIME);
        ProductionDTO productionDTO = productionMapper.productionToProductionDTO(updatedProduction);

        restProductionMockMvc.perform(put("/api/productions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(productionDTO)))
                .andExpect(status().isOk());

        // Validate the Production in the database
        List<Production> productions = productionRepository.findAll();
        assertThat(productions).hasSize(databaseSizeBeforeUpdate);
        Production testProduction = productions.get(productions.size() - 1);
        assertThat(testProduction.getDatetime()).isEqualTo(UPDATED_DATETIME);
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
