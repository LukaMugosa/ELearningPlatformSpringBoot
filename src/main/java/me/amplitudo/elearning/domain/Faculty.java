package me.amplitudo.elearning.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Faculty.
 */
@Entity
@Table(name = "faculty")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Faculty implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "date_created")
    private Instant dateCreated;

    @Column(name = "date_updated")
    private Instant dateUpdated;

    @OneToMany(mappedBy = "faculty")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Orientation> orientations = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "faculty_orientation_faculties",
               joinColumns = @JoinColumn(name = "faculty_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "orientation_faculties_id", referencedColumnName = "id"))
    private Set<Orientation> orientationFaculties = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "faculty_users",
               joinColumns = @JoinColumn(name = "faculty_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "users_id", referencedColumnName = "id"))
    private Set<Profile> users = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "faculties", allowSetters = true)
    private Building building;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Faculty name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public Faculty dateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Instant getDateUpdated() {
        return dateUpdated;
    }

    public Faculty dateUpdated(Instant dateUpdated) {
        this.dateUpdated = dateUpdated;
        return this;
    }

    public void setDateUpdated(Instant dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public Set<Orientation> getOrientations() {
        return orientations;
    }

    public Faculty orientations(Set<Orientation> orientations) {
        this.orientations = orientations;
        return this;
    }

    public Faculty addOrientations(Orientation orientation) {
        this.orientations.add(orientation);
        orientation.setFaculty(this);
        return this;
    }

    public Faculty removeOrientations(Orientation orientation) {
        this.orientations.remove(orientation);
        orientation.setFaculty(null);
        return this;
    }

    public void setOrientations(Set<Orientation> orientations) {
        this.orientations = orientations;
    }

    public Set<Orientation> getOrientationFaculties() {
        return orientationFaculties;
    }

    public Faculty orientationFaculties(Set<Orientation> orientations) {
        this.orientationFaculties = orientations;
        return this;
    }

    public Faculty addOrientationFaculties(Orientation orientation) {
        this.orientationFaculties.add(orientation);
        orientation.getFaculties().add(this);
        return this;
    }

    public Faculty removeOrientationFaculties(Orientation orientation) {
        this.orientationFaculties.remove(orientation);
        orientation.getFaculties().remove(this);
        return this;
    }

    public void setOrientationFaculties(Set<Orientation> orientations) {
        this.orientationFaculties = orientations;
    }

    public Set<Profile> getUsers() {
        return users;
    }

    public Faculty users(Set<Profile> profiles) {
        this.users = profiles;
        return this;
    }

    public Faculty addUsers(Profile profile) {
        this.users.add(profile);
        profile.getFaculties().add(this);
        return this;
    }

    public Faculty removeUsers(Profile profile) {
        this.users.remove(profile);
        profile.getFaculties().remove(this);
        return this;
    }

    public void setUsers(Set<Profile> profiles) {
        this.users = profiles;
    }

    public Building getBuilding() {
        return building;
    }

    public Faculty building(Building building) {
        this.building = building;
        return this;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Faculty)) {
            return false;
        }
        return id != null && id.equals(((Faculty) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Faculty{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", dateUpdated='" + getDateUpdated() + "'" +
            "}";
    }
}
