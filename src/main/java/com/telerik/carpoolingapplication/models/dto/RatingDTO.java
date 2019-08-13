package com.telerik.carpoolingapplication.models.dto;

public class RatingDTO {
    private double rating;

    public RatingDTO() {
    }

    public RatingDTO(double rating) {
        this.rating = rating;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
