package com.telerik.carpoolingapplication.controllers;

import com.telerik.carpoolingapplication.models.*;
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

        return Messages.TRIP_CREATED;
    }

    @PutMapping
    public String editTrip(@Valid @RequestBody EditTripDTO editTripDTO) {

        //Add response logic here!
        tripService.editTrip(editTripDTO);

        return Messages.TRIP_UPDATED;
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
    public String changeTripStatus(@PathVariable int id, @RequestParam String status) {
        //Add unauthorized and forbidden logic and response!
        try {
            tripService.changeTripStatus(id, status);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals(Messages.TRIP_NOT_FOUND)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
            }
            if (e.getMessage().equals(Messages.NO_SUCH_STATUS)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
            }
        }
        return Messages.TRIP_STATUS_CHANGED;
    }

    @PostMapping("/{id}/comments")
    public String addComment(@PathVariable int id, @RequestBody CommentDTO commentDTO) {
        try {
            tripService.addComment(id, commentDTO);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals(Messages.UNAUTHORIZED)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
            }
            if (e.getMessage().equals(Messages.TRIP_NOT_FOUND)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
            }
        }
        return Messages.COMMENT_ADDED;
    }

    @PostMapping("/{id}/passengers")
    public String apply(@PathVariable int id) {
        try {
            tripService.apply(id);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals(Messages.UNAUTHORIZED_MESSAGE)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
            }
            if (e.getMessage().equals(Messages.TRIP_NOT_FOUND)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
            }
            if (e.getMessage().equals(Messages.YOUR_OWN_TRIP) || e.getMessage().equals(Messages.ALREADY_APPLIED)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
            }
        }
        return Messages.APPLIED;
    }

    @PatchMapping("{tripId}/passengers/{passengerId}")
    public String changePassengerStatus(@PathVariable int tripId, @PathVariable int passengerId
            , @RequestParam String status) {
        try {
            tripService.changePassengerStatus(tripId, passengerId, status);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals(Messages.TRIP_NOT_FOUND) || e.getMessage().equals("Passenger not found!")) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
            }
            if (e.getMessage().equals(Messages.NO_SUCH_STATUS)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
            }

        }
        return Messages.PASSENGER_STATUS_CHANGED;
    }

    @PostMapping("{id}/driver/rate")
    public String rateDriver(@PathVariable int id, @RequestBody DriverRatingDTO driverRatingDTO) {

        try {
            tripService.rateDriver(id, driverRatingDTO);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }

        return Messages.DRIVER_RATED;
    }

    //Update and add validations!
    @PostMapping("{tripId}/passengers/{passengerId}/rate")
    public String ratePassenger(@PathVariable int tripId, @PathVariable int passengerId
            , @RequestBody PassengerRatingDTO passengerRatingDTO) {
        try {
            tripService.ratePassenger(tripId, passengerId, passengerRatingDTO);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return Messages.PASSENGER_RATED;
    }
}
