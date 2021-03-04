package me.amplitudo.elearning.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link me.amplitudo.elearning.domain.Lecture} entity. This class is used
 * in {@link me.amplitudo.elearning.web.rest.LectureResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /lectures?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LectureCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private StringFilter description;

    private StringFilter materialFilePath;

    private StringFilter fileTypeIconPath;

    private InstantFilter dateCreated;

    private InstantFilter dateUpdated;

    private LongFilter courseId;

    private LongFilter userId;

    public LectureCriteria() {
    }

    public LectureCriteria(LectureCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.materialFilePath = other.materialFilePath == null ? null : other.materialFilePath.copy();
        this.fileTypeIconPath = other.fileTypeIconPath == null ? null : other.fileTypeIconPath.copy();
        this.dateCreated = other.dateCreated == null ? null : other.dateCreated.copy();
        this.dateUpdated = other.dateUpdated == null ? null : other.dateUpdated.copy();
        this.courseId = other.courseId == null ? null : other.courseId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
    }

    @Override
    public LectureCriteria copy() {
        return new LectureCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitle() {
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getMaterialFilePath() {
        return materialFilePath;
    }

    public void setMaterialFilePath(StringFilter materialFilePath) {
        this.materialFilePath = materialFilePath;
    }

    public StringFilter getFileTypeIconPath() {
        return fileTypeIconPath;
    }

    public void setFileTypeIconPath(StringFilter fileTypeIconPath) {
        this.fileTypeIconPath = fileTypeIconPath;
    }

    public InstantFilter getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(InstantFilter dateCreated) {
        this.dateCreated = dateCreated;
    }

    public InstantFilter getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(InstantFilter dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public LongFilter getCourseId() {
        return courseId;
    }

    public void setCourseId(LongFilter courseId) {
        this.courseId = courseId;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final LectureCriteria that = (LectureCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(description, that.description) &&
            Objects.equals(materialFilePath, that.materialFilePath) &&
            Objects.equals(fileTypeIconPath, that.fileTypeIconPath) &&
            Objects.equals(dateCreated, that.dateCreated) &&
            Objects.equals(dateUpdated, that.dateUpdated) &&
            Objects.equals(courseId, that.courseId) &&
            Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        title,
        description,
        materialFilePath,
        fileTypeIconPath,
        dateCreated,
        dateUpdated,
        courseId,
        userId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LectureCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (materialFilePath != null ? "materialFilePath=" + materialFilePath + ", " : "") +
                (fileTypeIconPath != null ? "fileTypeIconPath=" + fileTypeIconPath + ", " : "") +
                (dateCreated != null ? "dateCreated=" + dateCreated + ", " : "") +
                (dateUpdated != null ? "dateUpdated=" + dateUpdated + ", " : "") +
                (courseId != null ? "courseId=" + courseId + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }

}
