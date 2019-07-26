package com.telerik.carpoolingapplication.repositories;

import com.telerik.carpoolingapplication.models.*;
import com.telerik.carpoolingapplication.models.enums.TripStatus;

import java.util.List;

public interface TripRepository {
    List<TripDTO> getFilteredTrips(TripStatus status, String driverUsername, String origin, String destination
            , String latestDepartureTime, String earliestDepartureTime
            , int places, boolean cigarettes, boolean animals, boolean baggage);

    void createTrip(CreateTripDTO createTripDTO);

    void editTrip(EditTripDTO editTripDTO);

    TripDTO getTrip(int id);

    void changeTripStatus(TripDTO tripDTO, TripStatus updatedStatus);

    void addComment(TripDTO tripDTO, CommentDTO commentDTO);

    void apply(int id);

    void changePassengerStatus(int tripId, int passengerId, String status);

    void rateDriver(int id, RatingDTO ratingDTO);

    void ratePassenger(int tripId, int passengerId, RatingDTO ratingDTO);

}