package com.server.Repository;

import com.server.Entity.UserCredentials;
import com.server.Entity.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDataRepository extends JpaRepository<UserData, Long > {

    UserData findByCredentials(UserCredentials credentials);

}
