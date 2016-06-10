package com.demo.superheroes.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Superhero.
 */
@Entity
@Table(name = "superhero")
public class Superhero implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "pseudonym")
    private String pseudonym;

    @Column(name = "date_of_appearance")
    private ZonedDateTime dateOfAppearance;

    @ManyToOne
    private Publisher publisher;

    @ManyToMany
    @JoinTable(name = "superhero_skill",
               joinColumns = @JoinColumn(name="superhero_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="skill_id", referencedColumnName="ID"))
    private Set<Skill> skills = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "superhero_ally_with",
               joinColumns = @JoinColumn(name="superhero_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="superhero_ally_id", referencedColumnName="ID"))
    private Set<Superhero> allyWiths = new HashSet<>();

    @ManyToMany(mappedBy = "allyWiths")
    @JsonIgnore
    private Set<Superhero> allyTos = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPseudonym() {
        return pseudonym;
    }

    public void setPseudonym(String pseudonym) {
        this.pseudonym = pseudonym;
    }

    public ZonedDateTime getDateOfAppearance() {
        return dateOfAppearance;
    }

    public void setDateOfAppearance(ZonedDateTime dateOfAppearance) {
        this.dateOfAppearance = dateOfAppearance;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public Set<Skill> getSkills() {
        return skills;
    }

    public void setSkills(Set<Skill> skills) {
        this.skills = skills;
    }

    public Set<Superhero> getAllyWiths() {
        return allyWiths;
    }

    public void setAllyWiths(Set<Superhero> superheroes) {
        this.allyWiths = superheroes;
    }

    public Set<Superhero> getAllyTos() {
        return allyTos;
    }

    public void setAllyTos(Set<Superhero> superheroes) {
        this.allyTos = superheroes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Superhero superhero = (Superhero) o;
        if(superhero.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, superhero.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Superhero{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", pseudonym='" + pseudonym + "'" +
            ", dateOfAppearance='" + dateOfAppearance + "'" +
            '}';
    }
}
