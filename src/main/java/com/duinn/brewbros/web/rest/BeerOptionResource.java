package com.duinn.brewbros.web.rest;

import com.duinn.brewbros.domain.BeerOption;
import com.duinn.brewbros.service.BeerOptionService;
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
 * REST controller for managing {@link com.duinn.brewbros.domain.BeerOption}.
 */
@RestController
@RequestMapping("/api")
public class BeerOptionResource {

    private final Logger log = LoggerFactory.getLogger(BeerOptionResource.class);

    private static final String ENTITY_NAME = "beerOption";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BeerOptionService beerOptionService;

    public BeerOptionResource(BeerOptionService beerOptionService) {
        this.beerOptionService = beerOptionService;
    }

    /**
     * {@code POST  /beer-options} : Create a new beerOption.
     *
     * @param beerOption the beerOption to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new beerOption, or with status {@code 400 (Bad Request)} if the beerOption has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/beer-options")
    public ResponseEntity<BeerOption> createBeerOption(@Valid @RequestBody BeerOption beerOption) throws URISyntaxException {
        log.debug("REST request to save BeerOption : {}", beerOption);
        if (beerOption.getId() != null) {
            throw new BadRequestAlertException("A new beerOption cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BeerOption result = beerOptionService.save(beerOption);
        return ResponseEntity.created(new URI("/api/beer-options/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /beer-options} : Updates an existing beerOption.
     *
     * @param beerOption the beerOption to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated beerOption,
     * or with status {@code 400 (Bad Request)} if the beerOption is not valid,
     * or with status {@code 500 (Internal Server Error)} if the beerOption couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/beer-options")
    public ResponseEntity<BeerOption> updateBeerOption(@Valid @RequestBody BeerOption beerOption) throws URISyntaxException {
        log.debug("REST request to update BeerOption : {}", beerOption);
        if (beerOption.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BeerOption result = beerOptionService.save(beerOption);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, beerOption.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /beer-options} : get all the beerOptions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of beerOptions in body.
     */
    @GetMapping("/beer-options")
    public ResponseEntity<List<BeerOption>> getAllBeerOptions(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of BeerOptions");
        Page<BeerOption> page = beerOptionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /beer-options/:id} : get the "id" beerOption.
     *
     * @param id the id of the beerOption to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the beerOption, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/beer-options/{id}")
    public ResponseEntity<BeerOption> getBeerOption(@PathVariable Long id) {
        log.debug("REST request to get BeerOption : {}", id);
        Optional<BeerOption> beerOption = beerOptionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(beerOption);
    }

    /**
     * {@code DELETE  /beer-options/:id} : delete the "id" beerOption.
     *
     * @param id the id of the beerOption to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/beer-options/{id}")
    public ResponseEntity<Void> deleteBeerOption(@PathVariable Long id) {
        log.debug("REST request to delete BeerOption : {}", id);
        beerOptionService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
