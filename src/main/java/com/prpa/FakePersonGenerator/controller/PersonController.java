package com.prpa.FakePersonGenerator.controller;

import com.prpa.FakePersonGenerator.model.Person;
import com.prpa.FakePersonGenerator.model.Picture;
import com.prpa.FakePersonGenerator.model.exceptions.GenericResourceNotFoundException;
import com.prpa.FakePersonGenerator.services.PersonGeneratorService;
import com.prpa.FakePersonGenerator.services.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/person")
public class PersonController {

    public static final String PROFILE_ENDPOINT = "/api/v1/person/profile/";
    private final PersonGeneratorService generatorService;
    private final PictureService pictureService;

    @Autowired
    public PersonController(PersonGeneratorService generatorService, PictureService pictureService) {
        this.generatorService = generatorService;
        this.pictureService = pictureService;
    }

    @GetMapping("/generate")
    public ResponseEntity<Person> getGeneratePerson(@RequestParam(value = "seed", required = false) String seed) {
        long seedNumber = new Random().nextLong();
        if (seed != null && !seed.isBlank())
            seedNumber = seed.chars().mapToLong(ch -> (long) ch).sum() + seed.hashCode();

        return ResponseEntity.ok(generatorService.generate(seedNumber));
    }

    @GetMapping(value = "/profile/{pictureId}", produces = "image/svg+xml")
    public byte[] getPersonProfilePicture(@PathVariable("pictureId") UUID id) {
        Optional<Picture> pictureFound = pictureService.findById(id.toString());
        if (pictureFound.isEmpty())
            throw new GenericResourceNotFoundException("An image with the %s id was not found.".formatted(id));
        return pictureFound.get().getImageData();
    }

}
