package com.duinn.brewbros.web.rest;

import com.duinn.brewbros.BrewbrosApp;
import com.duinn.brewbros.domain.BeerOption;
import com.duinn.brewbros.repository.BeerOptionRepository;
import com.duinn.brewbros.service.BeerOptionService;
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
 * Integration tests for the {@Link BeerOptionResource} REST controller.
 */
@SpringBootTest(classes = BrewbrosApp.class)
public class BeerOptionResourceIT {

    private static final Integer DEFAULT_AMOUNT = 0;
    private static final Integer UPDATED_AMOUNT = 1;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_BRAND = "AAAAAAAAAA";
    private static final String UPDATED_BRAND = "BBBBBBBBBB";

    private static final Integer DEFAULT_VOLUME = 1;
    private static final Integer UPDATED_VOLUME = 2;

    private static final Double DEFAULT_ABV = 0D;
    private static final Double UPDATED_ABV = 1D;

    @Autowired
    private BeerOptionRepository beerOptionRepository;

    @Autowired
    private BeerOptionService beerOptionService;

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

    private MockMvc restBeerOptionMockMvc;

    private BeerOption beerOption;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BeerOptionResource beerOptionResource = new BeerOptionResource(beerOptionService);
        this.restBeerOptionMockMvc = MockMvcBuilders.standaloneSetup(beerOptionResource)
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
    public static BeerOption createEntity(EntityManager em) {
        BeerOption beerOption = new BeerOption()
            .amount(DEFAULT_AMOUNT)
            .name(DEFAULT_NAME)
            .brand(DEFAULT_BRAND)
            .volume(DEFAULT_VOLUME)
            .abv(DEFAULT_ABV);
        return beerOption;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BeerOption createUpdatedEntity(EntityManager em) {
        BeerOption beerOption = new BeerOption()
            .amount(UPDATED_AMOUNT)
            .name(UPDATED_NAME)
            .brand(UPDATED_BRAND)
            .volume(UPDATED_VOLUME)
            .abv(UPDATED_ABV);
        return beerOption;
    }

    @BeforeEach
    public void initTest() {
        beerOption = createEntity(em);
    }

    @Test
    @Transactional
    public void createBeerOption() throws Exception {
        int databaseSizeBeforeCreate = beerOptionRepository.findAll().size();

        // Create the BeerOption
        restBeerOptionMockMvc.perform(post("/api/beer-options")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(beerOption)))
            .andExpect(status().isCreated());

        // Validate the BeerOption in the database
        List<BeerOption> beerOptionList = beerOptionRepository.findAll();
        assertThat(beerOptionList).hasSize(databaseSizeBeforeCreate + 1);
        BeerOption testBeerOption = beerOptionList.get(beerOptionList.size() - 1);
        assertThat(testBeerOption.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testBeerOption.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBeerOption.getBrand()).isEqualTo(DEFAULT_BRAND);
        assertThat(testBeerOption.getVolume()).isEqualTo(DEFAULT_VOLUME);
        assertThat(testBeerOption.getAbv()).isEqualTo(DEFAULT_ABV);
    }

    @Test
    @Transactional
    public void createBeerOptionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = beerOptionRepository.findAll().size();

        // Create the BeerOption with an existing ID
        beerOption.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBeerOptionMockMvc.perform(post("/api/beer-options")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(beerOption)))
            .andExpect(status().isBadRequest());

        // Validate the BeerOption in the database
        List<BeerOption> beerOptionList = beerOptionRepository.findAll();
        assertThat(beerOptionList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllBeerOptions() throws Exception {
        // Initialize the database
        beerOptionRepository.saveAndFlush(beerOption);

        // Get all the beerOptionList
        restBeerOptionMockMvc.perform(get("/api/beer-options?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(beerOption.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].brand").value(hasItem(DEFAULT_BRAND.toString())))
            .andExpect(jsonPath("$.[*].volume").value(hasItem(DEFAULT_VOLUME)))
            .andExpect(jsonPath("$.[*].abv").value(hasItem(DEFAULT_ABV.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getBeerOption() throws Exception {
        // Initialize the database
        beerOptionRepository.saveAndFlush(beerOption);

        // Get the beerOption
        restBeerOptionMockMvc.perform(get("/api/beer-options/{id}", beerOption.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(beerOption.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.brand").value(DEFAULT_BRAND.toString()))
            .andExpect(jsonPath("$.volume").value(DEFAULT_VOLUME))
            .andExpect(jsonPath("$.abv").value(DEFAULT_ABV.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingBeerOption() throws Exception {
        // Get the beerOption
        restBeerOptionMockMvc.perform(get("/api/beer-options/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBeerOption() throws Exception {
        // Initialize the database
        beerOptionService.save(beerOption);

        int databaseSizeBeforeUpdate = beerOptionRepository.findAll().size();

        // Update the beerOption
        BeerOption updatedBeerOption = beerOptionRepository.findById(beerOption.getId()).get();
        // Disconnect from session so that the updates on updatedBeerOption are not directly saved in db
        em.detach(updatedBeerOption);
        updatedBeerOption
            .amount(UPDATED_AMOUNT)
            .name(UPDATED_NAME)
            .brand(UPDATED_BRAND)
            .volume(UPDATED_VOLUME)
            .abv(UPDATED_ABV);

        restBeerOptionMockMvc.perform(put("/api/beer-options")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBeerOption)))
            .andExpect(status().isOk());

        // Validate the BeerOption in the database
        List<BeerOption> beerOptionList = beerOptionRepository.findAll();
        assertThat(beerOptionList).hasSize(databaseSizeBeforeUpdate);
        BeerOption testBeerOption = beerOptionList.get(beerOptionList.size() - 1);
        assertThat(testBeerOption.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testBeerOption.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBeerOption.getBrand()).isEqualTo(UPDATED_BRAND);
        assertThat(testBeerOption.getVolume()).isEqualTo(UPDATED_VOLUME);
        assertThat(testBeerOption.getAbv()).isEqualTo(UPDATED_ABV);
    }

    @Test
    @Transactional
    public void updateNonExistingBeerOption() throws Exception {
        int databaseSizeBeforeUpdate = beerOptionRepository.findAll().size();

        // Create the BeerOption

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBeerOptionMockMvc.perform(put("/api/beer-options")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(beerOption)))
            .andExpect(status().isBadRequest());

        // Validate the BeerOption in the database
        List<BeerOption> beerOptionList = beerOptionRepository.findAll();
        assertThat(beerOptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBeerOption() throws Exception {
        // Initialize the database
        beerOptionService.save(beerOption);

        int databaseSizeBeforeDelete = beerOptionRepository.findAll().size();

        // Delete the beerOption
        restBeerOptionMockMvc.perform(delete("/api/beer-options/{id}", beerOption.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<BeerOption> beerOptionList = beerOptionRepository.findAll();
        assertThat(beerOptionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BeerOption.class);
        BeerOption beerOption1 = new BeerOption();
        beerOption1.setId(1L);
        BeerOption beerOption2 = new BeerOption();
        beerOption2.setId(beerOption1.getId());
        assertThat(beerOption1).isEqualTo(beerOption2);
        beerOption2.setId(2L);
        assertThat(beerOption1).isNotEqualTo(beerOption2);
        beerOption1.setId(null);
        assertThat(beerOption1).isNotEqualTo(beerOption2);
    }
}
