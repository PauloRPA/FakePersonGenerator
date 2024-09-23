package com.prpa.FakePersonGenerator.services;

import com.prpa.FakePersonGenerator.model.Lastname;
import com.prpa.FakePersonGenerator.model.Region;
import com.prpa.FakePersonGenerator.model.dtos.LastnameDto;
import com.prpa.FakePersonGenerator.model.exceptions.AlreadyExistsException;
import com.prpa.FakePersonGenerator.model.exceptions.GenericResourceNotFoundException;
import com.prpa.FakePersonGenerator.repository.LastnameRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class LastnameService {

    private final LastnameRepository lastnameRepository;
    private final RegionService regionService;

    public LastnameService(LastnameRepository lastnameRepository, RegionService regionService) {
        this.lastnameRepository = lastnameRepository;
        this.regionService = regionService;
    }

    public Page<Lastname> findAll(Pageable pageRequest) {
        return lastnameRepository.findAll(pageRequest);
    }

    public Optional<Lastname> findById(Long id) {
        return lastnameRepository.findById(id);
    }

    public Lastname update(Long lastnameId, LastnameDto newLastname) {
        Optional<Lastname> found = lastnameRepository.findById(lastnameId);
        Lastname lastnameFound = found.orElseThrow(() ->
                new GenericResourceNotFoundException("Lastame with %d id not found.".formatted(lastnameId)));

        Long regionId = Objects.requireNonNullElse(newLastname.getRegionId(), lastnameFound.getRegion().getId());
        String lastname = Objects.requireNonNullElse(newLastname.getLastname(), lastnameFound.getLastname());

        Optional<Region> regionFound = regionService.findById(regionId);
        if (regionFound.isEmpty())
            throw new GenericResourceNotFoundException("Region with %d id not found.".formatted(newLastname.getRegionId()));

        lastnameFound.setLastname(lastname);
        lastnameFound.setRegion(regionFound.get());

        Optional<Lastname> persistedNameToSave =
                lastnameRepository.findByLastnameAndRegion(lastnameFound.getLastname(), lastnameFound.getRegion());
        final boolean isPersistingSameName = lastnameId.equals(persistedNameToSave.map(Lastname::getId).orElse(Long.MIN_VALUE));

        if (isPersistingSameName)
            return found.get();
        else if (persistedNameToSave.isPresent()) {
            throw new AlreadyExistsException("name", lastnameFound.getLastname());
        }

        return lastnameRepository.save(lastnameFound);
    }

    public Lastname save(LastnameDto lastnameDto) {
        Optional<Region> region = regionService.findById(lastnameDto.getRegionId());

        if (region.isEmpty()) {
            throw new GenericResourceNotFoundException("Region with id %d not found".formatted(lastnameDto.getRegionId()));
        }

        if (lastnameRepository.existsByLastnameAndRegion(lastnameDto.getLastname(), region.get())) {
            throw new AlreadyExistsException("name", lastnameDto.getLastname());
        }

        Lastname newName = Lastname.builder()
                .lastname(lastnameDto.getLastname())
                .region(region.get())
                .build();

        return lastnameRepository.save(newName);
    }

    public boolean remove(Long id) {
        Optional<Lastname> found = lastnameRepository.findById(id);
        if (found.isEmpty()) return false;

        lastnameRepository.deleteById(found.get().getId());
        return true;
    }
}
