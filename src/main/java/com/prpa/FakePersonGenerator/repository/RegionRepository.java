package com.prpa.FakePersonGenerator.repository;

import com.prpa.FakePersonGenerator.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RegionRepository extends JpaRepository<Region, Long> {

    boolean existsByName(String name);

    @Query("select r from Region r inner join Name n on r.id = n.region.id group by r.id having count(distinct n.gender) > 1")
    List<Region> findRegionsWithMoreThanOneName();

    @Query("select r from Region r inner join Lastname n on n.region.id = r.id group by n.region.id")
    List<Region> findRegionsWithMoreThanOneLastname();

    @Query("select r from Region r inner join Name n on n.region.id = r.id inner join Lastname l on l.region.id = r.id group by n.region.id, l.region.id having count(distinct n.gender) > 1")
    List<Region> findRegionsWithMoreThanOneNameAndLastname();
}
