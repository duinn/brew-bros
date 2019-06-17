package com.duinn.brewbros.repository;

import com.duinn.brewbros.domain.BeerOption;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the BeerOption entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BeerOptionRepository extends JpaRepository<BeerOption, Long> {

}
