package com.telerik.carpoolingapplication.services;

import com.telerik.carpoolingapplication.models.*;
import com.telerik.carpoolingapplication.models.constants.Constants;
import com.telerik.carpoolingapplication.models.enums.TripStatus;
import com.telerik.carpoolingapplication.repositories.FilterHelper;
import com.telerik.carpoolingapplication.repositories.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Service
public class TripServiceImpl implements TripService {
    private TripRepository tripRepository;
    private FilterHelper filterHelper;

    @Autowired
    public TripServiceImpl(TripRepository tripRepository, FilterHelper filterHelper) {
        this.tripRepository = tripRepository;
        this.filterHelper = filterHelper;
    }

    @Override
    public List<TripDTO> getTrips(String type, String parameter, String value) {
        List<TripDTO> trips = new ArrayList<>();
        if (type == null) {
            trips = filterHelper.unsortedUnfiltered();
        } else if (type.equals("filter")) {
            if (parameter == null) {
                trips = filterHelper.unsortedUnfiltered();
            } else if (parameter.equals("status")) {
                if (value == null) {
                    trips = filterHelper.unsortedUnfiltered();
                } else {
                    try {
                        trips = filterHelper.filterByStatus(TripStatus.valueOf(value));
                    } catch (IllegalArgumentException e) {
                        throw new IllegalArgumentException(Constants.NO_SUCH_STATUS);
                    }
                }
            } else if (parameter.equals("driver")) {
                trips = checkIfValueIsNull(value, () -> filterHelper.unsortedUnfiltered()
                        , () -> filterHelper.filterByDriver(value));
            } else if (parameter.equals("origin")) {
                trips = checkIfValueIsNull(value, () -> filterHelper.unsortedUnfiltered()
                        , () -> filterHelper.filterByOrigin(value));
            } else if (parameter.equals("destination")) {
                trips = checkIfValueIsNull(value, () -> filterHelper.unsortedUnfiltered()
                        , () -> filterHelper.filterByDestination(value));
            } else if (parameter.equals("earliestDepartureTime")) {
                trips = checkIfValueIsNull(value, () -> filterHelper.unsortedUnfiltered()
                        , () -> filterHelper.filterByEarliestDepartureTime(value));
            } else if (parameter.equals("latestDepartureTime")) {
                trips = checkIfValueIsNull(value, () -> filterHelper.unsortedUnfiltered()
                        , () -> filterHelper.filterByLatestDepartureTime(value));
            } else if (parameter.equals("availablePlaces")) {
                try {
                    if (value != null) {
                        int availablePlaces = Integer.parseInt(value);
                        trips = filterHelper.filterByAvailablePlaces(availablePlaces);
                    } else {
                        trips = filterHelper.unsortedUnfiltered();
                    }
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException(Constants.NOT_A_NUMBER);
                }
            } else if (parameter.equals("smoking")) {
                checkIfValueIsBoolean(value);
                trips = checkIfValueIsNull(value, () -> filterHelper.unsortedUnfiltered()
                        , () -> filterHelper.filterBySmoking(Boolean.valueOf(value)));
            } else if (parameter.equals("pets")) {
                checkIfValueIsBoolean(value);
                trips = checkIfValueIsNull(value, () -> filterHelper.unsortedUnfiltered()
                        , () -> filterHelper.filterByPets(Boolean.valueOf(value)));
            } else if (parameter.equals("luggage")) {
                checkIfValueIsBoolean(value);
                trips = checkIfValueIsNull(value, () -> filterHelper.unsortedUnfiltered()
                        , () -> filterHelper.filterByLuggage(Boolean.valueOf(value)));
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

    private List<TripDTO> checkIfValueIsNull(String value
            , Supplier<List<TripDTO>> allTrips, Supplier<List<TripDTO>> filteredTrips) {
        if (value == null) {
            return allTrips.get();
        } else {
            return filteredTrips.get();
        }
    }

    private void checkIfValueIsBoolean(String value) {
        if (value != null && !(value.equals("true") || value.equals("false"))) {
            throw new IllegalArgumentException(Constants.NOT_A_BOOLEAN);
        }
    }
}
