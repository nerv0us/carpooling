package com.telerik.carpoolingapplication.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "users")
public class UserDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    private String username;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String email;
    @NotNull
    private String phone;
    private Double ratingAsDriver;
    private Double ratingAsPassenger;
    private String avatarUri;

    public UserDTO() {
    }

    public UserDTO(int id, String username, String firstName, String lastName, String email
            , String phone, Double ratingAsDriver, Double ratingAsPassenger, String avatarUri) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.ratingAsDriver = ratingAsDriver;
        this.ratingAsPassenger = ratingAsPassenger;
        this.avatarUri = avatarUri;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getRatingAsDriver() {
        return ratingAsDriver;
    }

    public void setRatingAsDriver(Double ratingAsDriver) {
        this.ratingAsDriver = ratingAsDriver;
    }

    public Double getRatingAsPassenger() {
        return ratingAsPassenger;
    }

    public void setRatingAsPassenger(Double ratingAsPassenger) {
        this.ratingAsPassenger = ratingAsPassenger;
    }

    public String getAvatarUri() {
        return avatarUri;
    }

    public void setAvatarUri(String avatarUri) {
        this.avatarUri = avatarUri;
    }
}
