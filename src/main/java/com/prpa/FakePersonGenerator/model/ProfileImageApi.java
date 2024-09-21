package com.prpa.FakePersonGenerator.model;

import org.springframework.web.bind.annotation.PathVariable;

public interface ProfileImageApi {

    byte[] fetchPicture(@PathVariable("seed") String seed);

}
