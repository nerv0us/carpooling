package com.telerik.carpoolingapplication.repositories;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
public class SortRepositoryImpl implements SortRepository {
    private SessionFactory sessionFactory;

    @Autowired
    public SortRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
