package com.prpa.FakePersonGenerator.model.exceptions;

public class RegionExistsException extends RuntimeException {

    private String region;

    public RegionExistsException(String region) {
        this.region = region;
    }

    public String getRegionName() {
        return this.region;
    }
}
