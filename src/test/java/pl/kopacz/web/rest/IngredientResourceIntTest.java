package pl.kopacz.web.rest;

import pl.kopacz.CompanyApp;

import pl.kopacz.domain.Ingredient;
import pl.kopacz.domain.Spice;
import pl.kopacz.repository.IngredientRepository;
import pl.kopacz.service.IngredientService;
import pl.kopacz.service.dto.IngredientDTO;
import pl.kopacz.service.mapper.IngredientMapper;

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
 * Test class for the IngredientResource REST controller.
 *
 * @see IngredientResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CompanyApp.class)
public class IngredientResourceIntTest {

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    @Inject
    private IngredientRepository ingredientRepository;

    @Inject
    private IngredientMapper ingredientMapper;

    @Inject
    private IngredientService ingredientService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restIngredientMockMvc;

    private Ingredient ingredient;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        IngredientResource ingredientResource = new IngredientResource();
        ReflectionTestUtils.setField(ingredientResource, "ingredientService", ingredientService);
        this.restIngredientMockMvc = MockMvcBuilders.standaloneSetup(ingredientResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ingredient createEntity(EntityManager em) {
        Ingredient ingredient = new Ingredient()
                .amount(DEFAULT_AMOUNT);
        // Add required entity
        Spice spice = SpiceResourceIntTest.createEntity(em);
        em.persist(spice);
        em.flush();
        ingredient.setSpice(spice);
        return ingredient;
    }

    @Before
    public void initTest() {
        ingredient = createEntity(em);
    }

    @Test
    @Transactional
    public void createIngredient() throws Exception {
        int databaseSizeBeforeCreate = ingredientRepository.findAll().size();

        // Create the Ingredient
        IngredientDTO ingredientDTO = ingredientMapper.ingredientToIngredientDTO(ingredient);

        restIngredientMockMvc.perform(post("/api/ingredients")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ingredientDTO)))
                .andExpect(status().isCreated());

        // Validate the Ingredient in the database
        List<Ingredient> ingredients = ingredientRepository.findAll();
        assertThat(ingredients).hasSize(databaseSizeBeforeCreate + 1);
        Ingredient testIngredient = ingredients.get(ingredients.size() - 1);
        assertThat(testIngredient.getAmount()).isEqualTo(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = ingredientRepository.findAll().size();
        // set the field null
        ingredient.setAmount(null);

        // Create the Ingredient, which fails.
        IngredientDTO ingredientDTO = ingredientMapper.ingredientToIngredientDTO(ingredient);

        restIngredientMockMvc.perform(post("/api/ingredients")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ingredientDTO)))
                .andExpect(status().isBadRequest());

        List<Ingredient> ingredients = ingredientRepository.findAll();
        assertThat(ingredients).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllIngredients() throws Exception {
        // Initialize the database
        ingredientRepository.saveAndFlush(ingredient);

        // Get all the ingredients
        restIngredientMockMvc.perform(get("/api/ingredients?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(ingredient.getId().intValue())))
                .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())));
    }

    @Test
    @Transactional
    public void getIngredient() throws Exception {
        // Initialize the database
        ingredientRepository.saveAndFlush(ingredient);

        // Get the ingredient
        restIngredientMockMvc.perform(get("/api/ingredients/{id}", ingredient.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ingredient.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingIngredient() throws Exception {
        // Get the ingredient
        restIngredientMockMvc.perform(get("/api/ingredients/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIngredient() throws Exception {
        // Initialize the database
        ingredientRepository.saveAndFlush(ingredient);
        int databaseSizeBeforeUpdate = ingredientRepository.findAll().size();

        // Update the ingredient
        Ingredient updatedIngredient = ingredientRepository.findOne(ingredient.getId());
        updatedIngredient
                .amount(UPDATED_AMOUNT);
        IngredientDTO ingredientDTO = ingredientMapper.ingredientToIngredientDTO(updatedIngredient);

        restIngredientMockMvc.perform(put("/api/ingredients")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ingredientDTO)))
                .andExpect(status().isOk());

        // Validate the Ingredient in the database
        List<Ingredient> ingredients = ingredientRepository.findAll();
        assertThat(ingredients).hasSize(databaseSizeBeforeUpdate);
        Ingredient testIngredient = ingredients.get(ingredients.size() - 1);
        assertThat(testIngredient.getAmount()).isEqualTo(UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void deleteIngredient() throws Exception {
        // Initialize the database
        ingredientRepository.saveAndFlush(ingredient);
        int databaseSizeBeforeDelete = ingredientRepository.findAll().size();

        // Get the ingredient
        restIngredientMockMvc.perform(delete("/api/ingredients/{id}", ingredient.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Ingredient> ingredients = ingredientRepository.findAll();
        assertThat(ingredients).hasSize(databaseSizeBeforeDelete - 1);
    }
}
