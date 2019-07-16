package com.telerik.carpoolingapplication.repositories;

import com.telerik.carpoolingapplication.models.LoginDTO;
import com.telerik.carpoolingapplication.models.UserDTO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private SessionFactory sessionFactory;

    @Autowired
    public UserRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public void editUser(UserDTO userDTO) {

    }

    @Override
    public UserDTO getUser(String username) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query<UserDTO> query = session.createQuery("from UserDTO where username = :username", UserDTO.class);
            query.setParameter("username", username);
            List<UserDTO> users = query.list();
            session.getTransaction().commit();
            if (!users.isEmpty()) {
                return users.get(0);
            } else {
                return null;
            }
        }
    }

    @Override
    public void authorize(LoginDTO loginDTO) {

    }

    @Override
    public void createUser(UserDTO userDTO) {

    }
}
