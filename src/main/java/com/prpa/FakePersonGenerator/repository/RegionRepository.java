package com.prpa.FakePersonGenerator.repository;

import com.prpa.FakePersonGenerator.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepository extends JpaRepository<Region, Long> {

    boolean existsByName(String name);

}
