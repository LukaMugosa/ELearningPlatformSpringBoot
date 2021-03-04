package me.amplitudo.elearning.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A AssignmentProfile.
 */
@Entity
@Table(name = "assignment_profile")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AssignmentProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "points")
    private Integer points;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file_type_icon_path")
    private String fileTypeIconPath;

    @Column(name = "date_created")
    private Instant dateCreated;

    @Column(name = "date_updated")
    private Instant dateUpdated;

    @ManyToOne
    @JsonIgnoreProperties(value = "assignmentProfiles", allowSetters = true)
    private Assignment assignment;

    @ManyToOne
    @JsonIgnoreProperties(value = "assignmentProfiles", allowSetters = true)
    private Profile user;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPoints() {
        return points;
    }

    public AssignmentProfile points(Integer points) {
        this.points = points;
        return this;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getFilePath() {
        return filePath;
    }

    public AssignmentProfile filePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileTypeIconPath() {
        return fileTypeIconPath;
    }

    public AssignmentProfile fileTypeIconPath(String fileTypeIconPath) {
        this.fileTypeIconPath = fileTypeIconPath;
        return this;
    }

    public void setFileTypeIconPath(String fileTypeIconPath) {
        this.fileTypeIconPath = fileTypeIconPath;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public AssignmentProfile dateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Instant getDateUpdated() {
        return dateUpdated;
    }

    public AssignmentProfile dateUpdated(Instant dateUpdated) {
        this.dateUpdated = dateUpdated;
        return this;
    }

    public void setDateUpdated(Instant dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public AssignmentProfile assignment(Assignment assignment) {
        this.assignment = assignment;
        return this;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    public Profile getUser() {
        return user;
    }

    public AssignmentProfile user(Profile profile) {
        this.user = profile;
        return this;
    }

    public void setUser(Profile profile) {
        this.user = profile;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssignmentProfile)) {
            return false;
        }
        return id != null && id.equals(((AssignmentProfile) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssignmentProfile{" +
            "id=" + getId() +
            ", points=" + getPoints() +
            ", filePath='" + getFilePath() + "'" +
            ", fileTypeIconPath='" + getFileTypeIconPath() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", dateUpdated='" + getDateUpdated() + "'" +
            "}";
    }
}
