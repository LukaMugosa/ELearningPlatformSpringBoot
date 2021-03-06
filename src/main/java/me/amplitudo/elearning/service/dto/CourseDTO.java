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

    private String professorFirstName;
    private String professorLastName;

    private Long assistantId;

    private String assistantFirstName;
    private String assistantLastName;


    private Long yearId;
    private String yearName;

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

    public String getYearName() {
        return yearName;
    }

    public void setYearName(String yearName) {
        this.yearName = yearName;
    }

    public void setDateUpdated(Instant dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public Long getProfessorId() {
        return professorId;
    }

    public void setProfessorId(Long userId) {
        this.professorId = userId;
    }

    public Long getAssistantId() {
        return assistantId;
    }

    public void setAssistantId(Long userId) {
        this.assistantId = userId;
    }

    public Long getYearId() {
        return yearId;
    }

    public void setYearId(Long yearId) {
        this.yearId = yearId;
    }

    public String getProfessorFirstName() {
        return professorFirstName;
    }

    public void setProfessorFirstName(String professorFirstName) {
        this.professorFirstName = professorFirstName;
    }

    public String getProfessorLastName() {
        return professorLastName;
    }

    public void setProfessorLastName(String professorLastName) {
        this.professorLastName = professorLastName;
    }

    public String getAssistantFirstName() {
        return assistantFirstName;
    }

    public void setAssistantFirstName(String assistantFirstName) {
        this.assistantFirstName = assistantFirstName;
    }

    public String getAssistantLastName() {
        return assistantLastName;
    }

    public void setAssistantLastName(String assistantLastName) {
        this.assistantLastName = assistantLastName;
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
            "}";
    }
}
