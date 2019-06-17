package com.duinn.brewbros.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A BeerOptionOrder.
 */
@Entity
@Table(name = "beer_option_order")
public class BeerOptionOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Min(value = 1)
    @Column(name = "amount")
    private Integer amount;

    @ManyToOne
    @JsonIgnoreProperties("beerOptionOrders")
    private Order order;

    @ManyToOne
    @JsonIgnoreProperties("beerOptionOrders")
    private BeerOption beerOption;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public BeerOptionOrder amount(Integer amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Order getOrder() {
        return order;
    }

    public BeerOptionOrder order(Order order) {
        this.order = order;
        return this;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public BeerOption getBeerOption() {
        return beerOption;
    }

    public BeerOptionOrder beerOption(BeerOption beerOption) {
        this.beerOption = beerOption;
        return this;
    }

    public void setBeerOption(BeerOption beerOption) {
        this.beerOption = beerOption;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BeerOptionOrder)) {
            return false;
        }
        return id != null && id.equals(((BeerOptionOrder) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "BeerOptionOrder{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            "}";
    }
}
