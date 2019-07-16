package com.telerik.carpoolingapplication.models;

import com.telerik.carpoolingapplication.models.enums.TripStatus;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "trips")
public class TripDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @NotNull
    private UserDTO driver;

    @NotNull
    private String carModel;

    @NotNull
    private String message;

    //Date-time format
    @NotNull
    private String departureTime;

    @NotNull
    private String origin;
    @NotNull
    private String destination;
    @NotNull
    private int availablePlaces;
    @ManyToMany()
    @JoinTable(name = "trips_passengers",
            joinColumns = {@JoinColumn(name = "trip_id")},
            inverseJoinColumns = {@JoinColumn(name = "passenger_id")})
    private List<PassengerDTO> passengers;
    @NotNull
    private TripStatus tripStatus;
    @OneToMany
    @JoinTable(name = "trips_comments",
            joinColumns = {@JoinColumn(name = "trip_id")},
            inverseJoinColumns = {@JoinColumn(name = "comment_id")})
    private List<CommentDTO> comments;
    private boolean smoking;
    private boolean pets;
    private boolean luggage;

    public TripDTO() {
    }

    public TripDTO(@NotNull UserDTO driver, @NotNull String carModel, @NotNull String message
            , @NotNull String departureTime, @NotNull String origin, @NotNull String destination
            , @NotNull int availablePlaces, List<PassengerDTO> passengers, @NotNull TripStatus tripStatus
            , List<CommentDTO> comments, boolean smoking, boolean pets, boolean luggage) {
        this.driver = driver;
        this.carModel = carModel;
        this.message = message;
        this.departureTime = departureTime;
        this.origin = origin;
        this.destination = destination;
        this.availablePlaces = availablePlaces;
        this.passengers = passengers;
        this.tripStatus = tripStatus;
        this.comments = comments;
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
