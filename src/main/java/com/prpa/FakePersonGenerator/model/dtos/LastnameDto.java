package com.prpa.FakePersonGenerator.model.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class LastnameDto {

    @NotBlank
    private String lastname;

    @NotNull
    private Long regionId;

}
