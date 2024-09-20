package com.prpa.FakePersonGenerator.services;

import com.prpa.FakePersonGenerator.model.Region;
import com.prpa.FakePersonGenerator.model.exceptions.GenericResourceNotFoundException;
import com.prpa.FakePersonGenerator.model.exceptions.RegionExistsException;
import com.prpa.FakePersonGenerator.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RegionService {

    private final RegionRepository regionRepository;

    @Autowired
    public RegionService(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    public List<Region> findAll() {
        return regionRepository.findAll();
    }

    public Region save(String region) {
        if (regionRepository.existsByName(region)) {
            throw new RegionExistsException(region);
        }

        return regionRepository.save(new Region(null, region));
    }

    public Optional<Region> findById(Long id) {
        return regionRepository.findById(id);
    }

    public boolean remove(Long region) {
        Optional<Region> found = regionRepository.findById(region);
        if (found.isEmpty()) return false;

        regionRepository.deleteById(found.get().getId());
        return true;
    }

    public long count() {
        return regionRepository.count();
    }

    public Region update(Long regionId, String newRegion) {
        Optional<Region> found = regionRepository.findById(regionId);
        if (found.isEmpty())
            throw new GenericResourceNotFoundException("Region %d not found.".formatted(regionId));

        Region toSave = found.get();
        toSave.setName(newRegion);
        return regionRepository.save(toSave);
    }

    public Page<Region> findAll(Pageable page) {
        return regionRepository.findAll(page);
    }
}

