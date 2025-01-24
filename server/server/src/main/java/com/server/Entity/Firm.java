package com.server.Entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name= "firm")
public class Firm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String firmName;
    @OneToMany(mappedBy = "firm", cascade = CascadeType.ALL , orphanRemoval = true)
    @JsonManagedReference
    public List<EmployeeCredentials> employeeCredentials;

    @OneToMany(mappedBy = "firm")
    @JsonManagedReference(value = "firm-requests")
    public List<Request> requests;

    @OneToOne(cascade =CascadeType.ALL, optional = true)
    @JoinColumn(name ="primary_admin_id", referencedColumnName = "id", nullable = true)
    public EmployeeCredentials admin;


}
