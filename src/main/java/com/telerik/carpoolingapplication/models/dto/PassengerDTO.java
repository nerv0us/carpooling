package com.telerik.carpoolingapplication.models.dto;

import com.telerik.carpoolingapplication.models.enums.PassengerStatusEnum;
import org.hibernate.annotations.ColumnDefault;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PassengerDTO {
    @NotNull
    private int userId;

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
    @Email
    private String email;

    @NotNull
    @Size(min = 4, max = 25, message = "Phone number should be between 4 and 25 characters.")
    private String phone;

    @NotNull
    @ColumnDefault("0")
    private Double ratingAsPassenger;

    @NotNull
    private PassengerStatusEnum passengerStatusEnum;

    public PassengerDTO() {
    }

    public PassengerDTO(int userId, @NotNull String username, @NotNull String firstName
            , @NotNull String lastName, @NotNull String email, @NotNull String phone
            , Double ratingAsPassenger) {
        this.userId = userId;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.ratingAsPassenger = ratingAsPassenger;
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

    public PassengerStatusEnum getPassengerStatusEnum() {
        return passengerStatusEnum;
    }

    public void setPassengerStatusEnum(PassengerStatusEnum passengerStatusEnum) {
        this.passengerStatusEnum = passengerStatusEnum;
    }
}
