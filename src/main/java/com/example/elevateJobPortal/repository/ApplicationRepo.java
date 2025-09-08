package com.example.elevateJobPortal.repository;

import com.example.elevateJobPortal.entity.Application;
import com.example.elevateJobPortal.entity.Job;
import com.example.elevateJobPortal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepo extends JpaRepository<Application, Long> {

    List<Application> findByApplicant(User applicant);
    List<Application> findByJob(Job job);

}