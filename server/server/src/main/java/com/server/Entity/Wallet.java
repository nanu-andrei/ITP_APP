package com.server.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;



@Entity
@Getter
@Setter
@Table(name="wallet")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String address;

    @Column(nullable = false)
    private String encryptedPrivateKey;

    @Transient
    private String privateKey;

    @OneToOne
    @JoinColumn(name = "employee_credentials_id")
    @JsonBackReference
    private EmployeeCredentials employeeCredentials;

}
