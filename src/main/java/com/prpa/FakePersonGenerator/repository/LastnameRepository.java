package com.prpa.FakePersonGenerator.repository;

import com.prpa.FakePersonGenerator.model.Lastname;
import com.prpa.FakePersonGenerator.model.Region;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LastnameRepository extends JpaRepository<Lastname, Long> {
    Page<Lastname> findByRegion(Pageable randomPage, Region region);

    long countByRegion(Region region);

    Optional<Lastname> findByLastnameAndRegion(String lastname, Region region);

    boolean existsByLastnameAndRegion(String lastname, Region region);
}
