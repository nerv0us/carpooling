package com.telerik.carpoolingapplication.repositories;

import com.telerik.carpoolingapplication.models.CreateTripDTO;
import com.telerik.carpoolingapplication.models.ModelsMapper;
import com.telerik.carpoolingapplication.models.TripDTO;
import com.telerik.carpoolingapplication.models.UserDTO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

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
}
