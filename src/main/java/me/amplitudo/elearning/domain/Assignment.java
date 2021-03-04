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
 * A Assignment.
 */
@Entity
@Table(name = "assignment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Assignment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "max_points", nullable = false)
    private Integer maxPoints;

    @NotNull
    @Column(name = "dead_line", nullable = false)
    private Instant deadLine;

    @NotNull
    @Column(name = "file_path", nullable = false)
    private String filePath;

    @NotNull
    @Column(name = "file_type_icon_path", nullable = false)
    private String fileTypeIconPath;

    @Column(name = "date_created")
    private Instant dateCreated;

    @Column(name = "date_updated")
    private Instant dateUpdated;

    @OneToMany(mappedBy = "assignment")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<AssignmentProfile> assignmentProfiles = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "assignments", allowSetters = true)
    private Profile user;

    @ManyToOne
    @JsonIgnoreProperties(value = "assignments", allowSetters = true)
    private Course course;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Assignment title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public Assignment description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMaxPoints() {
        return maxPoints;
    }

    public Assignment maxPoints(Integer maxPoints) {
        this.maxPoints = maxPoints;
        return this;
    }

    public void setMaxPoints(Integer maxPoints) {
        this.maxPoints = maxPoints;
    }

    public Instant getDeadLine() {
        return deadLine;
    }

    public Assignment deadLine(Instant deadLine) {
        this.deadLine = deadLine;
        return this;
    }

    public void setDeadLine(Instant deadLine) {
        this.deadLine = deadLine;
    }

    public String getFilePath() {
        return filePath;
    }

    public Assignment filePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileTypeIconPath() {
        return fileTypeIconPath;
    }

    public Assignment fileTypeIconPath(String fileTypeIconPath) {
        this.fileTypeIconPath = fileTypeIconPath;
        return this;
    }

    public void setFileTypeIconPath(String fileTypeIconPath) {
        this.fileTypeIconPath = fileTypeIconPath;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public Assignment dateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Instant getDateUpdated() {
        return dateUpdated;
    }

    public Assignment dateUpdated(Instant dateUpdated) {
        this.dateUpdated = dateUpdated;
        return this;
    }

    public void setDateUpdated(Instant dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public Set<AssignmentProfile> getAssignmentProfiles() {
        return assignmentProfiles;
    }

    public Assignment assignmentProfiles(Set<AssignmentProfile> assignmentProfiles) {
        this.assignmentProfiles = assignmentProfiles;
        return this;
    }

    public Assignment addAssignmentProfiles(AssignmentProfile assignmentProfile) {
        this.assignmentProfiles.add(assignmentProfile);
        assignmentProfile.setAssignment(this);
        return this;
    }

    public Assignment removeAssignmentProfiles(AssignmentProfile assignmentProfile) {
        this.assignmentProfiles.remove(assignmentProfile);
        assignmentProfile.setAssignment(null);
        return this;
    }

    public void setAssignmentProfiles(Set<AssignmentProfile> assignmentProfiles) {
        this.assignmentProfiles = assignmentProfiles;
    }

    public Profile getUser() {
        return user;
    }

    public Assignment user(Profile profile) {
        this.user = profile;
        return this;
    }

    public void setUser(Profile profile) {
        this.user = profile;
    }

    public Course getCourse() {
        return course;
    }

    public Assignment course(Course course) {
        this.course = course;
        return this;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Assignment)) {
            return false;
        }
        return id != null && id.equals(((Assignment) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Assignment{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", maxPoints=" + getMaxPoints() +
            ", deadLine='" + getDeadLine() + "'" +
            ", filePath='" + getFilePath() + "'" +
            ", fileTypeIconPath='" + getFileTypeIconPath() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", dateUpdated='" + getDateUpdated() + "'" +
            "}";
    }
}
