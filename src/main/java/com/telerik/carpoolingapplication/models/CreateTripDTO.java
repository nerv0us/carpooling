package com.telerik.carpoolingapplication.models;

public class CreateTripDTO {
    private String carModel;
    private String message;
    //Date-time format
    private String departureTime;
    private String origin;
    private String destination;
    private int availablePlaces;
    private boolean smoking;
    private boolean pets;
    private boolean luggage;

    public CreateTripDTO() {
    }

    public CreateTripDTO(String carModel, String message, String departureTime, String origin
            , String destination, int availablePlaces, boolean smoking, boolean pets, boolean luggage) {
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
