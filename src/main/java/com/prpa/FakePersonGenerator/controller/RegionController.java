package com.prpa.FakePersonGenerator.controller;

import com.prpa.FakePersonGenerator.model.Region;
import com.prpa.FakePersonGenerator.model.exceptions.GenericResourceNotFoundException;
import com.prpa.FakePersonGenerator.services.GeneratorService;
import com.prpa.FakePersonGenerator.services.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/region")
public class RegionController {

    private final static String REGION_CONTROLLER_PATH = "/api/v1/region";

    private final RegionService regionService;
    private final GeneratorService generatorService;

    @Autowired
    public RegionController(RegionService regionService, GeneratorService generatorService) {
        this.regionService = regionService;
        this.generatorService = generatorService;
    }

    @GetMapping
    public ResponseEntity<List<String>> getAllRegion() {
        return ResponseEntity.ok(regionService.findAll().stream().map(Region::getName).toList());
    }

    @GetMapping("/random")
    public ResponseEntity<Region> getRandomRegion(@RequestParam(value = "seed", required = false) String seed) {
        generatorService.setSeed(seed);
        Region randomRegion = generatorService.getRandomRegion();
        return ResponseEntity.ok(randomRegion);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Region>> getRegionById(@PathVariable("id") Long id) {
        Optional<Region> found = regionService.findById(id);
        if (found.isEmpty()) {
            throw new GenericResourceNotFoundException("A region with id %d not found".formatted(id));
        }
        return ResponseEntity.ok(found);
    }

    @PutMapping("/{regionId}")
    public ResponseEntity<Optional<Region>> putRegion(
            UriComponentsBuilder uriBuilder,
            @PathVariable("regionId") Long regionId,
            @RequestParam("newRegion") String newRegion
    ) {

        Region saved = regionService.update(regionId, newRegion);

        final URI newResourceLocation = uriBuilder
                .path(REGION_CONTROLLER_PATH)
                .path("/{id}")
                .build(saved.getId());
        return ResponseEntity.created(newResourceLocation).build();
    }

    @PostMapping
    public ResponseEntity<Region> postRegion(@RequestParam("regionName") String region, UriComponentsBuilder uriBuilder) {
        Region saved = regionService.save(region);

        final URI newResourceLocation = uriBuilder
                .path(REGION_CONTROLLER_PATH)
                .path("/{id}")
                .build(saved.getId());
        return ResponseEntity.created(newResourceLocation).body(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Region> deleteRegion(@PathVariable("id") Long id) {
        if (!regionService.remove(id)) {
            throw new GenericResourceNotFoundException("A region with %s id could not be found.".formatted(id));
        }

        return ResponseEntity.noContent().build();
    }


}
