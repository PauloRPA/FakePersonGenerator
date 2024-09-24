package com.prpa.FakePersonGenerator.model.dtos;


import com.prpa.FakePersonGenerator.model.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class NameDto {

    @NotBlank
    private String name;

    @NotNull
    private Long regionId;

    @NotNull
    private Gender gender;

}