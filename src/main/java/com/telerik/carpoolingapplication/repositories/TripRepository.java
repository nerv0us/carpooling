package com.telerik.carpoolingapplication.repositories;

import com.telerik.carpoolingapplication.models.TripDTO;

import java.util.List;

public interface TripRepository {
    List<TripDTO> getTrips();
}
