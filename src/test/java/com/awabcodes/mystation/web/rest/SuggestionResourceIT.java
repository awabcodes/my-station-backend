package com.awabcodes.mystation.web.rest;

import com.awabcodes.mystation.MyStationApp;
import com.awabcodes.mystation.domain.Suggestion;
import com.awabcodes.mystation.repository.SuggestionRepository;
import com.awabcodes.mystation.service.SuggestionService;
import com.awabcodes.mystation.web.rest.errors.ExceptionTranslator;
import com.awabcodes.mystation.service.dto.SuggestionCriteria;
import com.awabcodes.mystation.service.SuggestionQueryService;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.awabcodes.mystation.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link SuggestionResource} REST controller.
 */
@SpringBootTest(classes = MyStationApp.class)
public class SuggestionResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    @Autowired
    private SuggestionRepository suggestionRepository;

    @Autowired
    private SuggestionService suggestionService;

    @Autowired
    private SuggestionQueryService suggestionQueryService;

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

    private MockMvc restSuggestionMockMvc;

    private Suggestion suggestion;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SuggestionResource suggestionResource = new SuggestionResource(suggestionService, suggestionQueryService);
        this.restSuggestionMockMvc = MockMvcBuilders.standaloneSetup(suggestionResource)
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
    public static Suggestion createEntity(EntityManager em) {
        Suggestion suggestion = new Suggestion()
            .title(DEFAULT_TITLE)
            .message(DEFAULT_MESSAGE)
            .date(DEFAULT_DATE);
        return suggestion;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Suggestion createUpdatedEntity(EntityManager em) {
        Suggestion suggestion = new Suggestion()
            .title(UPDATED_TITLE)
            .message(UPDATED_MESSAGE)
            .date(UPDATED_DATE);
        return suggestion;
    }

    @BeforeEach
    public void initTest() {
        suggestion = createEntity(em);
    }

    @Test
    @Transactional
    public void createSuggestion() throws Exception {
        int databaseSizeBeforeCreate = suggestionRepository.findAll().size();

        // Create the Suggestion
        restSuggestionMockMvc.perform(post("/api/suggestions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(suggestion)))
            .andExpect(status().isCreated());

        // Validate the Suggestion in the database
        List<Suggestion> suggestionList = suggestionRepository.findAll();
        assertThat(suggestionList).hasSize(databaseSizeBeforeCreate + 1);
        Suggestion testSuggestion = suggestionList.get(suggestionList.size() - 1);
        assertThat(testSuggestion.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testSuggestion.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(testSuggestion.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void createSuggestionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = suggestionRepository.findAll().size();

        // Create the Suggestion with an existing ID
        suggestion.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSuggestionMockMvc.perform(post("/api/suggestions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(suggestion)))
            .andExpect(status().isBadRequest());

        // Validate the Suggestion in the database
        List<Suggestion> suggestionList = suggestionRepository.findAll();
        assertThat(suggestionList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = suggestionRepository.findAll().size();
        // set the field null
        suggestion.setTitle(null);

        // Create the Suggestion, which fails.

        restSuggestionMockMvc.perform(post("/api/suggestions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(suggestion)))
            .andExpect(status().isBadRequest());

        List<Suggestion> suggestionList = suggestionRepository.findAll();
        assertThat(suggestionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMessageIsRequired() throws Exception {
        int databaseSizeBeforeTest = suggestionRepository.findAll().size();
        // set the field null
        suggestion.setMessage(null);

        // Create the Suggestion, which fails.

        restSuggestionMockMvc.perform(post("/api/suggestions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(suggestion)))
            .andExpect(status().isBadRequest());

        List<Suggestion> suggestionList = suggestionRepository.findAll();
        assertThat(suggestionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = suggestionRepository.findAll().size();
        // set the field null
        suggestion.setDate(null);

        // Create the Suggestion, which fails.

        restSuggestionMockMvc.perform(post("/api/suggestions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(suggestion)))
            .andExpect(status().isBadRequest());

        List<Suggestion> suggestionList = suggestionRepository.findAll();
        assertThat(suggestionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSuggestions() throws Exception {
        // Initialize the database
        suggestionRepository.saveAndFlush(suggestion);

        // Get all the suggestionList
        restSuggestionMockMvc.perform(get("/api/suggestions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(suggestion.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getSuggestion() throws Exception {
        // Initialize the database
        suggestionRepository.saveAndFlush(suggestion);

        // Get the suggestion
        restSuggestionMockMvc.perform(get("/api/suggestions/{id}", suggestion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(suggestion.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }


    @Test
    @Transactional
    public void getSuggestionsByIdFiltering() throws Exception {
        // Initialize the database
        suggestionRepository.saveAndFlush(suggestion);

        Long id = suggestion.getId();

        defaultSuggestionShouldBeFound("id.equals=" + id);
        defaultSuggestionShouldNotBeFound("id.notEquals=" + id);

        defaultSuggestionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSuggestionShouldNotBeFound("id.greaterThan=" + id);

        defaultSuggestionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSuggestionShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllSuggestionsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        suggestionRepository.saveAndFlush(suggestion);

        // Get all the suggestionList where title equals to DEFAULT_TITLE
        defaultSuggestionShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the suggestionList where title equals to UPDATED_TITLE
        defaultSuggestionShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllSuggestionsByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        suggestionRepository.saveAndFlush(suggestion);

        // Get all the suggestionList where title not equals to DEFAULT_TITLE
        defaultSuggestionShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the suggestionList where title not equals to UPDATED_TITLE
        defaultSuggestionShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllSuggestionsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        suggestionRepository.saveAndFlush(suggestion);

        // Get all the suggestionList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultSuggestionShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the suggestionList where title equals to UPDATED_TITLE
        defaultSuggestionShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllSuggestionsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        suggestionRepository.saveAndFlush(suggestion);

        // Get all the suggestionList where title is not null
        defaultSuggestionShouldBeFound("title.specified=true");

        // Get all the suggestionList where title is null
        defaultSuggestionShouldNotBeFound("title.specified=false");
    }
                @Test
    @Transactional
    public void getAllSuggestionsByTitleContainsSomething() throws Exception {
        // Initialize the database
        suggestionRepository.saveAndFlush(suggestion);

        // Get all the suggestionList where title contains DEFAULT_TITLE
        defaultSuggestionShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the suggestionList where title contains UPDATED_TITLE
        defaultSuggestionShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllSuggestionsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        suggestionRepository.saveAndFlush(suggestion);

        // Get all the suggestionList where title does not contain DEFAULT_TITLE
        defaultSuggestionShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the suggestionList where title does not contain UPDATED_TITLE
        defaultSuggestionShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }


    @Test
    @Transactional
    public void getAllSuggestionsByMessageIsEqualToSomething() throws Exception {
        // Initialize the database
        suggestionRepository.saveAndFlush(suggestion);

        // Get all the suggestionList where message equals to DEFAULT_MESSAGE
        defaultSuggestionShouldBeFound("message.equals=" + DEFAULT_MESSAGE);

        // Get all the suggestionList where message equals to UPDATED_MESSAGE
        defaultSuggestionShouldNotBeFound("message.equals=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    public void getAllSuggestionsByMessageIsNotEqualToSomething() throws Exception {
        // Initialize the database
        suggestionRepository.saveAndFlush(suggestion);

        // Get all the suggestionList where message not equals to DEFAULT_MESSAGE
        defaultSuggestionShouldNotBeFound("message.notEquals=" + DEFAULT_MESSAGE);

        // Get all the suggestionList where message not equals to UPDATED_MESSAGE
        defaultSuggestionShouldBeFound("message.notEquals=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    public void getAllSuggestionsByMessageIsInShouldWork() throws Exception {
        // Initialize the database
        suggestionRepository.saveAndFlush(suggestion);

        // Get all the suggestionList where message in DEFAULT_MESSAGE or UPDATED_MESSAGE
        defaultSuggestionShouldBeFound("message.in=" + DEFAULT_MESSAGE + "," + UPDATED_MESSAGE);

        // Get all the suggestionList where message equals to UPDATED_MESSAGE
        defaultSuggestionShouldNotBeFound("message.in=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    public void getAllSuggestionsByMessageIsNullOrNotNull() throws Exception {
        // Initialize the database
        suggestionRepository.saveAndFlush(suggestion);

        // Get all the suggestionList where message is not null
        defaultSuggestionShouldBeFound("message.specified=true");

        // Get all the suggestionList where message is null
        defaultSuggestionShouldNotBeFound("message.specified=false");
    }
                @Test
    @Transactional
    public void getAllSuggestionsByMessageContainsSomething() throws Exception {
        // Initialize the database
        suggestionRepository.saveAndFlush(suggestion);

        // Get all the suggestionList where message contains DEFAULT_MESSAGE
        defaultSuggestionShouldBeFound("message.contains=" + DEFAULT_MESSAGE);

        // Get all the suggestionList where message contains UPDATED_MESSAGE
        defaultSuggestionShouldNotBeFound("message.contains=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    public void getAllSuggestionsByMessageNotContainsSomething() throws Exception {
        // Initialize the database
        suggestionRepository.saveAndFlush(suggestion);

        // Get all the suggestionList where message does not contain DEFAULT_MESSAGE
        defaultSuggestionShouldNotBeFound("message.doesNotContain=" + DEFAULT_MESSAGE);

        // Get all the suggestionList where message does not contain UPDATED_MESSAGE
        defaultSuggestionShouldBeFound("message.doesNotContain=" + UPDATED_MESSAGE);
    }


    @Test
    @Transactional
    public void getAllSuggestionsByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        suggestionRepository.saveAndFlush(suggestion);

        // Get all the suggestionList where date equals to DEFAULT_DATE
        defaultSuggestionShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the suggestionList where date equals to UPDATED_DATE
        defaultSuggestionShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllSuggestionsByDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        suggestionRepository.saveAndFlush(suggestion);

        // Get all the suggestionList where date not equals to DEFAULT_DATE
        defaultSuggestionShouldNotBeFound("date.notEquals=" + DEFAULT_DATE);

        // Get all the suggestionList where date not equals to UPDATED_DATE
        defaultSuggestionShouldBeFound("date.notEquals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllSuggestionsByDateIsInShouldWork() throws Exception {
        // Initialize the database
        suggestionRepository.saveAndFlush(suggestion);

        // Get all the suggestionList where date in DEFAULT_DATE or UPDATED_DATE
        defaultSuggestionShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the suggestionList where date equals to UPDATED_DATE
        defaultSuggestionShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllSuggestionsByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        suggestionRepository.saveAndFlush(suggestion);

        // Get all the suggestionList where date is not null
        defaultSuggestionShouldBeFound("date.specified=true");

        // Get all the suggestionList where date is null
        defaultSuggestionShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    public void getAllSuggestionsByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        suggestionRepository.saveAndFlush(suggestion);

        // Get all the suggestionList where date is greater than or equal to DEFAULT_DATE
        defaultSuggestionShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the suggestionList where date is greater than or equal to UPDATED_DATE
        defaultSuggestionShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllSuggestionsByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        suggestionRepository.saveAndFlush(suggestion);

        // Get all the suggestionList where date is less than or equal to DEFAULT_DATE
        defaultSuggestionShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the suggestionList where date is less than or equal to SMALLER_DATE
        defaultSuggestionShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    public void getAllSuggestionsByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        suggestionRepository.saveAndFlush(suggestion);

        // Get all the suggestionList where date is less than DEFAULT_DATE
        defaultSuggestionShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the suggestionList where date is less than UPDATED_DATE
        defaultSuggestionShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllSuggestionsByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        suggestionRepository.saveAndFlush(suggestion);

        // Get all the suggestionList where date is greater than DEFAULT_DATE
        defaultSuggestionShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the suggestionList where date is greater than SMALLER_DATE
        defaultSuggestionShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSuggestionShouldBeFound(String filter) throws Exception {
        restSuggestionMockMvc.perform(get("/api/suggestions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(suggestion.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));

        // Check, that the count call also returns 1
        restSuggestionMockMvc.perform(get("/api/suggestions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSuggestionShouldNotBeFound(String filter) throws Exception {
        restSuggestionMockMvc.perform(get("/api/suggestions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSuggestionMockMvc.perform(get("/api/suggestions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingSuggestion() throws Exception {
        // Get the suggestion
        restSuggestionMockMvc.perform(get("/api/suggestions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSuggestion() throws Exception {
        // Initialize the database
        suggestionService.save(suggestion);

        int databaseSizeBeforeUpdate = suggestionRepository.findAll().size();

        // Update the suggestion
        Suggestion updatedSuggestion = suggestionRepository.findById(suggestion.getId()).get();
        // Disconnect from session so that the updates on updatedSuggestion are not directly saved in db
        em.detach(updatedSuggestion);
        updatedSuggestion
            .title(UPDATED_TITLE)
            .message(UPDATED_MESSAGE)
            .date(UPDATED_DATE);

        restSuggestionMockMvc.perform(put("/api/suggestions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSuggestion)))
            .andExpect(status().isOk());

        // Validate the Suggestion in the database
        List<Suggestion> suggestionList = suggestionRepository.findAll();
        assertThat(suggestionList).hasSize(databaseSizeBeforeUpdate);
        Suggestion testSuggestion = suggestionList.get(suggestionList.size() - 1);
        assertThat(testSuggestion.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSuggestion.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testSuggestion.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingSuggestion() throws Exception {
        int databaseSizeBeforeUpdate = suggestionRepository.findAll().size();

        // Create the Suggestion

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSuggestionMockMvc.perform(put("/api/suggestions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(suggestion)))
            .andExpect(status().isBadRequest());

        // Validate the Suggestion in the database
        List<Suggestion> suggestionList = suggestionRepository.findAll();
        assertThat(suggestionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSuggestion() throws Exception {
        // Initialize the database
        suggestionService.save(suggestion);

        int databaseSizeBeforeDelete = suggestionRepository.findAll().size();

        // Delete the suggestion
        restSuggestionMockMvc.perform(delete("/api/suggestions/{id}", suggestion.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Suggestion> suggestionList = suggestionRepository.findAll();
        assertThat(suggestionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
