package com.telerik.carpoolingapplication.repositories;

import com.telerik.carpoolingapplication.models.TripDTO;
import com.telerik.carpoolingapplication.models.enums.TripStatus;

import java.util.List;

public interface FilterAndSortHelper {
    List<TripDTO> unsortedUnfiltered();

    List<TripDTO> filterByStatus(TripStatus tripStatus);

    List<TripDTO> filterByDriver(String driverUsername);

    List<TripDTO> filterByOrigin(String origin);

    List<TripDTO> filterByDestination(String value);
}
