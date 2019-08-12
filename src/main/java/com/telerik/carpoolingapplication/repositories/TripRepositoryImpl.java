package com.telerik.carpoolingapplication.repositories;

import com.telerik.carpoolingapplication.exceptions.UnauthorizedException;
import com.telerik.carpoolingapplication.models.*;
import com.telerik.carpoolingapplication.models.constants.Constants;
import com.telerik.carpoolingapplication.models.dto.*;
import com.telerik.carpoolingapplication.models.enums.PassengerStatusEnum;
import com.telerik.carpoolingapplication.models.enums.TripStatus;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class TripRepositoryImpl implements TripRepository {
    private SessionFactory sessionFactory;

    @Autowired
    public TripRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<TripDTO> getFilteredTrips(TripStatus status, String driverUsername, String origin, String destination
            , String latestDepartureTime, String earliestDepartureTime, Integer places, Boolean cigarettes
            , Boolean animals, Boolean baggage) {
        Session session = sessionFactory.getCurrentSession();
        Query<Trip> tripQuery = queryBuilder(session, status, driverUsername, origin, destination, latestDepartureTime
                , earliestDepartureTime, places, cigarettes, animals, baggage);
        return getPassengersStatusesAndComments(tripQuery.list(), session);
    }

    @Override
    public void createTrip(CreateTripDTO createTripDTO, int userId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User driver = session.get(User.class, userId);
            Trip newTrip = ModelsMapper.fromCreateTripDTO(createTripDTO, driver);
            session.save(newTrip);
            session.getTransaction().commit();
        }
    }

    @Override
    public void editTrip(EditTripDTO editTripDTO, int userId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User driver = session.get(User.class, userId);
            Query<Trip> query = session.createQuery("from  Trip where driver = :driver and id = :id", Trip.class);
            query.setParameter("driver", driver);
            query.setParameter("id", editTripDTO.getId());
            Trip tripToEdit = query.list().get(0);
            ModelsMapper.updateTrip(tripToEdit, editTripDTO);
            session.update(tripToEdit);
            session.getTransaction().commit();
        }
    }

    @Override
    public TripDTO getTrip(int tripId) {
        TripDTO tripDTO;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Trip trip = session.get(Trip.class, tripId);
            if (trip == null) {
                throw new IllegalArgumentException(Constants.TRIP_NOT_FOUND);
            }
            Query<PassengerStatus> query = session.createQuery("from PassengerStatus where trip.id = :id"
                    , PassengerStatus.class);
            query.setParameter("id", tripId);
            List<PassengerStatus> passengerStatuses = query.list();
            Query<Comment> query1 = session.createQuery("from Comment where trip.id = :id", Comment.class);
            query1.setParameter("id", tripId);
            List<Comment> comments = query1.list();
            tripDTO = ModelsMapper.fromTrip(trip, passengerStatuses, comments);
            session.getTransaction().commit();
        }
        return tripDTO;
    }

    @Override
    public void changeTripStatus(TripDTO tripDTO, TripStatus updatedStatus) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Trip trip = session.get(Trip.class, tripDTO.getId());
            trip.setTripStatus(updatedStatus);
            session.update(trip);
            session.getTransaction().commit();
        }
    }

    @Override
    public void addComment(TripDTO tripDTO, CommentDTO commentDTO, int userId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User user = session.get(User.class, userId);
            List<PassengerStatus> passengers = passengers(tripDTO.getId(), userId
                    , PassengerStatusEnum.ACCEPTED);
            if (tripDTO.getDriver().getId() != userId && passengers.isEmpty()) {
                throw new UnauthorizedException(Constants.YOU_DO_NOT_PARTICIPATE);
            }
            Trip trip = session.get(Trip.class, tripDTO.getId());
            Comment comment = ModelsMapper.fromCommentDTO(commentDTO, user, trip);
            session.save(comment);
            session.getTransaction().commit();
        }
    }

    @Override
    public void apply(int id, UserDTO userDTO) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Trip trip = session.get(Trip.class, id);
            User user = session.get(User.class, userDTO.getId());
            PassengerStatus passengerStatus = new PassengerStatus(user, PassengerStatusEnum.PENDING, trip);
            session.save(passengerStatus);
            session.getTransaction().commit();
        }
    }

    @Override
    public void changePassengerStatus(PassengerStatus passengerStatus, int tripId, int placesReducingValue) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            if (placesReducingValue != 0) {
                Trip trip = session.get(Trip.class, tripId);
                trip.setAvailablePlaces(trip.getAvailablePlaces() + placesReducingValue);
                session.update(trip);
            }
            Query<PassengerStatus> query = session.createQuery("from PassengerStatus where trip.id = :tripId " +
                            "and user.id = :userId", PassengerStatus.class);
            query.setParameter("tripId", tripId);
            query.setParameter("userId", passengerStatus.getUser().getId());
            PassengerStatus newStatus = query.list().get(0);
            newStatus.setStatus(passengerStatus.getStatus());
            session.update(newStatus);
            session.getTransaction().commit();
        }
    }

    @Override
    public void rateDriver(TripDTO tripDTO, UserDTO userDTO, RatingDTO ratingDTO) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User user = session.get(User.class, userDTO.getId());
            User driver = session.get(User.class, tripDTO.getDriver().getId());
            Trip trip = session.get(Trip.class, tripDTO.getId());
            List<Rating> ratings = ratings(session, tripDTO.getId(), driver.getId()
                    , user.getId(), true);
            if (ratings.isEmpty()) {
                Rating rating = new Rating(ratingDTO.getRating(), user, driver, true, trip);
                session.save(rating);
            } else {
                Rating ratingDriver = ratings.get(0);
                ratingDriver.setRating(ratingDTO.getRating());
                session.update(ratingDriver);
            }
            double averageRating = calculateAverageRating(session, driver.getId(), true);
            driver.setRatingAsDriver(averageRating);
            session.update(driver);
            session.getTransaction().commit();
        }
    }

    @Override
    public void ratePassenger(int tripId, int passengerId, UserDTO userDTO, RatingDTO ratingDTO) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Trip trip = session.get(Trip.class, tripId);
            User user = session.get(User.class, userDTO.getId());
            User passenger = session.get(User.class, passengerId);
            List<Rating> ratings = ratings(session, tripId, passengerId, user.getId(), false);
            if (ratings.isEmpty()) {
                Rating rating = new Rating(ratingDTO.getRating(), user, passenger, false, trip);
                session.save(rating);
            } else {
                Rating rating = ratings.get(0);
                rating.setRating(ratingDTO.getRating());
                session.update(rating);
            }
            double averageRating = calculateAverageRating(session, passengerId, false);
            passenger.setRatingAsPassenger(averageRating);
            session.update(passenger);
            session.getTransaction().commit();
        }
    }

    public List<PassengerStatus> passengers(int tripId, int userId
            , PassengerStatusEnum passengerStatusEnum) {
        List<PassengerStatus> passengerStatuses;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query<PassengerStatus> query;
            if (passengerStatusEnum != null) {
                query = session.createQuery("from PassengerStatus " +
                        "where trip.id = :tripId " +
                        "and user.id = :userId " +
                        "and status = :status", PassengerStatus.class);
                query.setParameter("tripId", tripId);
                query.setParameter("userId", userId);
                query.setParameter("status", passengerStatusEnum);
            } else {
                query = session.createQuery("from PassengerStatus " +
                        "where trip.id = :tripId " +
                        "and user.id = :userId ", PassengerStatus.class);
                query.setParameter("tripId", tripId);
                query.setParameter("userId", userId);
            }
            passengerStatuses = query.list();
            session.getTransaction().commit();
        }
        return passengerStatuses;
    }

    private List<Rating> ratings(Session session, int tripId, int ratingReceiverId, int ratingGiverId
            , boolean isReceiverDriver) {
        Query<Rating> ratingQuery = session.createQuery("from Rating " +
                "where trip.id = :tripId " +
                "and ratingReceiver.id = :ratingReceiverId " +
                "and ratingGiver.id = :ratingGiverId " +
                "and isReceiverDriver = :isReceiverDriver ", Rating.class);
        ratingQuery.setParameter("tripId", tripId);
        ratingQuery.setParameter("ratingReceiverId", ratingReceiverId);
        ratingQuery.setParameter("ratingGiverId", ratingGiverId);
        ratingQuery.setParameter("isReceiverDriver", isReceiverDriver);
        return ratingQuery.list();
    }

    private double calculateAverageRating(Session session, int userId, boolean isReceiverDriver) {
        Query calculated = session.createQuery("select avg(rating) from Rating " +
                "where ratingReceiver.id = :userId " +
                "and isReceiverDriver = :isReceiverDriver ");
        calculated.setParameter("userId", userId);
        calculated.setParameter("isReceiverDriver", isReceiverDriver);
        return (double) calculated.list().get(0);
    }

    private List<TripDTO> getPassengersStatusesAndComments(List<Trip> trips, Session session) {
        Query<PassengerStatus> statusesQuery = session.createQuery("from PassengerStatus ",
                PassengerStatus.class);
        List<PassengerStatus> passengerStatuses = statusesQuery.list();
        Query<Comment> commentsQuery = session.createQuery("from Comment "
                , Comment.class);
        List<Comment> comments = commentsQuery.list();
        return ModelsMapper.fromTrip(trips, passengerStatuses, comments);
    }

    private Query<Trip> queryBuilder(Session session, TripStatus status, String driverUsername, String origin, String destination
            , String latestDepartureTime, String earliestDepartureTime, Integer places, Boolean cigarettes
            , Boolean animals, Boolean baggage) {
        String queryText = "from Trip ";
        List<String> parameters = new ArrayList<>();
        if (status != null) {
            queryText += "where tripStatus = :status ";
            parameters.add("status");
        }
        if (queryText.endsWith("Trip ")) {
            if (driverUsername != null) {
                queryText += "where driver.username = :driverUsername ";
                parameters.add("driverUsername");
            }
        } else {
            if (driverUsername != null) {
                queryText += "and driver.username = :driverUsername ";
                parameters.add("driverUsername");
            }
        }
        if (queryText.endsWith("Trip ")) {
            if (origin != null) {
                queryText += "where origin = :origin ";
                parameters.add("origin");
            }
        } else {
            if (origin != null) {
                queryText += "and origin = :origin ";
                parameters.add("origin");
            }
        }
        if (queryText.endsWith("Trip ")) {
            if (destination != null) {
                queryText += "where destination = :destination ";
                parameters.add("destination");
            }
        } else {
            if (destination != null) {
                queryText += "and destination = :destination ";
                parameters.add("destination");
            }
        }
        if (queryText.endsWith("Trip ")) {
            if (latestDepartureTime != null) {
                /*latestDepartureTime = latestDepartureTime.replaceAll(" ","");*/
                queryText += "where departureTime >= :latestDepartureTime ";
                parameters.add("latestDepartureTime");
            }
        } else {
            if (latestDepartureTime != null) {
                /*latestDepartureTime = latestDepartureTime.replaceAll(" ","");*/
                queryText += "and departureTime >= :latestDepartureTime ";
                parameters.add("latestDepartureTime");
            }
        }
        if (queryText.endsWith("Trip ")) {
            if (earliestDepartureTime != null) {
                /*earliestDepartureTime = earliestDepartureTime.replaceAll(" ","");*/
                queryText += "where departureTime <= :earliestDepartureTime ";
                parameters.add("earliestDepartureTime");
            }
        } else {
            if (earliestDepartureTime != null) {
                /*earliestDepartureTime = earliestDepartureTime.replaceAll(" ","");*/
                queryText += "and departureTime <= :earliestDepartureTime ";
                parameters.add("earliestDepartureTime");
            }
        }
        if (queryText.endsWith("Trip ")) {
            if (places != null) {
                queryText += "where availablePlaces = :places ";
                parameters.add("places");
            }
        } else {
            if (places != null) {
                queryText += "and availablePlaces = :places ";
                parameters.add("places");
            }
        }
        if (queryText.endsWith("Trip ")) {
            if (cigarettes != null) {
                queryText += "where smoking = :cigarettes ";
                parameters.add("cigarettes");
            }
        } else {
            if (cigarettes != null) {
                queryText += "and smoking = :cigarettes ";
                parameters.add("cigarettes");
            }
        }
        if (queryText.endsWith("Trip ")) {
            if (animals != null) {
                queryText += "where pets = :animals ";
                parameters.add("animals");
            }
        } else {
            if (animals != null) {
                queryText += "and pets = :animals ";
                parameters.add("animals");
            }
        }
        if (queryText.endsWith("Trip ")) {
            if (baggage != null) {
                queryText += "where luggage = :baggage ";
                parameters.add("baggage");
            }
        } else {
            if (baggage != null) {
                queryText += "and luggage = :baggage ";
                parameters.add("baggage");
            }
        }
        Query<Trip> tripQuery = session.createQuery(queryText, Trip.class);
        for (String parameter : parameters) {
            switch (parameter) {
                case "status":
                    tripQuery.setParameter(parameter, status);
                    break;
                case "driverUsername":
                    tripQuery.setParameter(parameter, driverUsername);
                    break;
                case "origin":
                    tripQuery.setParameter(parameter, origin);
                    break;
                case "destination":
                    tripQuery.setParameter(parameter, destination);
                    break;
                case "latestDepartureTime":
                    tripQuery.setParameter(parameter, latestDepartureTime);
                    break;
                case "earliestDepartureTime":
                    tripQuery.setParameter(parameter, earliestDepartureTime);
                    break;
                case "places":
                    tripQuery.setParameter(parameter, places);
                    break;
                case "cigarettes":
                    tripQuery.setParameter(parameter, cigarettes);
                    break;
                case "animals":
                    tripQuery.setParameter(parameter, animals);
                    break;
                case "baggage":
                    tripQuery.setParameter(parameter, baggage);
                    break;
            }
        }
        /*tripQuery.setMaxResults(6);*/
        return tripQuery;
    }
}
