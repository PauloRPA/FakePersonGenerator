package com.prpa.FakePersonGenerator.repository;

import com.prpa.FakePersonGenerator.model.Lastname;
import com.prpa.FakePersonGenerator.model.Region;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LastnameRepository extends JpaRepository<Lastname, Long> {
    Page<Lastname> findByRegion(Pageable randomPage, Region region);

    long countByRegion(Region region);
}
