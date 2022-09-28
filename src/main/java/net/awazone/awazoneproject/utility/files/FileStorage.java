package net.awazone.awazoneproject.utility.files;

import net.awazone.awazoneproject.controller.exception.CustomFileNotFoundException;
import net.awazone.awazoneproject.controller.exception.FileStorageException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

import static java.nio.file.Files.createDirectories;

@Component
public class FileStorage {

    private final Path fileLocation;
    private final FileStore fileStore;

    public FileStorage(FileStore fileLocation) {
        this.fileStore = fileLocation;
        this.fileLocation = Paths.get(fileLocation.getUploadDir()).toAbsolutePath().normalize();
        try {
            createDirectories(this.fileLocation);
        } catch (IOException exception) {
            throw new FileStorageException("Could not create directory");
        }
    }
    public String storeFile(MultipartFile file) {
        String thisFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            if(thisFileName.contains("..")) {
                throw new FileStorageException("Invalid File");
            }
            Path fileStorageLocation = fileLocation.resolve(thisFileName);
            Files.copy(file.getInputStream(), fileStorageLocation, StandardCopyOption.REPLACE_EXISTING);

            return thisFileName;

        } catch (IOException e) {
            throw new FileStorageException("Error uploading file to server");
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = fileLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if(resource.exists()) {
                return resource;
            }
            else throw new CustomFileNotFoundException("File not found " + fileName);
        } catch (MalformedURLException e) {
            throw new CustomFileNotFoundException("File not found " + fileName);
        }
    }

    public void deleteFile(String fileName) throws IOException {
        Path location = Paths.get(fileStore.getUploadDir()+"/"+fileName);
        Files.deleteIfExists(location);
    }
}
