package com.telerik.carpoolingapplication.repositories;

import com.telerik.carpoolingapplication.models.CreateUserDTO;
import com.telerik.carpoolingapplication.models.ModelsMapper;
import com.telerik.carpoolingapplication.models.User;
import com.telerik.carpoolingapplication.models.UserDTO;
import com.telerik.carpoolingapplication.models.constants.Constants;
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
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User userToEdit = session.get(User.class, userDTO.getId());
            if (!isEmailExist(userDTO.getEmail()).isEmpty() &&
                    !userToEdit.getEmail().equals(userDTO.getEmail())) {
                throw new IllegalArgumentException(String.format(Constants.EMAIL_ALREADY_EXIST, userDTO.getEmail()));
            }
            ModelsMapper.editUser(userToEdit, userDTO);
            session.getTransaction().commit();
        }
    }

    @Override
    public User getByUsername(String username) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query<User> query = session.createQuery("from User where username = :username", User.class);
            query.setParameter("username", username);
            List<User> users = query.list();
            session.getTransaction().commit();
            if (!users.isEmpty()) {
                return users.get(0);
            } else {
                return null;
            }
        }
    }

    @Override
    public User getById(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(User.class, id);
    }

    @Override
    public void createUser(CreateUserDTO userDTO) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            if (!isEmailExist(userDTO.getEmail()).isEmpty()) {
                throw new IllegalArgumentException(String.format(Constants.EMAIL_ALREADY_EXIST, userDTO.getEmail()));
            }
            User user = ModelsMapper.createUser(userDTO);
            session.save(user);
            session.getTransaction().commit();
        }
    }

    private List<User> isEmailExist(String email) {
        Session session = sessionFactory.getCurrentSession();
        Query<User> query = session.createQuery("from User where email = :email", User.class);
        query.setParameter("email", email);
        return query.list();
    }
}
