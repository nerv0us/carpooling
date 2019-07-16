package com.telerik.carpoolingapplication.controllers;

import com.telerik.carpoolingapplication.models.CreateTripDTO;
import com.telerik.carpoolingapplication.models.EditTripDTO;
import com.telerik.carpoolingapplication.models.TripDTO;
import com.telerik.carpoolingapplication.models.constants.Constants;
import com.telerik.carpoolingapplication.services.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/trips")
public class TripRestController {
    private TripService tripService;

    @Autowired
    public TripRestController(TripService tripService) {
        this.tripService = tripService;
    }

    @GetMapping
    public List<TripDTO> getTrips() {
        List<TripDTO> trips;

        //Add unauthorized logic and response!
        try {
            trips = tripService.getTrips();
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return trips;
    }

    @PostMapping
    public String createTrip(@Valid @RequestBody CreateTripDTO createTripDTO) {

        //Add unauthorized logic and response here!
        tripService.createTrip(createTripDTO);

        return Constants.TRIP_CREATED_MESSAGE;
    }

    @PutMapping
    public String editTrip(@Valid @RequestBody EditTripDTO editTripDTO){

        tripService.editTrip(editTripDTO);

        return Constants.TRIP_UPDATED_MESSAGE;
    }
}
