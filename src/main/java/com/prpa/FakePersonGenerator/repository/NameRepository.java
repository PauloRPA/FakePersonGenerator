package com.prpa.FakePersonGenerator.repository;

import com.prpa.FakePersonGenerator.model.Name;
import com.prpa.FakePersonGenerator.model.Region;
import com.prpa.FakePersonGenerator.model.enums.Gender;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NameRepository extends JpaRepository<Name, Long> {

    Page<Name> findByRegionAndGender(Pageable randomPage, Region region, Gender gender);

    long countByRegionAndGender(Region region, Gender gender);

    boolean existsByNameAndGenderAndRegion(String name, Gender gender, Region region);
}
