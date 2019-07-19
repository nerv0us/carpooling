package com.telerik.carpoolingapplication.services;

import com.telerik.carpoolingapplication.models.UserDTO;
import com.telerik.carpoolingapplication.models.constants.Messages;
import com.telerik.carpoolingapplication.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Transactional
@Service
public class FileServiceImpl implements FileService {
    public static final String STORAGE_ROUTE = "/Users/jiwkojelew/Telerik/Final Project/carpooling-application/src/main/resources/static/images/users/";
    private UserRepository userRepository;

    public FileServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void storeFile(int userId, MultipartFile file) throws IOException {
        if (userRepository.getById(userId) == null) {
            throw new IllegalArgumentException(String.format(Messages.USER_NOT_FOUND, userId));
        }
        UserDTO user = userRepository.getById(userId);
        if (!user.getAvatarUri().isEmpty() && !user.getAvatarUri().contains("default")) {
            Files.delete(Paths.get(user.getAvatarUri()));
        }
        String fileName = getFileName(file);
        String filePath = STORAGE_ROUTE + fileName;

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
            userRepository.saveImage(userId, filePath);
        } catch (IOException e) {
            throw new IOException("Failed to store file " + fileName, e);
        }
    }

    private String getFileName(MultipartFile file) {
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        int index = originalFileName.lastIndexOf('.');
        return (UUID.randomUUID()) + originalFileName.substring(index);
    }
}
