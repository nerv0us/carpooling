package com.telerik.carpoolingapplication.models;

import com.telerik.carpoolingapplication.models.constants.Constants;
import com.telerik.carpoolingapplication.models.enums.PassengerStatusEnum;
import com.telerik.carpoolingapplication.models.enums.TripStatus;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
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
                .filter(ps ->ps.getStatus() == PassengerStatusEnum.accepted)
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
        return new Trip(user, createTripDTO.getCarModel(), createTripDTO.getMessage()
                , createTripDTO.getDepartureTime(), createTripDTO.getOrigin(), createTripDTO.getDestination()
                , createTripDTO.getAvailablePlaces(), TripStatus.available, createTripDTO.smoking()
                , createTripDTO.pets(), createTripDTO.luggage());
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
        passengerDTO.setPassengerStatusEnum(passengerStatus.getStatus());
        return passengerDTO;
    }

    public static UserDTO getUser(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhone(user.getPhone());
        userDTO.setRatingAsDriver(user.getRatingAsDriver());
        userDTO.setRatingAsDriver(user.getRatingAsDriver());
        userDTO.setAvatarUri(user.getAvatarUri());
        return userDTO;
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

    public static User createUser(CreateUserDTO userDTO) {
        User user = new User();

        user.setUsername(userDTO.getUsername());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        user.setAvatarUri(Constants.DEFAULT_USER_AVATAR_ROUTE);


        return user;
    }

    public static Comment fromCommentDTO(CommentDTO commentDTO, User user, Trip trip) {
        return new Comment(commentDTO.getMessage(), user, trip);
    }

    public static CommentDTO fromComment(Comment comment) {
        return new CommentDTO(comment.getMessage(), comment.getUser().getId());
    }
}
