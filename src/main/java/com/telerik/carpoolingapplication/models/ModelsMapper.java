package com.telerik.carpoolingapplication.models;

import com.telerik.carpoolingapplication.models.enums.TripStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ModelsMapper {

    public static List<TripDTO> fromTrip(List<Trip> trips, List<PassengerStatus> passengerStatuses, List<Comment> comments) {
        List<TripDTO> tripDTOS = new ArrayList<>();
        for (Trip trip : trips) {
            TripDTO tripDTO = fromTrip(trip, passengerStatuses, comments);
            tripDTOS.add(tripDTO);
        }
        return tripDTOS;
    }

    public static TripDTO fromTrip(Trip trip, List<PassengerStatus> passengerStatuses, List<Comment> comments) {
        List<PassengerDTO> tripDTOPassengers = new ArrayList<>();
        List<User> passengers = new ArrayList<>();
        List<CommentDTO> commentDTOS = new ArrayList<>();

        List<PassengerStatus> filteredPassengerStatuses = passengerStatuses.stream()
                .filter(ps -> ps.getTrip().getId() == trip.getId())
                .collect(Collectors.toList());

        for (PassengerStatus passengerStatus : filteredPassengerStatuses) {
            passengers.add(passengerStatus.getUser());
        }

        List<Comment> filteredByTripId = comments.stream()
                .filter(c -> c.getTrip().getId() == trip.getId())
                .collect(Collectors.toList());
        for (Comment comment : filteredByTripId) {
            CommentDTO commentDTO = fromComment(comment);
            commentDTOS.add(commentDTO);
        }

        for (User user : passengers) {
            PassengerStatus passengerStatus = passengerStatuses.stream()
                    .filter(p -> p.getUser().getId() == user.getId())
                    .findFirst()
                    .orElse(null);
            PassengerDTO currentPassenger = fromUserToPassenger(user, passengerStatus);
            tripDTOPassengers.add(currentPassenger);
        }
        TripDTO tripDTO = new TripDTO(trip.getId(), trip.getDriver(), trip.getCarModel()
                , trip.getMessage(), trip.getDepartureTime(), trip.getOrigin(), trip.getDestination()
                , trip.getAvailablePlaces(), trip.getTripStatus(), trip.isSmoking()
                , trip.isPets(), trip.isLuggage());
        tripDTO.setPassengers(tripDTOPassengers);
        tripDTO.setComments(commentDTOS);
        return tripDTO;
    }

    public static Trip fromCreateTripDTO(CreateTripDTO createTripDTO, User user) {
        Trip trip = new Trip();

        trip.setDriver(user);
        trip.setCarModel(createTripDTO.getCarModel());
        trip.setMessage(createTripDTO.getMessage());
        trip.setDepartureTime(createTripDTO.getDepartureTime());
        trip.setOrigin(createTripDTO.getOrigin());
        trip.setDestination(createTripDTO.getDestination());
        trip.setAvailablePlaces(createTripDTO.getAvailablePlaces());
        trip.setSmoking(createTripDTO.smoking());
        trip.setPets(createTripDTO.pets());
        trip.setLuggage(createTripDTO.luggage());
        trip.setTripStatus(TripStatus.available);

        return trip;
    }

    public static void updateTrip(Trip tripToEdit, EditTripDTO editTripDTO) {
        tripToEdit.setCarModel(editTripDTO.getCarModel());
        tripToEdit.setMessage(editTripDTO.getMessage());
        tripToEdit.setDepartureTime(editTripDTO.getDepartureTime());
        tripToEdit.setOrigin(editTripDTO.getOrigin());
        tripToEdit.setDestination(editTripDTO.getDestination());
        tripToEdit.setAvailablePlaces(editTripDTO.getAvailablePlaces());
        tripToEdit.setSmoking(editTripDTO.smoking());
        tripToEdit.setPets(editTripDTO.pets());
        tripToEdit.setLuggage(editTripDTO.luggage());
    }

    public static PassengerDTO fromUserToPassenger(User user, PassengerStatus passengerStatus) {
        PassengerDTO passengerDTO = new PassengerDTO();

        passengerDTO.setUserId(user.getId());
        passengerDTO.setUsername(user.getUsername());
        passengerDTO.setFirstName(user.getFirstName());
        passengerDTO.setLastName(user.getLastName());
        passengerDTO.setEmail(user.getEmail());
        passengerDTO.setPhone(user.getPhone());
        passengerDTO.setRatingAsPassenger(user.getRatingAsPassenger());
        passengerDTO.setPassengerStatusEnum(passengerStatus.getPassengerStatusEnum());
        return passengerDTO;
    }

    public static void editUser(User userToEdit, UserDTO userDTO) {
        userToEdit.setUsername(userDTO.getUsername());
        userToEdit.setFirstName(userDTO.getFirstName());
        userToEdit.setLastName(userDTO.getLastName());
        userToEdit.setEmail(userDTO.getEmail());
        userToEdit.setPhone(userDTO.getPhone());
        userToEdit.setRatingAsDriver(userDTO.getRatingAsDriver());
        userToEdit.setRatingAsPassenger(userDTO.getRatingAsPassenger());
    }

    public static Comment fromCommentDTO(CommentDTO commentDTO, User user, Trip trip) {
        return new Comment(commentDTO.getMessage(), user, trip);
    }

    public static CommentDTO fromComment(Comment comment) {
        return new CommentDTO(comment.getMessage(), comment.getUser().getId());
    }

    public static Rating fromRatingDriverDTO(RatingDTO ratingDTO, User ratingGiver
            , User ratingReceiver, Trip trip) {
        double rating = ratingDTO.getRating();
        return new Rating(rating, ratingGiver, ratingReceiver, trip);
    }
}
