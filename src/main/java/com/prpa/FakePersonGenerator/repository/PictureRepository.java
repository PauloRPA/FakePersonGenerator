package com.prpa.FakePersonGenerator.repository;

import com.prpa.FakePersonGenerator.model.Picture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PictureRepository extends JpaRepository<Picture, Long> {
    Optional<Picture> findById(UUID id);

    Optional<Picture> findBySeed(String seed);
}
