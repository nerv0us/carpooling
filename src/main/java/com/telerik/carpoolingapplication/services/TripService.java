package com.telerik.carpoolingapplication.services;

import com.telerik.carpoolingapplication.models.*;

import java.util.List;

public interface TripService {
    List<TripDTO> getTrips();

    void createTrip(CreateTripDTO createTripDTO);

    void editTrip(EditTripDTO editTripDTO);

    TripDTO getTrip(int id);

    void changeTripStatus(int id, String status);

    void addComment(int id, CommentDTO commentDTO);

    /*void apply(int id);

    void changePassengerStatus(int tripId, int passengerId, String status);

    void rateDriver(int id, DriverRatingDTO driverRatingDTO);

    void ratePassenger(int tripId, int passengerId, PassengerRatingDTO passengerRatingDTO);*/
}
