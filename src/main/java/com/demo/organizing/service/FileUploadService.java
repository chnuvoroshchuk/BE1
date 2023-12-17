package com.demo.organizing.service;

import com.demo.organizing.entities.UploadedFile;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface FileUploadService {
    UploadedFile uploadToDb(MultipartFile file, String username);

    UploadedFile downloadFile(Long id);

    List<UploadedFile> getAllDocuments(String username);

    boolean deleteDocumentById(Long id);
}
