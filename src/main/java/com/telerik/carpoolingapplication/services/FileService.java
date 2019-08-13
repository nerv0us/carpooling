package com.telerik.carpoolingapplication.services;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface FileService {
    void storeFile(int userId, MultipartFile image, HttpServletRequest request) throws IOException;
}
