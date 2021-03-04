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
 * Criteria class for the {@link me.amplitudo.elearning.domain.Course} entity. This class is used
 * in {@link me.amplitudo.elearning.web.rest.CourseResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /courses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CourseCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private InstantFilter dateCreated;

    private InstantFilter dateUpdated;

    private LongFilter notificationsId;

    private LongFilter assignmentsId;

    private LongFilter professorId;

    private LongFilter assistantId;

    private LongFilter yearId;

    private LongFilter orientationsId;

    private LongFilter usersId;

    private LongFilter lecturesId;

    public CourseCriteria() {
    }

    public CourseCriteria(CourseCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.dateCreated = other.dateCreated == null ? null : other.dateCreated.copy();
        this.dateUpdated = other.dateUpdated == null ? null : other.dateUpdated.copy();
        this.notificationsId = other.notificationsId == null ? null : other.notificationsId.copy();
        this.assignmentsId = other.assignmentsId == null ? null : other.assignmentsId.copy();
        this.professorId = other.professorId == null ? null : other.professorId.copy();
        this.assistantId = other.assistantId == null ? null : other.assistantId.copy();
        this.yearId = other.yearId == null ? null : other.yearId.copy();
        this.orientationsId = other.orientationsId == null ? null : other.orientationsId.copy();
        this.usersId = other.usersId == null ? null : other.usersId.copy();
        this.lecturesId = other.lecturesId == null ? null : other.lecturesId.copy();
    }

    @Override
    public CourseCriteria copy() {
        return new CourseCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
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

    public LongFilter getNotificationsId() {
        return notificationsId;
    }

    public void setNotificationsId(LongFilter notificationsId) {
        this.notificationsId = notificationsId;
    }

    public LongFilter getAssignmentsId() {
        return assignmentsId;
    }

    public void setAssignmentsId(LongFilter assignmentsId) {
        this.assignmentsId = assignmentsId;
    }

    public LongFilter getProfessorId() {
        return professorId;
    }

    public void setProfessorId(LongFilter professorId) {
        this.professorId = professorId;
    }

    public LongFilter getAssistantId() {
        return assistantId;
    }

    public void setAssistantId(LongFilter assistantId) {
        this.assistantId = assistantId;
    }

    public LongFilter getYearId() {
        return yearId;
    }

    public void setYearId(LongFilter yearId) {
        this.yearId = yearId;
    }

    public LongFilter getOrientationsId() {
        return orientationsId;
    }

    public void setOrientationsId(LongFilter orientationsId) {
        this.orientationsId = orientationsId;
    }

    public LongFilter getUsersId() {
        return usersId;
    }

    public void setUsersId(LongFilter usersId) {
        this.usersId = usersId;
    }

    public LongFilter getLecturesId() {
        return lecturesId;
    }

    public void setLecturesId(LongFilter lecturesId) {
        this.lecturesId = lecturesId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CourseCriteria that = (CourseCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(dateCreated, that.dateCreated) &&
            Objects.equals(dateUpdated, that.dateUpdated) &&
            Objects.equals(notificationsId, that.notificationsId) &&
            Objects.equals(assignmentsId, that.assignmentsId) &&
            Objects.equals(professorId, that.professorId) &&
            Objects.equals(assistantId, that.assistantId) &&
            Objects.equals(yearId, that.yearId) &&
            Objects.equals(orientationsId, that.orientationsId) &&
            Objects.equals(usersId, that.usersId) &&
            Objects.equals(lecturesId, that.lecturesId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        dateCreated,
        dateUpdated,
        notificationsId,
        assignmentsId,
        professorId,
        assistantId,
        yearId,
        orientationsId,
        usersId,
        lecturesId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CourseCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (dateCreated != null ? "dateCreated=" + dateCreated + ", " : "") +
                (dateUpdated != null ? "dateUpdated=" + dateUpdated + ", " : "") +
                (notificationsId != null ? "notificationsId=" + notificationsId + ", " : "") +
                (assignmentsId != null ? "assignmentsId=" + assignmentsId + ", " : "") +
                (professorId != null ? "professorId=" + professorId + ", " : "") +
                (assistantId != null ? "assistantId=" + assistantId + ", " : "") +
                (yearId != null ? "yearId=" + yearId + ", " : "") +
                (orientationsId != null ? "orientationsId=" + orientationsId + ", " : "") +
                (usersId != null ? "usersId=" + usersId + ", " : "") +
                (lecturesId != null ? "lecturesId=" + lecturesId + ", " : "") +
            "}";
    }

}
