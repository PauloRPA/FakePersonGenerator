package com.prpa.FakePersonGenerator.model;

import com.prpa.FakePersonGenerator.model.enums.Gender;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Person
 */
@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class Person {

    @NotBlank
    private String name;
    private String lastname;
    private String region;
    private String profilePic;
    private LocalDate birth;

    @Enumerated
    private Gender gender;

}
