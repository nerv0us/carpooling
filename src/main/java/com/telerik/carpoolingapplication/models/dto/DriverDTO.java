package com.telerik.carpoolingapplication.models.dto;

public class DriverDTO {
    private String username;

    private String firstName;

    private String lastName;

    private double ratingAsDriver;

    public DriverDTO() {
    }

    public DriverDTO(String username, String firstName, String lastName, double ratingAsDriver) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.ratingAsDriver = ratingAsDriver;
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

    public double getRatingAsDriver() {
        return ratingAsDriver;
    }

    public void setRatingAsDriver(double ratingAsDriver) {
        this.ratingAsDriver = ratingAsDriver;
    }
}
