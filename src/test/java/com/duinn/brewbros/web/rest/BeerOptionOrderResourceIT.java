package com.duinn.brewbros.web.rest;

import com.duinn.brewbros.BrewbrosApp;
import com.duinn.brewbros.domain.BeerOptionOrder;
import com.duinn.brewbros.repository.BeerOptionOrderRepository;
import com.duinn.brewbros.service.BeerOptionOrderService;
import com.duinn.brewbros.web.rest.errors.ExceptionTranslator;

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

import static com.duinn.brewbros.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link BeerOptionOrderResource} REST controller.
 */
@SpringBootTest(classes = BrewbrosApp.class)
public class BeerOptionOrderResourceIT {

    private static final Integer DEFAULT_AMOUNT = 1;
    private static final Integer UPDATED_AMOUNT = 2;

    @Autowired
    private BeerOptionOrderRepository beerOptionOrderRepository;

    @Autowired
    private BeerOptionOrderService beerOptionOrderService;

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

    private MockMvc restBeerOptionOrderMockMvc;

    private BeerOptionOrder beerOptionOrder;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BeerOptionOrderResource beerOptionOrderResource = new BeerOptionOrderResource(beerOptionOrderService);
        this.restBeerOptionOrderMockMvc = MockMvcBuilders.standaloneSetup(beerOptionOrderResource)
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
    public static BeerOptionOrder createEntity(EntityManager em) {
        BeerOptionOrder beerOptionOrder = new BeerOptionOrder()
            .amount(DEFAULT_AMOUNT);
        return beerOptionOrder;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BeerOptionOrder createUpdatedEntity(EntityManager em) {
        BeerOptionOrder beerOptionOrder = new BeerOptionOrder()
            .amount(UPDATED_AMOUNT);
        return beerOptionOrder;
    }

    @BeforeEach
    public void initTest() {
        beerOptionOrder = createEntity(em);
    }

    @Test
    @Transactional
    public void createBeerOptionOrder() throws Exception {
        int databaseSizeBeforeCreate = beerOptionOrderRepository.findAll().size();

        // Create the BeerOptionOrder
        restBeerOptionOrderMockMvc.perform(post("/api/beer-option-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(beerOptionOrder)))
            .andExpect(status().isCreated());

        // Validate the BeerOptionOrder in the database
        List<BeerOptionOrder> beerOptionOrderList = beerOptionOrderRepository.findAll();
        assertThat(beerOptionOrderList).hasSize(databaseSizeBeforeCreate + 1);
        BeerOptionOrder testBeerOptionOrder = beerOptionOrderList.get(beerOptionOrderList.size() - 1);
        assertThat(testBeerOptionOrder.getAmount()).isEqualTo(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    public void createBeerOptionOrderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = beerOptionOrderRepository.findAll().size();

        // Create the BeerOptionOrder with an existing ID
        beerOptionOrder.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBeerOptionOrderMockMvc.perform(post("/api/beer-option-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(beerOptionOrder)))
            .andExpect(status().isBadRequest());

        // Validate the BeerOptionOrder in the database
        List<BeerOptionOrder> beerOptionOrderList = beerOptionOrderRepository.findAll();
        assertThat(beerOptionOrderList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllBeerOptionOrders() throws Exception {
        // Initialize the database
        beerOptionOrderRepository.saveAndFlush(beerOptionOrder);

        // Get all the beerOptionOrderList
        restBeerOptionOrderMockMvc.perform(get("/api/beer-option-orders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(beerOptionOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT)));
    }
    
    @Test
    @Transactional
    public void getBeerOptionOrder() throws Exception {
        // Initialize the database
        beerOptionOrderRepository.saveAndFlush(beerOptionOrder);

        // Get the beerOptionOrder
        restBeerOptionOrderMockMvc.perform(get("/api/beer-option-orders/{id}", beerOptionOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(beerOptionOrder.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT));
    }

    @Test
    @Transactional
    public void getNonExistingBeerOptionOrder() throws Exception {
        // Get the beerOptionOrder
        restBeerOptionOrderMockMvc.perform(get("/api/beer-option-orders/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBeerOptionOrder() throws Exception {
        // Initialize the database
        beerOptionOrderService.save(beerOptionOrder);

        int databaseSizeBeforeUpdate = beerOptionOrderRepository.findAll().size();

        // Update the beerOptionOrder
        BeerOptionOrder updatedBeerOptionOrder = beerOptionOrderRepository.findById(beerOptionOrder.getId()).get();
        // Disconnect from session so that the updates on updatedBeerOptionOrder are not directly saved in db
        em.detach(updatedBeerOptionOrder);
        updatedBeerOptionOrder
            .amount(UPDATED_AMOUNT);

        restBeerOptionOrderMockMvc.perform(put("/api/beer-option-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBeerOptionOrder)))
            .andExpect(status().isOk());

        // Validate the BeerOptionOrder in the database
        List<BeerOptionOrder> beerOptionOrderList = beerOptionOrderRepository.findAll();
        assertThat(beerOptionOrderList).hasSize(databaseSizeBeforeUpdate);
        BeerOptionOrder testBeerOptionOrder = beerOptionOrderList.get(beerOptionOrderList.size() - 1);
        assertThat(testBeerOptionOrder.getAmount()).isEqualTo(UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void updateNonExistingBeerOptionOrder() throws Exception {
        int databaseSizeBeforeUpdate = beerOptionOrderRepository.findAll().size();

        // Create the BeerOptionOrder

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBeerOptionOrderMockMvc.perform(put("/api/beer-option-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(beerOptionOrder)))
            .andExpect(status().isBadRequest());

        // Validate the BeerOptionOrder in the database
        List<BeerOptionOrder> beerOptionOrderList = beerOptionOrderRepository.findAll();
        assertThat(beerOptionOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBeerOptionOrder() throws Exception {
        // Initialize the database
        beerOptionOrderService.save(beerOptionOrder);

        int databaseSizeBeforeDelete = beerOptionOrderRepository.findAll().size();

        // Delete the beerOptionOrder
        restBeerOptionOrderMockMvc.perform(delete("/api/beer-option-orders/{id}", beerOptionOrder.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<BeerOptionOrder> beerOptionOrderList = beerOptionOrderRepository.findAll();
        assertThat(beerOptionOrderList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BeerOptionOrder.class);
        BeerOptionOrder beerOptionOrder1 = new BeerOptionOrder();
        beerOptionOrder1.setId(1L);
        BeerOptionOrder beerOptionOrder2 = new BeerOptionOrder();
        beerOptionOrder2.setId(beerOptionOrder1.getId());
        assertThat(beerOptionOrder1).isEqualTo(beerOptionOrder2);
        beerOptionOrder2.setId(2L);
        assertThat(beerOptionOrder1).isNotEqualTo(beerOptionOrder2);
        beerOptionOrder1.setId(null);
        assertThat(beerOptionOrder1).isNotEqualTo(beerOptionOrder2);
    }
}
