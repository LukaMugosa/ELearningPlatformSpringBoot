package me.amplitudo.elearning.service.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.io.Serializable;

/**
 * A DTO for the {@link me.amplitudo.elearning.domain.Profile} entity.
 */
public class ProfileDTO implements Serializable {
    
    private Long id;

    private String phoneNumber;

    private LocalDate dateOfBirth;

    private Integer index;

    private Integer yearOfEnrollment;

    private String verificationNumber;

    private Boolean isApproved;

    private Instant dateCreated;

    private Instant dateUpdated;


    private Long userId;

    private Long yearId;

    private Long facultyId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getYearOfEnrollment() {
        return yearOfEnrollment;
    }

    public void setYearOfEnrollment(Integer yearOfEnrollment) {
        this.yearOfEnrollment = yearOfEnrollment;
    }

    public String getVerificationNumber() {
        return verificationNumber;
    }

    public void setVerificationNumber(String verificationNumber) {
        this.verificationNumber = verificationNumber;
    }

    public Boolean isIsApproved() {
        return isApproved;
    }

    public void setIsApproved(Boolean isApproved) {
        this.isApproved = isApproved;
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

    public Long getYearId() {
        return yearId;
    }

    public void setYearId(Long yearId) {
        this.yearId = yearId;
    }

    public Long getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(Long orientationId) {
        this.facultyId = orientationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfileDTO)) {
            return false;
        }

        return id != null && id.equals(((ProfileDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfileDTO{" +
            "id=" + getId() +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", dateOfBirth='" + getDateOfBirth() + "'" +
            ", index=" + getIndex() +
            ", yearOfEnrollment=" + getYearOfEnrollment() +
            ", verificationNumber='" + getVerificationNumber() + "'" +
            ", isApproved='" + isIsApproved() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", dateUpdated='" + getDateUpdated() + "'" +
            ", userId=" + getUserId() +
            ", yearId=" + getYearId() +
            ", facultyId=" + getFacultyId() +
            "}";
    }
}
