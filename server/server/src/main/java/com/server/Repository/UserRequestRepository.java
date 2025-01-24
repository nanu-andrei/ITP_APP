package com.server.Repository;

import com.server.Entity.Request;
import com.server.Entity.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRequestRepository extends JpaRepository<Request,Long> {

    List<Request> findByFirmFirmName(String firmName);

    List<Request> findByUserData(UserData userData);

    Request findByDetailsVin(String vin);
    List<Request> findAllByDetailsVin(String vin);
}
