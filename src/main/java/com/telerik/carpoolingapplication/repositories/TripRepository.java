package com.telerik.carpoolingapplication.repositories;

import com.telerik.carpoolingapplication.models.*;
import com.telerik.carpoolingapplication.models.enums.TripStatus;

import java.util.List;

public interface TripRepository {
    List<TripDTO> getTrips();

    void createTrip(CreateTripDTO createTripDTO);

    void editTrip(EditTripDTO editTripDTO);

    TripDTO getTrip(int id);

    void changeTripStatus(TripDTO tripDTO, TripStatus updatedStatus);

    void addComment(TripDTO tripDTO, CommentDTO commentDTO);

    void apply(TripDTO tripDTO);

    void changePassengerStatus(int tripId, int passengerId, String status);

    void rateDriver(int id, RatingDTO ratingDTO);
}
