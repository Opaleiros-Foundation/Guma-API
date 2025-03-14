package university.jala.gumaapi.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileProcessingService {
    String processFile(MultipartFile file);
}
