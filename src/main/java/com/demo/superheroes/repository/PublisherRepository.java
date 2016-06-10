package com.demo.superheroes.repository;

import com.demo.superheroes.domain.Publisher;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Publisher entity.
 */
@SuppressWarnings("unused")
public interface PublisherRepository extends JpaRepository<Publisher,Long> {

}
