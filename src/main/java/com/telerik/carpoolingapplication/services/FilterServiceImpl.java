package com.telerik.carpoolingapplication.services;

import com.telerik.carpoolingapplication.models.TripDTO;
import com.telerik.carpoolingapplication.models.constants.Constants;
import com.telerik.carpoolingapplication.models.enums.TripStatus;
import com.telerik.carpoolingapplication.repositories.FilterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FilterServiceImpl implements FilterService {
    private FilterRepository filterRepository;

    @Autowired
    public FilterServiceImpl(FilterRepository filterRepository) {
        this.filterRepository = filterRepository;
    }


    @Override
    public List<TripDTO> getTripsUnsortedUnfiltered() {
        return filterRepository.getTripsUnsortedUnfiltered();
    }

    @Override
    public List<TripDTO> getTripsFiltered(String parameter, String value) {
        List<TripDTO> trips;
        switch (parameter) {
            case "status":
                try {
                    TripStatus tripStatus = TripStatus.valueOf(value);
                    trips = filterRepository.filterByStatus(tripStatus);
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException(Constants.NO_SUCH_STATUS);
                }
                break;
            case "driver":
                trips = filterRepository.filterByDriver(value);
                break;
            case "origin":
                trips = filterRepository.filterByOrigin(value);
                break;
            case "destination":
                trips = filterRepository.filterByDestination(value);
                break;
            case "earliestDepartureTime":
                trips = filterRepository.filterByEarliestDepartureTime(value);
                break;
            case "latestDepartureTime":
                trips = filterRepository.filterByLatestDepartureTime(value);
                break;
            case "availablePlaces":
                try {
                    int availablePlaces = Integer.parseInt(value);
                    trips = filterRepository.filterByAvailablePlaces(availablePlaces);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException(Constants.NOT_A_NUMBER);
                }
                break;
            case "smoking":
                checkIfValueIsBoolean(value);
                trips = filterRepository.filterBySmoking(Boolean.valueOf(value));
                break;
            case "pets":
                checkIfValueIsBoolean(value);
                trips = filterRepository.filterByPets(Boolean.valueOf(value));
                break;
            case "luggage":
                checkIfValueIsBoolean(value);
                trips = filterRepository.filterByLuggage(Boolean.valueOf(value));
                break;
            default:
                throw new IllegalArgumentException(Constants.BAD_REQUEST);
        }

        if (trips == null || trips.isEmpty()) {
            throw new IllegalArgumentException(Constants.NOT_AVAILABLE_TRIPS);
        }
        return trips;
    }

    private void checkIfValueIsBoolean(String value) {
        if (value != null && !(value.equals("true") || value.equals("false"))) {
            throw new IllegalArgumentException(Constants.NOT_A_BOOLEAN);
        }
    }
}
