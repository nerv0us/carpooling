package com.telerik.carpoolingapplication.services;

import com.telerik.carpoolingapplication.models.*;
import com.telerik.carpoolingapplication.models.constants.Constants;
import com.telerik.carpoolingapplication.models.enums.TripStatus;
import com.telerik.carpoolingapplication.repositories.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class TripServiceImpl implements TripService {
    private TripRepository tripRepository;

    @Autowired
    public TripServiceImpl(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    @Override
    public List<TripDTO> getTrips(String tripStatus, String driverUsername
            , String origin, String destination, String latestDepartureTime
            , String earliestDepartureTime, String availablePlaces
            , String smoking, String pets, String luggage, String sortParameter, String ascending) {

        TripStatus status = tripStatusParser(tripStatus);
        Integer places = integerParser(availablePlaces);
        Boolean cigarettes = booleanParser(smoking);
        Boolean animals = booleanParser(pets);
        Boolean baggage = booleanParser(luggage);

        List<TripDTO> trips = tripRepository.getFilteredTrips(status, driverUsername, origin, destination, latestDepartureTime
                , earliestDepartureTime, places, cigarettes, animals, baggage);

        if (sortParameter != null) {
            tripSorter(trips, sortParameter, ascending);
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

    private TripStatus tripStatusParser(String tripStatus) {
        if (tripStatus != null) {
            try {
                return TripStatus.valueOf(tripStatus);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(Constants.NO_SUCH_STATUS);
            }
        }
        return null;
    }

    private Integer integerParser(String availablePlaces) {
        if (availablePlaces != null) {
            try {
                return Integer.parseInt(availablePlaces);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(Constants.NOT_A_NUMBER);
            }
        }
        return null;
    }

    private Boolean booleanParser(String smoking) {
        if (smoking != null) {
            try {
                return Boolean.parseBoolean(smoking);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(Constants.NOT_A_BOOLEAN);
            }
        }
        return null;
    }

    private void tripSorter(List<TripDTO> trips, String sortParameter, String ascending) {
        Comparator<TripDTO> comparator;
        if (ascending == null) {
            ascending = ".";
        }
        switch (sortParameter) {
            case "driverRating":
                if (ascending.equals("true")) {
                    comparator = (o1, o2) -> {
                        if (o1.getDriver().getRatingAsDriver() > o2.getDriver().getRatingAsDriver()) {
                            return 1;
                        }
                        return -1;
                    };
                } else {
                    comparator = (o1, o2) -> {
                        if (o1.getDriver().getRatingAsDriver() < o2.getDriver().getRatingAsDriver()) {
                            return 1;
                        }
                        return -1;
                    };
                }
                trips.sort(comparator);
                break;
            case "departureTime":
                if (ascending.equals("true")) {
                    comparator = Comparator.comparing(TripDTO::getDepartureTime);
                } else {
                    comparator = Comparator.comparing(TripDTO::getDepartureTime).reversed();
                }
                trips.sort(comparator);
                break;
            case "availablePlaces":
                if (ascending.equals("true")) {
                    comparator = (o1, o2) -> {
                        if (o1.getAvailablePlaces() > o2.getAvailablePlaces()) {
                            return 1;
                        }
                        return -1;
                    };
                } else {
                    comparator = (o1, o2) -> {
                        if (o1.getAvailablePlaces() < o2.getAvailablePlaces()) {
                            return 1;
                        }
                        return -1;
                    };
                }
                trips.sort(comparator);
                break;
        }
    }
}
