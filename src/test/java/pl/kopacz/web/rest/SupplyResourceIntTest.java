package pl.kopacz.web.rest;

import pl.kopacz.CompanyApp;

import pl.kopacz.domain.Supply;
import pl.kopacz.domain.Spice;
import pl.kopacz.repository.SupplyRepository;
import pl.kopacz.service.SupplyService;
import pl.kopacz.service.dto.SupplyDTO;
import pl.kopacz.service.mapper.SupplyMapper;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SupplyResource REST controller.
 *
 * @see SupplyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CompanyApp.class)
public class SupplyResourceIntTest {

    private static final String DEFAULT_SERIAL_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_SERIAL_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_EXPIRATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXPIRATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    @Inject
    private SupplyRepository supplyRepository;

    @Inject
    private SupplyMapper supplyMapper;

    @Inject
    private SupplyService supplyService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restSupplyMockMvc;

    private Supply supply;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SupplyResource supplyResource = new SupplyResource();
        ReflectionTestUtils.setField(supplyResource, "supplyService", supplyService);
        this.restSupplyMockMvc = MockMvcBuilders.standaloneSetup(supplyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Supply createEntity(EntityManager em) {
        Supply supply = new Supply()
                .serialNumber(DEFAULT_SERIAL_NUMBER)
                .expirationDate(DEFAULT_EXPIRATION_DATE)
                .amount(DEFAULT_AMOUNT);
        // Add required entity
        Spice spice = SpiceResourceIntTest.createEntity(em);
        em.persist(spice);
        em.flush();
        supply.setSpice(spice);
        return supply;
    }

    @Before
    public void initTest() {
        supply = createEntity(em);
    }

    @Test
    @Transactional
    public void createSupply() throws Exception {
        int databaseSizeBeforeCreate = supplyRepository.findAll().size();

        // Create the Supply
        SupplyDTO supplyDTO = supplyMapper.supplyToSupplyDTO(supply);

        restSupplyMockMvc.perform(post("/api/supplies")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(supplyDTO)))
                .andExpect(status().isCreated());

        // Validate the Supply in the database
        List<Supply> supplies = supplyRepository.findAll();
        assertThat(supplies).hasSize(databaseSizeBeforeCreate + 1);
        Supply testSupply = supplies.get(supplies.size() - 1);
        assertThat(testSupply.getSerialNumber()).isEqualTo(DEFAULT_SERIAL_NUMBER);
        assertThat(testSupply.getExpirationDate()).isEqualTo(DEFAULT_EXPIRATION_DATE);
        assertThat(testSupply.getAmount()).isEqualTo(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    public void checkSerialNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = supplyRepository.findAll().size();
        // set the field null
        supply.setSerialNumber(null);

        // Create the Supply, which fails.
        SupplyDTO supplyDTO = supplyMapper.supplyToSupplyDTO(supply);

        restSupplyMockMvc.perform(post("/api/supplies")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(supplyDTO)))
                .andExpect(status().isBadRequest());

        List<Supply> supplies = supplyRepository.findAll();
        assertThat(supplies).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkExpirationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = supplyRepository.findAll().size();
        // set the field null
        supply.setExpirationDate(null);

        // Create the Supply, which fails.
        SupplyDTO supplyDTO = supplyMapper.supplyToSupplyDTO(supply);

        restSupplyMockMvc.perform(post("/api/supplies")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(supplyDTO)))
                .andExpect(status().isBadRequest());

        List<Supply> supplies = supplyRepository.findAll();
        assertThat(supplies).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = supplyRepository.findAll().size();
        // set the field null
        supply.setAmount(null);

        // Create the Supply, which fails.
        SupplyDTO supplyDTO = supplyMapper.supplyToSupplyDTO(supply);

        restSupplyMockMvc.perform(post("/api/supplies")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(supplyDTO)))
                .andExpect(status().isBadRequest());

        List<Supply> supplies = supplyRepository.findAll();
        assertThat(supplies).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSupplies() throws Exception {
        // Initialize the database
        supplyRepository.saveAndFlush(supply);

        // Get all the supplies
        restSupplyMockMvc.perform(get("/api/supplies?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(supply.getId().intValue())))
                .andExpect(jsonPath("$.[*].serialNumber").value(hasItem(DEFAULT_SERIAL_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(DEFAULT_EXPIRATION_DATE.toString())))
                .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())));
    }

    @Test
    @Transactional
    public void getSupply() throws Exception {
        // Initialize the database
        supplyRepository.saveAndFlush(supply);

        // Get the supply
        restSupplyMockMvc.perform(get("/api/supplies/{id}", supply.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(supply.getId().intValue()))
            .andExpect(jsonPath("$.serialNumber").value(DEFAULT_SERIAL_NUMBER.toString()))
            .andExpect(jsonPath("$.expirationDate").value(DEFAULT_EXPIRATION_DATE.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSupply() throws Exception {
        // Get the supply
        restSupplyMockMvc.perform(get("/api/supplies/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSupply() throws Exception {
        // Initialize the database
        supplyRepository.saveAndFlush(supply);
        int databaseSizeBeforeUpdate = supplyRepository.findAll().size();

        // Update the supply
        Supply updatedSupply = supplyRepository.findOne(supply.getId());
        updatedSupply
                .serialNumber(UPDATED_SERIAL_NUMBER)
                .expirationDate(UPDATED_EXPIRATION_DATE)
                .amount(UPDATED_AMOUNT);
        SupplyDTO supplyDTO = supplyMapper.supplyToSupplyDTO(updatedSupply);

        restSupplyMockMvc.perform(put("/api/supplies")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(supplyDTO)))
                .andExpect(status().isOk());

        // Validate the Supply in the database
        List<Supply> supplies = supplyRepository.findAll();
        assertThat(supplies).hasSize(databaseSizeBeforeUpdate);
        Supply testSupply = supplies.get(supplies.size() - 1);
        assertThat(testSupply.getSerialNumber()).isEqualTo(UPDATED_SERIAL_NUMBER);
        assertThat(testSupply.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testSupply.getAmount()).isEqualTo(UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void deleteSupply() throws Exception {
        // Initialize the database
        supplyRepository.saveAndFlush(supply);
        int databaseSizeBeforeDelete = supplyRepository.findAll().size();

        // Get the supply
        restSupplyMockMvc.perform(delete("/api/supplies/{id}", supply.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Supply> supplies = supplyRepository.findAll();
        assertThat(supplies).hasSize(databaseSizeBeforeDelete - 1);
    }
}
