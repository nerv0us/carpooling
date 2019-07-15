package com.telerik.carpoolingapplication.models;

import com.telerik.carpoolingapplication.models.enums.PassengerStatus;

public class PassengerDTO {
    private int userId;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Double ratingAsPassenger;
    private PassengerStatus passengerStatus;

    public PassengerDTO() {
    }

    public PassengerDTO(int userId, String username, String firstName, String lastName
            , String email, String phone, Double ratingAsPassenger, PassengerStatus passengerStatus) {
        this.userId = userId;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.ratingAsPassenger = ratingAsPassenger;
        this.passengerStatus = passengerStatus;
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
