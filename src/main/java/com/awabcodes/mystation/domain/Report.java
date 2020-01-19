package com.awabcodes.mystation.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A Report.
 */
@Entity
@Table(name = "report")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Report implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "weekly_consumption", nullable = false)
    private String weeklyConsumption;

    @NotNull
    @Column(name = "monthly_consumption", nullable = false)
    private String monthlyConsumption;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Station station;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Report title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWeeklyConsumption() {
        return weeklyConsumption;
    }

    public Report weeklyConsumption(String weeklyConsumption) {
        this.weeklyConsumption = weeklyConsumption;
        return this;
    }

    public void setWeeklyConsumption(String weeklyConsumption) {
        this.weeklyConsumption = weeklyConsumption;
    }

    public String getMonthlyConsumption() {
        return monthlyConsumption;
    }

    public Report monthlyConsumption(String monthlyConsumption) {
        this.monthlyConsumption = monthlyConsumption;
        return this;
    }

    public void setMonthlyConsumption(String monthlyConsumption) {
        this.monthlyConsumption = monthlyConsumption;
    }

    public Station getStation() {
        return station;
    }

    public Report station(Station station) {
        this.station = station;
        return this;
    }

    public void setStation(Station station) {
        this.station = station;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Report)) {
            return false;
        }
        return id != null && id.equals(((Report) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Report{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", weeklyConsumption='" + getWeeklyConsumption() + "'" +
            ", monthlyConsumption='" + getMonthlyConsumption() + "'" +
            "}";
    }
}
