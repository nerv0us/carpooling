package com.telerik.carpoolingapplication.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "ratings")
public class RatingDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private double rating;

    @NotNull
    private int ratingGiverId;

    @NotNull
    private int ratingReceiverId;

    public RatingDTO() {
    }

    public RatingDTO(@NotNull double rating, @NotNull int ratingGiverId, @NotNull int ratingReceiverId) {
        this.rating = rating;
        this.ratingGiverId = ratingGiverId;
        this.ratingReceiverId = ratingReceiverId;
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

    public int getRatingReceiverId() {
        return ratingReceiverId;
    }

    public void setRatingReceiverId(int ratingReceiverId) {
        this.ratingReceiverId = ratingReceiverId;
    }
}
