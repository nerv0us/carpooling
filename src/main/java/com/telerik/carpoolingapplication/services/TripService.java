package com.telerik.carpoolingapplication.services;

import com.telerik.carpoolingapplication.models.*;

import java.util.List;

public interface TripService {

    List<TripDTO> getTrips(String tripStatus, String driverUsername
            , String origin, String destination, String latestDepartureTime
            , String earliestDepartureTime, String availablePlaces
            , String smoking, String pets, String luggage, String sortParameter, String ascending);

    void createTrip(CreateTripDTO createTripDTO, UserDTO user);

    void editTrip(EditTripDTO editTripDTO);

    TripDTO getTrip(int id);

    void changeTripStatus(int id, String status);

    void addComment(int id, CommentDTO commentDTO);

    void apply(int id);

    void changePassengerStatus(int tripId, int passengerId, String status);

    void rateDriver(int id, RatingDTO ratingDTO);

    void ratePassenger(int tripId, int passengerId, RatingDTO ratingDTO);
}
