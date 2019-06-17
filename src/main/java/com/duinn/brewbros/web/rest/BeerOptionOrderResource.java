package com.duinn.brewbros.web.rest;

import com.duinn.brewbros.domain.BeerOptionOrder;
import com.duinn.brewbros.service.BeerOptionOrderService;
import com.duinn.brewbros.web.rest.errors.BadRequestAlertException;

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
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.duinn.brewbros.domain.BeerOptionOrder}.
 */
@RestController
@RequestMapping("/api")
public class BeerOptionOrderResource {

    private final Logger log = LoggerFactory.getLogger(BeerOptionOrderResource.class);

    private static final String ENTITY_NAME = "beerOptionOrder";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BeerOptionOrderService beerOptionOrderService;

    public BeerOptionOrderResource(BeerOptionOrderService beerOptionOrderService) {
        this.beerOptionOrderService = beerOptionOrderService;
    }

    /**
     * {@code POST  /beer-option-orders} : Create a new beerOptionOrder.
     *
     * @param beerOptionOrder the beerOptionOrder to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new beerOptionOrder, or with status {@code 400 (Bad Request)} if the beerOptionOrder has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/beer-option-orders")
    public ResponseEntity<BeerOptionOrder> createBeerOptionOrder(@Valid @RequestBody BeerOptionOrder beerOptionOrder) throws URISyntaxException {
        log.debug("REST request to save BeerOptionOrder : {}", beerOptionOrder);
        if (beerOptionOrder.getId() != null) {
            throw new BadRequestAlertException("A new beerOptionOrder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BeerOptionOrder result = beerOptionOrderService.save(beerOptionOrder);
        return ResponseEntity.created(new URI("/api/beer-option-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /beer-option-orders} : Updates an existing beerOptionOrder.
     *
     * @param beerOptionOrder the beerOptionOrder to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated beerOptionOrder,
     * or with status {@code 400 (Bad Request)} if the beerOptionOrder is not valid,
     * or with status {@code 500 (Internal Server Error)} if the beerOptionOrder couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/beer-option-orders")
    public ResponseEntity<BeerOptionOrder> updateBeerOptionOrder(@Valid @RequestBody BeerOptionOrder beerOptionOrder) throws URISyntaxException {
        log.debug("REST request to update BeerOptionOrder : {}", beerOptionOrder);
        if (beerOptionOrder.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BeerOptionOrder result = beerOptionOrderService.save(beerOptionOrder);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, beerOptionOrder.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /beer-option-orders} : get all the beerOptionOrders.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of beerOptionOrders in body.
     */
    @GetMapping("/beer-option-orders")
    public ResponseEntity<List<BeerOptionOrder>> getAllBeerOptionOrders(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of BeerOptionOrders");
        Page<BeerOptionOrder> page = beerOptionOrderService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /beer-option-orders/:id} : get the "id" beerOptionOrder.
     *
     * @param id the id of the beerOptionOrder to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the beerOptionOrder, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/beer-option-orders/{id}")
    public ResponseEntity<BeerOptionOrder> getBeerOptionOrder(@PathVariable Long id) {
        log.debug("REST request to get BeerOptionOrder : {}", id);
        Optional<BeerOptionOrder> beerOptionOrder = beerOptionOrderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(beerOptionOrder);
    }

    /**
     * {@code DELETE  /beer-option-orders/:id} : delete the "id" beerOptionOrder.
     *
     * @param id the id of the beerOptionOrder to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/beer-option-orders/{id}")
    public ResponseEntity<Void> deleteBeerOptionOrder(@PathVariable Long id) {
        log.debug("REST request to delete BeerOptionOrder : {}", id);
        beerOptionOrderService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
