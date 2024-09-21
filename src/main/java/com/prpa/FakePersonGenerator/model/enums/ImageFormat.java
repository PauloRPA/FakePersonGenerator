package com.prpa.FakePersonGenerator.model.enums;

import lombok.Getter;

@Getter
public enum ImageFormat {
    SVG("image/svg+xml"),
    PNG("image/png"),
    JPEG("image/jpeg");

    private final String mimeType;

    ImageFormat(String mimeType) {
        this.mimeType = mimeType;
    }
}
