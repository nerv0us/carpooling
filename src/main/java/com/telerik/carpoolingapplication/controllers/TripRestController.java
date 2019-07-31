package com.telerik.carpoolingapplication.controllers;

import com.telerik.carpoolingapplication.exceptions.UnauthorizedException;
import com.telerik.carpoolingapplication.models.*;
import com.telerik.carpoolingapplication.models.constants.Constants;
import com.telerik.carpoolingapplication.models.dto.*;
import com.telerik.carpoolingapplication.security.JwtTokenProvider;
import com.telerik.carpoolingapplication.services.TripService;
import com.telerik.carpoolingapplication.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;

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
        return trips;
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PostMapping
    public String createTrip(@Valid @RequestBody CreateTripDTO createTripDTO, HttpServletRequest request) {
        UserDTO user = getAuthorizedUser(request);
        try {
            tripService.createTrip(createTripDTO, user);
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }

        return Constants.TRIP_CREATED;
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PutMapping
    public String editTrip(@Valid @RequestBody EditTripDTO editTripDTO, HttpServletRequest request) {
        UserDTO user = getAuthorizedUser(request);
        try {
            tripService.editTrip(editTripDTO, user);
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (UnauthorizedException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return Constants.TRIP_UPDATED;
    }

    @GetMapping("/{id}")
    public TripDTO getTrip(@PathVariable int id) {
        try {
            return tripService.getTrip(id, new UserDTO());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}")
    public String changeTripStatus(@PathVariable int id, @RequestParam String status, HttpServletRequest request) {
        UserDTO user = getAuthorizedUser(request);
        try {
            tripService.changeTripStatus(id, user, status);
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (UnauthorizedException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return Constants.TRIP_STATUS_CHANGED;
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PostMapping("/{id}/comments")
    public String addComment(@PathVariable int id, @RequestBody CommentDTO commentDTO, HttpServletRequest request) {
        UserDTO user = getAuthorizedUser(request);
        try {
            tripService.addComment(id, user, commentDTO);
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (UnauthorizedException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return Constants.COMMENT_ADDED;
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PostMapping("/{id}/passengers")
    public String apply(@PathVariable int id, HttpServletRequest request) {
        UserDTO user = getAuthorizedUser(request);
        try {
            tripService.apply(id, user);
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (UnauthorizedException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return Constants.APPLIED;
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PatchMapping("{tripId}/passengers/{passengerId}")
    public String changePassengerStatus(@PathVariable int tripId, @PathVariable int passengerId
            , @RequestParam String status, HttpServletRequest request) {
        UserDTO user = getAuthorizedUser(request);
        try {
            tripService.changePassengerStatus(tripId, passengerId, user, status);
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (UnauthorizedException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return Constants.PASSENGER_STATUS_CHANGED;
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PostMapping("{id}/driver/rate")
    public String rateDriver(@PathVariable int id, @RequestBody RatingDTO ratingDTO, HttpServletRequest request) {
        UserDTO user = getAuthorizedUser(request);
        try {
            tripService.rateDriver(id, user, ratingDTO);
        } catch (IllegalArgumentException e) {
            responseStatusMessageValidator(e.getMessage());
        }
        return Constants.DRIVER_RATED;
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PostMapping("{tripId}/passengers/{passengerId}/rate")
    public String ratePassenger(@PathVariable int tripId, @PathVariable int passengerId
            , @RequestBody RatingDTO ratingDTO, HttpServletRequest request) {
        UserDTO user = getAuthorizedUser(request);
        try {
            tripService.ratePassenger(tripId, passengerId, user, ratingDTO);
        } catch (IllegalArgumentException e) {
            responseStatusMessageValidator(e.getMessage());
            if (e.getMessage().equals(Constants.NO_SUCH_PASSENGER)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
            }
        }
        return Constants.PASSENGER_RATED;
    }

    private void responseStatusMessageValidator(String message) {
        if (message.equals(Constants.TRIP_NOT_FOUND)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
        }
        if (message.equals(Constants.USER_NOT_FOUND)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, message);
        }
        if (message.equals(Constants.RATE_YOURSELF)
                || message.equals(Constants.RATING_NOT_ALLOWED_BEFORE_TRIP_IS_DONE)
                || message.equals(Constants.YOU_DO_NOT_PARTICIPATE)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, message);
        }
    }

    private UserDTO getAuthorizedUser(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        User user = userService.getByUsername(jwtTokenProvider.getUsername(token));
        return ModelsMapper.getUser(user);
    }
}
