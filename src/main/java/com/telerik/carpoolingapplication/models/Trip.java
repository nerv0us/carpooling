package com.telerik.carpoolingapplication.models;

import com.telerik.carpoolingapplication.models.enums.TripStatus;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "trips")
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @NotNull
    private User driver;

    @NotNull
    private String carModel;

    @NotNull
    private String message;

    @NotNull
    private String departureTime;

    @NotNull
    private String origin;

    @NotNull
    private String destination;

    @NotNull
    private int availablePlaces;

    @NotNull
    private TripStatus tripStatus;

    private boolean smoking;
    private boolean pets;
    private boolean luggage;

    public Trip() {
    }

    public Trip(User driver, String carModel, String message, String departureTime, String origin, String destination,
                int availablePlaces, TripStatus tripStatus, boolean smoking, boolean pets, boolean luggage) {
        this.driver = driver;
        this.carModel = carModel;
        this.message = message;
        this.departureTime = departureTime;
        this.origin = origin;
        this.destination = destination;
        this.availablePlaces = availablePlaces;
        this.tripStatus = tripStatus;
        this.smoking = smoking;
        this.pets = pets;
        this.luggage = luggage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getDriver() {
        return driver;
    }

    public void setDriver(User driver) {
        this.driver = driver;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getAvailablePlaces() {
        return availablePlaces;
    }

    public void setAvailablePlaces(int availablePlaces) {
        this.availablePlaces = availablePlaces;
    }

    public TripStatus getTripStatus() {
        return tripStatus;
    }

    public void setTripStatus(TripStatus tripStatus) {
        this.tripStatus = tripStatus;
    }

    public boolean isSmoking() {
        return smoking;
    }

    public void setSmoking(boolean smoking) {
        this.smoking = smoking;
    }

    public boolean isPets() {
        return pets;
    }

    public void setPets(boolean pets) {
        this.pets = pets;
    }

    public boolean isLuggage() {
        return luggage;
    }

    public void setLuggage(boolean luggage) {
        this.luggage = luggage;
    }
}
