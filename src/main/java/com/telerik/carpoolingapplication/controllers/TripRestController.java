package com.telerik.carpoolingapplication.controllers;

import com.telerik.carpoolingapplication.models.*;
import com.telerik.carpoolingapplication.models.constants.Constants;
import com.telerik.carpoolingapplication.security.JwtTokenProvider;
import com.telerik.carpoolingapplication.services.TripService;
import com.telerik.carpoolingapplication.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

//TODO: Extract logic in services, change testUsers with authenticated!
@RestController
@RequestMapping("/api/trips")
public class TripRestController {
    private final TripService tripService;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public TripRestController(TripService tripService, UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.tripService = tripService;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /* Paging for getTripsFiltered()?
                _end
                integer($int32)
            (query)
                _start
                integer($int32)
            (query)
            */
    @GetMapping
    public List<TripDTO> getTrips(@RequestParam(required = false) String tripStatus
            , @RequestParam(required = false) String driverUsername
            , @RequestParam(required = false) String origin
            , @RequestParam(required = false) String destination
            , @RequestParam(required = false) String earliestDepartureTime
            , @RequestParam(required = false) String latestDepartureTime
            , @RequestParam(required = false) String availablePlaces
            , @RequestParam(required = false) String smoking
            , @RequestParam(required = false) String pets
            , @RequestParam(required = false) String luggage
            , @RequestParam(required = false) String sortParameter
            , @RequestParam(required = false) String ascending) {
        List<TripDTO> trips;
        trips = tripService.getTrips(tripStatus, driverUsername, origin, destination, earliestDepartureTime
                , latestDepartureTime, availablePlaces, smoking, pets, luggage, sortParameter, ascending);
        //Add unauthorized logic and response!
        //Edit responses/messages
        /*try {
            if (type == null || parameter == null || value == null) {
                trips = filterService.getTripsUnsortedUnfiltered();
            } else if (type.equals("filter")) {
                trips = filterService.getTripsFiltered(parameter, value);
            } else if (type.equals("sort")) {
                trips = sortService.getTripsSorted(parameter, value);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }*/
        return trips;
    }

    @PostMapping
    public String createTrip(@Valid @RequestBody CreateTripDTO createTripDTO, HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        UserDTO user = userService.getByUsername(jwtTokenProvider.getUsername(token));
        try {
            tripService.createTrip(createTripDTO, user);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

        return Constants.TRIP_CREATED;
    }

    @PutMapping
    public String editTrip(@Valid @RequestBody EditTripDTO editTripDTO) {
        try {
            tripService.editTrip(editTripDTO);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals(Constants.UNAUTHORIZED)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
            }
            if (e.getMessage().equals(Constants.INVALID_ID_SUPPLIED)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
            }
        }
        return Constants.TRIP_UPDATED;
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
            if (e.getMessage().equals(Constants.TRIP_NOT_FOUND)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
            }
            if (e.getMessage().equals(Constants.NO_SUCH_STATUS)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
            }
        }
        return Constants.TRIP_STATUS_CHANGED;
    }

    @PostMapping("/{id}/comments")
    public String addComment(@PathVariable int id, @RequestBody CommentDTO commentDTO) {
        try {
            tripService.addComment(id, commentDTO);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals(Constants.UNAUTHORIZED)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
            }
            if (e.getMessage().equals(Constants.TRIP_NOT_FOUND)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
            }
            if (e.getMessage().equals(Constants.YOU_DO_NOT_PARTICIPATE)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
            }
            if (e.getMessage().equals(Constants.TRIP_NOT_FINISHED)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
            }
        }
        return Constants.COMMENT_ADDED;
    }

    @PostMapping("/{id}/passengers")
    public String apply(@PathVariable int id) {
        try {
            tripService.apply(id);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals(Constants.TRIP_NOT_FOUND)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
            }
            if (e.getMessage().equals(Constants.UNAUTHORIZED_MESSAGE)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
            }
            if (e.getMessage().equals(Constants.YOUR_OWN_TRIP) || e.getMessage().equals(Constants.ALREADY_APPLIED)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
            }
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getMessage());
        }
        return Constants.APPLIED;
    }

    @PatchMapping("{tripId}/passengers/{passengerId}")
    public String changePassengerStatus(@PathVariable int tripId, @PathVariable int passengerId
            , @RequestParam String status) {
        try {
            tripService.changePassengerStatus(tripId, passengerId, status);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals(Constants.TRIP_NOT_FOUND)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
            }
            if (e.getMessage().equals(Constants.NO_SUCH_PASSENGER)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
            }
            if (e.getMessage().equals(Constants.NO_SUCH_STATUS)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
            }

        }
        return Constants.PASSENGER_STATUS_CHANGED;
    }

    @PostMapping("{id}/driver/rate")
    public String rateDriver(@PathVariable int id, @RequestBody RatingDTO ratingDTO) {
        try {
            tripService.rateDriver(id, ratingDTO);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }

        return Constants.DRIVER_RATED;
    }

    //Update and add validations!
    @PostMapping("{tripId}/passengers/{passengerId}/rate")
    public String ratePassenger(@PathVariable int tripId, @PathVariable int passengerId
            , @RequestBody RatingDTO ratingDTO) {
        try {
            tripService.ratePassenger(tripId, passengerId, ratingDTO);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return Constants.PASSENGER_RATED;
    }
}
