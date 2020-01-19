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

/**
 * Criteria class for the {@link com.awabcodes.mystation.domain.Report} entity. This class is used
 * in {@link com.awabcodes.mystation.web.rest.ReportResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /reports?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ReportCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private StringFilter weeklyConsumption;

    private StringFilter monthlyConsumption;

    private LongFilter stationId;

    public ReportCriteria(){
    }

    public ReportCriteria(ReportCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.weeklyConsumption = other.weeklyConsumption == null ? null : other.weeklyConsumption.copy();
        this.monthlyConsumption = other.monthlyConsumption == null ? null : other.monthlyConsumption.copy();
        this.stationId = other.stationId == null ? null : other.stationId.copy();
    }

    @Override
    public ReportCriteria copy() {
        return new ReportCriteria(this);
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

    public StringFilter getWeeklyConsumption() {
        return weeklyConsumption;
    }

    public void setWeeklyConsumption(StringFilter weeklyConsumption) {
        this.weeklyConsumption = weeklyConsumption;
    }

    public StringFilter getMonthlyConsumption() {
        return monthlyConsumption;
    }

    public void setMonthlyConsumption(StringFilter monthlyConsumption) {
        this.monthlyConsumption = monthlyConsumption;
    }

    public LongFilter getStationId() {
        return stationId;
    }

    public void setStationId(LongFilter stationId) {
        this.stationId = stationId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ReportCriteria that = (ReportCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(weeklyConsumption, that.weeklyConsumption) &&
            Objects.equals(monthlyConsumption, that.monthlyConsumption) &&
            Objects.equals(stationId, that.stationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        title,
        weeklyConsumption,
        monthlyConsumption,
        stationId
        );
    }

    @Override
    public String toString() {
        return "ReportCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (weeklyConsumption != null ? "weeklyConsumption=" + weeklyConsumption + ", " : "") +
                (monthlyConsumption != null ? "monthlyConsumption=" + monthlyConsumption + ", " : "") +
                (stationId != null ? "stationId=" + stationId + ", " : "") +
            "}";
    }

}
