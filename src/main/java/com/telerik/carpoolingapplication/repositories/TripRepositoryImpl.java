package com.telerik.carpoolingapplication.repositories;

import com.telerik.carpoolingapplication.models.*;
import com.telerik.carpoolingapplication.models.constants.Messages;
import com.telerik.carpoolingapplication.models.enums.PassengerStatus;
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
            //
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
                throw new IllegalArgumentException(Messages.UNAUTHORIZED);
            }

            TripDTO tripToEdit = session.get(TripDTO.class, editTripDTO.getId());
            if (tripToEdit == null) {
                throw new IllegalArgumentException(Messages.INVALID_ID_SUPPLIED);
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
                throw new IllegalArgumentException(Messages.UNAUTHORIZED);
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

            //For testing purposes! Should be logged user!
            UserDTO fakeUser = session.get(UserDTO.class, 3);
            if (fakeUser == null) {
                throw new IllegalArgumentException(Messages.UNAUTHORIZED);
            }

            //For testing purposes! Should be logged user!
            PassengerDTO fakePassenger = ModelsMapper.fromUserToPassanger(fakeUser);
            if (fakePassenger.getUserId() == tripDTO.getDriver().getId()) {
                throw new IllegalArgumentException(Messages.YOUR_OWN_TRIP);
            }

            List<PassengerDTO> passengers = tripDTO.getPassengers();
            PassengerDTO passengerDTO = passengers.stream()
                    .filter(p -> p.getUserId() == fakePassenger.getUserId())
                    .findFirst()
                    .orElse(null);

            if (passengerDTO != null) {
                throw new IllegalArgumentException(Messages.ALREADY_APPLIED);
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
                throw new IllegalArgumentException(Messages.TRIP_NOT_FOUND);
            }

            List<PassengerDTO> passengers = tripDTO.getPassengers();
            PassengerDTO passengerDTO = passengers.stream()
                    .filter(p -> p.getId() == passengerId)
                    .findFirst()
                    .orElse(null);
            if (passengerDTO == null) {           //Should become constant after merge!
                throw new IllegalArgumentException("Passenger not found!");
            }

            try {
                PassengerStatus passengerStatus = PassengerStatus.valueOf(status);
                passengerDTO.setPassengerStatus(passengerStatus);
            }catch (IllegalArgumentException e){
                throw new IllegalArgumentException(Messages.NO_SUCH_STATUS);
            }
            session.update(passengerDTO);

            session.getTransaction().commit();
        }
    }
}
