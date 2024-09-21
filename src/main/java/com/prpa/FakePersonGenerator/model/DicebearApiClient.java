package com.prpa.FakePersonGenerator.model;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Component
@FeignClient(name = "dicebear", url = "${dicebear.url}")
public interface DicebearApiClient extends ProfileImageApi {

    @RequestMapping(method = RequestMethod.GET, value = "${dicebear.style}${dicebear.format}?seed={seed}")
    byte[] fetchPicture(@PathVariable("seed") String seed);
}
