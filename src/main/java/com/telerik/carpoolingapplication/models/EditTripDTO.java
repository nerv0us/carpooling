package com.telerik.carpoolingapplication.models;

public class EditTripDTO {
    //?
    private int id;
    private String carModel;
    private String message;
    //Date-time format
    private String departure;
    private String origin;
    private String destination;
    private int availablePlaces;
    private boolean smoking;
    private boolean pets;
    private boolean luggage;

    public EditTripDTO() {
    }

    public EditTripDTO(int id, String carModel, String message, String departure, String origin
            , String destination, int availablePlaces, boolean smoking, boolean pets, boolean luggage) {
        this.id = id;
        this.carModel = carModel;
        this.message = message;
        this.departure = departure;
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

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
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
