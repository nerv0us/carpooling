package com.telerik.carpoolingapplication.services;

import com.telerik.carpoolingapplication.models.*;
import com.telerik.carpoolingapplication.models.constants.Constants;
import com.telerik.carpoolingapplication.models.enums.TripStatus;
import com.telerik.carpoolingapplication.repositories.FilterRepository;
import com.telerik.carpoolingapplication.repositories.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TripServiceImpl implements TripService {
    private TripRepository tripRepository;
    private FilterRepository filterRepository;

    @Autowired
    public TripServiceImpl(TripRepository tripRepository, FilterRepository filterRepository) {
        this.tripRepository = tripRepository;
        this.filterRepository = filterRepository;
    }

    @Override
    public void createTrip(CreateTripDTO createTripDTO) {
        tripRepository.createTrip(createTripDTO);
    }

    @Override
    public void editTrip(EditTripDTO editTripDTO) {
        tripRepository.editTrip(editTripDTO);
    }

    @Override
    public TripDTO getTrip(int id) {
        return tripRepository.getTrip(id);
    }

    @Override
    public void changeTripStatus(int id, String status) {
        // Throws IllegalArgumentException(Messages.TRIP_NOT_FOUND);
        TripDTO tripDTO = getTrip(id);
        try {
            TripStatus updatedStatus = TripStatus.valueOf(status);
            tripRepository.changeTripStatus(tripDTO, updatedStatus);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(Constants.NO_SUCH_STATUS);
        }
    }

    @Override
    public void addComment(int id, CommentDTO commentDTO) {
        // Throws IllegalArgumentException(Messages.TRIP_NOT_FOUND);
        TripDTO tripDTO = getTrip(id);

        // Throws IllegalArgumentException(Messages.UNAUTHORIZED);
        tripRepository.addComment(tripDTO, commentDTO);
    }

    @Override
    public void apply(int id) {
        tripRepository.apply(id);
    }

    @Override
    public void changePassengerStatus(int tripId, int passengerId, String status) {
        tripRepository.changePassengerStatus(tripId, passengerId, status);
    }

    @Override
    public void rateDriver(int id, RatingDTO ratingDTO) {
        tripRepository.rateDriver(id, ratingDTO);
    }

    @Override
    public void ratePassenger(int tripId, int passengerId, RatingDTO ratingDTO) {
        tripRepository.ratePassenger(tripId, passengerId, ratingDTO);
    }
}
