package com.demo.superheroes.web.rest;

import com.demo.superheroes.SuperheroesApp;
import com.demo.superheroes.domain.Superhero;
import com.demo.superheroes.repository.SuperheroRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the SuperheroResource REST controller.
 *
 * @see SuperheroResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SuperheroesApp.class)
@WebAppConfiguration
@IntegrationTest
public class SuperheroResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_PSEUDONYM = "AAAAA";
    private static final String UPDATED_PSEUDONYM = "BBBBB";

    private static final ZonedDateTime DEFAULT_DATE_OF_APPEARANCE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE_OF_APPEARANCE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_OF_APPEARANCE_STR = dateTimeFormatter.format(DEFAULT_DATE_OF_APPEARANCE);

    @Inject
    private SuperheroRepository superheroRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSuperheroMockMvc;

    private Superhero superhero;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SuperheroResource superheroResource = new SuperheroResource();
        ReflectionTestUtils.setField(superheroResource, "superheroRepository", superheroRepository);
        this.restSuperheroMockMvc = MockMvcBuilders.standaloneSetup(superheroResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        superhero = new Superhero();
        superhero.setName(DEFAULT_NAME);
        superhero.setPseudonym(DEFAULT_PSEUDONYM);
        superhero.setDateOfAppearance(DEFAULT_DATE_OF_APPEARANCE);
    }

    @Test
    @Transactional
    public void createSuperhero() throws Exception {
        int databaseSizeBeforeCreate = superheroRepository.findAll().size();

        // Create the Superhero

        restSuperheroMockMvc.perform(post("/api/superheroes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(superhero)))
                .andExpect(status().isCreated());

        // Validate the Superhero in the database
        List<Superhero> superheroes = superheroRepository.findAll();
        assertThat(superheroes).hasSize(databaseSizeBeforeCreate + 1);
        Superhero testSuperhero = superheroes.get(superheroes.size() - 1);
        assertThat(testSuperhero.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSuperhero.getPseudonym()).isEqualTo(DEFAULT_PSEUDONYM);
        assertThat(testSuperhero.getDateOfAppearance()).isEqualTo(DEFAULT_DATE_OF_APPEARANCE);
    }

    @Test
    @Transactional
    public void getAllSuperheroes() throws Exception {
        // Initialize the database
        superheroRepository.saveAndFlush(superhero);

        // Get all the superheroes
        restSuperheroMockMvc.perform(get("/api/superheroes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(superhero.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].pseudonym").value(hasItem(DEFAULT_PSEUDONYM.toString())))
                .andExpect(jsonPath("$.[*].dateOfAppearance").value(hasItem(DEFAULT_DATE_OF_APPEARANCE_STR)));
    }

    @Test
    @Transactional
    public void getSuperhero() throws Exception {
        // Initialize the database
        superheroRepository.saveAndFlush(superhero);

        // Get the superhero
        restSuperheroMockMvc.perform(get("/api/superheroes/{id}", superhero.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(superhero.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.pseudonym").value(DEFAULT_PSEUDONYM.toString()))
            .andExpect(jsonPath("$.dateOfAppearance").value(DEFAULT_DATE_OF_APPEARANCE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingSuperhero() throws Exception {
        // Get the superhero
        restSuperheroMockMvc.perform(get("/api/superheroes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSuperhero() throws Exception {
        // Initialize the database
        superheroRepository.saveAndFlush(superhero);
        int databaseSizeBeforeUpdate = superheroRepository.findAll().size();

        // Update the superhero
        Superhero updatedSuperhero = new Superhero();
        updatedSuperhero.setId(superhero.getId());
        updatedSuperhero.setName(UPDATED_NAME);
        updatedSuperhero.setPseudonym(UPDATED_PSEUDONYM);
        updatedSuperhero.setDateOfAppearance(UPDATED_DATE_OF_APPEARANCE);

        restSuperheroMockMvc.perform(put("/api/superheroes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedSuperhero)))
                .andExpect(status().isOk());

        // Validate the Superhero in the database
        List<Superhero> superheroes = superheroRepository.findAll();
        assertThat(superheroes).hasSize(databaseSizeBeforeUpdate);
        Superhero testSuperhero = superheroes.get(superheroes.size() - 1);
        assertThat(testSuperhero.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSuperhero.getPseudonym()).isEqualTo(UPDATED_PSEUDONYM);
        assertThat(testSuperhero.getDateOfAppearance()).isEqualTo(UPDATED_DATE_OF_APPEARANCE);
    }

    @Test
    @Transactional
    public void deleteSuperhero() throws Exception {
        // Initialize the database
        superheroRepository.saveAndFlush(superhero);
        int databaseSizeBeforeDelete = superheroRepository.findAll().size();

        // Get the superhero
        restSuperheroMockMvc.perform(delete("/api/superheroes/{id}", superhero.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Superhero> superheroes = superheroRepository.findAll();
        assertThat(superheroes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
