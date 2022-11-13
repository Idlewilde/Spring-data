package com.example.springintro.model.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "orders")
public class Order extends BaseEntity {
    @ManyToOne(optional = false)
    private User buyer;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Game> products;

    public Order() {
    }

    public Order(User buyer, Set<Game> products) {
        this.buyer = buyer;
        this.products = products;
    }

    public Set<Game> getProducts() {
        return products;
    }

    public void setGames(Set<Game> products) {
        this.products = products;
    }

    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

}
