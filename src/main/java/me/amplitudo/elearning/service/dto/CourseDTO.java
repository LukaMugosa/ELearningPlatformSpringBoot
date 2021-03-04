package me.amplitudo.elearning.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A DTO for the {@link me.amplitudo.elearning.domain.Course} entity.
 */
public class CourseDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private Instant dateCreated;

    private Instant dateUpdated;


    private Long professorId;

    private Long assistantId;

    private Long yearId;
    private Set<OrientationDTO> orientations = new HashSet<>();
    private Set<ProfileDTO> users = new HashSet<>();

    private Long lecturesId;

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

    public Instant getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Instant getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Instant dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public Long getProfessorId() {
        return professorId;
    }

    public void setProfessorId(Long profileId) {
        this.professorId = profileId;
    }

    public Long getAssistantId() {
        return assistantId;
    }

    public void setAssistantId(Long profileId) {
        this.assistantId = profileId;
    }

    public Long getYearId() {
        return yearId;
    }

    public void setYearId(Long yearId) {
        this.yearId = yearId;
    }

    public Set<OrientationDTO> getOrientations() {
        return orientations;
    }

    public void setOrientations(Set<OrientationDTO> orientations) {
        this.orientations = orientations;
    }

    public Set<ProfileDTO> getUsers() {
        return users;
    }

    public void setUsers(Set<ProfileDTO> profiles) {
        this.users = profiles;
    }

    public Long getLecturesId() {
        return lecturesId;
    }

    public void setLecturesId(Long lectureId) {
        this.lecturesId = lectureId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CourseDTO)) {
            return false;
        }

        return id != null && id.equals(((CourseDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CourseDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", dateUpdated='" + getDateUpdated() + "'" +
            ", professorId=" + getProfessorId() +
            ", assistantId=" + getAssistantId() +
            ", yearId=" + getYearId() +
            ", orientations='" + getOrientations() + "'" +
            ", users='" + getUsers() + "'" +
            ", lecturesId=" + getLecturesId() +
            "}";
    }
}
