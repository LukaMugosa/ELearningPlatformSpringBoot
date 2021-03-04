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
 * Criteria class for the {@link me.amplitudo.elearning.domain.Faculty} entity. This class is used
 * in {@link me.amplitudo.elearning.web.rest.FacultyResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /faculties?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FacultyCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private InstantFilter dateCreated;

    private InstantFilter dateUpdated;

    private LongFilter orientationsId;

    private LongFilter orientationFacultiesId;

    private LongFilter usersId;

    private LongFilter buildingId;

    public FacultyCriteria() {
    }

    public FacultyCriteria(FacultyCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.dateCreated = other.dateCreated == null ? null : other.dateCreated.copy();
        this.dateUpdated = other.dateUpdated == null ? null : other.dateUpdated.copy();
        this.orientationsId = other.orientationsId == null ? null : other.orientationsId.copy();
        this.orientationFacultiesId = other.orientationFacultiesId == null ? null : other.orientationFacultiesId.copy();
        this.usersId = other.usersId == null ? null : other.usersId.copy();
        this.buildingId = other.buildingId == null ? null : other.buildingId.copy();
    }

    @Override
    public FacultyCriteria copy() {
        return new FacultyCriteria(this);
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

    public LongFilter getOrientationsId() {
        return orientationsId;
    }

    public void setOrientationsId(LongFilter orientationsId) {
        this.orientationsId = orientationsId;
    }

    public LongFilter getOrientationFacultiesId() {
        return orientationFacultiesId;
    }

    public void setOrientationFacultiesId(LongFilter orientationFacultiesId) {
        this.orientationFacultiesId = orientationFacultiesId;
    }

    public LongFilter getUsersId() {
        return usersId;
    }

    public void setUsersId(LongFilter usersId) {
        this.usersId = usersId;
    }

    public LongFilter getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(LongFilter buildingId) {
        this.buildingId = buildingId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final FacultyCriteria that = (FacultyCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(dateCreated, that.dateCreated) &&
            Objects.equals(dateUpdated, that.dateUpdated) &&
            Objects.equals(orientationsId, that.orientationsId) &&
            Objects.equals(orientationFacultiesId, that.orientationFacultiesId) &&
            Objects.equals(usersId, that.usersId) &&
            Objects.equals(buildingId, that.buildingId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        dateCreated,
        dateUpdated,
        orientationsId,
        orientationFacultiesId,
        usersId,
        buildingId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FacultyCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (dateCreated != null ? "dateCreated=" + dateCreated + ", " : "") +
                (dateUpdated != null ? "dateUpdated=" + dateUpdated + ", " : "") +
                (orientationsId != null ? "orientationsId=" + orientationsId + ", " : "") +
                (orientationFacultiesId != null ? "orientationFacultiesId=" + orientationFacultiesId + ", " : "") +
                (usersId != null ? "usersId=" + usersId + ", " : "") +
                (buildingId != null ? "buildingId=" + buildingId + ", " : "") +
            "}";
    }

}
