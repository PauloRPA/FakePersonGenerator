package com.prpa.FakePersonGenerator.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity @Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "lastname")
public class Lastname {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String lastname;

    @ManyToOne(optional = false)
    @JoinColumn(name = "region_id", referencedColumnName = "id")
    private Region region;

}
