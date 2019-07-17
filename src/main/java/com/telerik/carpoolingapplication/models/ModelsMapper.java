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
}
