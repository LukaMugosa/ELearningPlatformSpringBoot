package me.amplitudo.elearning.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Profile.
 */
@Entity
@Table(name = "profile")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Profile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "jhi_index")
    private Integer index;

    @Column(name = "year_of_enrollment")
    private Integer yearOfEnrollment;

    @Column(name = "verification_number")
    private String verificationNumber;

    @Column(name = "is_approved")
    private Boolean isApproved;

    @Column(name = "date_created")
    private Instant dateCreated;

    @Column(name = "date_updated")
    private Instant dateUpdated;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "user")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<AssignmentProfile> assignmentProfiles = new HashSet<>();

    @OneToMany(mappedBy = "user")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Lecture> lectures = new HashSet<>();

    @OneToMany(mappedBy = "user")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Notification> notifications = new HashSet<>();

    @OneToMany(mappedBy = "user")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Assignment> assignments = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "users", allowSetters = true)
    private Year year;

    @ManyToOne
    @JsonIgnoreProperties(value = "users", allowSetters = true)
    private Orientation faculty;

    @ManyToMany(mappedBy = "users")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<Faculty> faculties = new HashSet<>();

    @ManyToMany(mappedBy = "users")
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Profile phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public Profile dateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Integer getIndex() {
        return index;
    }

    public Profile index(Integer index) {
        this.index = index;
        return this;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getYearOfEnrollment() {
        return yearOfEnrollment;
    }

    public Profile yearOfEnrollment(Integer yearOfEnrollment) {
        this.yearOfEnrollment = yearOfEnrollment;
        return this;
    }

    public void setYearOfEnrollment(Integer yearOfEnrollment) {
        this.yearOfEnrollment = yearOfEnrollment;
    }

    public String getVerificationNumber() {
        return verificationNumber;
    }

    public Profile verificationNumber(String verificationNumber) {
        this.verificationNumber = verificationNumber;
        return this;
    }

    public void setVerificationNumber(String verificationNumber) {
        this.verificationNumber = verificationNumber;
    }

    public Boolean isIsApproved() {
        return isApproved;
    }

    public Profile isApproved(Boolean isApproved) {
        this.isApproved = isApproved;
        return this;
    }

    public void setIsApproved(Boolean isApproved) {
        this.isApproved = isApproved;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public Profile dateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Instant getDateUpdated() {
        return dateUpdated;
    }

    public Profile dateUpdated(Instant dateUpdated) {
        this.dateUpdated = dateUpdated;
        return this;
    }

    public void setDateUpdated(Instant dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public User getUser() {
        return user;
    }

    public Profile user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<AssignmentProfile> getAssignmentProfiles() {
        return assignmentProfiles;
    }

    public Profile assignmentProfiles(Set<AssignmentProfile> assignmentProfiles) {
        this.assignmentProfiles = assignmentProfiles;
        return this;
    }

    public Profile addAssignmentProfiles(AssignmentProfile assignmentProfile) {
        this.assignmentProfiles.add(assignmentProfile);
        assignmentProfile.setUser(this);
        return this;
    }

    public Profile removeAssignmentProfiles(AssignmentProfile assignmentProfile) {
        this.assignmentProfiles.remove(assignmentProfile);
        assignmentProfile.setUser(null);
        return this;
    }

    public void setAssignmentProfiles(Set<AssignmentProfile> assignmentProfiles) {
        this.assignmentProfiles = assignmentProfiles;
    }

    public Set<Lecture> getLectures() {
        return lectures;
    }

    public Profile lectures(Set<Lecture> lectures) {
        this.lectures = lectures;
        return this;
    }

    public Profile addLectures(Lecture lecture) {
        this.lectures.add(lecture);
        lecture.setUser(this);
        return this;
    }

    public Profile removeLectures(Lecture lecture) {
        this.lectures.remove(lecture);
        lecture.setUser(null);
        return this;
    }

    public void setLectures(Set<Lecture> lectures) {
        this.lectures = lectures;
    }

    public Set<Notification> getNotifications() {
        return notifications;
    }

    public Profile notifications(Set<Notification> notifications) {
        this.notifications = notifications;
        return this;
    }

    public Profile addNotifications(Notification notification) {
        this.notifications.add(notification);
        notification.setUser(this);
        return this;
    }

    public Profile removeNotifications(Notification notification) {
        this.notifications.remove(notification);
        notification.setUser(null);
        return this;
    }

    public void setNotifications(Set<Notification> notifications) {
        this.notifications = notifications;
    }

    public Set<Assignment> getAssignments() {
        return assignments;
    }

    public Profile assignments(Set<Assignment> assignments) {
        this.assignments = assignments;
        return this;
    }

    public Profile addAssignments(Assignment assignment) {
        this.assignments.add(assignment);
        assignment.setUser(this);
        return this;
    }

    public Profile removeAssignments(Assignment assignment) {
        this.assignments.remove(assignment);
        assignment.setUser(null);
        return this;
    }

    public void setAssignments(Set<Assignment> assignments) {
        this.assignments = assignments;
    }

    public Year getYear() {
        return year;
    }

    public Profile year(Year year) {
        this.year = year;
        return this;
    }

    public void setYear(Year year) {
        this.year = year;
    }

    public Orientation getFaculty() {
        return faculty;
    }

    public Profile faculty(Orientation orientation) {
        this.faculty = orientation;
        return this;
    }

    public void setFaculty(Orientation orientation) {
        this.faculty = orientation;
    }

    public Set<Faculty> getFaculties() {
        return faculties;
    }

    public Profile faculties(Set<Faculty> faculties) {
        this.faculties = faculties;
        return this;
    }

    public Profile addFaculties(Faculty faculty) {
        this.faculties.add(faculty);
        faculty.getUsers().add(this);
        return this;
    }

    public Profile removeFaculties(Faculty faculty) {
        this.faculties.remove(faculty);
        faculty.getUsers().remove(this);
        return this;
    }

    public void setFaculties(Set<Faculty> faculties) {
        this.faculties = faculties;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public Profile courses(Set<Course> courses) {
        this.courses = courses;
        return this;
    }

    public Profile addCourses(Course course) {
        this.courses.add(course);
        course.getUsers().add(this);
        return this;
    }

    public Profile removeCourses(Course course) {
        this.courses.remove(course);
        course.getUsers().remove(this);
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
        if (!(o instanceof Profile)) {
            return false;
        }
        return id != null && id.equals(((Profile) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Profile{" +
            "id=" + getId() +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", dateOfBirth='" + getDateOfBirth() + "'" +
            ", index=" + getIndex() +
            ", yearOfEnrollment=" + getYearOfEnrollment() +
            ", verificationNumber='" + getVerificationNumber() + "'" +
            ", isApproved='" + isIsApproved() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", dateUpdated='" + getDateUpdated() + "'" +
            "}";
    }
}
