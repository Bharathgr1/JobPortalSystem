package com.example.elevateJobPortal.repository;

import com.example.elevateJobPortal.entity.Job;
import com.example.elevateJobPortal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepo extends JpaRepository<Job, Long> {
    List<Job> findByEmployer(User employer);

}