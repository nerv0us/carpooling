package com.telerik.carpoolingapplication.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "ratings_passengers")
public class RatingPassenger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private double rating;

    @NotNull
    @ManyToOne
    private User ratingGiver;

    @NotNull
    @ManyToOne
    private User ratingReceiver;

    @NotNull
    @ManyToOne
    private Trip trip;

    public RatingPassenger() {
    }

    public RatingPassenger(@NotNull double rating, @NotNull User ratingGiver, @NotNull User ratingReceiver
            , @NotNull Trip trip) {
        this.rating = rating;
        this.ratingGiver = ratingGiver;
        this.ratingReceiver = ratingReceiver;
        this.trip = trip;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public User getRatingGiver() {
        return ratingGiver;
    }

    public void setRatingGiver(User ratingGiver) {
        this.ratingGiver = ratingGiver;
    }

    public User getRatingReceiver() {
        return ratingReceiver;
    }

    public void setRatingReceiver(User ratingReceiver) {
        this.ratingReceiver = ratingReceiver;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }
}
