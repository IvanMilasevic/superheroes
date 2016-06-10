package com.demo.superheroes.repository;

import com.demo.superheroes.domain.Superhero;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Superhero entity.
 */
@SuppressWarnings("unused")
public interface SuperheroRepository extends JpaRepository<Superhero,Long> {

    @Query("select distinct superhero from Superhero superhero left join fetch superhero.skills left join fetch superhero.allyWiths")
    List<Superhero> findAllWithEagerRelationships();

    @Query("select superhero from Superhero superhero left join fetch superhero.skills left join fetch superhero.allyWiths where superhero.id =:id")
    Superhero findOneWithEagerRelationships(@Param("id") Long id);

}
