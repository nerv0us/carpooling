package com.telerik.carpoolingapplication.models.dto;

import org.hibernate.annotations.ColumnDefault;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserDTO {
    @NotNull
    private int id;

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
    private double ratingAsDriver;

    @NotNull
    @ColumnDefault("0")
    private double ratingAsPassenger;

    private String avatarUri;

    public UserDTO() {
    }

    public UserDTO(@NotNull int id,@NotNull String username, @NotNull String firstName, @NotNull String lastName
            , @NotNull String email, @NotNull String phone, Double ratingAsDriver
            , Double ratingAsPassenger, String avatarUri) {
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

    public double getRatingAsDriver() {
        return ratingAsDriver;
    }

    public void setRatingAsDriver(double ratingAsDriver) {
        this.ratingAsDriver = ratingAsDriver;
    }

    public double getRatingAsPassenger() {
        return ratingAsPassenger;
    }

    public void setRatingAsPassenger(double ratingAsPassenger) {
        this.ratingAsPassenger = ratingAsPassenger;
    }

    public String getAvatarUri() {
        return avatarUri;
    }

    public void setAvatarUri(String avatarUri) {
        this.avatarUri = avatarUri;
    }
}
