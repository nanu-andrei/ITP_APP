package com.server.Repository;

import com.server.Entity.Firm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FirmRepository extends JpaRepository<Firm, Long> {

    @Query("SELECT f FROM Firm f WHERE LOWER(TRIM(f.firmName)) = LOWER(TRIM(:firmName))")
    Firm findByFirmName(@Param("firmName") String firmName);

}
