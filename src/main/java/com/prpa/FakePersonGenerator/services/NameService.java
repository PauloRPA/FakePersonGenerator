package com.prpa.FakePersonGenerator.services;

import com.prpa.FakePersonGenerator.model.Name;
import com.prpa.FakePersonGenerator.model.Region;
import com.prpa.FakePersonGenerator.model.dtos.NameDto;
import com.prpa.FakePersonGenerator.model.enums.Gender;
import com.prpa.FakePersonGenerator.model.exceptions.GenericResourceNotFoundException;
import com.prpa.FakePersonGenerator.model.exceptions.AlreadyExistsException;
import com.prpa.FakePersonGenerator.repository.NameRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;
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
        Optional<Name> nameById = nameRepository.findById(nameId);
        Name nameFound = nameById.orElseThrow(() ->
                new GenericResourceNotFoundException("Name %d not found.".formatted(nameId)));

        String nameToSave = Objects.requireNonNullElse(newName.getName(), nameFound.getName());
        Gender genderToSave = Objects.requireNonNullElse(newName.getGender(), nameFound.getGender());

        Long regionIdToLookup = Objects.requireNonNullElse(newName.getRegionId(), nameFound.getRegion().getId());
        Region regionToSave = regionService.findById(regionIdToLookup).orElseThrow(() ->
                new GenericResourceNotFoundException("Region with %d id not found.".formatted(newName.getRegionId())));

        nameFound.setName(nameToSave);
        nameFound.setRegion(regionToSave);
        nameFound.setGender(genderToSave);

        Optional<Name> queryToSameValue =
                nameRepository.findByNameAndGenderAndRegion(nameFound.getName(), nameFound.getGender(), nameFound.getRegion());
        final boolean isPersistingSameName = nameId.equals(queryToSameValue.map(Name::getId).orElse(Long.MIN_VALUE));

        if (isPersistingSameName)
            return nameFound;
        else if (queryToSameValue.isPresent()) {
            throw new AlreadyExistsException("name", nameFound.getName());
        }

        return nameRepository.save(nameFound);
    }

    public boolean remove(Long id) {
        Optional<Name> found = nameRepository.findById(id);
        if (found.isEmpty()) return false;

        nameRepository.deleteById(found.get().getId());
        return true;
    }

    public Name save(NameDto nameDto) {
        Optional<Region> regionById = regionService.findById(nameDto.getRegionId());
        Region regionFound = regionById.orElseThrow(() ->
                new GenericResourceNotFoundException("Region with id %d not found".formatted(nameDto.getRegionId())));

        if (nameRepository.existsByNameAndGenderAndRegion(nameDto.getName(), nameDto.getGender(), regionFound)) {
            throw new AlreadyExistsException("name", nameDto.getName());
        }

        Name newName = Name.builder()
                .name(nameDto.getName())
                .gender(nameDto.getGender())
                .region(regionFound)
                .build();

        return nameRepository.save(newName);
    }
}
