package com.telerik.carpoolingapplication.services;

import com.telerik.carpoolingapplication.models.CreateTripDTO;
import com.telerik.carpoolingapplication.models.TripDTO;

import java.util.List;

public interface TripService {
    List<TripDTO> getTrips();

    void createTrip(CreateTripDTO createTripDTO);
}