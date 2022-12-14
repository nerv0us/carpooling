package com.telerik.carpoolingapplication.repositories;

import com.telerik.carpoolingapplication.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class FileRepositoryImpl implements FileRepository {
    private SessionFactory sessionFactory;

    @Autowired
    public FileRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void saveImage(int userId, String filePath) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User userToUpdate = session.get(User.class, userId);
            userToUpdate.setAvatarUri(filePath);
            session.update(userToUpdate);
            session.getTransaction().commit();
        }
    }
}
