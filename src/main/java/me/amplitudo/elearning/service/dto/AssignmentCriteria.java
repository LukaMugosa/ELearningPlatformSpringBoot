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
 * Criteria class for the {@link me.amplitudo.elearning.domain.Assignment} entity. This class is used
 * in {@link me.amplitudo.elearning.web.rest.AssignmentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /assignments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AssignmentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private StringFilter description;

    private IntegerFilter maxPoints;

    private InstantFilter deadLine;

    private StringFilter filePath;

    private StringFilter fileTypeIconPath;

    private InstantFilter dateCreated;

    private InstantFilter dateUpdated;

    private LongFilter assignmentProfilesId;

    private LongFilter userId;

    private LongFilter courseId;

    public AssignmentCriteria() {
    }

    public AssignmentCriteria(AssignmentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.maxPoints = other.maxPoints == null ? null : other.maxPoints.copy();
        this.deadLine = other.deadLine == null ? null : other.deadLine.copy();
        this.filePath = other.filePath == null ? null : other.filePath.copy();
        this.fileTypeIconPath = other.fileTypeIconPath == null ? null : other.fileTypeIconPath.copy();
        this.dateCreated = other.dateCreated == null ? null : other.dateCreated.copy();
        this.dateUpdated = other.dateUpdated == null ? null : other.dateUpdated.copy();
        this.assignmentProfilesId = other.assignmentProfilesId == null ? null : other.assignmentProfilesId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.courseId = other.courseId == null ? null : other.courseId.copy();
    }

    @Override
    public AssignmentCriteria copy() {
        return new AssignmentCriteria(this);
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

    public IntegerFilter getMaxPoints() {
        return maxPoints;
    }

    public void setMaxPoints(IntegerFilter maxPoints) {
        this.maxPoints = maxPoints;
    }

    public InstantFilter getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(InstantFilter deadLine) {
        this.deadLine = deadLine;
    }

    public StringFilter getFilePath() {
        return filePath;
    }

    public void setFilePath(StringFilter filePath) {
        this.filePath = filePath;
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

    public LongFilter getAssignmentProfilesId() {
        return assignmentProfilesId;
    }

    public void setAssignmentProfilesId(LongFilter assignmentProfilesId) {
        this.assignmentProfilesId = assignmentProfilesId;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getCourseId() {
        return courseId;
    }

    public void setCourseId(LongFilter courseId) {
        this.courseId = courseId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AssignmentCriteria that = (AssignmentCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(description, that.description) &&
            Objects.equals(maxPoints, that.maxPoints) &&
            Objects.equals(deadLine, that.deadLine) &&
            Objects.equals(filePath, that.filePath) &&
            Objects.equals(fileTypeIconPath, that.fileTypeIconPath) &&
            Objects.equals(dateCreated, that.dateCreated) &&
            Objects.equals(dateUpdated, that.dateUpdated) &&
            Objects.equals(assignmentProfilesId, that.assignmentProfilesId) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(courseId, that.courseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        title,
        description,
        maxPoints,
        deadLine,
        filePath,
        fileTypeIconPath,
        dateCreated,
        dateUpdated,
        assignmentProfilesId,
        userId,
        courseId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssignmentCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (maxPoints != null ? "maxPoints=" + maxPoints + ", " : "") +
                (deadLine != null ? "deadLine=" + deadLine + ", " : "") +
                (filePath != null ? "filePath=" + filePath + ", " : "") +
                (fileTypeIconPath != null ? "fileTypeIconPath=" + fileTypeIconPath + ", " : "") +
                (dateCreated != null ? "dateCreated=" + dateCreated + ", " : "") +
                (dateUpdated != null ? "dateUpdated=" + dateUpdated + ", " : "") +
                (assignmentProfilesId != null ? "assignmentProfilesId=" + assignmentProfilesId + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (courseId != null ? "courseId=" + courseId + ", " : "") +
            "}";
    }

}
