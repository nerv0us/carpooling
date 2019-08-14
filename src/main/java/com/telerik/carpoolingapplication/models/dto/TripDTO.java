package com.telerik.carpoolingapplication.models.dto;

import com.telerik.carpoolingapplication.models.enums.TripStatus;

import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

public class TripDTO {
    @NotNull
    private int id;

    @ManyToOne
    @NotNull
    private UserDTO driver;

    @NotNull
    @Size(min = 3, max = 20, message = "Car model should be between 3 and 20 symbols")
    private String carModel;

    @NotNull
    @Size(min = 3, max = 200, message = "Message should be between 3 and 200 symbols")
    private String message;

    @NotNull
    @Size(min = 18, max = 19, message = "Departure time should be between 18 and 19 symbols")
    private String departureTime;

    @NotNull
    @Size(min = 3, max = 30, message = "Origin should be between 3 and 30 symbols")
    private String origin;

    @NotNull
    @Size(min = 3, max = 30, message = "Destination should be between 3 and 30 symbols")
    private String destination;

    @NotNull
    @Min(1)
    @Max(7)
    private int availablePlaces;

    private List<PassengerDTO> passengers;

    @NotNull
    private TripStatus tripStatus;

    private List<CommentDTO> comments;

    private boolean smoking;
    private boolean pets;
    private boolean luggage;

    public TripDTO() {
    }

    public TripDTO(int id, UserDTO driver, String carModel, String message, String departureTime, String origin,
                   String destination, int availablePlaces, TripStatus tripStatus, boolean smoking, boolean pets,
                   boolean luggage) {
        this.id = id;
        this.driver = driver;
        this.carModel = carModel;
        this.message = message;
        this.departureTime = departureTime;
        this.origin = origin;
        this.destination = destination;
        this.availablePlaces = availablePlaces;
        this.passengers = new ArrayList<>();
        this.tripStatus = tripStatus;
        this.comments = new ArrayList<>();
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

    public UserDTO getDriver() {
        return driver;
    }

    public void setDriver(UserDTO driver) {
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

    public List<PassengerDTO> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<PassengerDTO> passengers) {
        this.passengers = passengers;
    }

    public TripStatus getTripStatus() {
        return tripStatus;
    }

    public void setTripStatus(TripStatus tripStatus) {
        this.tripStatus = tripStatus;
    }

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
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
