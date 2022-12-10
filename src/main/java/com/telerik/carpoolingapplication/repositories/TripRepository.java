package com.telerik.carpoolingapplication.repositories;

import com.telerik.carpoolingapplication.models.*;
import com.telerik.carpoolingapplication.models.dto.*;
import com.telerik.carpoolingapplication.models.enums.PassengerStatusEnum;
import com.telerik.carpoolingapplication.models.enums.TripStatus;

import java.util.List;

public interface TripRepository {
    List<TripDTO> getFilteredTrips(TripStatus status, String driverUsername, String origin, String destination,
                                   String latestDepartureTime, String earliestDepartureTime, Integer places,
                                   Boolean cigarettes, Boolean animals, Boolean baggage);

    void createTrip(CreateTripDTO createTripDTO, int userId);

    void editTrip(EditTripDTO editTripDTO, int userId);

    TripDTO getTrip(int tripId);

    void changeTripStatus(TripDTO tripDTO, TripStatus updatedStatus);

    void addComment(TripDTO tripDTO, CommentDTO commentDTO, int userId);

    void apply(int id, UserDTO user);

    void changePassengerStatus(PassengerStatus passengerStatus, int tripId, int placesReducingValue);

    void rateDriver(TripDTO tripDTO, UserDTO userDTO, RatingDTO ratingDTO);

    void ratePassenger(int tripId, int passengerId, UserDTO userDTO, RatingDTO ratingDTO);

    List<PassengerStatus> passengers(int tripId, int userId, PassengerStatusEnum passengerStatusEnum);

    double calculateAverageRating(int userId, boolean isReceiverDriver);
}