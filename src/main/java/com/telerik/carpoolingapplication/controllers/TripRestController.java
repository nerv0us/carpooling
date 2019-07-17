package com.telerik.carpoolingapplication.controllers;

import com.telerik.carpoolingapplication.models.CreateTripDTO;
import com.telerik.carpoolingapplication.models.EditTripDTO;
import com.telerik.carpoolingapplication.models.TripDTO;
import com.telerik.carpoolingapplication.models.constants.Messages;
import com.telerik.carpoolingapplication.services.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/trips")
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

        return Messages.TRIP_CREATED_MESSAGE;
    }

    @PutMapping
    public String editTrip(@Valid @RequestBody EditTripDTO editTripDTO) {

        //Add response logic here!
        tripService.editTrip(editTripDTO);

        return Messages.TRIP_UPDATED_MESSAGE;
    }

    @GetMapping("/{id}")
    public TripDTO getTrip(@PathVariable int id) {
        //Add unauthorized logic and response!
        try {
            return tripService.getTrip(id);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PatchMapping("/{id}")
    public String changeTripStatus(@PathVariable int id, @RequestParam String status){
        //Add unauthorized and forbidden logic and response!
        try {
            tripService.changeTripStatus(id, status);
        } catch (IllegalArgumentException e){
            if (e.getMessage().equals(Messages.TRIP_NOT_FOUND)){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
            }
            if (e.getMessage().equals(Messages.NO_SUCH_STATUS)){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
            }
        }
        return Messages.STATUS_CHANGED;
    }
}
