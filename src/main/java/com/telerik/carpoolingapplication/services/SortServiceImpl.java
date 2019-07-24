package com.telerik.carpoolingapplication.services;

import com.telerik.carpoolingapplication.repositories.SortRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SortServiceImpl implements SortService{
    private SortRepository sortRepository;

    @Autowired
    public SortServiceImpl(SortRepository sortRepository) {
        this.sortRepository = sortRepository;
    }
}
