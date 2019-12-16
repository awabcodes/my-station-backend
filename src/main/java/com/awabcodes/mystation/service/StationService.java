package com.awabcodes.mystation.service;

import com.awabcodes.mystation.domain.Station;
import com.awabcodes.mystation.repository.StationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Station}.
 */
@Service
@Transactional
public class StationService {

    private final Logger log = LoggerFactory.getLogger(StationService.class);

    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    /**
     * Save a station.
     *
     * @param station the entity to save.
     * @return the persisted entity.
     */
    public Station save(Station station) {
        log.debug("Request to save Station : {}", station);
        return stationRepository.save(station);
    }

    /**
     * Get all the stations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Station> findAll(Pageable pageable) {
        log.debug("Request to get all Stations");
        return stationRepository.findAll(pageable);
    }


    /**
     * Get one station by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Station> findOne(Long id) {
        log.debug("Request to get Station : {}", id);
        return stationRepository.findById(id);
    }

    /**
     * Delete the station by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Station : {}", id);
        stationRepository.deleteById(id);
    }
}
