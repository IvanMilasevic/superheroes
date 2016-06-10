package com.demo.superheroes.web.rest;

import com.demo.superheroes.SuperheroesApp;
import com.demo.superheroes.domain.Publisher;
import com.demo.superheroes.repository.PublisherRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the PublisherResource REST controller.
 *
 * @see PublisherResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SuperheroesApp.class)
@WebAppConfiguration
@IntegrationTest
public class PublisherResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private PublisherRepository publisherRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPublisherMockMvc;

    private Publisher publisher;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PublisherResource publisherResource = new PublisherResource();
        ReflectionTestUtils.setField(publisherResource, "publisherRepository", publisherRepository);
        this.restPublisherMockMvc = MockMvcBuilders.standaloneSetup(publisherResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        publisher = new Publisher();
        publisher.setName(DEFAULT_NAME);
        publisher.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createPublisher() throws Exception {
        int databaseSizeBeforeCreate = publisherRepository.findAll().size();

        // Create the Publisher

        restPublisherMockMvc.perform(post("/api/publishers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(publisher)))
                .andExpect(status().isCreated());

        // Validate the Publisher in the database
        List<Publisher> publishers = publisherRepository.findAll();
        assertThat(publishers).hasSize(databaseSizeBeforeCreate + 1);
        Publisher testPublisher = publishers.get(publishers.size() - 1);
        assertThat(testPublisher.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPublisher.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllPublishers() throws Exception {
        // Initialize the database
        publisherRepository.saveAndFlush(publisher);

        // Get all the publishers
        restPublisherMockMvc.perform(get("/api/publishers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(publisher.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getPublisher() throws Exception {
        // Initialize the database
        publisherRepository.saveAndFlush(publisher);

        // Get the publisher
        restPublisherMockMvc.perform(get("/api/publishers/{id}", publisher.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(publisher.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPublisher() throws Exception {
        // Get the publisher
        restPublisherMockMvc.perform(get("/api/publishers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePublisher() throws Exception {
        // Initialize the database
        publisherRepository.saveAndFlush(publisher);
        int databaseSizeBeforeUpdate = publisherRepository.findAll().size();

        // Update the publisher
        Publisher updatedPublisher = new Publisher();
        updatedPublisher.setId(publisher.getId());
        updatedPublisher.setName(UPDATED_NAME);
        updatedPublisher.setDescription(UPDATED_DESCRIPTION);

        restPublisherMockMvc.perform(put("/api/publishers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPublisher)))
                .andExpect(status().isOk());

        // Validate the Publisher in the database
        List<Publisher> publishers = publisherRepository.findAll();
        assertThat(publishers).hasSize(databaseSizeBeforeUpdate);
        Publisher testPublisher = publishers.get(publishers.size() - 1);
        assertThat(testPublisher.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPublisher.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deletePublisher() throws Exception {
        // Initialize the database
        publisherRepository.saveAndFlush(publisher);
        int databaseSizeBeforeDelete = publisherRepository.findAll().size();

        // Get the publisher
        restPublisherMockMvc.perform(delete("/api/publishers/{id}", publisher.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Publisher> publishers = publisherRepository.findAll();
        assertThat(publishers).hasSize(databaseSizeBeforeDelete - 1);
    }
}
