package com.telerik.carpoolingapplication.models.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class RatingDTO {
    @NotNull
    @Min(1)
    @Max(5)
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
