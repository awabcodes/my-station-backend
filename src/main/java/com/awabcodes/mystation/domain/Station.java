package com.awabcodes.mystation.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A Station.
 */
@Entity
@Table(name = "station")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Station implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Min(value = 0)
    @Max(value = 100)
    @Column(name = "gas_level", nullable = false)
    private Integer gasLevel;

    @NotNull
    @Min(value = 0)
    @Max(value = 100)
    @Column(name = "benzene_level", nullable = false)
    private Integer benzeneLevel;

    @NotNull
    @Column(name = "last_tank_fill", nullable = false)
    private Instant lastTankFill;

    @NotNull
    @Column(name = "city", nullable = false)
    private String city;

    @NotNull
    @Column(name = "location", nullable = false)
    private String location;

    @NotNull
    @Column(name = "map_url", nullable = false)
    private String mapUrl;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Station name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getGasLevel() {
        return gasLevel;
    }

    public Station gasLevel(Integer gasLevel) {
        this.gasLevel = gasLevel;
        return this;
    }

    public void setGasLevel(Integer gasLevel) {
        this.gasLevel = gasLevel;
    }

    public Integer getBenzeneLevel() {
        return benzeneLevel;
    }

    public Station benzeneLevel(Integer benzeneLevel) {
        this.benzeneLevel = benzeneLevel;
        return this;
    }

    public void setBenzeneLevel(Integer benzeneLevel) {
        this.benzeneLevel = benzeneLevel;
    }

    public Instant getLastTankFill() {
        return lastTankFill;
    }

    public Station lastTankFill(Instant lastTankFill) {
        this.lastTankFill = lastTankFill;
        return this;
    }

    public void setLastTankFill(Instant lastTankFill) {
        this.lastTankFill = lastTankFill;
    }

    public String getCity() {
        return city;
    }

    public Station city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLocation() {
        return location;
    }

    public Station location(String location) {
        this.location = location;
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMapUrl() {
        return mapUrl;
    }

    public Station mapUrl(String mapUrl) {
        this.mapUrl = mapUrl;
        return this;
    }

    public void setMapUrl(String mapUrl) {
        this.mapUrl = mapUrl;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Station)) {
            return false;
        }
        return id != null && id.equals(((Station) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Station{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", gasLevel=" + getGasLevel() +
            ", benzeneLevel=" + getBenzeneLevel() +
            ", lastTankFill='" + getLastTankFill() + "'" +
            ", city='" + getCity() + "'" +
            ", location='" + getLocation() + "'" +
            ", mapUrl='" + getMapUrl() + "'" +
            "}";
    }
}
