package com.telerik.carpoolingapplication.services;

import com.telerik.carpoolingapplication.models.UserDTO;
import com.telerik.carpoolingapplication.models.constants.Messages;
import com.telerik.carpoolingapplication.repositories.FileRepository;
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
import java.util.Objects;
import java.util.UUID;

@Transactional
@Service
public class FileServiceImpl implements FileService {
    private UserRepository userRepository;
    private FileRepository fileRepository;

    public FileServiceImpl(UserRepository userRepository, FileRepository fileRepository) {
        this.userRepository = userRepository;
        this.fileRepository = fileRepository;
    }

    @Override
    public void storeFile(int userId, MultipartFile file) throws IOException {
        if(file.getSize() > 5242880) {
            throw new IllegalArgumentException("File should be less than 5MB");
        }
        if (isFileFormatInvalid(file)) {
            throw new IllegalArgumentException("Invalid file format");
        }
        if (userRepository.getById(userId) == null) {
            throw new IllegalArgumentException(String.format(Messages.USER_NOT_FOUND, userId));
        }
        UserDTO user = userRepository.getById(userId);
        if (!user.getAvatarUri().isEmpty() && !user.getAvatarUri().contains(Messages.DEFAULT_USER_IMAGE_NAME)) {
            Files.delete(Paths.get(user.getAvatarUri()));
        }

        String fileName = changeFileName(file);
        String filePath = Messages.STORAGE_ROUTE + fileName;

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
            fileRepository.saveImage(userId, filePath);
        } catch (IOException e) {
            throw new IOException("Failed to store file " + fileName, e);
        }
    }

    private boolean isFileFormatInvalid(MultipartFile file) {
        return !Objects.equals(file.getContentType(), "image/jpeg") &&
                !Objects.equals(file.getContentType(), "image/jpg") &&
                !Objects.equals(file.getContentType(), "image/png") &&
                !Objects.equals(file.getContentType(), "image/gif");
    }

    private String changeFileName(MultipartFile file) {
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        int index = originalFileName.lastIndexOf('.');
        return (UUID.randomUUID()) + originalFileName.substring(index);
    }
}
