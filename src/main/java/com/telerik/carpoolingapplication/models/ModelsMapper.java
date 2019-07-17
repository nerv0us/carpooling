package com.telerik.carpoolingapplication.models;

import com.telerik.carpoolingapplication.models.enums.PassengerStatus;
import com.telerik.carpoolingapplication.models.enums.TripStatus;

import java.util.ArrayList;

public class ModelsMapper {

    public static TripDTO fromCreateTripDTO(CreateTripDTO createTripDTO, UserDTO userDTO) {
        TripDTO tripDTO = new TripDTO();

        tripDTO.setDriver(userDTO);
        tripDTO.setCarModel(createTripDTO.getCarModel());
        tripDTO.setMessage(createTripDTO.getMessage());
        tripDTO.setDepartureTime(createTripDTO.getDepartureTime());
        tripDTO.setOrigin(createTripDTO.getOrigin());
        tripDTO.setDestination(createTripDTO.getDestination());
        tripDTO.setAvailablePlaces(createTripDTO.getAvailablePlaces());
        tripDTO.setSmoking(createTripDTO.smoking());
        tripDTO.setPets(createTripDTO.pets());
        tripDTO.setLuggage(createTripDTO.luggage());
        tripDTO.setPassengers(new ArrayList<>());
        tripDTO.setTripStatus(TripStatus.available);

        return tripDTO;
    }

    public static void updateTrip(TripDTO tripToEdit, EditTripDTO editTripDTO) {
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

    public static PassengerDTO fromUserToPassanger(UserDTO userDTO) {
        PassengerDTO passengerDTO = new PassengerDTO();

        passengerDTO.setUserId(userDTO.getId());
        passengerDTO.setUsername(userDTO.getUsername());
        passengerDTO.setFirstName(userDTO.getFirstName());
        passengerDTO.setLastName(userDTO.getLastName());
        passengerDTO.setEmail(userDTO.getEmail());
        passengerDTO.setPhone(userDTO.getPhone());
        passengerDTO.setRatingAsPassenger(userDTO.getRatingAsPassenger());
        passengerDTO.setPassengerStatus(PassengerStatus.pending);

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

        return user;
    }
}
