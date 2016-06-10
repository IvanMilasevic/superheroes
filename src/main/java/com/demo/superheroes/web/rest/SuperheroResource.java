package com.demo.superheroes.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.demo.superheroes.domain.Superhero;
import com.demo.superheroes.repository.SuperheroRepository;
import com.demo.superheroes.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Superhero.
 */
@RestController
@RequestMapping("/api")
public class SuperheroResource {

    private final Logger log = LoggerFactory.getLogger(SuperheroResource.class);
        
    @Inject
    private SuperheroRepository superheroRepository;
    
    /**
     * POST  /superheroes : Create a new superhero.
     *
     * @param superhero the superhero to create
     * @return the ResponseEntity with status 201 (Created) and with body the new superhero, or with status 400 (Bad Request) if the superhero has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/superheroes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Superhero> createSuperhero(@RequestBody Superhero superhero) throws URISyntaxException {
        log.debug("REST request to save Superhero : {}", superhero);
        if (superhero.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("superhero", "idexists", "A new superhero cannot already have an ID")).body(null);
        }
        Superhero result = superheroRepository.save(superhero);
        return ResponseEntity.created(new URI("/api/superheroes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("superhero", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /superheroes : Updates an existing superhero.
     *
     * @param superhero the superhero to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated superhero,
     * or with status 400 (Bad Request) if the superhero is not valid,
     * or with status 500 (Internal Server Error) if the superhero couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/superheroes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Superhero> updateSuperhero(@RequestBody Superhero superhero) throws URISyntaxException {
        log.debug("REST request to update Superhero : {}", superhero);
        if (superhero.getId() == null) {
            return createSuperhero(superhero);
        }
        Superhero result = superheroRepository.save(superhero);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("superhero", superhero.getId().toString()))
            .body(result);
    }

    /**
     * GET  /superheroes : get all the superheroes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of superheroes in body
     */
    @RequestMapping(value = "/superheroes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Superhero> getAllSuperheroes() {
        log.debug("REST request to get all Superheroes");
        List<Superhero> superheroes = superheroRepository.findAllWithEagerRelationships();
        return superheroes;
    }

    /**
     * GET  /superheroes/:id : get the "id" superhero.
     *
     * @param id the id of the superhero to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the superhero, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/superheroes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Superhero> getSuperhero(@PathVariable Long id) {
        log.debug("REST request to get Superhero : {}", id);
        Superhero superhero = superheroRepository.findOneWithEagerRelationships(id);
        return Optional.ofNullable(superhero)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /superheroes/:id : delete the "id" superhero.
     *
     * @param id the id of the superhero to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/superheroes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSuperhero(@PathVariable Long id) {
        log.debug("REST request to delete Superhero : {}", id);
        superheroRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("superhero", id.toString())).build();
    }

}
