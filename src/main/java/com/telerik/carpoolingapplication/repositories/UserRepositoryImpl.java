package com.telerik.carpoolingapplication.repositories;

import com.telerik.carpoolingapplication.models.CreateUserDTO;
import com.telerik.carpoolingapplication.models.ModelsMapper;
import com.telerik.carpoolingapplication.models.UserDTO;
import com.telerik.carpoolingapplication.models.constants.Messages;
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
            UserDTO userToEdit = session.get(UserDTO.class, userDTO.getId());
            if (isEmailExist(userDTO.getEmail())) {
                throw new IllegalArgumentException(String.format(Messages.EMAIL_ALREADY_EXIST, userDTO.getEmail()));
            }
            ModelsMapper.editUser(userToEdit, userDTO);
            session.getTransaction().commit();
        }
    }

    @Override
    public UserDTO getByUsername(String username) {
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
    public UserDTO getById(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(UserDTO.class, id);
    }

    @Override
    public void createUser(CreateUserDTO userDTO) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            if (isEmailExist(userDTO.getEmail())) {
                throw new IllegalArgumentException(String.format(Messages.EMAIL_ALREADY_EXIST, userDTO.getEmail()));
            }
            UserDTO user = ModelsMapper.createUser(userDTO);
            session.save(user);
            session.getTransaction().commit();
        }
    }

    private boolean isEmailExist(String email) {
        Session session = sessionFactory.getCurrentSession();
        Query<UserDTO> query = session.createQuery("from UserDTO where email = :email", UserDTO.class);
        query.setParameter("email", email);
        return !query.list().isEmpty();
    }
}
