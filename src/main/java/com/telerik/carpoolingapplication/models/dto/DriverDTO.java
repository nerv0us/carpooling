package com.telerik.carpoolingapplication.models.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class DriverDTO {
    @NotNull
    @Size(min = 3, max = 50, message = "Username should be between 3 and 50 characters.")
    private String username;

    @NotNull
    @Size(min = 3, max = 25, message = "First name should be between 3 and 25 characters.")
    private String firstName;

    @NotNull
    @Size(min = 3, max = 25, message = "Last name should be between 3 and 25 characters.")
    private String lastName;

    @NotNull
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
