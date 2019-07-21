package com.telerik.carpoolingapplication.repositories;

import com.telerik.carpoolingapplication.models.*;
import com.telerik.carpoolingapplication.models.constants.Constants;
import com.telerik.carpoolingapplication.models.enums.PassengerStatus;
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
    public List<TripDTO> getTrips() {
        Session session = sessionFactory.getCurrentSession();

        //Think if query should return all trips or only those that are not passed!
        Query<TripDTO> query = session.createQuery("from TripDTO", TripDTO.class);

        return query.list();
    }

    @Override
    public void createTrip(CreateTripDTO createTripDTO) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            //Fake user for testing purposes that needs to be an authenticated user!
            UserDTO fakeUser = session.get(UserDTO.class, 1);

            TripDTO newTrip = ModelsMapper.fromCreateTripDTO(createTripDTO, fakeUser);
            session.save(newTrip);
            session.getTransaction().commit();
        }
    }

    @Override
    public void editTrip(EditTripDTO editTripDTO) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            //Fake user for testing purposes that needs to be an authenticated user!
            //

            //Ask for equal responses and validations and then catch exceptions!
            UserDTO fakeUser = session.get(UserDTO.class, 1);
            if (fakeUser == null) {
                throw new IllegalArgumentException(Constants.UNAUTHORIZED);
            }

            TripDTO tripToEdit = session.get(TripDTO.class, editTripDTO.getId());
            if (tripToEdit == null) {
                throw new IllegalArgumentException(Constants.INVALID_ID_SUPPLIED);
            }

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
            tripDTO = session.get(TripDTO.class, id);
            session.getTransaction().commit();
        }
        return tripDTO;
    }

    @Override
    public void changeTripStatus(TripDTO tripDTO, TripStatus updatedStatus) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            tripDTO.setTripStatus(updatedStatus);
            session.update(tripDTO);
            session.getTransaction().commit();
        }
    }

    @Override
    public void addComment(TripDTO tripDTO, CommentDTO commentDTO) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            UserDTO fakeUser = session.get(UserDTO.class, commentDTO.getAuthor().getId());

            //Setting fakeUser as author for testing purposes!
            commentDTO.setAuthor(fakeUser);
            if (fakeUser == null) {
                throw new IllegalArgumentException(Constants.UNAUTHORIZED);
            }

            session.save(commentDTO);
            tripDTO.getComments().add(commentDTO);
            session.update(tripDTO);

            session.getTransaction().commit();
        }
    }

    @Override
    public void apply(TripDTO tripDTO) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            //For testing purposes! Should be logged user!     //Example
            UserDTO loggedUser = session.get(UserDTO.class, 4);
            if (loggedUser == null){
                throw new IllegalArgumentException(Constants.UNAUTHORIZED);
            }

            //For testing purposes! Should be logged user!
            PassengerDTO fakePassenger = ModelsMapper.fromUserToPassanger(loggedUser);
            if (fakePassenger.getUserId() == tripDTO.getDriver().getId()) {
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

            tripDTO.getPassengers().add(fakePassenger);
            session.save(fakePassenger);
            session.update(tripDTO);

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

            // Throws IllegalArgumentException("Passenger not found!")
            PassengerDTO passengerDTO = findPassengerOrThrowNotFound(tripDTO.getPassengers(), passengerId);

            try {
                PassengerStatus passengerStatus = PassengerStatus.valueOf(status);
                passengerDTO.setPassengerStatus(passengerStatus);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(Constants.NO_SUCH_STATUS);
            }
            session.update(passengerDTO);

            session.getTransaction().commit();
        }
    }

    //Add additional validations for rate, like if trip is over or what the status of driver/passenger is!
    @Override
    public void rateDriver(int id, DriverRatingDTO driverRatingDTO) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            TripDTO currentTrip = getTrip(id);
            if (currentTrip == null) {
                throw new IllegalArgumentException(Constants.TRIP_NOT_FOUND);
            }

            //Testing purposes, should be logged user!
            UserDTO loggedUser = session.get(UserDTO.class, driverRatingDTO.getRatingGiverId());
            if (loggedUser == null){
                throw new IllegalArgumentException(Constants.UNAUTHORIZED);
            }

            List<DriverRatingDTO> driverRatings = currentTrip.getDriverRatings();
            DriverRatingDTO checkIfUserHasRated = driverRatings.stream()
                    .filter(r -> r.getRatingGiverId() == loggedUser.getId())
                    .findFirst()
                    .orElse(null);

            if (checkIfUserHasRated != null){
                throw new IllegalArgumentException(Constants.DRIVER_ALREADY_RATED);
            }

            // Add unauthorized and forbidden validations when security is implemented!

            UserDTO driver = currentTrip.getDriver();

            // Change default value of rating as driver and rating as passenger in users and passengers!
            Double currentRating = driver.getRatingAsDriver();
            if (currentRating == null) {
                currentRating = 0D;
            }
            currentRating += driverRatingDTO.getRating();
            driver.setRatingAsDriver(currentRating);

            currentTrip.getDriverRatings().add(driverRatingDTO);
            session.save(driverRatingDTO);
            session.update(driver);
            session.update(currentTrip);

            session.getTransaction().commit();
        }
    }

    // Fix DriverRatingDTO class and generate corresponding tables in database!
    @Override
    public void ratePassenger(int tripId, int passengerId, PassengerRatingDTO passengerRatingDTO) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            //Avoiding errors!
            passengerRatingDTO.setRatingReceiverId(passengerId);

            TripDTO tripDTO = getTrip(tripId);
            if (tripDTO == null) {
                throw new IllegalArgumentException(Constants.TRIP_NOT_FOUND);
            }

            // Add unauthorized and forbidden validations when security is implemented!

            // Throws IllegalArgumentException("Passenger not found!")
            PassengerDTO passengerDTO = findPassengerOrThrowNotFound(tripDTO.getPassengers(), passengerId);

            List<PassengerRatingDTO> passengersRatings = tripDTO.getPassengersRatings();
            PassengerRatingDTO checkIfPassengerIsRated = passengersRatings.stream()
                    .filter(r -> r.getRatingReceiverId() == passengerId)
                    .findFirst()
                    .orElse(null);

            if (checkIfPassengerIsRated != null){
                throw new IllegalArgumentException(Constants.PASSENGER_ALREADY_RATED);
            }


            Double currentRating = passengerDTO.getRatingAsPassenger();
            if (currentRating == null) {
                currentRating = 0D;
            }
            currentRating += passengerRatingDTO.getRating();
            passengerDTO.setRatingAsPassenger(currentRating);

            UserDTO userDTO = session.get(UserDTO.class, passengerDTO.getUserId());
            userDTO.setRatingAsPassenger(currentRating);

            tripDTO.getPassengersRatings().add(passengerRatingDTO);

            session.save(passengerRatingDTO);

            session.update(tripDTO);
            session.update(passengerDTO);
            session.update(userDTO);

            session.getTransaction().commit();
        }
    }

    private static PassengerDTO findPassengerOrThrowNotFound(List<PassengerDTO> passengers, int passengerId) {
        PassengerDTO passengerDTO = passengers.stream()
                .filter(p -> p.getId() == passengerId)
                .findFirst()
                .orElse(null);
        if (passengerDTO == null) {           //Should become constant after merge!
            throw new IllegalArgumentException("Passenger not found!");
        }

        return passengerDTO;
    }
}
