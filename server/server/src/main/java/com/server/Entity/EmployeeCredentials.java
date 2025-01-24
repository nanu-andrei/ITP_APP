package com.server.Entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name ="employee_credentials")
public class EmployeeCredentials {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "username")
    public String username;

    @OneToOne(mappedBy = "employeeCredentials", cascade = CascadeType.ALL)
    @JsonManagedReference
    public Wallet wallet;

    @Enumerated(EnumType.STRING)
    public Role role;

    public enum Role{ADMIN, INSPECTOR};

    @ManyToOne()
    @JoinColumn(name ="firm_id")
    @JsonBackReference
    public Firm firm;


}
