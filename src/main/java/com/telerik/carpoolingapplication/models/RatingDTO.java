package com.telerik.carpoolingapplication.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "ratings")
public class RatingDTO {
    @Id
    private int id;

    @NotNull
    private Double rating;

    @ManyToOne
    @NotNull
    private UserDTO userDTO;

    public RatingDTO() {
    }

    public RatingDTO(Double rating, UserDTO userDTO) {
        this.rating = rating;
        this.userDTO = userDTO;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }
}
