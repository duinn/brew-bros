package com.duinn.brewbros.service;

import com.duinn.brewbros.domain.BeerOption;
import com.duinn.brewbros.repository.BeerOptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link BeerOption}.
 */
@Service
@Transactional
public class BeerOptionService {

    private final Logger log = LoggerFactory.getLogger(BeerOptionService.class);

    private final BeerOptionRepository beerOptionRepository;

    public BeerOptionService(BeerOptionRepository beerOptionRepository) {
        this.beerOptionRepository = beerOptionRepository;
    }

    /**
     * Save a beerOption.
     *
     * @param beerOption the entity to save.
     * @return the persisted entity.
     */
    public BeerOption save(BeerOption beerOption) {
        log.debug("Request to save BeerOption : {}", beerOption);
        return beerOptionRepository.save(beerOption);
    }

    /**
     * Get all the beerOptions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BeerOption> findAll(Pageable pageable) {
        log.debug("Request to get all BeerOptions");
        return beerOptionRepository.findAll(pageable);
    }


    /**
     * Get one beerOption by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BeerOption> findOne(Long id) {
        log.debug("Request to get BeerOption : {}", id);
        return beerOptionRepository.findById(id);
    }

    /**
     * Delete the beerOption by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete BeerOption : {}", id);
        beerOptionRepository.deleteById(id);
    }
}
