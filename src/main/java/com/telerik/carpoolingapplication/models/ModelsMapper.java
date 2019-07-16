package com.telerik.carpoolingapplication.models;

import com.telerik.carpoolingapplication.models.enums.TripStatus;

import java.util.ArrayList;

public class ModelsMapper {
    public static TripDTO fromCreateTripDTO(CreateTripDTO createTripDTO, UserDTO userDTO){
        TripDTO tripDTO = new TripDTO();

        tripDTO.setDriver(userDTO);
        tripDTO.setCarModel(createTripDTO.getCarModel());
        tripDTO.setMessage(createTripDTO.getMessage());
        tripDTO.setDepartureTime(createTripDTO.getDepartureTime());
        tripDTO.setOrigin(createTripDTO.getOrigin());
        tripDTO.setDestination(createTripDTO.getDestination());
        tripDTO.setAvailablePlaces(createTripDTO.getAvailablePlaces());
        tripDTO.setSmoking(createTripDTO.isSmoking());
        tripDTO.setPets(createTripDTO.isPets());
        tripDTO.setLuggage(createTripDTO.isLuggage());
        tripDTO.setPassengers(new ArrayList<>());
        tripDTO.setTripStatus(TripStatus.available);

        return tripDTO;
    }
}
