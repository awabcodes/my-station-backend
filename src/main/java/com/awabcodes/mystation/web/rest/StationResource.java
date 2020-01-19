package com.awabcodes.mystation.web.rest;

import com.awabcodes.mystation.domain.Station;
import com.awabcodes.mystation.service.StationService;
import com.awabcodes.mystation.web.rest.errors.BadRequestAlertException;
import com.awabcodes.mystation.service.dto.StationCriteria;
import com.awabcodes.mystation.service.StationQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing {@link com.awabcodes.mystation.domain.Station}.
 */
@RestController
@RequestMapping("/api")
public class StationResource {

    private final Logger log = LoggerFactory.getLogger(StationResource.class);

    private static final String ENTITY_NAME = "station";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StationService stationService;

    private final StationQueryService stationQueryService;

    public StationResource(StationService stationService, StationQueryService stationQueryService) {
        this.stationService = stationService;
        this.stationQueryService = stationQueryService;
    }

    /**
     * {@code POST  /stations} : Create a new station.
     *
     * @param station the station to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new station, or with status {@code 400 (Bad Request)} if the station has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/stations")
    public ResponseEntity<Station> createStation(@Valid @RequestBody Station station) throws URISyntaxException {
        log.debug("REST request to save Station : {}", station);
        if (station.getId() != null) {
            throw new BadRequestAlertException("A new station cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Station result = stationService.save(station);
        return ResponseEntity.created(new URI("/api/stations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /stations} : Updates an existing station.
     *
     * @param station the station to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated station,
     * or with status {@code 400 (Bad Request)} if the station is not valid,
     * or with status {@code 500 (Internal Server Error)} if the station couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/stations")
    public ResponseEntity<Station> updateStation(@Valid @RequestBody Station station) throws URISyntaxException {
        log.debug("REST request to update Station : {}", station);
        if (station.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Station result = stationService.save(station);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, station.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /stations} : get all the stations.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of stations in body.
     */
    @GetMapping("/stations")
    public ResponseEntity<List<Station>> getAllStations(StationCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Stations by criteria: {}", criteria);
        Page<Station> page = stationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /stations/count} : count all the stations.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/stations/count")
    public ResponseEntity<Long> countStations(StationCriteria criteria) {
        log.debug("REST request to count Stations by criteria: {}", criteria);
        return ResponseEntity.ok().body(stationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /stations/:id} : get the "id" station.
     *
     * @param id the id of the station to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the station, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/stations/{id}")
    public ResponseEntity<Station> getStation(@PathVariable Long id) {
        log.debug("REST request to get Station : {}", id);
        Optional<Station> station = stationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(station);
    }

    /**
     * {@code DELETE  /stations/:id} : delete the "id" station.
     *
     * @param id the id of the station to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/stations/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
        log.debug("REST request to delete Station : {}", id);
        stationService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
