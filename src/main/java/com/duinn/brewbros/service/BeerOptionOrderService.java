package com.duinn.brewbros.service;

import com.duinn.brewbros.domain.BeerOptionOrder;
import com.duinn.brewbros.repository.BeerOptionOrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link BeerOptionOrder}.
 */
@Service
@Transactional
public class BeerOptionOrderService {

    private final Logger log = LoggerFactory.getLogger(BeerOptionOrderService.class);

    private final BeerOptionOrderRepository beerOptionOrderRepository;

    public BeerOptionOrderService(BeerOptionOrderRepository beerOptionOrderRepository) {
        this.beerOptionOrderRepository = beerOptionOrderRepository;
    }

    /**
     * Save a beerOptionOrder.
     *
     * @param beerOptionOrder the entity to save.
     * @return the persisted entity.
     */
    public BeerOptionOrder save(BeerOptionOrder beerOptionOrder) {
        log.debug("Request to save BeerOptionOrder : {}", beerOptionOrder);
        return beerOptionOrderRepository.save(beerOptionOrder);
    }

    /**
     * Get all the beerOptionOrders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BeerOptionOrder> findAll(Pageable pageable) {
        log.debug("Request to get all BeerOptionOrders");
        return beerOptionOrderRepository.findAll(pageable);
    }


    /**
     * Get one beerOptionOrder by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BeerOptionOrder> findOne(Long id) {
        log.debug("Request to get BeerOptionOrder : {}", id);
        return beerOptionOrderRepository.findById(id);
    }

    /**
     * Delete the beerOptionOrder by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete BeerOptionOrder : {}", id);
        beerOptionOrderRepository.deleteById(id);
    }
}
