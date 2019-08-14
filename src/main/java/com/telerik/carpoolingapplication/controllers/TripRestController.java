package com.telerik.carpoolingapplication.controllers;

import com.telerik.carpoolingapplication.exceptions.UnauthorizedException;
import com.telerik.carpoolingapplication.models.ModelsMapper;
import com.telerik.carpoolingapplication.models.User;
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

    @GetMapping
    public List<TripDTO> getTrips(@RequestParam(required = false) Integer page,
                                  @RequestParam(required = false) Integer showElements,
                                  @RequestParam(required = false) String tripStatus,
                                  @RequestParam(required = false) String driverUsername,
                                  @RequestParam(required = false) String origin,
                                  @RequestParam(required = false) String destination,
                                  @RequestParam(required = false) String earliestDepartureTime,
                                  @RequestParam(required = false) String latestDepartureTime,
                                  @RequestParam(required = false) String availablePlaces,
                                  @RequestParam(required = false) String smoking,
                                  @RequestParam(required = false) String pets,
                                  @RequestParam(required = false) String luggage,
                                  @RequestParam(required = false) String sortParameter,
                                  @RequestParam(required = false) String ascending) {
        try {
            return tripService.getTrips(page,  tripStatus, driverUsername, origin, destination, earliestDepartureTime,
                    latestDepartureTime, availablePlaces, smoking, pets, luggage, sortParameter, ascending);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PostMapping
    public String createTrip(@Valid @RequestBody CreateTripDTO createTripDTO, HttpServletRequest request) {
        try {
            UserDTO user = getAuthorizedUser(request);
            tripService.createTrip(createTripDTO, user);
        } catch (UnauthorizedException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return Constants.TRIP_CREATED;
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PutMapping
    public String editTrip(@Valid @RequestBody EditTripDTO editTripDTO, HttpServletRequest request) {
        try {
            UserDTO user = getAuthorizedUser(request);
            tripService.editTrip(editTripDTO, user);
        } catch (UnauthorizedException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return Constants.TRIP_UPDATED;
    }

    @GetMapping("/{id}")
    public TripDTO getTrip(@PathVariable int id) {
        try {
            return tripService.getTrip(id);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}")
    public String changeTripStatus(@PathVariable int id, @RequestParam String status, HttpServletRequest request) {
        try {
            UserDTO user = getAuthorizedUser(request);
            tripService.changeTripStatus(id, user, status);
        } catch (UnauthorizedException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return Constants.TRIP_STATUS_CHANGED;
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PostMapping("/{id}/comments")
    public String addComment(@PathVariable int id, @Valid @RequestBody CommentDTO commentDTO, HttpServletRequest request) {
        try {
            UserDTO user = getAuthorizedUser(request);
            tripService.addComment(id, user, commentDTO);
        } catch (UnauthorizedException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return Constants.COMMENT_ADDED;
    }

    @CrossOrigin
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PostMapping("/{id}/passengers")
    public String apply(@PathVariable int id, HttpServletRequest request) {
        try {
            UserDTO user = getAuthorizedUser(request);
            tripService.apply(id, user);
        } catch (UnauthorizedException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return Constants.APPLIED;
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PatchMapping("{tripId}/passengers/{passengerId}")
    public String changePassengerStatus(@PathVariable int tripId, @PathVariable int passengerId,
                                        @RequestParam String status, HttpServletRequest request) {
        try {
            UserDTO user = getAuthorizedUser(request);
            tripService.changePassengerStatus(tripId, passengerId, user, status);
        } catch (UnauthorizedException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
        return Constants.PASSENGER_STATUS_CHANGED;
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PostMapping("{id}/driver/rate")
    public String rateDriver(@PathVariable int id, @Valid @RequestBody RatingDTO ratingDTO, HttpServletRequest request) {
        try {
            UserDTO user = getAuthorizedUser(request);
            tripService.rateDriver(id, user, ratingDTO);
        } catch (UnauthorizedException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return Constants.DRIVER_RATED;
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PostMapping("{tripId}/passengers/{passengerId}/rate")
    public String ratePassenger(@PathVariable int tripId, @PathVariable int passengerId,
                                @RequestBody RatingDTO ratingDTO, HttpServletRequest request) {
        try {
            UserDTO user = getAuthorizedUser(request);
            tripService.ratePassenger(tripId, passengerId, user, ratingDTO);
        } catch (UnauthorizedException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return Constants.PASSENGER_RATED;
    }

    private UserDTO getAuthorizedUser(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        if (token == null) {
            throw new UnauthorizedException(Constants.INVALID_TOKEN_MESSAGE);
        }
        User user = userService.getByUsername(jwtTokenProvider.getUsername(token));
        return ModelsMapper.getUser(user);
    }
}