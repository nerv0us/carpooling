package com.telerik.carpoolingapplication.services;

import com.telerik.carpoolingapplication.models.TripDTO;
import com.telerik.carpoolingapplication.models.constants.Constants;
import com.telerik.carpoolingapplication.repositories.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripServiceImpl implements TripService {
    private TripRepository tripRepository;

    @Autowired
    public TripServiceImpl(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    @Override
    public List<TripDTO> getTrips() {
        List<TripDTO> trips = tripRepository.getTrips();
        if (trips == null || trips.isEmpty()){
            throw new IllegalArgumentException(Constants.noAvailableTrips);
        }

        return trips;
    }
}
