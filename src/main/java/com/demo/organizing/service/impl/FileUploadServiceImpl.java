package com.demo.organizing.service.impl;

import com.demo.organizing.entities.Person;
import com.demo.organizing.entities.UploadedFile;
import com.demo.organizing.repo.FileUploadRepository;
import com.demo.organizing.repo.PersonRepository;
import com.demo.organizing.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {
    private final PersonRepository personRepository;
    private final FileUploadRepository fileUploadRepository;

    @Override
    public UploadedFile uploadToDb(MultipartFile file, String username) {
        Person person = personRepository.findByUsername(username);
        UploadedFile uploadedFile = new UploadedFile();
        uploadedFile.setPerson(person);
        try {
            uploadedFile.setFileData(file.getBytes());
            uploadedFile.setType(file.getContentType());
            uploadedFile.setName(file.getOriginalFilename());
            return fileUploadRepository.save(uploadedFile);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public UploadedFile downloadFile(Long id) {
        return fileUploadRepository.getOne(id);
    }

    @Override
    public List<UploadedFile> getAllDocuments(String username) {
        Person person = personRepository.findByUsername(username);
        List<UploadedFile> uploadedFileList = fileUploadRepository.findAllByPerson(person);
        log.info("Fetching all documents");
        return uploadedFileList;
    }

    @Override
    public boolean deleteDocumentById(Long id) {
        fileUploadRepository.deleteById(id);
        return false;
    }
}
