package com.prpa.FakePersonGenerator.services;

import com.prpa.FakePersonGenerator.model.Name;
import com.prpa.FakePersonGenerator.model.Region;
import com.prpa.FakePersonGenerator.model.dtos.NameDto;
import com.prpa.FakePersonGenerator.model.exceptions.GenericResourceNotFoundException;
import com.prpa.FakePersonGenerator.model.exceptions.AlreadyExistsException;
import com.prpa.FakePersonGenerator.repository.NameRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NameService {

    private final NameRepository nameRepository;
    private final RegionService regionService;

    public NameService(NameRepository nameRepository, RegionService regionService) {
        this.nameRepository = nameRepository;
        this.regionService = regionService;
    }

    public Page<Name> findAll(Pageable pageRequest) {
        return nameRepository.findAll(pageRequest);
    }

    public Optional<Name> findById(Long id) {
        return nameRepository.findById(id);
    }

    public Name update(Long nameId, NameDto newName) {
        Optional<Name> nameFound = nameRepository.findById(nameId);
        Optional<Region> regionFound = regionService.findById(newName.getRegionId());

        if (nameFound.isEmpty())
            throw new GenericResourceNotFoundException("Name %d not found.".formatted(nameId));
        else if (regionFound.isEmpty()) {
            throw new GenericResourceNotFoundException("Region with %d id not found.".formatted(newName.getRegionId()));
        }

        Name toSave = nameFound.get();
        toSave.setName(newName.getName());
        toSave.setRegion(regionFound.get());
        toSave.setGender(newName.getGender());

        Optional<Name> persistedNameToSave =
                nameRepository.findByNameAndGenderAndRegion(toSave.getName(), toSave.getGender(), toSave.getRegion());
        final boolean isPersistingSameName = nameId.equals(persistedNameToSave.map(Name::getId).orElse(Long.MIN_VALUE));

        if (isPersistingSameName)
            return nameFound.get();
        else if (persistedNameToSave.isPresent()) {
            throw new AlreadyExistsException("name", toSave.getName());
        }

        return nameRepository.save(toSave);
    }

    public boolean remove(Long id) {
        Optional<Name> found = nameRepository.findById(id);
        if (found.isEmpty()) return false;

        nameRepository.deleteById(found.get().getId());
        return true;
    }

    public Name save(NameDto nameDto) {
        Optional<Region> region = regionService.findById(nameDto.getRegionId());

        if (region.isEmpty()) {
            throw new GenericResourceNotFoundException("Region with id %d not found".formatted(nameDto.getRegionId()));
        }

        if (nameRepository.existsByNameAndGenderAndRegion(nameDto.getName(), nameDto.getGender(), region.get())) {
            throw new AlreadyExistsException("name", nameDto.getName());
        }

        Name newName = Name.builder()
                .name(nameDto.getName())
                .gender(nameDto.getGender())
                .region(region.get())
                .build();

        return nameRepository.save(newName);
    }
}
