package com.telerik.carpoolingapplication.services;

import com.telerik.carpoolingapplication.models.TripDTO;

import java.util.List;

public interface SortService {
    List<TripDTO> getTripsSorted(String parameter, String value);
}
