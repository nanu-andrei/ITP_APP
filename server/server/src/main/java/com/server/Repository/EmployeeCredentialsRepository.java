package com.server.Repository;

import com.server.Entity.EmployeeCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeCredentialsRepository extends JpaRepository<EmployeeCredentials, Long> {


    Logger logger = LoggerFactory.getLogger(EmployeeCredentialsRepository.class);

//    @Query("SELECT u FROM EmployeeCredentials u WHERE LOWER(u.username) = LOWER(?1)")
    EmployeeCredentials findByUsername(String username);


}
