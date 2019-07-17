package com.telerik.carpoolingapplication.services;

import com.telerik.carpoolingapplication.models.CommentDTO;
import com.telerik.carpoolingapplication.models.CreateTripDTO;
import com.telerik.carpoolingapplication.models.EditTripDTO;
import com.telerik.carpoolingapplication.models.TripDTO;
import com.telerik.carpoolingapplication.models.constants.Messages;
import com.telerik.carpoolingapplication.models.enums.TripStatus;
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
        if (trips == null || trips.isEmpty()) {
            throw new IllegalArgumentException(Messages.NOT_AVAILABLE_TRIPS);
        }
        return trips;
    }

    @Override
    public void createTrip(CreateTripDTO createTripDTO) {
        tripRepository.createTrip(createTripDTO);
    }

    @Override
    public void editTrip(EditTripDTO editTripDTO) {
        tripRepository.editTrip(editTripDTO);
    }

    @Override
    public TripDTO getTrip(int id) {
        TripDTO tripDTO = tripRepository.getTrip(id);

        if (tripDTO == null) {
            throw new IllegalArgumentException(Messages.TRIP_NOT_FOUND);
        }

        return tripDTO;
    }

    @Override
    public void changeTripStatus(int id, String status) {
        // Throws IllegalArgumentException(Messages.TRIP_NOT_FOUND);
        TripDTO tripDTO = getTrip(id);
        try {
            TripStatus updatedStatus = TripStatus.valueOf(status);
            tripRepository.changeTripStatus(tripDTO, updatedStatus);
        }catch (IllegalArgumentException e){
            throw new IllegalArgumentException(Messages.NO_SUCH_STATUS);
        }
    }

    @Override
    public void addComment(int id, CommentDTO commentDTO) {
        // Throws IllegalArgumentException(Messages.TRIP_NOT_FOUND);
        TripDTO tripDTO = getTrip(id);

        // Throws IllegalArgumentException(Messages.UNAUTHORIZED);
        tripRepository.addComment(tripDTO, commentDTO);
    }

    @Override
    public void apply(int id) {
        // Throws IllegalArgumentException(Messages.TRIP_NOT_FOUND);
        TripDTO tripDTO = getTrip(id);

        tripRepository.apply(tripDTO);
    }
}
