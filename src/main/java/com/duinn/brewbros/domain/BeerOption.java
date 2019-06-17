package com.duinn.brewbros.domain;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A BeerOption.
 */
@Entity
@Table(name = "beer_option")
public class BeerOption implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Min(value = 0)
    @Column(name = "amount")
    private Integer amount;

    @Column(name = "name")
    private String name;

    @Column(name = "brand")
    private String brand;

    @Column(name = "volume")
    private Integer volume;

    @DecimalMin(value = "0")
    @DecimalMax(value = "100")
    @Column(name = "abv")
    private Double abv;

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

    public BeerOption amount(Integer amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public BeerOption name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public BeerOption brand(String brand) {
        this.brand = brand;
        return this;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Integer getVolume() {
        return volume;
    }

    public BeerOption volume(Integer volume) {
        this.volume = volume;
        return this;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public Double getAbv() {
        return abv;
    }

    public BeerOption abv(Double abv) {
        this.abv = abv;
        return this;
    }

    public void setAbv(Double abv) {
        this.abv = abv;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BeerOption)) {
            return false;
        }
        return id != null && id.equals(((BeerOption) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "BeerOption{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", name='" + getName() + "'" +
            ", brand='" + getBrand() + "'" +
            ", volume=" + getVolume() +
            ", abv=" + getAbv() +
            "}";
    }
}
