package com.telerik.carpoolingapplication.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "driver_ratings")
public class DriverRatingDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private double rating;

    @NotNull
    private int ratingGiverId;

    public DriverRatingDTO() {
    }

    public DriverRatingDTO(@NotNull double rating, @NotNull int ratingGiverId) {
        this.rating = rating;
        this.ratingGiverId = ratingGiverId;
    }

    public int getId() {
        return id;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getRatingGiverId() {
        return ratingGiverId;
    }

    public void setRatingGiverId(int ratingGiverId) {
        this.ratingGiverId = ratingGiverId;
    }
}
