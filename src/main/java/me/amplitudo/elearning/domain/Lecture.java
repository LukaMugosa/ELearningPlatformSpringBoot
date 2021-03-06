package me.amplitudo.elearning.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A Lecture.
 */
@Entity
@Table(name = "lecture")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Lecture implements Serializable {

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
    @Column(name = "material_file_path", nullable = false)
    private String materialFilePath;

    @NotNull
    @Column(name = "file_type_icon_path", nullable = false)
    private String fileTypeIconPath;

    @Column(name = "date_created")
    private Instant dateCreated;

    @Column(name = "date_updated")
    private Instant dateUpdated;

    @ManyToOne
    @JsonIgnoreProperties(value = "lectures", allowSetters = true)
    private Course course;

    @ManyToOne
    @JsonIgnoreProperties(value = "lectures", allowSetters = true)
    private Profile user;

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

    public Lecture title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public Lecture description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMaterialFilePath() {
        return materialFilePath;
    }

    public Lecture materialFilePath(String materialFilePath) {
        this.materialFilePath = materialFilePath;
        return this;
    }

    public void setMaterialFilePath(String materialFilePath) {
        this.materialFilePath = materialFilePath;
    }

    public String getFileTypeIconPath() {
        return fileTypeIconPath;
    }

    public Lecture fileTypeIconPath(String fileTypeIconPath) {
        this.fileTypeIconPath = fileTypeIconPath;
        return this;
    }

    public void setFileTypeIconPath(String fileTypeIconPath) {
        this.fileTypeIconPath = fileTypeIconPath;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public Lecture dateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Instant getDateUpdated() {
        return dateUpdated;
    }

    public Lecture dateUpdated(Instant dateUpdated) {
        this.dateUpdated = dateUpdated;
        return this;
    }

    public void setDateUpdated(Instant dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public Course getCourse() {
        return course;
    }

    public Lecture course(Course course) {
        this.course = course;
        return this;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Profile getUser() {
        return user;
    }

    public Lecture user(Profile profile) {
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
        if (!(o instanceof Lecture)) {
            return false;
        }
        return id != null && id.equals(((Lecture) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Lecture{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", materialFilePath='" + getMaterialFilePath() + "'" +
            ", fileTypeIconPath='" + getFileTypeIconPath() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", dateUpdated='" + getDateUpdated() + "'" +
            "}";
    }
}
