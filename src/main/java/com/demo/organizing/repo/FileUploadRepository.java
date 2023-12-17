package com.demo.organizing.repo;

import com.demo.organizing.entities.Person;
import com.demo.organizing.entities.UploadedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileUploadRepository extends JpaRepository<UploadedFile,Long> {
    List<UploadedFile> findAllByPerson(Person person);
}