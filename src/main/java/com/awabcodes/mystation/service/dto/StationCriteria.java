package com.awabcodes.mystation.service.dto;

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
 * Criteria class for the {@link com.awabcodes.mystation.domain.Station} entity. This class is used
 * in {@link com.awabcodes.mystation.web.rest.StationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /stations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class StationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private IntegerFilter gasLevel;

    private IntegerFilter benzeneLevel;

    private InstantFilter lastTankFill;

    private StringFilter city;

    private StringFilter location;

    private StringFilter mapUrl;

    private LongFilter userId;

    private LongFilter reportId;

    public StationCriteria(){
    }

    public StationCriteria(StationCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.gasLevel = other.gasLevel == null ? null : other.gasLevel.copy();
        this.benzeneLevel = other.benzeneLevel == null ? null : other.benzeneLevel.copy();
        this.lastTankFill = other.lastTankFill == null ? null : other.lastTankFill.copy();
        this.city = other.city == null ? null : other.city.copy();
        this.location = other.location == null ? null : other.location.copy();
        this.mapUrl = other.mapUrl == null ? null : other.mapUrl.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.reportId = other.reportId == null ? null : other.reportId.copy();
    }

    @Override
    public StationCriteria copy() {
        return new StationCriteria(this);
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

    public IntegerFilter getGasLevel() {
        return gasLevel;
    }

    public void setGasLevel(IntegerFilter gasLevel) {
        this.gasLevel = gasLevel;
    }

    public IntegerFilter getBenzeneLevel() {
        return benzeneLevel;
    }

    public void setBenzeneLevel(IntegerFilter benzeneLevel) {
        this.benzeneLevel = benzeneLevel;
    }

    public InstantFilter getLastTankFill() {
        return lastTankFill;
    }

    public void setLastTankFill(InstantFilter lastTankFill) {
        this.lastTankFill = lastTankFill;
    }

    public StringFilter getCity() {
        return city;
    }

    public void setCity(StringFilter city) {
        this.city = city;
    }

    public StringFilter getLocation() {
        return location;
    }

    public void setLocation(StringFilter location) {
        this.location = location;
    }

    public StringFilter getMapUrl() {
        return mapUrl;
    }

    public void setMapUrl(StringFilter mapUrl) {
        this.mapUrl = mapUrl;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getReportId() {
        return reportId;
    }

    public void setReportId(LongFilter reportId) {
        this.reportId = reportId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final StationCriteria that = (StationCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(gasLevel, that.gasLevel) &&
            Objects.equals(benzeneLevel, that.benzeneLevel) &&
            Objects.equals(lastTankFill, that.lastTankFill) &&
            Objects.equals(city, that.city) &&
            Objects.equals(location, that.location) &&
            Objects.equals(mapUrl, that.mapUrl) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(reportId, that.reportId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        gasLevel,
        benzeneLevel,
        lastTankFill,
        city,
        location,
        mapUrl,
        userId,
        reportId
        );
    }

    @Override
    public String toString() {
        return "StationCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (gasLevel != null ? "gasLevel=" + gasLevel + ", " : "") +
                (benzeneLevel != null ? "benzeneLevel=" + benzeneLevel + ", " : "") +
                (lastTankFill != null ? "lastTankFill=" + lastTankFill + ", " : "") +
                (city != null ? "city=" + city + ", " : "") +
                (location != null ? "location=" + location + ", " : "") +
                (mapUrl != null ? "mapUrl=" + mapUrl + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (reportId != null ? "reportId=" + reportId + ", " : "") +
            "}";
    }

}
