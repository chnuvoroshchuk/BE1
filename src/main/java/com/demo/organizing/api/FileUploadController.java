package com.demo.organizing.api;

import com.demo.organizing.entities.UploadedFile;
import com.demo.organizing.enums.FileUploadResponse;
import com.demo.organizing.service.FileUploadService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("api")
public class FileUploadController {
    private final FileUploadService fileUploadService;

    public FileUploadController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @PostMapping(value = "/upload/db", headers = "content-type=multipart/*")
    public FileUploadResponse uploadDb(@RequestParam("file")MultipartFile multipartFile, @RequestParam(name = "username") String username) {
        UploadedFile uploadedFile = fileUploadService.uploadToDb(multipartFile, username);
        FileUploadResponse response = new FileUploadResponse();
        if(uploadedFile!=null){
            String downloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/download/")
                    .path(String.valueOf(uploadedFile.getId()))
                    .toUriString();
            response.setDownloadUri(downloadUri);
            response.setId(uploadedFile.getId());
            response.setType(uploadedFile.getType());
            response.setUploadStatus(true);
            response.setMessage("File Uploaded Successfully!");
            return response;

        }
        response.setMessage("Oops 1 something went wrong please re-upload.");
        return response;
    }
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {
        UploadedFile uploadedFileToRet =  fileUploadService.downloadFile(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(uploadedFileToRet.getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename= "+uploadedFileToRet.getName())
                .body(new ByteArrayResource(uploadedFileToRet.getFileData()));
    }
    @GetMapping("/download/all")
    public ResponseEntity<List<UploadedFile>> findAll(@RequestParam(name = "username") String username) {
        return ResponseEntity.ok().body(fileUploadService.getAllDocuments(username));
    }

    @DeleteMapping("/document/{id}")
    public ResponseEntity<?> deleteDocumentById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok().body(
                fileUploadService.deleteDocumentById(id)
                        ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED)
        );
    }
}
