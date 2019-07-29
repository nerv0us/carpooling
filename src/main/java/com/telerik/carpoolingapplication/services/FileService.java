package com.telerik.carpoolingapplication.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    void storeFile(int userId, MultipartFile file) throws IOException;
}
