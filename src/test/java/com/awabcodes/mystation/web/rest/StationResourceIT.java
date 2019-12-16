package com.awabcodes.mystation.web.rest;

import com.awabcodes.mystation.MyStationApp;
import com.awabcodes.mystation.domain.Station;
import com.awabcodes.mystation.repository.StationRepository;
import com.awabcodes.mystation.service.StationService;
import com.awabcodes.mystation.web.rest.errors.ExceptionTranslator;
import com.awabcodes.mystation.service.dto.StationCriteria;
import com.awabcodes.mystation.service.StationQueryService;

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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.awabcodes.mystation.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link StationResource} REST controller.
 */
@SpringBootTest(classes = MyStationApp.class)
public class StationResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_GAS_LEVEL = 0;
    private static final Integer UPDATED_GAS_LEVEL = 1;
    private static final Integer SMALLER_GAS_LEVEL = 0 - 1;

    private static final Integer DEFAULT_BENZENE_LEVEL = 0;
    private static final Integer UPDATED_BENZENE_LEVEL = 1;
    private static final Integer SMALLER_BENZENE_LEVEL = 0 - 1;

    private static final Instant DEFAULT_LAST_TANK_FILL = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_TANK_FILL = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_MAP_URL = "AAAAAAAAAA";
    private static final String UPDATED_MAP_URL = "BBBBBBBBBB";

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private StationService stationService;

    @Autowired
    private StationQueryService stationQueryService;

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

    private MockMvc restStationMockMvc;

    private Station station;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StationResource stationResource = new StationResource(stationService, stationQueryService);
        this.restStationMockMvc = MockMvcBuilders.standaloneSetup(stationResource)
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
    public static Station createEntity(EntityManager em) {
        Station station = new Station()
            .name(DEFAULT_NAME)
            .gasLevel(DEFAULT_GAS_LEVEL)
            .benzeneLevel(DEFAULT_BENZENE_LEVEL)
            .lastTankFill(DEFAULT_LAST_TANK_FILL)
            .city(DEFAULT_CITY)
            .location(DEFAULT_LOCATION)
            .mapUrl(DEFAULT_MAP_URL);
        return station;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Station createUpdatedEntity(EntityManager em) {
        Station station = new Station()
            .name(UPDATED_NAME)
            .gasLevel(UPDATED_GAS_LEVEL)
            .benzeneLevel(UPDATED_BENZENE_LEVEL)
            .lastTankFill(UPDATED_LAST_TANK_FILL)
            .city(UPDATED_CITY)
            .location(UPDATED_LOCATION)
            .mapUrl(UPDATED_MAP_URL);
        return station;
    }

    @BeforeEach
    public void initTest() {
        station = createEntity(em);
    }

    @Test
    @Transactional
    public void createStation() throws Exception {
        int databaseSizeBeforeCreate = stationRepository.findAll().size();

        // Create the Station
        restStationMockMvc.perform(post("/api/stations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(station)))
            .andExpect(status().isCreated());

        // Validate the Station in the database
        List<Station> stationList = stationRepository.findAll();
        assertThat(stationList).hasSize(databaseSizeBeforeCreate + 1);
        Station testStation = stationList.get(stationList.size() - 1);
        assertThat(testStation.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testStation.getGasLevel()).isEqualTo(DEFAULT_GAS_LEVEL);
        assertThat(testStation.getBenzeneLevel()).isEqualTo(DEFAULT_BENZENE_LEVEL);
        assertThat(testStation.getLastTankFill()).isEqualTo(DEFAULT_LAST_TANK_FILL);
        assertThat(testStation.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testStation.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testStation.getMapUrl()).isEqualTo(DEFAULT_MAP_URL);
    }

    @Test
    @Transactional
    public void createStationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stationRepository.findAll().size();

        // Create the Station with an existing ID
        station.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStationMockMvc.perform(post("/api/stations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(station)))
            .andExpect(status().isBadRequest());

        // Validate the Station in the database
        List<Station> stationList = stationRepository.findAll();
        assertThat(stationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = stationRepository.findAll().size();
        // set the field null
        station.setName(null);

        // Create the Station, which fails.

        restStationMockMvc.perform(post("/api/stations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(station)))
            .andExpect(status().isBadRequest());

        List<Station> stationList = stationRepository.findAll();
        assertThat(stationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGasLevelIsRequired() throws Exception {
        int databaseSizeBeforeTest = stationRepository.findAll().size();
        // set the field null
        station.setGasLevel(null);

        // Create the Station, which fails.

        restStationMockMvc.perform(post("/api/stations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(station)))
            .andExpect(status().isBadRequest());

        List<Station> stationList = stationRepository.findAll();
        assertThat(stationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBenzeneLevelIsRequired() throws Exception {
        int databaseSizeBeforeTest = stationRepository.findAll().size();
        // set the field null
        station.setBenzeneLevel(null);

        // Create the Station, which fails.

        restStationMockMvc.perform(post("/api/stations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(station)))
            .andExpect(status().isBadRequest());

        List<Station> stationList = stationRepository.findAll();
        assertThat(stationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastTankFillIsRequired() throws Exception {
        int databaseSizeBeforeTest = stationRepository.findAll().size();
        // set the field null
        station.setLastTankFill(null);

        // Create the Station, which fails.

        restStationMockMvc.perform(post("/api/stations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(station)))
            .andExpect(status().isBadRequest());

        List<Station> stationList = stationRepository.findAll();
        assertThat(stationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = stationRepository.findAll().size();
        // set the field null
        station.setCity(null);

        // Create the Station, which fails.

        restStationMockMvc.perform(post("/api/stations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(station)))
            .andExpect(status().isBadRequest());

        List<Station> stationList = stationRepository.findAll();
        assertThat(stationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLocationIsRequired() throws Exception {
        int databaseSizeBeforeTest = stationRepository.findAll().size();
        // set the field null
        station.setLocation(null);

        // Create the Station, which fails.

        restStationMockMvc.perform(post("/api/stations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(station)))
            .andExpect(status().isBadRequest());

        List<Station> stationList = stationRepository.findAll();
        assertThat(stationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMapUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = stationRepository.findAll().size();
        // set the field null
        station.setMapUrl(null);

        // Create the Station, which fails.

        restStationMockMvc.perform(post("/api/stations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(station)))
            .andExpect(status().isBadRequest());

        List<Station> stationList = stationRepository.findAll();
        assertThat(stationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStations() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList
        restStationMockMvc.perform(get("/api/stations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(station.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].gasLevel").value(hasItem(DEFAULT_GAS_LEVEL)))
            .andExpect(jsonPath("$.[*].benzeneLevel").value(hasItem(DEFAULT_BENZENE_LEVEL)))
            .andExpect(jsonPath("$.[*].lastTankFill").value(hasItem(DEFAULT_LAST_TANK_FILL.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].mapUrl").value(hasItem(DEFAULT_MAP_URL)));
    }
    
    @Test
    @Transactional
    public void getStation() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get the station
        restStationMockMvc.perform(get("/api/stations/{id}", station.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(station.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.gasLevel").value(DEFAULT_GAS_LEVEL))
            .andExpect(jsonPath("$.benzeneLevel").value(DEFAULT_BENZENE_LEVEL))
            .andExpect(jsonPath("$.lastTankFill").value(DEFAULT_LAST_TANK_FILL.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.mapUrl").value(DEFAULT_MAP_URL));
    }


    @Test
    @Transactional
    public void getStationsByIdFiltering() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        Long id = station.getId();

        defaultStationShouldBeFound("id.equals=" + id);
        defaultStationShouldNotBeFound("id.notEquals=" + id);

        defaultStationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultStationShouldNotBeFound("id.greaterThan=" + id);

        defaultStationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultStationShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllStationsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where name equals to DEFAULT_NAME
        defaultStationShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the stationList where name equals to UPDATED_NAME
        defaultStationShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllStationsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where name not equals to DEFAULT_NAME
        defaultStationShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the stationList where name not equals to UPDATED_NAME
        defaultStationShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllStationsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where name in DEFAULT_NAME or UPDATED_NAME
        defaultStationShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the stationList where name equals to UPDATED_NAME
        defaultStationShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllStationsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where name is not null
        defaultStationShouldBeFound("name.specified=true");

        // Get all the stationList where name is null
        defaultStationShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllStationsByNameContainsSomething() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where name contains DEFAULT_NAME
        defaultStationShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the stationList where name contains UPDATED_NAME
        defaultStationShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllStationsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where name does not contain DEFAULT_NAME
        defaultStationShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the stationList where name does not contain UPDATED_NAME
        defaultStationShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllStationsByGasLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where gasLevel equals to DEFAULT_GAS_LEVEL
        defaultStationShouldBeFound("gasLevel.equals=" + DEFAULT_GAS_LEVEL);

        // Get all the stationList where gasLevel equals to UPDATED_GAS_LEVEL
        defaultStationShouldNotBeFound("gasLevel.equals=" + UPDATED_GAS_LEVEL);
    }

    @Test
    @Transactional
    public void getAllStationsByGasLevelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where gasLevel not equals to DEFAULT_GAS_LEVEL
        defaultStationShouldNotBeFound("gasLevel.notEquals=" + DEFAULT_GAS_LEVEL);

        // Get all the stationList where gasLevel not equals to UPDATED_GAS_LEVEL
        defaultStationShouldBeFound("gasLevel.notEquals=" + UPDATED_GAS_LEVEL);
    }

    @Test
    @Transactional
    public void getAllStationsByGasLevelIsInShouldWork() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where gasLevel in DEFAULT_GAS_LEVEL or UPDATED_GAS_LEVEL
        defaultStationShouldBeFound("gasLevel.in=" + DEFAULT_GAS_LEVEL + "," + UPDATED_GAS_LEVEL);

        // Get all the stationList where gasLevel equals to UPDATED_GAS_LEVEL
        defaultStationShouldNotBeFound("gasLevel.in=" + UPDATED_GAS_LEVEL);
    }

    @Test
    @Transactional
    public void getAllStationsByGasLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where gasLevel is not null
        defaultStationShouldBeFound("gasLevel.specified=true");

        // Get all the stationList where gasLevel is null
        defaultStationShouldNotBeFound("gasLevel.specified=false");
    }

    @Test
    @Transactional
    public void getAllStationsByGasLevelIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where gasLevel is greater than or equal to DEFAULT_GAS_LEVEL
        defaultStationShouldBeFound("gasLevel.greaterThanOrEqual=" + DEFAULT_GAS_LEVEL);

        // Get all the stationList where gasLevel is greater than or equal to (DEFAULT_GAS_LEVEL + 1)
        defaultStationShouldNotBeFound("gasLevel.greaterThanOrEqual=" + (DEFAULT_GAS_LEVEL + 1));
    }

    @Test
    @Transactional
    public void getAllStationsByGasLevelIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where gasLevel is less than or equal to DEFAULT_GAS_LEVEL
        defaultStationShouldBeFound("gasLevel.lessThanOrEqual=" + DEFAULT_GAS_LEVEL);

        // Get all the stationList where gasLevel is less than or equal to SMALLER_GAS_LEVEL
        defaultStationShouldNotBeFound("gasLevel.lessThanOrEqual=" + SMALLER_GAS_LEVEL);
    }

    @Test
    @Transactional
    public void getAllStationsByGasLevelIsLessThanSomething() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where gasLevel is less than DEFAULT_GAS_LEVEL
        defaultStationShouldNotBeFound("gasLevel.lessThan=" + DEFAULT_GAS_LEVEL);

        // Get all the stationList where gasLevel is less than (DEFAULT_GAS_LEVEL + 1)
        defaultStationShouldBeFound("gasLevel.lessThan=" + (DEFAULT_GAS_LEVEL + 1));
    }

    @Test
    @Transactional
    public void getAllStationsByGasLevelIsGreaterThanSomething() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where gasLevel is greater than DEFAULT_GAS_LEVEL
        defaultStationShouldNotBeFound("gasLevel.greaterThan=" + DEFAULT_GAS_LEVEL);

        // Get all the stationList where gasLevel is greater than SMALLER_GAS_LEVEL
        defaultStationShouldBeFound("gasLevel.greaterThan=" + SMALLER_GAS_LEVEL);
    }


    @Test
    @Transactional
    public void getAllStationsByBenzeneLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where benzeneLevel equals to DEFAULT_BENZENE_LEVEL
        defaultStationShouldBeFound("benzeneLevel.equals=" + DEFAULT_BENZENE_LEVEL);

        // Get all the stationList where benzeneLevel equals to UPDATED_BENZENE_LEVEL
        defaultStationShouldNotBeFound("benzeneLevel.equals=" + UPDATED_BENZENE_LEVEL);
    }

    @Test
    @Transactional
    public void getAllStationsByBenzeneLevelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where benzeneLevel not equals to DEFAULT_BENZENE_LEVEL
        defaultStationShouldNotBeFound("benzeneLevel.notEquals=" + DEFAULT_BENZENE_LEVEL);

        // Get all the stationList where benzeneLevel not equals to UPDATED_BENZENE_LEVEL
        defaultStationShouldBeFound("benzeneLevel.notEquals=" + UPDATED_BENZENE_LEVEL);
    }

    @Test
    @Transactional
    public void getAllStationsByBenzeneLevelIsInShouldWork() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where benzeneLevel in DEFAULT_BENZENE_LEVEL or UPDATED_BENZENE_LEVEL
        defaultStationShouldBeFound("benzeneLevel.in=" + DEFAULT_BENZENE_LEVEL + "," + UPDATED_BENZENE_LEVEL);

        // Get all the stationList where benzeneLevel equals to UPDATED_BENZENE_LEVEL
        defaultStationShouldNotBeFound("benzeneLevel.in=" + UPDATED_BENZENE_LEVEL);
    }

    @Test
    @Transactional
    public void getAllStationsByBenzeneLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where benzeneLevel is not null
        defaultStationShouldBeFound("benzeneLevel.specified=true");

        // Get all the stationList where benzeneLevel is null
        defaultStationShouldNotBeFound("benzeneLevel.specified=false");
    }

    @Test
    @Transactional
    public void getAllStationsByBenzeneLevelIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where benzeneLevel is greater than or equal to DEFAULT_BENZENE_LEVEL
        defaultStationShouldBeFound("benzeneLevel.greaterThanOrEqual=" + DEFAULT_BENZENE_LEVEL);

        // Get all the stationList where benzeneLevel is greater than or equal to (DEFAULT_BENZENE_LEVEL + 1)
        defaultStationShouldNotBeFound("benzeneLevel.greaterThanOrEqual=" + (DEFAULT_BENZENE_LEVEL + 1));
    }

    @Test
    @Transactional
    public void getAllStationsByBenzeneLevelIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where benzeneLevel is less than or equal to DEFAULT_BENZENE_LEVEL
        defaultStationShouldBeFound("benzeneLevel.lessThanOrEqual=" + DEFAULT_BENZENE_LEVEL);

        // Get all the stationList where benzeneLevel is less than or equal to SMALLER_BENZENE_LEVEL
        defaultStationShouldNotBeFound("benzeneLevel.lessThanOrEqual=" + SMALLER_BENZENE_LEVEL);
    }

    @Test
    @Transactional
    public void getAllStationsByBenzeneLevelIsLessThanSomething() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where benzeneLevel is less than DEFAULT_BENZENE_LEVEL
        defaultStationShouldNotBeFound("benzeneLevel.lessThan=" + DEFAULT_BENZENE_LEVEL);

        // Get all the stationList where benzeneLevel is less than (DEFAULT_BENZENE_LEVEL + 1)
        defaultStationShouldBeFound("benzeneLevel.lessThan=" + (DEFAULT_BENZENE_LEVEL + 1));
    }

    @Test
    @Transactional
    public void getAllStationsByBenzeneLevelIsGreaterThanSomething() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where benzeneLevel is greater than DEFAULT_BENZENE_LEVEL
        defaultStationShouldNotBeFound("benzeneLevel.greaterThan=" + DEFAULT_BENZENE_LEVEL);

        // Get all the stationList where benzeneLevel is greater than SMALLER_BENZENE_LEVEL
        defaultStationShouldBeFound("benzeneLevel.greaterThan=" + SMALLER_BENZENE_LEVEL);
    }


    @Test
    @Transactional
    public void getAllStationsByLastTankFillIsEqualToSomething() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where lastTankFill equals to DEFAULT_LAST_TANK_FILL
        defaultStationShouldBeFound("lastTankFill.equals=" + DEFAULT_LAST_TANK_FILL);

        // Get all the stationList where lastTankFill equals to UPDATED_LAST_TANK_FILL
        defaultStationShouldNotBeFound("lastTankFill.equals=" + UPDATED_LAST_TANK_FILL);
    }

    @Test
    @Transactional
    public void getAllStationsByLastTankFillIsNotEqualToSomething() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where lastTankFill not equals to DEFAULT_LAST_TANK_FILL
        defaultStationShouldNotBeFound("lastTankFill.notEquals=" + DEFAULT_LAST_TANK_FILL);

        // Get all the stationList where lastTankFill not equals to UPDATED_LAST_TANK_FILL
        defaultStationShouldBeFound("lastTankFill.notEquals=" + UPDATED_LAST_TANK_FILL);
    }

    @Test
    @Transactional
    public void getAllStationsByLastTankFillIsInShouldWork() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where lastTankFill in DEFAULT_LAST_TANK_FILL or UPDATED_LAST_TANK_FILL
        defaultStationShouldBeFound("lastTankFill.in=" + DEFAULT_LAST_TANK_FILL + "," + UPDATED_LAST_TANK_FILL);

        // Get all the stationList where lastTankFill equals to UPDATED_LAST_TANK_FILL
        defaultStationShouldNotBeFound("lastTankFill.in=" + UPDATED_LAST_TANK_FILL);
    }

    @Test
    @Transactional
    public void getAllStationsByLastTankFillIsNullOrNotNull() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where lastTankFill is not null
        defaultStationShouldBeFound("lastTankFill.specified=true");

        // Get all the stationList where lastTankFill is null
        defaultStationShouldNotBeFound("lastTankFill.specified=false");
    }

    @Test
    @Transactional
    public void getAllStationsByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where city equals to DEFAULT_CITY
        defaultStationShouldBeFound("city.equals=" + DEFAULT_CITY);

        // Get all the stationList where city equals to UPDATED_CITY
        defaultStationShouldNotBeFound("city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllStationsByCityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where city not equals to DEFAULT_CITY
        defaultStationShouldNotBeFound("city.notEquals=" + DEFAULT_CITY);

        // Get all the stationList where city not equals to UPDATED_CITY
        defaultStationShouldBeFound("city.notEquals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllStationsByCityIsInShouldWork() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where city in DEFAULT_CITY or UPDATED_CITY
        defaultStationShouldBeFound("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY);

        // Get all the stationList where city equals to UPDATED_CITY
        defaultStationShouldNotBeFound("city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllStationsByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where city is not null
        defaultStationShouldBeFound("city.specified=true");

        // Get all the stationList where city is null
        defaultStationShouldNotBeFound("city.specified=false");
    }
                @Test
    @Transactional
    public void getAllStationsByCityContainsSomething() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where city contains DEFAULT_CITY
        defaultStationShouldBeFound("city.contains=" + DEFAULT_CITY);

        // Get all the stationList where city contains UPDATED_CITY
        defaultStationShouldNotBeFound("city.contains=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllStationsByCityNotContainsSomething() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where city does not contain DEFAULT_CITY
        defaultStationShouldNotBeFound("city.doesNotContain=" + DEFAULT_CITY);

        // Get all the stationList where city does not contain UPDATED_CITY
        defaultStationShouldBeFound("city.doesNotContain=" + UPDATED_CITY);
    }


    @Test
    @Transactional
    public void getAllStationsByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where location equals to DEFAULT_LOCATION
        defaultStationShouldBeFound("location.equals=" + DEFAULT_LOCATION);

        // Get all the stationList where location equals to UPDATED_LOCATION
        defaultStationShouldNotBeFound("location.equals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void getAllStationsByLocationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where location not equals to DEFAULT_LOCATION
        defaultStationShouldNotBeFound("location.notEquals=" + DEFAULT_LOCATION);

        // Get all the stationList where location not equals to UPDATED_LOCATION
        defaultStationShouldBeFound("location.notEquals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void getAllStationsByLocationIsInShouldWork() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where location in DEFAULT_LOCATION or UPDATED_LOCATION
        defaultStationShouldBeFound("location.in=" + DEFAULT_LOCATION + "," + UPDATED_LOCATION);

        // Get all the stationList where location equals to UPDATED_LOCATION
        defaultStationShouldNotBeFound("location.in=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void getAllStationsByLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where location is not null
        defaultStationShouldBeFound("location.specified=true");

        // Get all the stationList where location is null
        defaultStationShouldNotBeFound("location.specified=false");
    }
                @Test
    @Transactional
    public void getAllStationsByLocationContainsSomething() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where location contains DEFAULT_LOCATION
        defaultStationShouldBeFound("location.contains=" + DEFAULT_LOCATION);

        // Get all the stationList where location contains UPDATED_LOCATION
        defaultStationShouldNotBeFound("location.contains=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void getAllStationsByLocationNotContainsSomething() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where location does not contain DEFAULT_LOCATION
        defaultStationShouldNotBeFound("location.doesNotContain=" + DEFAULT_LOCATION);

        // Get all the stationList where location does not contain UPDATED_LOCATION
        defaultStationShouldBeFound("location.doesNotContain=" + UPDATED_LOCATION);
    }


    @Test
    @Transactional
    public void getAllStationsByMapUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where mapUrl equals to DEFAULT_MAP_URL
        defaultStationShouldBeFound("mapUrl.equals=" + DEFAULT_MAP_URL);

        // Get all the stationList where mapUrl equals to UPDATED_MAP_URL
        defaultStationShouldNotBeFound("mapUrl.equals=" + UPDATED_MAP_URL);
    }

    @Test
    @Transactional
    public void getAllStationsByMapUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where mapUrl not equals to DEFAULT_MAP_URL
        defaultStationShouldNotBeFound("mapUrl.notEquals=" + DEFAULT_MAP_URL);

        // Get all the stationList where mapUrl not equals to UPDATED_MAP_URL
        defaultStationShouldBeFound("mapUrl.notEquals=" + UPDATED_MAP_URL);
    }

    @Test
    @Transactional
    public void getAllStationsByMapUrlIsInShouldWork() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where mapUrl in DEFAULT_MAP_URL or UPDATED_MAP_URL
        defaultStationShouldBeFound("mapUrl.in=" + DEFAULT_MAP_URL + "," + UPDATED_MAP_URL);

        // Get all the stationList where mapUrl equals to UPDATED_MAP_URL
        defaultStationShouldNotBeFound("mapUrl.in=" + UPDATED_MAP_URL);
    }

    @Test
    @Transactional
    public void getAllStationsByMapUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where mapUrl is not null
        defaultStationShouldBeFound("mapUrl.specified=true");

        // Get all the stationList where mapUrl is null
        defaultStationShouldNotBeFound("mapUrl.specified=false");
    }
                @Test
    @Transactional
    public void getAllStationsByMapUrlContainsSomething() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where mapUrl contains DEFAULT_MAP_URL
        defaultStationShouldBeFound("mapUrl.contains=" + DEFAULT_MAP_URL);

        // Get all the stationList where mapUrl contains UPDATED_MAP_URL
        defaultStationShouldNotBeFound("mapUrl.contains=" + UPDATED_MAP_URL);
    }

    @Test
    @Transactional
    public void getAllStationsByMapUrlNotContainsSomething() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where mapUrl does not contain DEFAULT_MAP_URL
        defaultStationShouldNotBeFound("mapUrl.doesNotContain=" + DEFAULT_MAP_URL);

        // Get all the stationList where mapUrl does not contain UPDATED_MAP_URL
        defaultStationShouldBeFound("mapUrl.doesNotContain=" + UPDATED_MAP_URL);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultStationShouldBeFound(String filter) throws Exception {
        restStationMockMvc.perform(get("/api/stations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(station.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].gasLevel").value(hasItem(DEFAULT_GAS_LEVEL)))
            .andExpect(jsonPath("$.[*].benzeneLevel").value(hasItem(DEFAULT_BENZENE_LEVEL)))
            .andExpect(jsonPath("$.[*].lastTankFill").value(hasItem(DEFAULT_LAST_TANK_FILL.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].mapUrl").value(hasItem(DEFAULT_MAP_URL)));

        // Check, that the count call also returns 1
        restStationMockMvc.perform(get("/api/stations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultStationShouldNotBeFound(String filter) throws Exception {
        restStationMockMvc.perform(get("/api/stations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStationMockMvc.perform(get("/api/stations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingStation() throws Exception {
        // Get the station
        restStationMockMvc.perform(get("/api/stations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStation() throws Exception {
        // Initialize the database
        stationService.save(station);

        int databaseSizeBeforeUpdate = stationRepository.findAll().size();

        // Update the station
        Station updatedStation = stationRepository.findById(station.getId()).get();
        // Disconnect from session so that the updates on updatedStation are not directly saved in db
        em.detach(updatedStation);
        updatedStation
            .name(UPDATED_NAME)
            .gasLevel(UPDATED_GAS_LEVEL)
            .benzeneLevel(UPDATED_BENZENE_LEVEL)
            .lastTankFill(UPDATED_LAST_TANK_FILL)
            .city(UPDATED_CITY)
            .location(UPDATED_LOCATION)
            .mapUrl(UPDATED_MAP_URL);

        restStationMockMvc.perform(put("/api/stations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedStation)))
            .andExpect(status().isOk());

        // Validate the Station in the database
        List<Station> stationList = stationRepository.findAll();
        assertThat(stationList).hasSize(databaseSizeBeforeUpdate);
        Station testStation = stationList.get(stationList.size() - 1);
        assertThat(testStation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStation.getGasLevel()).isEqualTo(UPDATED_GAS_LEVEL);
        assertThat(testStation.getBenzeneLevel()).isEqualTo(UPDATED_BENZENE_LEVEL);
        assertThat(testStation.getLastTankFill()).isEqualTo(UPDATED_LAST_TANK_FILL);
        assertThat(testStation.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testStation.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testStation.getMapUrl()).isEqualTo(UPDATED_MAP_URL);
    }

    @Test
    @Transactional
    public void updateNonExistingStation() throws Exception {
        int databaseSizeBeforeUpdate = stationRepository.findAll().size();

        // Create the Station

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStationMockMvc.perform(put("/api/stations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(station)))
            .andExpect(status().isBadRequest());

        // Validate the Station in the database
        List<Station> stationList = stationRepository.findAll();
        assertThat(stationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteStation() throws Exception {
        // Initialize the database
        stationService.save(station);

        int databaseSizeBeforeDelete = stationRepository.findAll().size();

        // Delete the station
        restStationMockMvc.perform(delete("/api/stations/{id}", station.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Station> stationList = stationRepository.findAll();
        assertThat(stationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
