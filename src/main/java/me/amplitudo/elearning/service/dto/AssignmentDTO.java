package me.amplitudo.elearning.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link me.amplitudo.elearning.domain.Assignment} entity.
 */
public class AssignmentDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private Integer maxPoints;

    @NotNull
    private Instant deadLine;

    @NotNull
    private String filePath;

    @NotNull
    private String fileTypeIconPath;

    private Instant dateCreated;

    private Instant dateUpdated;


    private Long userId;

    private Long courseId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMaxPoints() {
        return maxPoints;
    }

    public void setMaxPoints(Integer maxPoints) {
        this.maxPoints = maxPoints;
    }

    public Instant getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(Instant deadLine) {
        this.deadLine = deadLine;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileTypeIconPath() {
        return fileTypeIconPath;
    }

    public void setFileTypeIconPath(String fileTypeIconPath) {
        this.fileTypeIconPath = fileTypeIconPath;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long profileId) {
        this.userId = profileId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssignmentDTO)) {
            return false;
        }

        return id != null && id.equals(((AssignmentDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssignmentDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", maxPoints=" + getMaxPoints() +
            ", deadLine='" + getDeadLine() + "'" +
            ", filePath='" + getFilePath() + "'" +
            ", fileTypeIconPath='" + getFileTypeIconPath() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", dateUpdated='" + getDateUpdated() + "'" +
            ", userId=" + getUserId() +
            ", courseId=" + getCourseId() +
            "}";
    }
}
