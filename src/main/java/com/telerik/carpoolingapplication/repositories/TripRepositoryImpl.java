package com.telerik.carpoolingapplication.repositories;

import com.telerik.carpoolingapplication.models.*;
import com.telerik.carpoolingapplication.models.constants.Messages;
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
                throw new IllegalArgumentException(Messages.UNAUTHORIZED_MESSAGE);
            }

            TripDTO tripToEdit = session.get(TripDTO.class, editTripDTO.getId());
            if (tripToEdit == null) {
                throw new IllegalArgumentException(Messages.INVALID_ID_SUPPLIED_MESSAGE);
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
}
