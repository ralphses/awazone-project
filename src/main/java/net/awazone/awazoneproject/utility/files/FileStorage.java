package net.awazone.awazoneproject.utility.files;

import lombok.extern.slf4j.Slf4j;
import net.awazone.awazoneproject.exception.FileStorageException;
import net.awazone.awazoneproject.exception.ResourceNotFoundException;
import net.awazone.awazoneproject.model.user.kyc.KycDocument;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static java.nio.file.Files.createDirectories;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

@Component
@Slf4j
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

    public String storeFile(MultipartFile file, String fileName) {

        boolean isFileValid = file.isEmpty() ||
                file.getSize() == 0 ||
                !(Objects.requireNonNull(file.getContentType()).equalsIgnoreCase(IMAGE_JPEG_VALUE) ||
                        Objects.requireNonNull(file.getContentType()).equalsIgnoreCase(IMAGE_PNG_VALUE));

        if(isFileValid) {
            throw new FileStorageException("Invalid or empty file");
        }


        try {
            if(fileName.contains("..")) {
                throw new FileStorageException("Invalid File name");
            }
            Path fileStorageLocation = fileLocation.resolve(fileName);
            Files.copy(file.getInputStream(), fileStorageLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;

        } catch (IOException e) {
            throw new FileStorageException("Error uploading file to server");
        }
    }

    private Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = fileLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if(resource.exists()) {
                return resource;
            }
            else throw new ResourceNotFoundException("File not found " + fileName);
        } catch (MalformedURLException e) {
            throw new ResourceNotFoundException("File not found " + fileName);
        }
    }

    public void deleteFile(String fileName) throws IOException {
        Path location = Paths.get(fileStore.getUploadDir()+"/"+fileName);
        Files.deleteIfExists(location);
    }

    public FileUploadResponse getFileUploadResponse(HttpServletRequest httpServletRequest, String documentName) {

        Resource documentResource = loadFileAsResource(documentName);

        try {
            AtomicReference<String> contentType = new AtomicReference<>(
                    httpServletRequest.getServletContext().getMimeType(documentResource.getFile().getAbsolutePath()));

            if(contentType.get() == null) contentType.set("application/octet-stream");

            return new FileUploadResponse(documentResource.getFilename(), "", contentType.get(), 0, documentResource);

        } catch (IOException e) {
            log.info("Could not determine file type");
            throw new ResourceNotFoundException("Could not determine file type");
        }
    }
}
