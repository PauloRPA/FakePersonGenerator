package com.prpa.FakePersonGenerator.services;

import com.prpa.FakePersonGenerator.model.Lastname;
import com.prpa.FakePersonGenerator.model.Name;
import com.prpa.FakePersonGenerator.model.Region;
import com.prpa.FakePersonGenerator.model.enums.Gender;
import com.prpa.FakePersonGenerator.model.exceptions.EmptyDatabaseException;
import com.prpa.FakePersonGenerator.repository.LastnameRepository;
import com.prpa.FakePersonGenerator.repository.NameRepository;
import com.prpa.FakePersonGenerator.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequestScope
public class GeneratorService {

    private final NameRepository nameRepository;
    private final LastnameRepository lastnameRepository;
    private final RegionRepository regionRepository;
    private final PictureService pictureService;
    private final RegionService regionService;
    private final Random random;

    @Autowired
    public GeneratorService(NameRepository nameRepository, LastnameRepository lastnameRepository, RegionRepository regionRepository, PictureService pictureService, RegionService regionService) {
        this.nameRepository = nameRepository;
        this.lastnameRepository = lastnameRepository;
        this.regionRepository = regionRepository;
        this.pictureService = pictureService;
        this.regionService = regionService;
        random = new Random();
    }

    public Name getRandomName(Region region, Gender gender) {
        final int bound = (int) nameRepository.countByRegionAndGender(region, gender);

        if (bound < 1)
            throw new EmptyDatabaseException(
                    "No name for region %s found. Insert at least one Name of each gender to use this endpoint."
                            .formatted(region.getName()));

        Pageable randomPage = Pageable.ofSize(1).withPage(random.nextInt(bound));
        Page<Name> pageFound = nameRepository.findByRegionAndGender(randomPage, region, gender);

        return getValueFromPage(pageFound);
    }

    public Lastname getRandomLastname(Region region) {
        final int bound = (int) lastnameRepository.countByRegion(region);

        if (bound < 1)
            throw new EmptyDatabaseException("The Lastname database is empty. Insert at least one Lastname to use this endpoint.");

        Pageable randomPage = Pageable.ofSize(1).withPage(random.nextInt(bound));
        Page<Lastname> pageFound = lastnameRepository.findByRegion(randomPage, region);

        return getValueFromPage(pageFound);
    }

    public Region getRandomRegion() {
        final int regionCount = (int) regionService.count();

        if (regionCount < 1)
            throw new EmptyDatabaseException("The region table is empty. Insert at least one Region to use this endpoint.");

        final int randomIndex = random.nextInt(regionCount);
        Page<Region> pageFound = regionService.findAll(Pageable.ofSize(1).withPage(randomIndex));

        return pageFound.getContent().get(0);
    }

    public Region getRandomReferencedRegion() {
        final int regionCount = (int) regionService.count();

        if (regionCount < 1)
            throw new EmptyDatabaseException("The region table is empty. Insert at least one Region to use this endpoint.");

        List<Region> validRegion = regionRepository.findRegionsWithMoreThanOneNameAndLastname();
        if (validRegion.isEmpty())
            throw new EmptyDatabaseException("""
                    There are no regions with at least two names (one of each gender) and a lastname referencing it.
                    Insert at least one Lastname for a region and two names (MALE AND FEMALE) referencing it to use this endpoint.
                    """);

        final int randomIndex = random.nextInt(validRegion.size());
        return validRegion.get(randomIndex);
    }

    public Region getRandomNameReferencedRegion() {
        final int regionCount = (int) regionService.count();

        if (regionCount < 1)
            throw new EmptyDatabaseException("The region table is empty. Insert at least one Region to use this endpoint.");

        List<Region> validRegion = regionRepository.findRegionsWithMoreThanOneName();
        if (validRegion.isEmpty())
            throw new EmptyDatabaseException("""
                    There are no regions with at least two names (one of each gender) referencing it.
                    Insert at least two names (MALE AND FEMALE) referencing it to use this endpoint.
                    """);

        final int randomIndex = random.nextInt(validRegion.size());
        return validRegion.get(randomIndex);
    }

    public Region getRandomLastnameReferencedRegion() {
        final int regionCount = (int) regionService.count();

        if (regionCount < 1)
            throw new EmptyDatabaseException("The region table is empty. Insert at least one Region to use this endpoint.");

        List<Region> validRegion = regionRepository.findRegionsWithMoreThanOneLastname();
        if (validRegion.isEmpty())
            throw new EmptyDatabaseException("""
                    There are no regions with at least one lastname referencing it.
                    Insert at least one lastname referencing it to use this endpoint.
                    """);

        final int randomIndex = random.nextInt(validRegion.size());
        return validRegion.get(randomIndex);
    }

    public Gender getRandomGender() {
        Gender[] genders = Gender.values();
        return genders[getRandom().nextInt(genders.length) % genders.length];
    }

    private <T> T getValueFromPage(Page<T> page) {
        return page.getContent().get(0);
    }

    public LocalDate generateRandomBirth(LocalDate reference, int minAge, int maxAge) {
        if (maxAge < minAge) throw new IllegalArgumentException("Min age cannot be greater than maxAge.");
        final long birthYearOfTheOlder = reference.minusYears(maxAge).toEpochDay();
        final long birthYearOfTheYounger = reference.minusYears(minAge).toEpochDay();
        final long randomTime = random.nextLong(birthYearOfTheOlder, birthYearOfTheYounger);

        return LocalDate.ofEpochDay(randomTime);
    }

    public Optional<String> findOrFetchPicture(Name name, Lastname lastname) {
        return pictureService.findOrFetchBySeed(name.getName() + lastname.getLastname());
    }

    public void setSeed(long seed) {
        this.random.setSeed(seed);
    }

    private Random getRandom() {
        return this.random;
    }
}
