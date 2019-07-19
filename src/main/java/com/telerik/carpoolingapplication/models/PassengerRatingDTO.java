package com.telerik.carpoolingapplication.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "passengers_ratings")
public class PassengerRatingDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private double rating;

    @NotNull
    private int ratingReceiverId;

    public PassengerRatingDTO() {
    }

    public PassengerRatingDTO(@NotNull double rating) {
        this.rating = rating;
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

    public int getRatingReceiverId() {
        return ratingReceiverId;
    }

    public void setRatingReceiverId(int ratingReceiverId) {
        this.ratingReceiverId = ratingReceiverId;
    }
}
