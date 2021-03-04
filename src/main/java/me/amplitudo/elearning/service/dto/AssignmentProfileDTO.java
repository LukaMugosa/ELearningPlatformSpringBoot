package me.amplitudo.elearning.service.dto;

import java.time.Instant;
import java.io.Serializable;

/**
 * A DTO for the {@link me.amplitudo.elearning.domain.AssignmentProfile} entity.
 */
public class AssignmentProfileDTO implements Serializable {
    
    private Long id;

    private Integer points;

    private String filePath;

    private String fileTypeIconPath;

    private Instant dateCreated;

    private Instant dateUpdated;


    private Long assignmentId;

    private Long userId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
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

    public Long getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(Long assignmentId) {
        this.assignmentId = assignmentId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long profileId) {
        this.userId = profileId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssignmentProfileDTO)) {
            return false;
        }

        return id != null && id.equals(((AssignmentProfileDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssignmentProfileDTO{" +
            "id=" + getId() +
            ", points=" + getPoints() +
            ", filePath='" + getFilePath() + "'" +
            ", fileTypeIconPath='" + getFileTypeIconPath() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", dateUpdated='" + getDateUpdated() + "'" +
            ", assignmentId=" + getAssignmentId() +
            ", userId=" + getUserId() +
            "}";
    }
}
