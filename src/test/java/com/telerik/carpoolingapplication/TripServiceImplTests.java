package com.telerik.carpoolingapplication;

import com.telerik.carpoolingapplication.models.CreateTripDTO;
import com.telerik.carpoolingapplication.models.TripDTO;
import com.telerik.carpoolingapplication.models.UserDTO;
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
}
