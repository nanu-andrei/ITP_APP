package com.server.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user_info")
public class UserData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String firstName;
    public String lastName;
    public String dateOfBirth;
    public String cnp;
    public String gender;
    @OneToOne
    @JoinColumn(name= "credential_id" , referencedColumnName = "id")
    @JsonBackReference
    private UserCredentials credentials;
    @OneToMany(mappedBy = "userData")
    @JsonManagedReference(value = "user-requests")
    private List<Request> requests = new ArrayList<>();
    @JsonCreator
    public UserData(@JsonProperty("id") Long id,
                    @JsonProperty("firstName") String firstName,
                    @JsonProperty("lastName") String lastName,
                    @JsonProperty("dateOfBirth") String dateOfBirth,
                    @JsonProperty("cnp") String cnp,
                    @JsonProperty("gender") String gender,
                    @JsonProperty("credentials") UserCredentials credentials) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.cnp = cnp;
        this.gender = gender;
        this.credentials = credentials;
    }
}
