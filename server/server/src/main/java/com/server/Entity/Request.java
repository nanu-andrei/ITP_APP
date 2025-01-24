package com.server.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user_request")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne
    @JoinColumn(name = "firm_id")
    @JsonBackReference(value = "firm-requests")
    public Firm firm;

    @ManyToOne
    @JoinColumn(name = "inspector_id")
    public EmployeeCredentials inspector;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "user_data_id")
    @JsonBackReference(value = "user-requests")
    private UserData userData;

    @Embedded
    public Details details;

    public String status;

    @Embeddable
    @Getter
    @Setter
    public static class Details
    {
        private String vin;
        public String manufacturer;
        public String model;
        public String productionYear;
        public String fuelType;
        public String plateNumber;
        public String color;

    }

}
