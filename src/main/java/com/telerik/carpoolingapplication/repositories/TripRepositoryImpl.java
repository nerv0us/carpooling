package com.telerik.carpoolingapplication.repositories;

import com.telerik.carpoolingapplication.models.CreateTripDTO;
import com.telerik.carpoolingapplication.models.TripDTO;
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
        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();



            /*    TripDTO newTrip = new TripDTO();*/
            /*    newTrip.setCarModel(createTripDTO.getCarModel());*/
            /*    newTrip.setMessage(createTripDTO.g);*/
            /*}*/
            /*{*/
            /*        "message": "string",*/
            /*        "departureTime": "2019-07-16T10:07:00.646Z",*/
            /*        "origin": "string",*/
            /*        "destination": "string",*/
            /*        "availablePlaces": 0,*/
            /*        "smoking": true,*/
            /*        "pets": true,*/
            /*        "luggage": true*/
            /*}*/
        }
    }
}
