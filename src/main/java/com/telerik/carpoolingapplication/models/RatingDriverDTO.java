package com.telerik.carpoolingapplication.models;

public class RatingDriverDTO {
    private double rating;

    public RatingDriverDTO() {
    }

    public RatingDriverDTO(double rating) {
        this.rating = rating;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
