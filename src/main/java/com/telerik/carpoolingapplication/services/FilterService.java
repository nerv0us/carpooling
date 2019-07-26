package com.telerik.carpoolingapplication.services;

import com.telerik.carpoolingapplication.models.TripDTO;

import java.util.List;

public interface FilterService {
    List<TripDTO> getTripsUnsortedUnfiltered();

    List<TripDTO> getTripsFiltered(String parameter, String value);
}