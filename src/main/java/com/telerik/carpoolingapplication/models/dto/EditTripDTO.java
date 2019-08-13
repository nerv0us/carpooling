package com.telerik.carpoolingapplication.models.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class EditTripDTO {
    @NotNull
    private int id;

    @NotNull
    @Size(min = 3, max = 20, message = "Car model should be between 3 and 20 symbols")
    private String carModel;

    @Size(min = 3, max = 200, message = "Message should be between 3 and 200 symbols")
    private String message;

    @NotNull
    @Size(min = 18, max = 19, message = "Departure time should be between 18 and 19 symbols")
    private String departureTime;

    @NotNull
    @Size(min = 3, max = 30, message = "Origin should be between 3 and 30 symbols")
    private String origin;

    @Size(min = 3, max = 30, message = "Destination should be between 3 and 30 symbols")
    private String destination;

    @NotNull
    @Min(1)
    @Max(7)
    private int availablePlaces;

    private boolean smoking;
    private boolean pets;
    private boolean luggage;

    public EditTripDTO() {
    }

    public EditTripDTO(int id, String carModel, String message, String departureTime, String origin
            , String destination, int availablePlaces, boolean smoking, boolean pets, boolean luggage) {
        this.id = id;
        this.carModel = carModel;
        this.message = message;
        this.departureTime = departureTime;
        this.origin = origin;
        this.destination = destination;
        this.availablePlaces = availablePlaces;
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

    public boolean smoking() {
        return smoking;
    }

    public void setSmoking(boolean smoking) {
        this.smoking = smoking;
    }

    public boolean pets() {
        return pets;
    }

    public void setPets(boolean pets) {
        this.pets = pets;
    }

    public boolean luggage() {
        return luggage;
    }

    public void setLuggage(boolean luggage) {
        this.luggage = luggage;
    }
}
