package me.amplitudo.elearning.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link me.amplitudo.elearning.domain.Lecture} entity.
 */
public class LectureDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private String materialFilePath;

    @NotNull
    private String fileTypeIconPath;

    private Instant dateCreated;

    private Instant dateUpdated;


    private Long userId;
    
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

    public String getMaterialFilePath() {
        return materialFilePath;
    }

    public void setMaterialFilePath(String materialFilePath) {
        this.materialFilePath = materialFilePath;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LectureDTO)) {
            return false;
        }

        return id != null && id.equals(((LectureDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LectureDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", materialFilePath='" + getMaterialFilePath() + "'" +
            ", fileTypeIconPath='" + getFileTypeIconPath() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", dateUpdated='" + getDateUpdated() + "'" +
            ", userId=" + getUserId() +
            "}";
    }
}
