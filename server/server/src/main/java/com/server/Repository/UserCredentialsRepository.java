package com.server.Repository;
import com.server.Entity.UserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCredentialsRepository extends JpaRepository<UserCredentials ,Long > {
    @Query("SELECT u FROM UserCredentials u WHERE u.username = ?1")
    UserCredentials findByUsername(String username);

}
