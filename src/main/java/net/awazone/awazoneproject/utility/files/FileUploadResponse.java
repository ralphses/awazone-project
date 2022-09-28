package net.awazone.awazoneproject.utility.files;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.core.io.Resource;

@Data
@AllArgsConstructor
public class FileUploadResponse {
    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;
    private Resource resource;
}
