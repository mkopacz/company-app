package pl.kopacz.web.rest;

import pl.kopacz.CompanyApp;

import pl.kopacz.domain.Spice;
import pl.kopacz.repository.SpiceRepository;
import pl.kopacz.service.SpiceService;
import pl.kopacz.service.dto.SpiceDTO;
import pl.kopacz.service.mapper.SpiceMapper;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SpiceResource REST controller.
 *
 * @see SpiceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CompanyApp.class)
public class SpiceResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCER = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCER = "BBBBBBBBBB";

    @Inject
    private SpiceRepository spiceRepository;

    @Inject
    private SpiceMapper spiceMapper;

    @Inject
    private SpiceService spiceService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restSpiceMockMvc;

    private Spice spice;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SpiceResource spiceResource = new SpiceResource();
        ReflectionTestUtils.setField(spiceResource, "spiceService", spiceService);
        this.restSpiceMockMvc = MockMvcBuilders.standaloneSetup(spiceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Spice createEntity(EntityManager em) {
        Spice spice = new Spice()
                .name(DEFAULT_NAME)
                .producer(DEFAULT_PRODUCER);
        return spice;
    }

    @Before
    public void initTest() {
        spice = createEntity(em);
    }

    @Test
    @Transactional
    public void createSpice() throws Exception {
        int databaseSizeBeforeCreate = spiceRepository.findAll().size();

        // Create the Spice
        SpiceDTO spiceDTO = spiceMapper.spiceToSpiceDTO(spice);

        restSpiceMockMvc.perform(post("/api/spices")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(spiceDTO)))
                .andExpect(status().isCreated());

        // Validate the Spice in the database
        List<Spice> spices = spiceRepository.findAll();
        assertThat(spices).hasSize(databaseSizeBeforeCreate + 1);
        Spice testSpice = spices.get(spices.size() - 1);
        assertThat(testSpice.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSpice.getProducer()).isEqualTo(DEFAULT_PRODUCER);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = spiceRepository.findAll().size();
        // set the field null
        spice.setName(null);

        // Create the Spice, which fails.
        SpiceDTO spiceDTO = spiceMapper.spiceToSpiceDTO(spice);

        restSpiceMockMvc.perform(post("/api/spices")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(spiceDTO)))
                .andExpect(status().isBadRequest());

        List<Spice> spices = spiceRepository.findAll();
        assertThat(spices).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProducerIsRequired() throws Exception {
        int databaseSizeBeforeTest = spiceRepository.findAll().size();
        // set the field null
        spice.setProducer(null);

        // Create the Spice, which fails.
        SpiceDTO spiceDTO = spiceMapper.spiceToSpiceDTO(spice);

        restSpiceMockMvc.perform(post("/api/spices")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(spiceDTO)))
                .andExpect(status().isBadRequest());

        List<Spice> spices = spiceRepository.findAll();
        assertThat(spices).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSpices() throws Exception {
        // Initialize the database
        spiceRepository.saveAndFlush(spice);

        // Get all the spices
        restSpiceMockMvc.perform(get("/api/spices?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(spice.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].producer").value(hasItem(DEFAULT_PRODUCER.toString())));
    }

    @Test
    @Transactional
    public void getSpice() throws Exception {
        // Initialize the database
        spiceRepository.saveAndFlush(spice);

        // Get the spice
        restSpiceMockMvc.perform(get("/api/spices/{id}", spice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(spice.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.producer").value(DEFAULT_PRODUCER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSpice() throws Exception {
        // Get the spice
        restSpiceMockMvc.perform(get("/api/spices/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSpice() throws Exception {
        // Initialize the database
        spiceRepository.saveAndFlush(spice);
        int databaseSizeBeforeUpdate = spiceRepository.findAll().size();

        // Update the spice
        Spice updatedSpice = spiceRepository.findOne(spice.getId());
        updatedSpice
                .name(UPDATED_NAME)
                .producer(UPDATED_PRODUCER);
        SpiceDTO spiceDTO = spiceMapper.spiceToSpiceDTO(updatedSpice);

        restSpiceMockMvc.perform(put("/api/spices")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(spiceDTO)))
                .andExpect(status().isOk());

        // Validate the Spice in the database
        List<Spice> spices = spiceRepository.findAll();
        assertThat(spices).hasSize(databaseSizeBeforeUpdate);
        Spice testSpice = spices.get(spices.size() - 1);
        assertThat(testSpice.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSpice.getProducer()).isEqualTo(UPDATED_PRODUCER);
    }

    @Test
    @Transactional
    public void deleteSpice() throws Exception {
        // Initialize the database
        spiceRepository.saveAndFlush(spice);
        int databaseSizeBeforeDelete = spiceRepository.findAll().size();

        // Get the spice
        restSpiceMockMvc.perform(delete("/api/spices/{id}", spice.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Spice> spices = spiceRepository.findAll();
        assertThat(spices).hasSize(databaseSizeBeforeDelete - 1);
    }
}
