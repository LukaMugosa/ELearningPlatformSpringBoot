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
 * Criteria class for the {@link me.amplitudo.elearning.domain.AssignmentProfile} entity. This class is used
 * in {@link me.amplitudo.elearning.web.rest.AssignmentProfileResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /assignment-profiles?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AssignmentProfileCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter points;

    private StringFilter filePath;

    private StringFilter fileTypeIconPath;

    private InstantFilter dateCreated;

    private InstantFilter dateUpdated;

    private LongFilter assignmentId;

    private LongFilter userId;

    public AssignmentProfileCriteria() {
    }

    public AssignmentProfileCriteria(AssignmentProfileCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.points = other.points == null ? null : other.points.copy();
        this.filePath = other.filePath == null ? null : other.filePath.copy();
        this.fileTypeIconPath = other.fileTypeIconPath == null ? null : other.fileTypeIconPath.copy();
        this.dateCreated = other.dateCreated == null ? null : other.dateCreated.copy();
        this.dateUpdated = other.dateUpdated == null ? null : other.dateUpdated.copy();
        this.assignmentId = other.assignmentId == null ? null : other.assignmentId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
    }

    @Override
    public AssignmentProfileCriteria copy() {
        return new AssignmentProfileCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getPoints() {
        return points;
    }

    public void setPoints(IntegerFilter points) {
        this.points = points;
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

    public LongFilter getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(LongFilter assignmentId) {
        this.assignmentId = assignmentId;
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
        final AssignmentProfileCriteria that = (AssignmentProfileCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(points, that.points) &&
            Objects.equals(filePath, that.filePath) &&
            Objects.equals(fileTypeIconPath, that.fileTypeIconPath) &&
            Objects.equals(dateCreated, that.dateCreated) &&
            Objects.equals(dateUpdated, that.dateUpdated) &&
            Objects.equals(assignmentId, that.assignmentId) &&
            Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        points,
        filePath,
        fileTypeIconPath,
        dateCreated,
        dateUpdated,
        assignmentId,
        userId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssignmentProfileCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (points != null ? "points=" + points + ", " : "") +
                (filePath != null ? "filePath=" + filePath + ", " : "") +
                (fileTypeIconPath != null ? "fileTypeIconPath=" + fileTypeIconPath + ", " : "") +
                (dateCreated != null ? "dateCreated=" + dateCreated + ", " : "") +
                (dateUpdated != null ? "dateUpdated=" + dateUpdated + ", " : "") +
                (assignmentId != null ? "assignmentId=" + assignmentId + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }

}
