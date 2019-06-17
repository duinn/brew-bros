package com.duinn.brewbros.domain;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import com.duinn.brewbros.domain.enumeration.OrderStatus;

/**
 * A Order.
 */
@Entity
@Table(name = "jhi_order")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "placed_date_time")
    private Instant placedDateTime;

    @Column(name = "delivery_date_time")
    private Instant deliveryDateTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order")
    private Set<BeerOptionOrder> beerOptionOrders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getPlacedDateTime() {
        return placedDateTime;
    }

    public Order placedDateTime(Instant placedDateTime) {
        this.placedDateTime = placedDateTime;
        return this;
    }

    public void setPlacedDateTime(Instant placedDateTime) {
        this.placedDateTime = placedDateTime;
    }

    public Instant getDeliveryDateTime() {
        return deliveryDateTime;
    }

    public Order deliveryDateTime(Instant deliveryDateTime) {
        this.deliveryDateTime = deliveryDateTime;
        return this;
    }

    public void setDeliveryDateTime(Instant deliveryDateTime) {
        this.deliveryDateTime = deliveryDateTime;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public Order orderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
        return this;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Set<BeerOptionOrder> getBeerOptionOrders() {
        return beerOptionOrders;
    }

    public Order beerOptionOrders(Set<BeerOptionOrder> beerOptionOrders) {
        this.beerOptionOrders = beerOptionOrders;
        return this;
    }

    public Order addBeerOptionOrder(BeerOptionOrder beerOptionOrder) {
        this.beerOptionOrders.add(beerOptionOrder);
        beerOptionOrder.setOrder(this);
        return this;
    }

    public Order removeBeerOptionOrder(BeerOptionOrder beerOptionOrder) {
        this.beerOptionOrders.remove(beerOptionOrder);
        beerOptionOrder.setOrder(null);
        return this;
    }

    public void setBeerOptionOrders(Set<BeerOptionOrder> beerOptionOrders) {
        this.beerOptionOrders = beerOptionOrders;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Order)) {
            return false;
        }
        return id != null && id.equals(((Order) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Order{" +
            "id=" + getId() +
            ", placedDateTime='" + getPlacedDateTime() + "'" +
            ", deliveryDateTime='" + getDeliveryDateTime() + "'" +
            ", orderStatus='" + getOrderStatus() + "'" +
            "}";
    }
}
