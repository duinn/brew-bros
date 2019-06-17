package com.duinn.brewbros.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

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

    @OneToMany(mappedBy = "beerOptionOrders")
    private Set<BeerOption> beerOptions = new HashSet<>();

    @ManyToOne
    private Order order;

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

    public Set<BeerOption> getBeerOptions() {
        return beerOptions;
    }

    public BeerOptionOrder beerOptions(Set<BeerOption> beerOptions) {
        this.beerOptions = beerOptions;
        return this;
    }

    public BeerOptionOrder addBeerOption(BeerOption beerOption) {
        this.beerOptions.add(beerOption);
        beerOption.setBeerOptionOrders(this);
        return this;
    }

    public BeerOptionOrder removeBeerOption(BeerOption beerOption) {
        this.beerOptions.remove(beerOption);
        beerOption.setBeerOptionOrders(null);
        return this;
    }

    public void setBeerOptions(Set<BeerOption> beerOptions) {
        this.beerOptions = beerOptions;
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
