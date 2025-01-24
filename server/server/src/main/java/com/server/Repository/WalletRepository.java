package com.server.Repository;

import com.server.Entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.web3j.crypto.ECKeyPair;

@Repository
public interface WalletRepository extends JpaRepository<Wallet,Long> {
    Wallet findByEmployeeCredentialsId(Long employeeId);

    Wallet findByAddress(String admin);
}
