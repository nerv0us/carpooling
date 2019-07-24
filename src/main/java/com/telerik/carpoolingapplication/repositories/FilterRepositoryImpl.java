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

    @Override
    public List<TripDTO> filterByEarliestDepartureTime(String earliestDepartureTime) {
        Session session = sessionFactory.getCurrentSession();
        Query<Trip> trips = session.createQuery("from Trip where departureTime >= :earliestDepartureTime", Trip.class);
        trips.setParameter("earliestDepartureTime", earliestDepartureTime);
        return getPassengerStatusesAndComments(trips, session);
    }

    @Override
    public List<TripDTO> filterByLatestDepartureTime(String latestDepartureTime) {
        Session session = sessionFactory.getCurrentSession();
        Query<Trip> trips = session.createQuery("from Trip where departureTime <= :latestDepartureTime", Trip.class);
        trips.setParameter("latestDepartureTime", latestDepartureTime);
        return getPassengerStatusesAndComments(trips, session);
    }

    @Override
    public List<TripDTO> filterByAvailablePlaces(int availablePlaces) {
        Session session = sessionFactory.getCurrentSession();
        Query<Trip> trips = session.createQuery("from Trip where availablePlaces = :availablePlaces", Trip.class);
        trips.setParameter("availablePlaces", availablePlaces);
        return getPassengerStatusesAndComments(trips, session);
    }

    @Override
    public List<TripDTO> filterBySmoking(boolean smoking) {
        Session session = sessionFactory.getCurrentSession();
        Query<Trip> trips = session.createQuery("from Trip where smoking = :smoking", Trip.class);
        trips.setParameter("smoking", smoking);
        return getPassengerStatusesAndComments(trips, session);
    }

    @Override
    public List<TripDTO> filterByPets(boolean pets) {
        Session session = sessionFactory.getCurrentSession();
        Query<Trip> trips = session.createQuery("from Trip where pets = :pets", Trip.class);
        trips.setParameter("pets", pets);
        return getPassengerStatusesAndComments(trips, session);
    }

    @Override
    public List<TripDTO> filterByLuggage(boolean luggage) {
        Session session = sessionFactory.getCurrentSession();
        Query<Trip> trips = session.createQuery("from Trip where luggage = :luggage", Trip.class);
        trips.setParameter("luggage", luggage);
        return getPassengerStatusesAndComments(trips, session);
    }

    public List<TripDTO> getPassengerStatusesAndComments(Query<Trip> trips, Session session) {
        Query<PassengerStatus> statusesQuery = session.createQuery("from PassengerStatus ",
                PassengerStatus.class);
        List<PassengerStatus> passengerStatuses = statusesQuery.list();
        Query<Comment> commentsQuery = session.createQuery("from Comment "
                , Comment.class);
        List<Comment> comments = commentsQuery.list();
        return ModelsMapper.fromTrip(trips.list(), passengerStatuses, comments);
    }
}
