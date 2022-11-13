package com.example.springintro.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "games")
public class Game extends BaseEntity {
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String trailer;
    @Column(nullable = false)
    private String thumbnail;

    @Column(nullable = false)
    private double size;
    @Column(nullable = false)
    private double price;
    private String description;
    @Column(name = "release_date", nullable = false)
    private LocalDate releaseDate;

    public Game(String title, String trailer, String thumbnail, double size, double price,
                String description, LocalDate releaseDate) {
        this.title = title;
        this.trailer = trailer;
        this.thumbnail = thumbnail;
        this.size = size;
        this.price = price;
        this.description = description;
        this.releaseDate = releaseDate;
    }

    public Game() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }
}
