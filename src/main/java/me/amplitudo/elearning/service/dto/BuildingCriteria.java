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
import io.github.jhipster.service.filter.BigDecimalFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link me.amplitudo.elearning.domain.Building} entity. This class is used
 * in {@link me.amplitudo.elearning.web.rest.BuildingResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /buildings?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BuildingCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter location;

    private BigDecimalFilter latitude;

    private BigDecimalFilter longitude;

    private InstantFilter dateCreated;

    private InstantFilter dateUpdated;

    private LongFilter facultiesId;

    public BuildingCriteria() {
    }

    public BuildingCriteria(BuildingCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.location = other.location == null ? null : other.location.copy();
        this.latitude = other.latitude == null ? null : other.latitude.copy();
        this.longitude = other.longitude == null ? null : other.longitude.copy();
        this.dateCreated = other.dateCreated == null ? null : other.dateCreated.copy();
        this.dateUpdated = other.dateUpdated == null ? null : other.dateUpdated.copy();
        this.facultiesId = other.facultiesId == null ? null : other.facultiesId.copy();
    }

    @Override
    public BuildingCriteria copy() {
        return new BuildingCriteria(this);
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

    public StringFilter getLocation() {
        return location;
    }

    public void setLocation(StringFilter location) {
        this.location = location;
    }

    public BigDecimalFilter getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimalFilter latitude) {
        this.latitude = latitude;
    }

    public BigDecimalFilter getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimalFilter longitude) {
        this.longitude = longitude;
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

    public LongFilter getFacultiesId() {
        return facultiesId;
    }

    public void setFacultiesId(LongFilter facultiesId) {
        this.facultiesId = facultiesId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BuildingCriteria that = (BuildingCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(location, that.location) &&
            Objects.equals(latitude, that.latitude) &&
            Objects.equals(longitude, that.longitude) &&
            Objects.equals(dateCreated, that.dateCreated) &&
            Objects.equals(dateUpdated, that.dateUpdated) &&
            Objects.equals(facultiesId, that.facultiesId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        location,
        latitude,
        longitude,
        dateCreated,
        dateUpdated,
        facultiesId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BuildingCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (location != null ? "location=" + location + ", " : "") +
                (latitude != null ? "latitude=" + latitude + ", " : "") +
                (longitude != null ? "longitude=" + longitude + ", " : "") +
                (dateCreated != null ? "dateCreated=" + dateCreated + ", " : "") +
                (dateUpdated != null ? "dateUpdated=" + dateUpdated + ", " : "") +
                (facultiesId != null ? "facultiesId=" + facultiesId + ", " : "") +
            "}";
    }

}
