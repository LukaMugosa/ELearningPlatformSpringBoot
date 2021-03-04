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
 * A Course.
 */
@Entity
@Table(name = "course")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Course implements Serializable {

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

    @OneToMany(mappedBy = "course")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Notification> notifications = new HashSet<>();

    @OneToMany(mappedBy = "course")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Assignment> assignments = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "courses", allowSetters = true)
    private Profile professor;

    @ManyToOne
    @JsonIgnoreProperties(value = "courses", allowSetters = true)
    private Profile assistant;

    @ManyToOne
    @JsonIgnoreProperties(value = "courses", allowSetters = true)
    private Year year;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "course_orientations",
               joinColumns = @JoinColumn(name = "course_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "orientations_id", referencedColumnName = "id"))
    private Set<Orientation> orientations = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "course_users",
               joinColumns = @JoinColumn(name = "course_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "users_id", referencedColumnName = "id"))
    private Set<Profile> users = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "courses", allowSetters = true)
    private Lecture lectures;

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

    public Course name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public Course dateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Instant getDateUpdated() {
        return dateUpdated;
    }

    public Course dateUpdated(Instant dateUpdated) {
        this.dateUpdated = dateUpdated;
        return this;
    }

    public void setDateUpdated(Instant dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public Set<Notification> getNotifications() {
        return notifications;
    }

    public Course notifications(Set<Notification> notifications) {
        this.notifications = notifications;
        return this;
    }

    public Course addNotifications(Notification notification) {
        this.notifications.add(notification);
        notification.setCourse(this);
        return this;
    }

    public Course removeNotifications(Notification notification) {
        this.notifications.remove(notification);
        notification.setCourse(null);
        return this;
    }

    public void setNotifications(Set<Notification> notifications) {
        this.notifications = notifications;
    }

    public Set<Assignment> getAssignments() {
        return assignments;
    }

    public Course assignments(Set<Assignment> assignments) {
        this.assignments = assignments;
        return this;
    }

    public Course addAssignments(Assignment assignment) {
        this.assignments.add(assignment);
        assignment.setCourse(this);
        return this;
    }

    public Course removeAssignments(Assignment assignment) {
        this.assignments.remove(assignment);
        assignment.setCourse(null);
        return this;
    }

    public void setAssignments(Set<Assignment> assignments) {
        this.assignments = assignments;
    }

    public Profile getProfessor() {
        return professor;
    }

    public Course professor(Profile profile) {
        this.professor = profile;
        return this;
    }

    public void setProfessor(Profile profile) {
        this.professor = profile;
    }

    public Profile getAssistant() {
        return assistant;
    }

    public Course assistant(Profile profile) {
        this.assistant = profile;
        return this;
    }

    public void setAssistant(Profile profile) {
        this.assistant = profile;
    }

    public Year getYear() {
        return year;
    }

    public Course year(Year year) {
        this.year = year;
        return this;
    }

    public void setYear(Year year) {
        this.year = year;
    }

    public Set<Orientation> getOrientations() {
        return orientations;
    }

    public Course orientations(Set<Orientation> orientations) {
        this.orientations = orientations;
        return this;
    }

    public Course addOrientations(Orientation orientation) {
        this.orientations.add(orientation);
        orientation.getCourses().add(this);
        return this;
    }

    public Course removeOrientations(Orientation orientation) {
        this.orientations.remove(orientation);
        orientation.getCourses().remove(this);
        return this;
    }

    public void setOrientations(Set<Orientation> orientations) {
        this.orientations = orientations;
    }

    public Set<Profile> getUsers() {
        return users;
    }

    public Course users(Set<Profile> profiles) {
        this.users = profiles;
        return this;
    }

    public Course addUsers(Profile profile) {
        this.users.add(profile);
        profile.getCourses().add(this);
        return this;
    }

    public Course removeUsers(Profile profile) {
        this.users.remove(profile);
        profile.getCourses().remove(this);
        return this;
    }

    public void setUsers(Set<Profile> profiles) {
        this.users = profiles;
    }

    public Lecture getLectures() {
        return lectures;
    }

    public Course lectures(Lecture lecture) {
        this.lectures = lecture;
        return this;
    }

    public void setLectures(Lecture lecture) {
        this.lectures = lecture;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Course)) {
            return false;
        }
        return id != null && id.equals(((Course) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Course{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", dateUpdated='" + getDateUpdated() + "'" +
            "}";
    }
}
