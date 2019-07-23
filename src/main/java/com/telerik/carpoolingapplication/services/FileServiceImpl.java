package com.telerik.carpoolingapplication.services;

import com.telerik.carpoolingapplication.models.User;
import com.telerik.carpoolingapplication.models.constants.Constants;
import com.telerik.carpoolingapplication.repositories.FileRepository;
import com.telerik.carpoolingapplication.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
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
        if (file.getSize() > Constants.MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File should be less than 5MB");
        }
        if (isFileFormatInvalid(file)) {
            throw new IllegalArgumentException("Invalid file format");
        }
        if (userRepository.getById(userId) == null) {
            throw new IllegalArgumentException(String.format(Constants.USER_NOT_FOUND, userId));
        }
        User user = userRepository.getById(userId);
        if (!user.getAvatarUri().isEmpty() && !user.getAvatarUri().contains(Constants.DEFAULT_USER_IMAGE_NAME)) {
            Files.delete(Paths.get(user.getAvatarUri()));
        }

        Path currentRelativePath = Paths.get("");
        String projectPath = currentRelativePath.normalize().toAbsolutePath().getParent().toString();
        String imageDirectory = getImageDirectory(projectPath);

        String fileName = changeFileName(file);
        String filePath = imageDirectory + fileName;

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

    private String getImageDirectory(String projectPath) {
        String imageDirectory = projectPath + Constants.STORAGE_ROUTE;

        File dir = new File(imageDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        return imageDirectory;
    }

    private String changeFileName(MultipartFile file) {
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        int index = originalFileName.lastIndexOf('.');
        return (UUID.randomUUID()) + originalFileName.substring(index);
    }
}
