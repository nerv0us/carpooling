package com.telerik.carpoolingapplication;

import com.telerik.carpoolingapplication.exceptions.UnauthorizedException;
import com.telerik.carpoolingapplication.exceptions.ValidationException;
import com.telerik.carpoolingapplication.models.PassengerStatus;
import com.telerik.carpoolingapplication.models.dto.*;
import com.telerik.carpoolingapplication.models.enums.PassengerStatusEnum;
import com.telerik.carpoolingapplication.models.enums.TripStatus;
import com.telerik.carpoolingapplication.repositories.TripRepository;
import com.telerik.carpoolingapplication.services.TripServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.Arrays;

@RunWith(MockitoJUnitRunner.Silent.class)
public class TripServiceImplTests {

    @Mock
    TripRepository tripRepository;

    @InjectMocks
    TripServiceImpl tripService;

    @Test
    public void createTrip_Should_CallRepository_When_Trip_Is_Valid() {
        // Arrange
        String departureTimeInFuture = "01/01/9999 00:00 AM";
        CreateTripDTO trip = new CreateTripDTO("Test", "test message", departureTimeInFuture,
                "TestCity", "TestCity", 1, true, true, true);
        UserDTO user = new UserDTO();

        // Act
        tripService.createTrip(trip, user);

        // Assert
        Mockito.verify(tripRepository, Mockito.times(1)).createTrip(trip, user.getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void createTrip_Should_ThrowException_When_DepartureTime_Is_In_The_Past() {
        // Arrange
        String departureTimeInPast = "01/01/2000 00:00 AM";
        CreateTripDTO trip = new CreateTripDTO("Test", "test message", departureTimeInPast,
                "TestCity", "TestCity", 1, true, true, true);
        UserDTO user = new UserDTO();

        // Act
        tripService.createTrip(trip, user);

        //Assert
        Mockito.verify(tripRepository, Mockito.never()).createTrip(trip, user.getId());
    }


    @Test(expected = ValidationException.class)
    public void createTrip_Should_ThrowException_When_DateOrTime_Is_Not_Valid() {
        // Arrange
        String invalidDepartureTime = "asd";
        CreateTripDTO trip = new CreateTripDTO("Test", "test message", invalidDepartureTime,
                "TestCity", "TestCity", 1, true, true, true);
        UserDTO user = new UserDTO();

        // Act
        tripService.createTrip(trip, user);
    }

    @Test
    public void getTrip_Should_ReturnTrip_When_TripExist() {
        // Arrange
        TripDTO trip = createTripHelper();
        Mockito.when(tripRepository.getTrip(1)).thenReturn(trip);
        UserDTO driver = new UserDTO();

        // Act
        TripDTO result = tripService.getTrip(1, driver);

        // Assert
        Assert.assertEquals(trip, result);
    }

    @Test
    public void changeTripStatus_Should_CallRepository_When_StatusIsValid() {
        // Arrange
        TripDTO trip = createTripHelper();
        TripStatus newStatus = TripStatus.valueOf("DONE");
        UserDTO user = new UserDTO(4, "TestUser", "TestUser",
                "username@mail.com", "12345", "08888888", 0D, 0D, "/avatar");
        Mockito.when(tripRepository.getTrip(1)).thenReturn(trip);

        // Act
        tripService.changeTripStatus(1, user, "DONE");

        // Assert
        Mockito.verify(tripRepository, Mockito.times(1)).changeTripStatus(trip, newStatus);
    }

    @Test(expected = IllegalArgumentException.class)
    public void changeTripStatus_Should_ThrowException_When_StatusIsNotValid() {
        // Arrange
        TripDTO trip = createTripHelper();
        TripStatus newStatus = TripStatus.valueOf("TEST");
        UserDTO user = new UserDTO();
        Mockito.when(tripRepository.getTrip(1)).thenReturn(trip);

        // Act
        tripService.changeTripStatus(1, user, "TEST");

        // Assert
        Mockito.verify(tripRepository, Mockito.never()).changeTripStatus(trip, newStatus);
    }

    @Test
    public void addComment_Should_CallRepository() {
        // Arrange
        TripDTO trip = createTripHelper();
        UserDTO user = new UserDTO();
        CommentDTO comment = new CommentDTO("test comment", 1);
        Mockito.when(tripRepository.getTrip(1)).thenReturn(trip);

        // Act
        tripService.addComment(1, user, comment);

        // Assert
        Mockito.verify(tripRepository, Mockito.times(1)).addComment(trip, comment, 0);
    }

    @Test
    public void apply_Should_CallRepository() {
        // Arrange
        TripDTO trip = createTripHelper();
        UserDTO user = new UserDTO();
        Mockito.when(tripRepository.getTrip(1)).thenReturn(trip);

        // Act
        tripService.apply(1, user);

        // Assert
        Mockito.verify(tripRepository, Mockito.times(1)).apply(1, user);
    }

    @Test(expected = UnauthorizedException.class)
    public void apply_Should_Throw_Exception_When_UserPassenger_And_UserDriver_Are_Equals() {
        // Arrange
        TripDTO trip = createTripHelper();
        UserDTO user = new UserDTO(4, "TestUser", "TestUser",
                "username@mail.com", "12345", "08888888", 0D, 0D, "/avatar");

        Mockito.when(tripRepository.getTrip(1)).thenReturn(trip);

        // Act
        tripService.apply(1, user);

        // Assert
        Mockito.verify(tripRepository, Mockito.times(1)).apply(trip.getId(), user);
    }

    @Test
    public void rateDriver_Should_CallRepository_When_TripStatus_Is_Done() {
        // Arrange
        TripDTO trip = createTripHelper();
        UserDTO user = new UserDTO();
        RatingDTO rating = new RatingDTO(4D);
        Mockito.when(tripRepository.getTrip(1)).thenReturn(trip);
        Mockito.when(tripRepository.passengers(trip.getId(), user.getId(), PassengerStatusEnum.ACCEPTED)).thenReturn(Arrays.asList
                (new PassengerStatus()));

        // Act
        tripService.rateDriver(trip.getId(), user, rating);

        // Assert
        Mockito.verify(tripRepository, Mockito.times(1)).rateDriver(trip, user, rating);
    }

    @Test(expected = UnauthorizedException.class)
    public void rateDriver_Should_ThrowException_When_TripStatus_Is_NotDone() {
        // Arrange
        UserDTO user = new UserDTO();

        LocalDateTime departureTimeInPast = LocalDateTime.now().plusDays(1);
        TripDTO trip = new TripDTO(1, user, "Test", "test message", departureTimeInPast.toString(),
                "TestCity", "TestCity", 1, TripStatus.CANCELED, true, true, true);

        RatingDTO rating = new RatingDTO(4D);

        Mockito.when(tripRepository.getTrip(1)).thenReturn(trip);
        Mockito.when(tripRepository.passengers(trip.getId(), user.getId(), PassengerStatusEnum.ACCEPTED)).thenReturn(Arrays.asList
                (new PassengerStatus()));

        // Act
        tripService.rateDriver(trip.getId(), user, rating);

        // Assert
        Mockito.verify(tripRepository, Mockito.never()).rateDriver(trip, user, rating);
    }

    @Test(expected = UnauthorizedException.class)
    public void rateDriver_Should_ThrowException_When_Passenger_DoNot_Participate() {
        // Arrange
        UserDTO user = new UserDTO();

        LocalDateTime departureTimeInPast = LocalDateTime.now().plusDays(1);
        TripDTO trip = new TripDTO(1, user, "Test", "test message", departureTimeInPast.toString(),
                "TestCity", "TestCity", 1, TripStatus.DONE, true, true, true);

        RatingDTO rating = new RatingDTO(1D);

        Mockito.when(tripRepository.getTrip(1)).thenReturn(trip);
        Mockito.when(tripRepository.passengers(trip.getId(), user.getId(), PassengerStatusEnum.ACCEPTED)).thenReturn(Arrays.asList
                (new PassengerStatus()));

        // Act
        tripService.rateDriver(trip.getId(), user, rating);

        // Assert
        Mockito.verify(tripRepository, Mockito.never()).rateDriver(trip, user, rating);
    }

    @Test
    public void getTrip_Should_Return_Trip_When_Exist() {
        // Arrange
        TripDTO trip = createTripHelper();
        Mockito.when(tripRepository.getTrip(1)).thenReturn(trip);

        // Act
        TripDTO result = tripService.getTrip(1);

        // Assert
        Assert.assertEquals(trip, result);
    }

    @Test
    public void editTrip_Should_CallRepository_When_User_Is_Driver() {
        // Arrange
        EditTripDTO editTripDTO = new EditTripDTO(1, "New", "test message", "01/10/2099 00:00 AM",
                "TestCity", "TestCity", 1, true, true, true);
        UserDTO user = new UserDTO(4, "TestUser", "TestUser",
                "username@mail.com", "12345", "08888888", 0D, 0D, "/avatar");
        Mockito.when(tripRepository.getTrip(1)).thenReturn(new TripDTO(1, user, "Test", "test message", "01/10/2099 00:00 AM",
                "TestCity", "TestCity", 1, TripStatus.DONE, true, true, true));
        // Act
        tripService.editTrip(editTripDTO, user);

        // Assert
        Mockito.verify(tripRepository, Mockito.times(1)).editTrip(editTripDTO, user.getId());
    }

    @Test(expected = UnauthorizedException.class)
    public void editTrip_Should_ThrowException_When_User_Is_Not_Driver() {
        // Arrange
        EditTripDTO editTripDTO = new EditTripDTO(1, "New", "test message", "01/10/2099 00:00 AM",
                "TestCity", "TestCity", 1, true, true, true);
        UserDTO driver = new UserDTO(4, "TestUser", "TestUser",
                "username@mail.com", "12345", "08888888", 0D, 0D, "/avatar");
        UserDTO user = new UserDTO();
        Mockito.when(tripRepository.getTrip(1)).thenReturn(new TripDTO(1, driver, "Test", "test message", "01/10/2099 00:00 AM",
                "TestCity", "TestCity", 1, TripStatus.DONE, true, true, true));

        // Act
        tripService.editTrip(editTripDTO, user);

        // Assert
        Mockito.verify(tripRepository, Mockito.never()).editTrip(editTripDTO, user.getId());
    }

    private TripDTO createTripHelper() {
        UserDTO user = new UserDTO(4, "TestUser", "TestUser",
                "username@mail.com", "12345", "08888888", 0D, 0D, "/avatar");

        LocalDateTime departureTimeInPast = LocalDateTime.now().plusDays(1);
        return new TripDTO(1, user, "Test", "test message", departureTimeInPast.toString(),
                "TestCity", "TestCity", 1, TripStatus.DONE, true, true, true);
    }
}