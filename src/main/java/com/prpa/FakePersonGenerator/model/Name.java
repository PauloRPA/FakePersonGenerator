package com.prpa.FakePersonGenerator.model;

import com.prpa.FakePersonGenerator.model.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity @Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@Table(name = "name")
public class Name {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @ManyToOne
    @JoinColumn(name = "region_id", referencedColumnName = "id")
    private Region region;

    @Enumerated(EnumType.STRING)
    private Gender gender;

}
