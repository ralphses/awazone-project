package net.awazone.awazoneproject.utility.files;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "file")
public class FileStore {
  private String uploadDir;
}
