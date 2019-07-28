package com.telerik.carpoolingapplication.services;

import com.telerik.carpoolingapplication.models.*;

import java.util.List;

public interface TripService {

    List<TripDTO> getTrips(String tripStatus, String driverUsername
            , String origin, String destination, String latestDepartureTime
            , String earliestDepartureTime, String availablePlaces
            , String smoking, String pets, String luggage, String sortParameter, String ascending);

    void createTrip(CreateTripDTO createTripDTO, UserDTO user);

    void editTrip(EditTripDTO editTripDTO, UserDTO user);

    TripDTO getTrip(int tripId, UserDTO user);

    void changeTripStatus(int tripId, UserDTO user, String status);

    void addComment(int tripId, UserDTO user, CommentDTO commentDTO);

    void apply(int id);

    void changePassengerStatus(int tripId, int passengerId, String status);

    void rateDriver(int id, RatingDTO ratingDTO);

    void ratePassenger(int tripId, int passengerId, RatingDTO ratingDTO);
}
