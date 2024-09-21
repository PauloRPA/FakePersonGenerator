package com.prpa.FakePersonGenerator.model;

import com.prpa.FakePersonGenerator.model.enums.ImageFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Entity @Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@Table(name = "picture")
public class Picture {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    private String seed;

    @NotNull
    private ImageFormat format;

    @Lob
    @NotNull
    @Basic(fetch = FetchType.LAZY)
    private byte[] imageData;
}
