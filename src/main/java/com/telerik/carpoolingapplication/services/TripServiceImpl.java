package com.telerik.carpoolingapplication.services;

import com.telerik.carpoolingapplication.models.*;
import com.telerik.carpoolingapplication.models.constants.Constants;
import com.telerik.carpoolingapplication.models.enums.TripStatus;
import com.telerik.carpoolingapplication.repositories.TripRepository;
import com.telerik.carpoolingapplication.security.CustomUserDetailsService;
import com.telerik.carpoolingapplication.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class TripServiceImpl implements TripService {
    private final TripRepository tripRepository;

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
    public void createTrip(CreateTripDTO createTripDTO, UserDTO user) {
        if (user == null) {
            throw new IllegalArgumentException(Constants.USER_NOT_FOUND);
        }
        tripRepository.createTrip(createTripDTO, user.getId());
    }

    @Override
    public void editTrip(EditTripDTO editTripDTO, UserDTO user) {
        TripDTO trip = getTrip(editTripDTO.getId(), user);
        if (user == null) {
            throw new IllegalArgumentException(Constants.USER_NOT_FOUND);
        }
        if (!(trip.getDriver().getId() == user.getId())) {
            throw new IllegalArgumentException(Constants.NOT_A_DRIVER);
        }
        tripRepository.editTrip(editTripDTO, user.getId());
    }

    @Override
    public TripDTO getTrip(int id, UserDTO user) {
        TripDTO trip = tripRepository.getTrip(id);
        if (user == null) {
            throw new IllegalArgumentException(Constants.USER_NOT_FOUND);
        }
        return trip;
    }

    @Override
    public void changeTripStatus(int id, UserDTO user, String status) {
        TripDTO trip = getTrip(id, user);
        if (!(trip.getDriver().getId() == user.getId())) {
            throw new IllegalArgumentException(Constants.NOT_A_DRIVER);
        }
        try {
            TripStatus updatedStatus = TripStatus.valueOf(status);
            tripRepository.changeTripStatus(trip, updatedStatus);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(Constants.NO_SUCH_STATUS);
        }
    }

    @Override
    public void addComment(int tripId, UserDTO user, CommentDTO commentDTO) {
        TripDTO tripDTO = getTrip(tripId, user);
        if (user.getId() != commentDTO.getUserId()) {
            throw new IllegalArgumentException();
        }
        tripRepository.addComment(tripDTO, commentDTO);
    }

    @Override
    public void apply(int tripId, UserDTO user) {
        TripDTO tripDTO = getTrip(tripId, user);
        if (tripDTO.getDriver().getId() == user.getId()) {
            throw new IllegalArgumentException(Constants.YOUR_OWN_TRIP);
        }
        List<PassengerDTO> passengers = tripDTO.getPassengers();
        PassengerDTO passengerDTO = passengers.stream()
                .filter(p -> p.getUserId() == user.getId())
                .findFirst()
                .orElse(null);
        if (passengerDTO != null) {
            throw new IllegalArgumentException(Constants.ALREADY_APPLIED);
        }

        tripRepository.apply(tripId, user);
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
