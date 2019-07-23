package com.telerik.carpoolingapplication.repositories;

import com.telerik.carpoolingapplication.models.TripDTO;
import com.telerik.carpoolingapplication.models.enums.TripStatus;

import java.util.List;

public interface FilterHelper {
    List<TripDTO> unsortedUnfiltered();

    List<TripDTO> filterByStatus(TripStatus tripStatus);

    List<TripDTO> filterByDriver(String driverUsername);

    List<TripDTO> filterByOrigin(String origin);

    List<TripDTO> filterByDestination(String destination);

    List<TripDTO> filterByEarliestDepartureTime(String earliestDepartureTime);

    List<TripDTO> filterByLatestDepartureTime(String latestDepartureTime);

    List<TripDTO> filterByAvailablePlaces(int availablePlaces);

    List<TripDTO> filterBySmoking(boolean smoking);

    List<TripDTO> filterByPets(boolean pets);

    List<TripDTO> filterByLuggage(boolean luggage);
}
