package com.telerik.carpoolingapplication.services;

import com.telerik.carpoolingapplication.models.dto.*;

import java.util.List;

public interface TripService {

    List<TripDTO> getTrips(Integer page ,String tripStatus, String driverUsername
            , String origin, String destination, String latestDepartureTime
            , String earliestDepartureTime, String availablePlaces
            , String smoking, String pets, String luggage, String sortParameter, String ascending);

    void createTrip(CreateTripDTO createTripDTO, UserDTO user);

    void editTrip(EditTripDTO editTripDTO, UserDTO user);

    TripDTO getTrip(int id);

    TripDTO getTrip(int tripId, UserDTO user);

    void changeTripStatus(int tripId, UserDTO user, String status);

    void addComment(int tripId, UserDTO user, CommentDTO commentDTO);

    void apply(int tripId, UserDTO user);

    void changePassengerStatus(int tripId, int passengerId, UserDTO user, String status);

    void rateDriver(int tripId, UserDTO user, RatingDTO ratingDTO);

    void ratePassenger(int tripId, int passengerId, UserDTO user, RatingDTO ratingDTO);
}
