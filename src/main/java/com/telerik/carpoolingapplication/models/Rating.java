package com.telerik.carpoolingapplication.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "ratings")
public class Rating {
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
    private boolean isReceiverDriver;

    @NotNull
    @ManyToOne
    private Trip trip;

    public Rating() {
    }

    public Rating(@NotNull double rating, @NotNull User ratingGiver, @NotNull User ratingReceiver
            , @NotNull boolean isReceiverDriver, @NotNull Trip trip) {
        this.rating = rating;
        this.ratingGiver = ratingGiver;
        this.ratingReceiver = ratingReceiver;
        this.isReceiverDriver = isReceiverDriver;
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

    public boolean isReceiverDriver() {
        return isReceiverDriver;
    }

    public void setReceiverDriver(boolean receiverDriver) {
        isReceiverDriver = receiverDriver;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }
}
