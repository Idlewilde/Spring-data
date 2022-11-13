package com.example.springintro.model.entity.DTOs;

import org.springframework.validation.annotation.Validated;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Validated
public class CreateGameDTO {
    private String title;
    private double price;
    private double size;
    private String trailer;
    private String thumbnailURL;
    private String description;
    private LocalDate releaseDate;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public CreateGameDTO(String[] commandParts) throws ParseException {
        this.title = commandParts[1];
        this.price = Double.parseDouble(commandParts[2]);
        this.size = Double.parseDouble(commandParts[3]);
        this.trailer = commandParts[4].split("=")[commandParts[3].split("=").length - 1];
        this.thumbnailURL = commandParts[5];
        this.description = commandParts[6];
        this.releaseDate = LocalDate.parse(commandParts[7], formatter);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
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

    public void validate() {
        if (!this.title.substring(0, 1).toUpperCase().equals(this.title.substring(0, 1))
                || this.title.length() > 100 || this.title.length() < 3 || this.price < 0 || this.size < 0 ||
                this.trailer.length() != 11 || (!this.thumbnailURL.startsWith("https://")
                && !this.thumbnailURL.startsWith("http://")) && this.description.length() < 20) {
            throw new IllegalArgumentException("Game cannot be created with provided information!");
        }
    }
}
