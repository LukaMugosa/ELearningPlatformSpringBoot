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
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link me.amplitudo.elearning.domain.Profile} entity. This class is used
 * in {@link me.amplitudo.elearning.web.rest.ProfileResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /profiles?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProfileCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter phoneNumber;

    private LocalDateFilter dateOfBirth;

    private IntegerFilter index;

    private IntegerFilter yearOfEnrollment;

    private StringFilter verificationNumber;

    private BooleanFilter isApproved;

    private InstantFilter dateCreated;

    private InstantFilter dateUpdated;

    private LongFilter userId;

    private LongFilter assignmentProfilesId;

    private LongFilter lecturesId;

    private LongFilter notificationsId;

    private LongFilter assignmentsId;

    private LongFilter yearId;

    private LongFilter facultyId;

    private LongFilter facultiesId;

    private LongFilter coursesId;

    public ProfileCriteria() {
    }

    public ProfileCriteria(ProfileCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.phoneNumber = other.phoneNumber == null ? null : other.phoneNumber.copy();
        this.dateOfBirth = other.dateOfBirth == null ? null : other.dateOfBirth.copy();
        this.index = other.index == null ? null : other.index.copy();
        this.yearOfEnrollment = other.yearOfEnrollment == null ? null : other.yearOfEnrollment.copy();
        this.verificationNumber = other.verificationNumber == null ? null : other.verificationNumber.copy();
        this.isApproved = other.isApproved == null ? null : other.isApproved.copy();
        this.dateCreated = other.dateCreated == null ? null : other.dateCreated.copy();
        this.dateUpdated = other.dateUpdated == null ? null : other.dateUpdated.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.assignmentProfilesId = other.assignmentProfilesId == null ? null : other.assignmentProfilesId.copy();
        this.lecturesId = other.lecturesId == null ? null : other.lecturesId.copy();
        this.notificationsId = other.notificationsId == null ? null : other.notificationsId.copy();
        this.assignmentsId = other.assignmentsId == null ? null : other.assignmentsId.copy();
        this.yearId = other.yearId == null ? null : other.yearId.copy();
        this.facultyId = other.facultyId == null ? null : other.facultyId.copy();
        this.facultiesId = other.facultiesId == null ? null : other.facultiesId.copy();
        this.coursesId = other.coursesId == null ? null : other.coursesId.copy();
    }

    @Override
    public ProfileCriteria copy() {
        return new ProfileCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(StringFilter phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDateFilter getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDateFilter dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public IntegerFilter getIndex() {
        return index;
    }

    public void setIndex(IntegerFilter index) {
        this.index = index;
    }

    public IntegerFilter getYearOfEnrollment() {
        return yearOfEnrollment;
    }

    public void setYearOfEnrollment(IntegerFilter yearOfEnrollment) {
        this.yearOfEnrollment = yearOfEnrollment;
    }

    public StringFilter getVerificationNumber() {
        return verificationNumber;
    }

    public void setVerificationNumber(StringFilter verificationNumber) {
        this.verificationNumber = verificationNumber;
    }

    public BooleanFilter getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(BooleanFilter isApproved) {
        this.isApproved = isApproved;
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

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getAssignmentProfilesId() {
        return assignmentProfilesId;
    }

    public void setAssignmentProfilesId(LongFilter assignmentProfilesId) {
        this.assignmentProfilesId = assignmentProfilesId;
    }

    public LongFilter getLecturesId() {
        return lecturesId;
    }

    public void setLecturesId(LongFilter lecturesId) {
        this.lecturesId = lecturesId;
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

    public LongFilter getYearId() {
        return yearId;
    }

    public void setYearId(LongFilter yearId) {
        this.yearId = yearId;
    }

    public LongFilter getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(LongFilter facultyId) {
        this.facultyId = facultyId;
    }

    public LongFilter getFacultiesId() {
        return facultiesId;
    }

    public void setFacultiesId(LongFilter facultiesId) {
        this.facultiesId = facultiesId;
    }

    public LongFilter getCoursesId() {
        return coursesId;
    }

    public void setCoursesId(LongFilter coursesId) {
        this.coursesId = coursesId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProfileCriteria that = (ProfileCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(phoneNumber, that.phoneNumber) &&
            Objects.equals(dateOfBirth, that.dateOfBirth) &&
            Objects.equals(index, that.index) &&
            Objects.equals(yearOfEnrollment, that.yearOfEnrollment) &&
            Objects.equals(verificationNumber, that.verificationNumber) &&
            Objects.equals(isApproved, that.isApproved) &&
            Objects.equals(dateCreated, that.dateCreated) &&
            Objects.equals(dateUpdated, that.dateUpdated) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(assignmentProfilesId, that.assignmentProfilesId) &&
            Objects.equals(lecturesId, that.lecturesId) &&
            Objects.equals(notificationsId, that.notificationsId) &&
            Objects.equals(assignmentsId, that.assignmentsId) &&
            Objects.equals(yearId, that.yearId) &&
            Objects.equals(facultyId, that.facultyId) &&
            Objects.equals(facultiesId, that.facultiesId) &&
            Objects.equals(coursesId, that.coursesId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        phoneNumber,
        dateOfBirth,
        index,
        yearOfEnrollment,
        verificationNumber,
        isApproved,
        dateCreated,
        dateUpdated,
        userId,
        assignmentProfilesId,
        lecturesId,
        notificationsId,
        assignmentsId,
        yearId,
        facultyId,
        facultiesId,
        coursesId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfileCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (phoneNumber != null ? "phoneNumber=" + phoneNumber + ", " : "") +
                (dateOfBirth != null ? "dateOfBirth=" + dateOfBirth + ", " : "") +
                (index != null ? "index=" + index + ", " : "") +
                (yearOfEnrollment != null ? "yearOfEnrollment=" + yearOfEnrollment + ", " : "") +
                (verificationNumber != null ? "verificationNumber=" + verificationNumber + ", " : "") +
                (isApproved != null ? "isApproved=" + isApproved + ", " : "") +
                (dateCreated != null ? "dateCreated=" + dateCreated + ", " : "") +
                (dateUpdated != null ? "dateUpdated=" + dateUpdated + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (assignmentProfilesId != null ? "assignmentProfilesId=" + assignmentProfilesId + ", " : "") +
                (lecturesId != null ? "lecturesId=" + lecturesId + ", " : "") +
                (notificationsId != null ? "notificationsId=" + notificationsId + ", " : "") +
                (assignmentsId != null ? "assignmentsId=" + assignmentsId + ", " : "") +
                (yearId != null ? "yearId=" + yearId + ", " : "") +
                (facultyId != null ? "facultyId=" + facultyId + ", " : "") +
                (facultiesId != null ? "facultiesId=" + facultiesId + ", " : "") +
                (coursesId != null ? "coursesId=" + coursesId + ", " : "") +
            "}";
    }

}
