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
public class FilterAndSortHelperImpl implements FilterAndSortHelper {
    private SessionFactory sessionFactory;

    @Autowired
    public FilterAndSortHelperImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<TripDTO> unsortedUnfiltered() {
        Session session = sessionFactory.getCurrentSession();
        Query<Trip> trips = session.createQuery("from Trip", Trip.class);
        return getPassengerStatusesAndComments(trips, session);
    }

    @Override
    public List<TripDTO> filterByStatus(TripStatus tripStatus) {
        Session session = sessionFactory.getCurrentSession();
        Query<Trip> trips = session.createQuery("from Trip where tripStatus = :tripStatus", Trip.class);
        trips.setParameter("tripStatus", tripStatus);
        return getPassengerStatusesAndComments(trips, session);
    }

    @Override
    public List<TripDTO> filterByDriver(String driverUsername) {
        Session session = sessionFactory.getCurrentSession();
        Query<Trip> trips = session.createQuery("from Trip where driver.username = :driverUsername", Trip.class);
        trips.setParameter("driverUsername", driverUsername);
        return getPassengerStatusesAndComments(trips, session);
    }

    @Override
    public List<TripDTO> filterByOrigin(String origin) {
        Session session = sessionFactory.getCurrentSession();
        Query<Trip> trips = session.createQuery("from Trip where origin = :origin", Trip.class);
        trips.setParameter("origin", origin);
        return getPassengerStatusesAndComments(trips, session);
    }

    @Override
    public List<TripDTO> filterByDestination(String destination) {
        Session session = sessionFactory.getCurrentSession();
        Query<Trip> trips = session.createQuery("from Trip where destination = :destination", Trip.class);
        trips.setParameter("destination", destination);
        return getPassengerStatusesAndComments(trips, session);
    }

    private List<TripDTO> getPassengerStatusesAndComments(Query<Trip> trips, Session session) {
        Query<PassengerStatus> statusesQuery = session.createQuery("from PassengerStatus ",
                PassengerStatus.class);
        List<PassengerStatus> passengerStatuses = statusesQuery.list();
        Query<Comment> commentsQuery = session.createQuery("from Comment "
                , Comment.class);
        List<Comment> comments = commentsQuery.list();
        return ModelsMapper.fromTrip(trips.list(), passengerStatuses, comments);
    }
}
