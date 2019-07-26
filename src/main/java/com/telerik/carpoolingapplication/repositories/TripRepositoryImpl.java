package com.telerik.carpoolingapplication.repositories;

import com.telerik.carpoolingapplication.models.*;
import com.telerik.carpoolingapplication.models.constants.Constants;
import com.telerik.carpoolingapplication.models.enums.PassengerStatusEnum;
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
public class TripRepositoryImpl implements TripRepository {
    private SessionFactory sessionFactory;

    @Autowired
    public TripRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void createTrip(CreateTripDTO createTripDTO) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            //Fake user for testing purposes that needs to be an authenticated user!
            User fakeDriver = session.get(User.class, 1);
            if (fakeDriver == null) {
                throw new IllegalArgumentException(Constants.UNAUTHORIZED);
            }

            Trip newTrip = ModelsMapper.fromCreateTripDTO(createTripDTO, fakeDriver);
            session.save(newTrip);
            session.getTransaction().commit();
        }
    }

    @Override
    public void editTrip(EditTripDTO editTripDTO) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            //Fake user for testing purposes that needs to be an authenticated user!

            //Ask for equal responses and validations and then catch exceptions!
            User fakeDriver = session.get(User.class, 1);
            if (fakeDriver == null) {
                throw new IllegalArgumentException(Constants.UNAUTHORIZED);
            }
            Query<Trip> query = session.createQuery("from  Trip where driver = :driver and id = :id", Trip.class);
            query.setParameter("driver", fakeDriver);
            query.setParameter("id", editTripDTO.getId());

            if (query.list().size() == 0) {
                throw new IllegalArgumentException(Constants.INVALID_ID_SUPPLIED);
            }

            Trip tripToEdit = query.list().get(0);

            ModelsMapper.updateTrip(tripToEdit, editTripDTO);

            session.update(tripToEdit);

            session.getTransaction().commit();
        }
    }

    @Override
    public TripDTO getTrip(int id) {
        TripDTO tripDTO;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Trip trip = session.get(Trip.class, id);
            if (trip == null) {
                throw new IllegalArgumentException(Constants.TRIP_NOT_FOUND);
            }

            Query<PassengerStatus> query = session.createQuery("from PassengerStatus where trip.id = :id"
                    , PassengerStatus.class);
            query.setParameter("id", id);
            List<PassengerStatus> passengerStatuses = query.list();

            Query<Comment> query1 = session.createQuery("from Comment where trip.id = :id", Comment.class);
            query1.setParameter("id", id);
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
    public void addComment(TripDTO tripDTO, CommentDTO commentDTO) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            //Logged user validation!
            User fakeUser = session.get(User.class, commentDTO.getUserId());
            if (fakeUser == null) {
                throw new IllegalArgumentException(Constants.UNAUTHORIZED);
            }

            if (fakeUser.getId() != tripDTO.getDriver().getId()) {
                Query<PassengerStatus> query = session.createQuery("from PassengerStatus " +
                        "where trip.id = :tripId and user.id = :userId", PassengerStatus.class);
                query.setParameter("tripId", tripDTO.getId());
                query.setParameter("userId", fakeUser.getId());

                //Think of concrete passengerStatus necessary to addComment!
                if (query.list().size() == 0) {
                    throw new IllegalArgumentException(Constants.YOU_DO_NOT_PARTICIPATE);
                }

                if (tripDTO.getTripStatus() != TripStatus.done) {
                    throw new IllegalArgumentException(Constants.TRIP_NOT_FINISHED);
                }

            }
            Trip trip = session.get(Trip.class, tripDTO.getId());
            Comment comment = ModelsMapper.fromCommentDTO(commentDTO, fakeUser, trip);

            session.save(comment);
            session.getTransaction().commit();
        }
    }

    @Override
    public void apply(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            //For testing purposes! Should be logged user!     //Example
            User loggedUser = session.get(User.class, 3);
            if (loggedUser == null) {
                throw new IllegalArgumentException(Constants.UNAUTHORIZED);
            }

            Trip trip = session.get(Trip.class, id);
            TripDTO tripDTO = getTrip(id);
            if (tripDTO == null) {
                throw new IllegalArgumentException(Constants.TRIP_NOT_FOUND);
            }

            PassengerStatus passengerStatus = new PassengerStatus(loggedUser, PassengerStatusEnum.pending
                    , trip);

            //For testing purposes! Should be logged user!
            PassengerDTO fakePassenger = ModelsMapper.fromUserToPassenger(loggedUser, passengerStatus);
            if (fakePassenger.getUserId() == trip.getDriver().getId()) {
                throw new IllegalArgumentException(Constants.YOUR_OWN_TRIP);
            }

            List<PassengerDTO> passengers = tripDTO.getPassengers();
            PassengerDTO passengerDTO = passengers.stream()
                    .filter(p -> p.getUserId() == fakePassenger.getUserId())
                    .findFirst()
                    .orElse(null);

            if (passengerDTO != null) {
                throw new IllegalArgumentException(Constants.ALREADY_APPLIED);
            }
            session.save(passengerStatus);

            session.getTransaction().commit();
        }
    }

    @Override
    public void changePassengerStatus(int tripId, int passengerId, String status) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            TripDTO tripDTO = getTrip(tripId);
            if (tripDTO == null) {
                throw new IllegalArgumentException(Constants.TRIP_NOT_FOUND);
            }

            Query<PassengerStatus> query = session.createQuery("from PassengerStatus " +
                    "where trip.id = :tripId and user.id = :passengerId", PassengerStatus.class);
            query.setParameter("tripId", tripId);
            query.setParameter("passengerId", passengerId);

            if (query.list().size() == 0) {
                throw new IllegalArgumentException(Constants.NO_SUCH_PASSENGER);
            }

            PassengerStatus passengerStatus = query.list().get(0);

            try {
                PassengerStatusEnum passengerStatusEnum = PassengerStatusEnum.valueOf(status);
                passengerStatus.setStatus(passengerStatusEnum);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(Constants.NO_SUCH_STATUS);
            }
            session.update(passengerStatus);

            session.getTransaction().commit();
        }
    }

    //Add additional validations for rate, like if trip is over or what the status of driver/passenger is!
    @Override
    public void rateDriver(int id, RatingDTO ratingDTO) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Trip currentTrip = session.get(Trip.class, id);
            tripNotFoundAndTripNotOverValidation(currentTrip);

            //Testing purposes, should be logged user!
            User loggedUser = session.get(User.class, 3);
            if (loggedUser == null) {
                throw new IllegalArgumentException(Constants.UNAUTHORIZED);
            }

            Query<PassengerStatus> passengerStatusQuery = session.createQuery("from PassengerStatus " +
                    "where trip.id = :tripId " +
                    "and user.id = :passengerId " +
                    "and status = :passengerStatusValue", PassengerStatus.class);
            passengerStatusQuery.setParameter("tripId", id);
            passengerStatusQuery.setParameter("passengerId", loggedUser.getId());
            passengerStatusQuery.setParameter("passengerStatusValue", PassengerStatusEnum.accepted);

            if (passengerStatusQuery.list().size() == 0) {
                throw new IllegalArgumentException(Constants.NO_SUCH_PASSENGER);
            }

            User driver = currentTrip.getDriver();

            Query<Rating> ratingQuery = session.createQuery("from Rating " +
                    "where trip.id = :tripId " +
                    "and ratingReceiver.id = :driverId " +
                    "and ratingGiver.id = :passengerId " +
                    "and isReceiverDriver = true ", Rating.class);
            ratingQuery.setParameter("tripId", id);
            ratingQuery.setParameter("driverId", driver.getId());
            ratingQuery.setParameter("passengerId", loggedUser.getId());

            if (ratingQuery.list().size() == 0) {
                Rating rating = new Rating(ratingDTO.getRating(), loggedUser, driver, true, currentTrip);
                session.save(rating);
            } else {
                Rating ratingDriver = ratingQuery.list().get(0);
                ratingDriver.setRating(ratingDTO.getRating());
                session.update(ratingDriver);
            }

            Query calculated = session.createQuery("select avg(rating) from Rating " +
                    "where ratingReceiver.id = :driverId and isReceiverDriver = true ", Rating.class);
            calculated.setParameter("driverId", driver.getId());
            double averageRating = (double) calculated.list().get(0);

            driver.setRatingAsDriver(averageRating);
            session.update(driver);

            session.getTransaction().commit();
        }
    }

    @Override
    public void ratePassenger(int tripId, int passengerId, RatingDTO ratingDTO) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Trip currentTrip = session.get(Trip.class, tripId);
            tripNotFoundAndTripNotOverValidation(currentTrip);

            User driver = currentTrip.getDriver();
            //Testing purposes! Should be logged user!
            User loggedUser = session.get(User.class, 2);
            // Add unauthorized and forbidden validations when security is implemented!
            if (passengerId == loggedUser.getId() || passengerId == driver.getId()) {
                throw new IllegalArgumentException(Constants.RATE_YOURSELF);
            }

            Query<PassengerStatus> passengerQuery = session.createQuery("from PassengerStatus " +
                    "where trip.id = :tripId " +
                    "and user.id = :passengerId " +
                    "and status = :status", PassengerStatus.class);
            passengerQuery.setParameter("tripId", tripId);
            passengerQuery.setParameter("passengerId", passengerId);
            passengerQuery.setParameter("status", PassengerStatusEnum.accepted);
            if (passengerQuery.list().size() == 0) {
                throw new IllegalArgumentException(Constants.NO_SUCH_PASSENGER);
            }

            Query<PassengerStatus> loggedUserQuery = session.createQuery("from PassengerStatus " +
                    "where trip.id = :tripId " +
                    "and user.id = :loggedUserId " +
                    "and status = :status", PassengerStatus.class);
            loggedUserQuery.setParameter("tripId", tripId);
            loggedUserQuery.setParameter("loggedUserId", loggedUser.getId());
            loggedUserQuery.setParameter("status", PassengerStatusEnum.accepted);
            if (loggedUserQuery.list().size() == 0) {
                throw new IllegalArgumentException(Constants.YOU_DO_NOT_PARTICIPATE);
            }


            Query<Rating> ratingQuery = session.createQuery("from Rating " +
                    "where trip.id = :tripId " +
                    "and ratingReceiver.id = :loggedUserId " +
                    "and ratingGiver.id = :passengerId " +
                    "and isReceiverDriver = false ", Rating.class);
            ratingQuery.setParameter("tripId", tripId);
            ratingQuery.setParameter("loggedUserId", loggedUser.getId());
            ratingQuery.setParameter("passengerId", passengerId);

            User passenger = session.get(User.class, passengerId);

            if (ratingQuery.list().size() == 0) {
                Rating rating = new Rating(ratingDTO.getRating(), loggedUser, passenger, false, currentTrip);
                session.save(rating);
            } else {
                Rating rating = ratingQuery.list().get(0);
                rating.setRating(ratingDTO.getRating());
                session.update(rating);
            }
            Query calculated = session.createQuery("select avg(rating) from Rating " +
                    "where ratingReceiver.id = :passengerId " +
                    "and isReceiverDriver = false ", Rating.class);
            calculated.setParameter("passengerId", passengerId);
            double averageRating = (double) calculated.list().get(0);

            passenger.setRatingAsPassenger(averageRating);
            session.update(passenger);

            session.getTransaction().commit();
        }
    }

    private void tripNotFoundAndTripNotOverValidation(Trip trip) {
        if (trip == null) {
            throw new IllegalArgumentException(Constants.TRIP_NOT_FOUND);
        }

        if (trip.getTripStatus() != TripStatus.done) {
            throw new IllegalArgumentException(Constants.RATING_NOT_ALLOWED_BEFORE_TRIP_IS_DONE);
        }
    }
}
