package com.prpa.FakePersonGenerator.services;

import com.prpa.FakePersonGenerator.model.Lastname;
import com.prpa.FakePersonGenerator.model.Name;
import com.prpa.FakePersonGenerator.model.Person;
import com.prpa.FakePersonGenerator.model.Region;
import com.prpa.FakePersonGenerator.model.enums.Gender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class PersonGeneratorService {

    private static final int DEFAULT_MIN_AGE = 5;
    private static final int DEFAULT_MAX_AGE = 100;

    private final GeneratorService generatorService;

    @Autowired
    public PersonGeneratorService(GeneratorService generatorService) {
        this.generatorService = generatorService;
    }

    public Person generate(String seed) {
        generatorService.setSeed(seed);

        Region randomRegion = generatorService.getRandomReferencedRegion();
        Gender randomGender = generatorService.getRandomGender();

        LocalDate referenceDateForAge = LocalDate.now();
        return generate(randomRegion, randomGender, DEFAULT_MIN_AGE, DEFAULT_MAX_AGE, referenceDateForAge);
    }

    private Person generate(Region region, Gender gender, int minAge, int maxAge, LocalDate reference) {
        final Name name = generatorService.getRandomName(region, gender);
        final Lastname lastname = generatorService.getRandomLastname(region);
        final LocalDate birthDate = generatorService.generateRandomBirth(reference, minAge, maxAge);
        final Optional<String> picture = generatorService.findOrFetchPicture(name, lastname);

        return Person.builder()
                .name(name.getName())
                .lastname(lastname.getLastname())
                .birth(birthDate)
                .gender(gender)
                .region(region.getName())
                .profilePic(picture.orElse(""))
                .build();
    }

}
