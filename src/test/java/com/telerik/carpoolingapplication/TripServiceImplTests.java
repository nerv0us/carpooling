package com.telerik.carpoolingapplication;

import com.telerik.carpoolingapplication.models.*;
import com.telerik.carpoolingapplication.models.enums.TripStatus;
import com.telerik.carpoolingapplication.repositories.TripRepository;
import com.telerik.carpoolingapplication.services.TripServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class TripServiceImplTests {

    @Mock
    TripRepository tripRepository;

    @InjectMocks
    TripServiceImpl tripService;

    //TODO: getTrips() (with all sorts and filters)!

    @Test
    public void createTrip_Should_CallRepositoryCreateTrip_When_CreatingTrip() {
        // Arrange
        CreateTripDTO trip = new CreateTripDTO();
        UserDTO user = new UserDTO();

        // Act
        tripService.createTrip(trip, user);

        // Assert
        Mockito.verify(tripRepository, Mockito.times(1)).createTrip(trip, user.getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void createTrip_Should_ThrowException_When_User_Is_Null() {
        // Arrange
        CreateTripDTO trip = new CreateTripDTO();
        UserDTO user = null;

        // Act
        tripService.createTrip(trip, user);
    }

    //TODO: editTrip !

    @Test
    public void getTrip_Should_CallRepositoryGetTrip_When_GettingTrip() {
        // Arrange
        UserDTO user = new UserDTO();

        // Act
        tripService.getTrip(1, user);

        // Assert
        Mockito.verify(tripRepository, Mockito.times(1)).getTrip(1);
    }
}
