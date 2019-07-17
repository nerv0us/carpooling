package com.telerik.carpoolingapplication.models;

import com.telerik.carpoolingapplication.models.enums.PassengerStatus;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "passengers")
public class PassengerDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    private int userId;
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
    private Double ratingAsPassenger;
    @NotNull
    private PassengerStatus passengerStatus;

    public PassengerDTO() {
    }

    public PassengerDTO(int userId, @NotNull String username, @NotNull String firstName
            , @NotNull String lastName, @NotNull String email, @NotNull String phone
            , Double ratingAsPassenger, @NotNull PassengerStatus passengerStatus) {
        this.userId = userId;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.ratingAsPassenger = ratingAsPassenger;
        this.passengerStatus = passengerStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public Double getRatingAsPassenger() {
        return ratingAsPassenger;
    }

    public void setRatingAsPassenger(Double ratingAsPassenger) {
        this.ratingAsPassenger = ratingAsPassenger;
    }

    public PassengerStatus getPassengerStatus() {
        return passengerStatus;
    }

    public void setPassengerStatus(PassengerStatus passengerStatus) {
        this.passengerStatus = passengerStatus;
    }
}
