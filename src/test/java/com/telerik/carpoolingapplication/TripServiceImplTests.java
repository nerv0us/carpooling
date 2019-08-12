package com.telerik.carpoolingapplication;

import com.telerik.carpoolingapplication.exceptions.ValidationException;
import com.telerik.carpoolingapplication.models.dto.CreateTripDTO;
import com.telerik.carpoolingapplication.models.dto.TripDTO;
import com.telerik.carpoolingapplication.models.dto.UserDTO;
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

@RunWith(MockitoJUnitRunner.Silent.class)
public class TripServiceImplTests {

    @Mock
    TripRepository tripRepository;

    @InjectMocks
    TripServiceImpl tripService;

    @Test
    public void createTrip_Should_CallRepositoryCreateTrip_When_CreatingTrip() {
        // Arrange
        LocalDateTime departureTime = LocalDateTime.now().plusDays(1);
        CreateTripDTO trip = new CreateTripDTO("Test", "test message", departureTime.toString(),
                "TestCity", "TestCity", 1, true, true, true);
        UserDTO user = new UserDTO();

        // Act
        tripService.createTrip(trip, user);

        // Assert
        Mockito.verify(tripRepository, Mockito.times(1)).createTrip(trip, user.getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void createTrip_Should_ThrowException_When_DepartureTime_IsNot_Valid() {
        // Arrange
        LocalDateTime departureTimeInPast = LocalDateTime.now().minusDays(1);
        CreateTripDTO trip = new CreateTripDTO("Test", "test message", departureTimeInPast.toString(),
                "TestCity", "TestCity", 1, true, true, true);
        UserDTO user = new UserDTO();

        // Act
        tripService.createTrip(trip, user);

        //Assert
        Mockito.verify(tripRepository, Mockito.never()).createTrip(trip, user.getId());
    }


    @Test(expected = ValidationException.class)
    public void createTrip_Should_ThrowException_When_User_Is_Null() {
        // Arrange
        CreateTripDTO trip = new CreateTripDTO();
        UserDTO user = null;

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



    private TripDTO createTripHelper() {
        UserDTO user = new UserDTO();
        LocalDateTime departureTimeInPast = LocalDateTime.now().plusDays(1);
        return new TripDTO(1, user, "Test", "test message", departureTimeInPast.toString(),
                "TestCity", "TestCity", 1, TripStatus.DONE, true, true, true);
    }

}
