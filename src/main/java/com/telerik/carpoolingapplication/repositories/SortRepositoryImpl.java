package com.telerik.carpoolingapplication.repositories;

import com.telerik.carpoolingapplication.models.*;
import com.telerik.carpoolingapplication.models.constants.Constants;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class SortRepositoryImpl implements SortRepository {
    private SessionFactory sessionFactory;
    private FilterRepository filterRepository;

    @Autowired
    public SortRepositoryImpl(SessionFactory sessionFactory, FilterRepository filterRepository) {
        this.sessionFactory = sessionFactory;
        this.filterRepository = filterRepository;
    }

    @Override
    public List<TripDTO> sortByStatus(String value) {
        return sortedTrips("from Trip order by tripStatus " + ascendingOrDescending(value));
    }

    @Override
    public List<TripDTO> sortByDriver(String value) {
        return sortedTrips("from Trip order by driver.username " + ascendingOrDescending(value));
    }

    @Override
    public List<TripDTO> sortByOrigin(String value) {
        return sortedTrips("from Trip order by origin " + ascendingOrDescending(value));
    }

    @Override
    public List<TripDTO> sortByDestination(String value) {
        return sortedTrips("from Trip order by destination " + ascendingOrDescending(value));
    }

    @Override
    public List<TripDTO> sortByDepartureTime(String value) {
        return sortedTrips("from Trip order by departureTime " + ascendingOrDescending(value));
    }

    @Override
    public List<TripDTO> sortByAvailablePlaces(String value) {
        return sortedTrips("from Trip order by availablePlaces " + ascendingOrDescending(value));
    }

    private List<TripDTO> sortedTrips(String string) {
        Session session = sessionFactory.getCurrentSession();
        Query<Trip> query = session.createQuery(string, Trip.class);
        List<Trip> trips = query.list();
        return filterRepository.getPassengersStatusesAndComments(trips, session);
    }

    private String ascendingOrDescending(String value) {
        if (!(value.equals("descending") || value.equals("ascending"))) {
            throw new IllegalArgumentException(Constants.BAD_REQUEST);
        } else if (value.equals("descending")) {
            return "desc";
        } else {
            return "asc";
        }
    }
}
