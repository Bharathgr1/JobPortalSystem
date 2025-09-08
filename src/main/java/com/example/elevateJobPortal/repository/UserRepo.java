package com.example.elevateJobPortal.repository;

import com.example.elevateJobPortal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);  // this is needed
}
