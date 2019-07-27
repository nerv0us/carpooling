package com.telerik.carpoolingapplication.repositories;

import com.telerik.carpoolingapplication.models.*;
import com.telerik.carpoolingapplication.models.enums.TripStatus;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class FilterRepositoryImpl implements FilterRepository {
    private SessionFactory sessionFactory;

    @Autowired
    public FilterRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<TripDTO> getTripsUnsortedUnfiltered() {
        return filteredTrips("from Trip ", "", "");
    }

    @Override
    public List<TripDTO> filterByStatus(TripStatus tripStatus) {
        return filteredTrips("from Trip where tripStatus = :tripStatus "
                , "tripStatus", String.valueOf(tripStatus));
    }

    @Override
    public List<TripDTO> filterByDriver(String driverUsername) {
        return filteredTrips("from Trip where driver.username = :driverUsername"
                , "driverUsername", driverUsername);
    }

    @Override
    public List<TripDTO> filterByOrigin(String origin) {
        return filteredTrips("from Trip where origin = :origin"
                , "origin", origin);
    }

    @Override
    public List<TripDTO> filterByDestination(String destination) {
        return filteredTrips("from Trip where destination = :destination"
                , "destination", destination);
    }

    @Override
    public List<TripDTO> filterByEarliestDepartureTime(String earliestDepartureTime) {
        return filteredTrips("from Trip where departureTime >= :earliestDepartureTime"
                , "earliestDepartureTime", earliestDepartureTime);
    }

    @Override
    public List<TripDTO> filterByLatestDepartureTime(String latestDepartureTime) {
        return filteredTrips("from Trip where departureTime <= :latestDepartureTime"
                , "latestDepartureTime", latestDepartureTime);
    }

    @Override
    public List<TripDTO> filterByAvailablePlaces(int availablePlaces) {
        return filteredTrips("from Trip where availablePlaces = :availablePlaces"
                , "availablePlaces", "" + availablePlaces);
    }

    @Override
    public List<TripDTO> filterBySmoking(boolean smoking) {
        return filteredTrips("from Trip where smoking = :smoking"
                , "smoking", String.valueOf(smoking));
    }

    @Override
    public List<TripDTO> filterByPets(boolean pets) {
        return filteredTrips("from Trip where pets = :pets"
                , "pets", String.valueOf(pets));
    }

    @Override
    public List<TripDTO> filterByLuggage(boolean luggage) {
        return filteredTrips("from Trip where luggage = :luggage"
                , "luggage", String.valueOf(luggage));
    }

    private List<TripDTO> filteredTrips(String string, String queryParameter, String parameterValue) {
        Session session = sessionFactory.getCurrentSession();
        Query<Trip> query = session.createQuery(string, Trip.class);
        if (!(queryParameter.equals(""))) {
            switch (queryParameter) {
                case "tripStatus":
                    query.setParameter(queryParameter, TripStatus.valueOf(parameterValue));
                    break;
                case "availablePlaces":
                    query.setParameter(queryParameter, Integer.valueOf(parameterValue));
                    break;
                case "smoking":
                case "pets":
                case "luggage":
                    query.setParameter(queryParameter, Boolean.valueOf(parameterValue));
                    break;
                default:
                    query.setParameter(queryParameter, parameterValue);
                    break;
            }
        }
        List<Trip> trips = query.list();
        return null;
    }
}
