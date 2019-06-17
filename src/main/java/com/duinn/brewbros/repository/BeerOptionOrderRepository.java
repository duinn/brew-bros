package com.duinn.brewbros.repository;

import com.duinn.brewbros.domain.BeerOptionOrder;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the BeerOptionOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BeerOptionOrderRepository extends JpaRepository<BeerOptionOrder, Long> {

}
