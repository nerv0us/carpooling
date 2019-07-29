package com.telerik.carpoolingapplication.models;

import com.telerik.carpoolingapplication.models.enums.PassengerStatusEnum;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "statuses")
public class PassengerStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @OneToOne
    private User user;

    @NotNull
    private PassengerStatusEnum status;

    @NotNull
    @OneToOne
    private Trip trip;

    public PassengerStatus() {
    }

    public PassengerStatus(@NotNull User user, @NotNull PassengerStatusEnum status
            , @NotNull Trip trip) {
        this.user = user;
        this.status = status;
        this.trip = trip;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public PassengerStatusEnum getStatus() {
        return status;
    }

    public void setStatus(PassengerStatusEnum passengerStatusEnum) {
        this.status = passengerStatusEnum;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }
}
