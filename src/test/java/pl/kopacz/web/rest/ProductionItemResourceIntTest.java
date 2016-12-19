package pl.kopacz.web.rest;

import pl.kopacz.CompanyApp;

import pl.kopacz.domain.ProductionItem;
import pl.kopacz.domain.Product;
import pl.kopacz.domain.Production;
import pl.kopacz.repository.ProductionItemRepository;
import pl.kopacz.service.ProductionItemService;
import pl.kopacz.service.dto.ProductionItemDTO;
import pl.kopacz.service.mapper.ProductionItemMapper;

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
 * Test class for the ProductionItemResource REST controller.
 *
 * @see ProductionItemResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CompanyApp.class)
public class ProductionItemResourceIntTest {

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    @Inject
    private ProductionItemRepository productionItemRepository;

    @Inject
    private ProductionItemMapper productionItemMapper;

    @Inject
    private ProductionItemService productionItemService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restProductionItemMockMvc;

    private ProductionItem productionItem;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProductionItemResource productionItemResource = new ProductionItemResource();
        ReflectionTestUtils.setField(productionItemResource, "productionItemService", productionItemService);
        this.restProductionItemMockMvc = MockMvcBuilders.standaloneSetup(productionItemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductionItem createEntity(EntityManager em) {
        ProductionItem productionItem = new ProductionItem()
                .amount(DEFAULT_AMOUNT);
        // Add required entity
        Product product = ProductResourceIntTest.createEntity(em);
        em.persist(product);
        em.flush();
        productionItem.setProduct(product);
        // Add required entity
        Production production = ProductionResourceIntTest.createEntity(em);
        em.persist(production);
        em.flush();
        productionItem.setProduction(production);
        return productionItem;
    }

    @Before
    public void initTest() {
        productionItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createProductionItem() throws Exception {
        int databaseSizeBeforeCreate = productionItemRepository.findAll().size();

        // Create the ProductionItem
        ProductionItemDTO productionItemDTO = productionItemMapper.productionItemToProductionItemDTO(productionItem);

        restProductionItemMockMvc.perform(post("/api/production-items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(productionItemDTO)))
                .andExpect(status().isCreated());

        // Validate the ProductionItem in the database
        List<ProductionItem> productionItems = productionItemRepository.findAll();
        assertThat(productionItems).hasSize(databaseSizeBeforeCreate + 1);
        ProductionItem testProductionItem = productionItems.get(productionItems.size() - 1);
        assertThat(testProductionItem.getAmount()).isEqualTo(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = productionItemRepository.findAll().size();
        // set the field null
        productionItem.setAmount(null);

        // Create the ProductionItem, which fails.
        ProductionItemDTO productionItemDTO = productionItemMapper.productionItemToProductionItemDTO(productionItem);

        restProductionItemMockMvc.perform(post("/api/production-items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(productionItemDTO)))
                .andExpect(status().isBadRequest());

        List<ProductionItem> productionItems = productionItemRepository.findAll();
        assertThat(productionItems).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProductionItems() throws Exception {
        // Initialize the database
        productionItemRepository.saveAndFlush(productionItem);

        // Get all the productionItems
        restProductionItemMockMvc.perform(get("/api/production-items?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(productionItem.getId().intValue())))
                .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())));
    }

    @Test
    @Transactional
    public void getProductionItem() throws Exception {
        // Initialize the database
        productionItemRepository.saveAndFlush(productionItem);

        // Get the productionItem
        restProductionItemMockMvc.perform(get("/api/production-items/{id}", productionItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(productionItem.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingProductionItem() throws Exception {
        // Get the productionItem
        restProductionItemMockMvc.perform(get("/api/production-items/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProductionItem() throws Exception {
        // Initialize the database
        productionItemRepository.saveAndFlush(productionItem);
        int databaseSizeBeforeUpdate = productionItemRepository.findAll().size();

        // Update the productionItem
        ProductionItem updatedProductionItem = productionItemRepository.findOne(productionItem.getId());
        updatedProductionItem
                .amount(UPDATED_AMOUNT);
        ProductionItemDTO productionItemDTO = productionItemMapper.productionItemToProductionItemDTO(updatedProductionItem);

        restProductionItemMockMvc.perform(put("/api/production-items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(productionItemDTO)))
                .andExpect(status().isOk());

        // Validate the ProductionItem in the database
        List<ProductionItem> productionItems = productionItemRepository.findAll();
        assertThat(productionItems).hasSize(databaseSizeBeforeUpdate);
        ProductionItem testProductionItem = productionItems.get(productionItems.size() - 1);
        assertThat(testProductionItem.getAmount()).isEqualTo(UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void deleteProductionItem() throws Exception {
        // Initialize the database
        productionItemRepository.saveAndFlush(productionItem);
        int databaseSizeBeforeDelete = productionItemRepository.findAll().size();

        // Get the productionItem
        restProductionItemMockMvc.perform(delete("/api/production-items/{id}", productionItem.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ProductionItem> productionItems = productionItemRepository.findAll();
        assertThat(productionItems).hasSize(databaseSizeBeforeDelete - 1);
    }
}
