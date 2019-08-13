package com.telerik.carpoolingapplication.services;

import com.telerik.carpoolingapplication.exceptions.UnauthorizedException;
import com.telerik.carpoolingapplication.exceptions.ValidationException;
import com.telerik.carpoolingapplication.models.*;
import com.telerik.carpoolingapplication.models.constants.Constants;
import com.telerik.carpoolingapplication.models.dto.*;
import com.telerik.carpoolingapplication.models.enums.PassengerStatusEnum;
import com.telerik.carpoolingapplication.models.enums.TripStatus;
import com.telerik.carpoolingapplication.repositories.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    public List<TripDTO> getTrips(Integer page, Integer showElements, String tripStatus, String driverUsername
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

        if (page == null || page == 1) {
            if (trips.isEmpty()) {
                return new ArrayList<>();
            } else if (trips.size() < showElements) {
                return trips.subList(0, trips.size());
            } else {
                return trips.subList(0, showElements);
            }
        }

        int start = (page - 1) * showElements;
        int end = start + showElements;

        if (start > trips.size() - 1) {
            return new ArrayList<>();
        } else if (trips.size() < end) {
            return trips.subList(start, trips.size());
        } else {
            return trips.subList(start, end);
        }
    }

    @Override
    public void createTrip(CreateTripDTO createTripDTO, UserDTO user) {
        if (user == null) {
            throw new ValidationException(Constants.USER_NOT_FOUND);
        }
        departureTimeValidator(createTripDTO.getDepartureTime());
        tripRepository.createTrip(createTripDTO, user.getId());
    }

    @Override
    public void editTrip(EditTripDTO editTripDTO, UserDTO user) {
        TripDTO trip = getTrip(editTripDTO.getId(), user);
        if ((trip.getDriver().getId() != user.getId())) {
            throw new UnauthorizedException(Constants.NOT_A_DRIVER);
        }
        departureTimeValidator(editTripDTO.getDepartureTime());
        tripRepository.editTrip(editTripDTO, user.getId());
    }

    @Override
    public TripDTO getTrip(int id) {
        try {
            return tripRepository.getTrip(id);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(Constants.TRIP_NOT_FOUND);
        }
    }

    @Override
    public TripDTO getTrip(int id, UserDTO user) {
        if (user == null) {
            throw new ValidationException(Constants.USER_NOT_FOUND);
        }
        return tripRepository.getTrip(id);
    }

    @Override
    public void changeTripStatus(int id, UserDTO user, String status) {
        TripDTO trip = getTrip(id, user);
        if ((trip.getDriver().getId()) != (user.getId())) {
            throw new UnauthorizedException(Constants.NOT_A_DRIVER);
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
        tripRepository.addComment(tripDTO, commentDTO, user.getId());
    }

    @Override
    public void apply(int tripId, UserDTO user) {
        TripDTO tripDTO = getTrip(tripId, user);
        if (tripDTO.getDriver().getId() == user.getId()) {
            throw new UnauthorizedException(Constants.YOUR_OWN_TRIP);
        }
        List<PassengerDTO> passengers = tripDTO.getPassengers();
        PassengerDTO passengerDTO = passengers.stream()
                .filter(p -> p.getUserId() == user.getId())
                .findFirst()
                .orElse(null);
        if (passengerDTO != null) {
            throw new UnauthorizedException(Constants.ALREADY_APPLIED);
        }

        tripRepository.apply(tripId, user);
    }

    @Override
    public void changePassengerStatus(int tripId, int passengerId, UserDTO user, String status) {
        TripDTO tripDTO = getTrip(tripId, user);
        if (tripDTO.getDriver().getId() != user.getId()) {
            throw new UnauthorizedException(Constants.NOT_A_DRIVER);
        }
        List<PassengerStatus> passengerStatuses = tripRepository.passengers(tripId, passengerId, null);
        if (passengerStatuses.isEmpty()) {
            throw new IllegalArgumentException(Constants.NO_SUCH_PASSENGER);
        }
        PassengerStatus passengerStatus = passengerStatuses.get(0);
        PassengerStatusEnum oldStatus = passengerStatus.getStatus();
        PassengerStatusEnum newStatus;
        try {
            newStatus = PassengerStatusEnum.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(Constants.NO_SUCH_STATUS);
        }

        if (String.valueOf(oldStatus).equals(String.valueOf(newStatus))) {
            throw new UnauthorizedException(Constants.SAME_STATUS);
        }

        if (newStatus == PassengerStatusEnum.ACCEPTED && tripDTO.getAvailablePlaces() == 0) {
            throw new UnauthorizedException(Constants.NO_AVAILABLE_PLACES);
        }
        int placesReducingValue = 0;
        if (newStatus == PassengerStatusEnum.ACCEPTED) {
            placesReducingValue = -1;
        } else if (oldStatus == PassengerStatusEnum.ACCEPTED) {
            placesReducingValue = 1;
        }
        passengerStatus.setStatus(newStatus);
        tripRepository.changePassengerStatus(passengerStatus, tripDTO.getId(), placesReducingValue);
    }

    @Override
    public void rateDriver(int tripId, UserDTO user, RatingDTO ratingDTO) {
        TripDTO tripDTO = getTrip(tripId, user);
        if (tripDTO.getDriver().getId() == user.getId()) {
            throw new UnauthorizedException(Constants.RATE_YOURSELF);
        }
        if (tripDTO.getTripStatus() != TripStatus.DONE) {
            throw new UnauthorizedException(Constants.RATING_NOT_ALLOWED_BEFORE_TRIP_IS_DONE);
        }
        List<PassengerStatus> passengerStatuses = tripRepository.passengers(tripId, user.getId()
                , PassengerStatusEnum.ACCEPTED);
        if (passengerStatuses.isEmpty()) {
            throw new UnauthorizedException(Constants.YOU_DO_NOT_PARTICIPATE);
        }
        tripRepository.rateDriver(tripDTO, user, ratingDTO);
    }

    @Override
    public void ratePassenger(int tripId, int passengerId, UserDTO user, RatingDTO ratingDTO) {
        TripDTO tripDTO = getTrip(tripId, user);
        if (passengerId == user.getId()) {
            throw new UnauthorizedException(Constants.RATE_YOURSELF);
        }
        if (tripDTO.getTripStatus() != TripStatus.DONE) {
            throw new UnauthorizedException(Constants.RATING_NOT_ALLOWED_BEFORE_TRIP_IS_DONE);
        }
        if (tripRepository.passengers(tripId, user.getId(), PassengerStatusEnum.ACCEPTED).isEmpty()
                && tripDTO.getDriver().getId() != user.getId()) {
            throw new UnauthorizedException(Constants.YOU_DO_NOT_PARTICIPATE);
        }
        if (tripRepository.passengers(tripId, passengerId, PassengerStatusEnum.ACCEPTED).isEmpty()) {
            throw new IllegalArgumentException(Constants.NO_SUCH_PASSENGER);
        }
        tripRepository.ratePassenger(tripId, passengerId, user, ratingDTO);
    }

    private void departureTimeValidator(String time) {
        LocalDateTime departureTime = LocalDateTime.parse(time
                , DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a"));
        if (departureTime.isBefore(LocalDateTime.now()) || departureTime.isEqual(LocalDateTime.now())) {
            throw new IllegalArgumentException(Constants.CREATE_TRIP_IN_PAST);
        }
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
