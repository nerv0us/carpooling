package com.telerik.carpoolingapplication.repositories;

import com.telerik.carpoolingapplication.models.TripDTO;

import java.util.List;

public interface SortRepository {
    List<TripDTO> sortByStatus(String value);

    List<TripDTO> sortByDriver(String value);

    List<TripDTO> sortByOrigin(String value);

    List<TripDTO> sortByDestination(String value);

    List<TripDTO> sortByEarliestDepartureTime(String value);

    List<TripDTO> sortByLatestDepartureTime(String value);

    List<TripDTO> sortByAvailablePlaces(String value);
}
