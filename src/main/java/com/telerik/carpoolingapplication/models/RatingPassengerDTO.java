package com.telerik.carpoolingapplication.models;

public class RatingPassengerDTO {
    private double rating;

    private int passengerId;

    public RatingPassengerDTO() {
    }

    public RatingPassengerDTO(double rating, int passengerId) {
        this.rating = rating;
        this.passengerId = passengerId;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(int passengerId) {
        this.passengerId = passengerId;
    }
}
