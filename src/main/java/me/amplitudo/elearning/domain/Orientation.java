package me.amplitudo.elearning.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * A Orientation.
 */
@Entity
@Table(name = "orientation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Orientation implements Serializable {

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
    private Set<Profile> users = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "orientations", allowSetters = true)
    private Faculty faculty;

    @ManyToMany(mappedBy = "orientationFaculties")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<Faculty> faculties = new HashSet<>();

    @ManyToMany(mappedBy = "orientations")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<Course> courses = new HashSet<>();

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

    public Orientation name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public Orientation dateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Instant getDateUpdated() {
        return dateUpdated;
    }

    public Orientation dateUpdated(Instant dateUpdated) {
        this.dateUpdated = dateUpdated;
        return this;
    }

    public void setDateUpdated(Instant dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public Set<Profile> getUsers() {
        return users;
    }

    public Orientation users(Set<Profile> profiles) {
        this.users = profiles;
        return this;
    }

    public Orientation addUsers(Profile profile) {
        this.users.add(profile);
        profile.setFaculty(this);
        return this;
    }

    public Orientation removeUsers(Profile profile) {
        this.users.remove(profile);
        profile.setFaculty(null);
        return this;
    }

    public void setUsers(Set<Profile> profiles) {
        this.users = profiles;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public Orientation faculty(Faculty faculty) {
        this.faculty = faculty;
        return this;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public Set<Faculty> getFaculties() {
        return faculties;
    }

    public Orientation faculties(Set<Faculty> faculties) {
        this.faculties = faculties;
        return this;
    }

    public Orientation addFaculties(Faculty faculty) {
        this.faculties.add(faculty);
        faculty.getOrientationFaculties().add(this);
        return this;
    }

    public Orientation removeFaculties(Faculty faculty) {
        this.faculties.remove(faculty);
        faculty.getOrientationFaculties().remove(this);
        return this;
    }

    public void setFaculties(Set<Faculty> faculties) {
        this.faculties = faculties;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public Orientation courses(Set<Course> courses) {
        this.courses = courses;
        return this;
    }

    public Orientation addCourses(Course course) {
        this.courses.add(course);
        course.getOrientations().add(this);
        return this;
    }

    public Orientation removeCourses(Course course) {
        this.courses.remove(course);
        course.getOrientations().remove(this);
        return this;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Orientation)) {
            return false;
        }
        return id != null && id.equals(((Orientation) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Orientation{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", dateUpdated='" + getDateUpdated() + "'" +
            "}";
    }
}
