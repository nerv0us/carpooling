package com.telerik.carpoolingapplication.services;

import com.telerik.carpoolingapplication.models.TripDTO;
import com.telerik.carpoolingapplication.models.constants.Constants;
import com.telerik.carpoolingapplication.models.enums.TripStatus;
import com.telerik.carpoolingapplication.repositories.SortRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SortServiceImpl implements SortService {
    private SortRepository sortRepository;

    @Autowired
    public SortServiceImpl(SortRepository sortRepository) {
        this.sortRepository = sortRepository;
    }

    @Override
    public List<TripDTO> getTripsSorted(String parameter, String value) {
        List<TripDTO> trips = new ArrayList<>();
        switch (parameter) {
            case "status":
                trips = sortRepository.sortByStatus(value);
                break;
            case "driver":
                trips = sortRepository.sortByDriver(value);
                break;
            case "origin":
                trips = sortRepository.sortByOrigin(value);
                break;
            case "destination":
                trips = sortRepository.sortByDestination(value);
                break;
            case "earliestDepartureTime":
                trips = sortRepository.sortByEarliestDepartureTime(value);
                break;
            case "latestDepartureTime":
                trips = sortRepository.sortByLatestDepartureTime(value);
                break;
            case "availablePlaces":
                trips = sortRepository.sortByAvailablePlaces(value);
                break;
        }

        if (trips == null || trips.isEmpty()) {
            throw new IllegalArgumentException(Constants.NOT_AVAILABLE_TRIPS);
        }
        return trips;
    }
}
