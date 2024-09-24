package com.prpa.FakePersonGenerator.repository;

import com.prpa.FakePersonGenerator.model.Name;
import com.prpa.FakePersonGenerator.model.Region;
import com.prpa.FakePersonGenerator.model.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NameRepository extends JpaRepository<Name, Long> {

    Page<Name> findByRegionAndGender(Pageable randomPage, Region region, Gender gender);

    long countByRegionAndGender(Region region, Gender gender);

    boolean existsByNameAndGenderAndRegion(String name, Gender gender, Region region);

    Optional<Name> findByNameAndGenderAndRegion(@NotBlank String name, Gender gender, Region region);

}
