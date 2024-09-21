package com.prpa.FakePersonGenerator.services;

import com.prpa.FakePersonGenerator.controller.PersonController;
import com.prpa.FakePersonGenerator.model.Picture;
import com.prpa.FakePersonGenerator.model.ProfileImageApi;
import com.prpa.FakePersonGenerator.model.enums.ImageFormat;
import com.prpa.FakePersonGenerator.repository.PictureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PictureService {

    private static final String PROFILE_BY_ID_ENDPOINT = PersonController.PROFILE_ENDPOINT;

    private final PictureRepository pictureRepository;
    private final ProfileImageApi apiClient;

    @Autowired
    public PictureService(PictureRepository pictureRepository, ProfileImageApi imageApi) {
        this.pictureRepository = pictureRepository;
        this.apiClient = imageApi;
    }

    public Optional<Picture> findById(String id) {
        return pictureRepository.findById(UUID.fromString(id));
    }

    public Optional<String> findOrFetchBySeed(String seed) {
        Optional<Picture> found = pictureRepository.findBySeed(seed);

        if (found.isEmpty()) {
            final byte[] image = apiClient.fetchPicture(seed);
            Picture newPic = Picture.builder()
                    .imageData(image)
                    .format(ImageFormat.SVG)
                    .seed(seed)
                    .build();
            found = Optional.of(save(newPic));
        }

        String id = found.get().getId().toString();
        return Optional.of((PROFILE_BY_ID_ENDPOINT + "%s").formatted(id));
    }

    private Picture save(Picture newPic) {
        return pictureRepository.save(newPic);
    }
}
