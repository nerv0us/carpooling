package com.telerik.carpoolingapplication.services;

import com.telerik.carpoolingapplication.models.*;
import com.telerik.carpoolingapplication.models.constants.Constants;
import com.telerik.carpoolingapplication.models.enums.TripStatus;
import com.telerik.carpoolingapplication.repositories.FilterAndSortHelper;
import com.telerik.carpoolingapplication.repositories.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TripServiceImpl implements TripService {
    private TripRepository tripRepository;
    private FilterAndSortHelper filterAndSortHelper;

    @Autowired
    public TripServiceImpl(TripRepository tripRepository, FilterAndSortHelper filterAndSortHelper) {
        this.tripRepository = tripRepository;
        this.filterAndSortHelper = filterAndSortHelper;
    }

    @Override
    public List<TripDTO> getTrips(String type, String parameter, String value) {
        List<TripDTO> trips = new ArrayList<>();
        if (type == null) {
            trips = filterAndSortHelper.unsortedUnfiltered();
        } else if (type.equals("filter")) {
            if (parameter.equals("status")) {
                try {
                    trips = filterAndSortHelper.filterByStatus(TripStatus.valueOf(value));
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException(Constants.NO_SUCH_STATUS);
                }
            } else if (parameter.equals("driver")) {
                trips = filterAndSortHelper.filterByDriver(value);
            }
        }



        if (trips == null || trips.isEmpty()) {
            throw new IllegalArgumentException(Constants.NOT_AVAILABLE_TRIPS);
        }
        return trips;
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
