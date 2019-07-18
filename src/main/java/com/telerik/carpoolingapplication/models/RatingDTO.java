package com.telerik.carpoolingapplication.models;

public class RatingDTO {
    private Double rating;

    public RatingDTO() {
    }

    public RatingDTO(Double rating) {
        this.rating = rating;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
