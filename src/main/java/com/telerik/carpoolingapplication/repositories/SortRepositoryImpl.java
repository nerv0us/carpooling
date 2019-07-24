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
        Session session = sessionFactory.getCurrentSession();
        Query<Trip> trips;
        if (!(value.equals("descending") || value.equals("ascending"))) {
            throw new IllegalArgumentException(Constants.ILLEGAL_VALUE);
        } else if (value.equals("descending")) {
            trips = session.createQuery("from Trip order by tripStatus desc ", Trip.class);
        } else {
            trips = session.createQuery("from Trip order by tripStatus asc ", Trip.class);
        }
        return filterRepository.getPassengerStatusesAndComments(trips, session);
    }

    @Override
    public List<TripDTO> sortByDriver(String value) {
        return null;
    }

    @Override
    public List<TripDTO> sortByOrigin(String value) {
        return null;
    }

    @Override
    public List<TripDTO> sortByDestination(String value) {
        return null;
    }

    @Override
    public List<TripDTO> sortByEarliestDepartureTime(String value) {
        return null;
    }

    @Override
    public List<TripDTO> sortByLatestDepartureTime(String value) {
        return null;
    }

    @Override
    public List<TripDTO> sortByAvailablePlaces(String value) {
        return null;
    }
}
