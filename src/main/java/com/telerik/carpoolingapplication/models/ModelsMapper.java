package com.telerik.carpoolingapplication.models;

import com.telerik.carpoolingapplication.models.enums.TripStatus;

import java.util.ArrayList;
import java.util.List;

public class ModelsMapper {

    public static List<TripDTO> fromTrip(List<Trip> trips) {
        List<TripDTO> tripDTOS = new ArrayList<>();
        for (Trip trip : trips) {
            TripDTO tripDTO = fromTrip(trip);
            tripDTOS.add(tripDTO);
        }
        return tripDTOS;
    }

    public static TripDTO fromTrip(Trip trip) {
        List<PassengerDTO> tripDTOPassengers = new ArrayList<>();
        for (User user : trip.getPassengers()) {
            PassengerDTO currentPassenger = fromUserToPassenger(user);
            tripDTOPassengers.add(currentPassenger);
        }
        TripDTO tripDTO = new TripDTO(trip.getId(), trip.getDriver(), trip.getCarModel()
                , trip.getMessage(), trip.getDepartureTime(), trip.getOrigin(), trip.getDestination()
                , trip.getAvailablePlaces(), trip.getTripStatus(), trip.isSmoking()
                , trip.isPets(), trip.isLuggage());
        tripDTO.setPassengers(tripDTOPassengers);
        return tripDTO;
    }

    public static Trip fromCreateTripDTO(CreateTripDTO createTripDTO, User user) {
        Trip trip = new Trip();

        trip.setDriver(user);
        trip.setCarModel(createTripDTO.getCarModel());
        trip.setMessage(createTripDTO.getMessage());
        trip.setDepartureTime(createTripDTO.getDepartureTime());
        trip.setOrigin(createTripDTO.getOrigin());
        trip.setDestination(createTripDTO.getDestination());
        trip.setAvailablePlaces(createTripDTO.getAvailablePlaces());
        trip.setSmoking(createTripDTO.smoking());
        trip.setPets(createTripDTO.pets());
        trip.setLuggage(createTripDTO.luggage());
        trip.setPassengers(new ArrayList<>());
        trip.setTripStatus(TripStatus.available);

        return trip;
    }

    public static void updateTrip(Trip tripToEdit, EditTripDTO editTripDTO) {
        tripToEdit.setCarModel(editTripDTO.getCarModel());
        tripToEdit.setMessage(editTripDTO.getMessage());
        tripToEdit.setDepartureTime(editTripDTO.getDepartureTime());
        tripToEdit.setOrigin(editTripDTO.getOrigin());
        tripToEdit.setDestination(editTripDTO.getDestination());
        tripToEdit.setAvailablePlaces(editTripDTO.getAvailablePlaces());
        tripToEdit.setSmoking(editTripDTO.smoking());
        tripToEdit.setPets(editTripDTO.pets());
        tripToEdit.setLuggage(editTripDTO.luggage());
    }

    public static PassengerDTO fromUserToPassenger(User user) {
        PassengerDTO passengerDTO = new PassengerDTO();

        passengerDTO.setUserId(user.getId());
        passengerDTO.setUsername(user.getUsername());
        passengerDTO.setFirstName(user.getFirstName());
        passengerDTO.setLastName(user.getLastName());
        passengerDTO.setEmail(user.getEmail());
        passengerDTO.setPhone(user.getPhone());
        passengerDTO.setRatingAsPassenger(user.getRatingAsPassenger());
        return passengerDTO;
    }

    public static void editUser(UserDTO userToEdit, UserDTO userDTO) {
        userToEdit.setUsername(userDTO.getUsername());
        userToEdit.setFirstName(userDTO.getFirstName());
        userToEdit.setLastName(userDTO.getLastName());
        userToEdit.setEmail(userDTO.getEmail());
        userToEdit.setPhone(userDTO.getPhone());
        userToEdit.setRatingAsDriver(userDTO.getRatingAsDriver());
        userToEdit.setRatingAsPassenger(userDTO.getRatingAsPassenger());
        userToEdit.setAvatarUri(userDTO.getAvatarUri());
    }

    public static UserDTO createUser(CreateUserDTO userDTO) {
        UserDTO user = new UserDTO();
        user.setUsername(userDTO.getUsername());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        user.setAvatarUri("static/images/users/defaultUserPhoto.jpg");
        user.setRatingAsPassenger(0D);
        user.setRatingAsDriver(0D);

        return user;
    }
}
