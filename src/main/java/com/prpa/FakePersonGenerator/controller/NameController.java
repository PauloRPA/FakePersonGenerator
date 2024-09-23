package com.prpa.FakePersonGenerator.controller;

import com.prpa.FakePersonGenerator.model.Name;
import com.prpa.FakePersonGenerator.model.Region;
import com.prpa.FakePersonGenerator.model.dtos.NameDto;
import com.prpa.FakePersonGenerator.model.enums.Gender;
import com.prpa.FakePersonGenerator.model.exceptions.GenericResourceNotFoundException;
import com.prpa.FakePersonGenerator.services.GeneratorService;
import com.prpa.FakePersonGenerator.services.NameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/name")
public class NameController {

    private final static String REGION_CONTROLLER_PATH = "/api/v1/name";
    private final static Integer DEFAULT_PAGE_SIZE = 20;
    private static final Integer MIN_PAGE_SIZE = 5;
    private static final Integer MAX_PAGE_SIZE = 200;

    private final NameService nameService;
    private final GeneratorService generatorService;

    @Autowired
    public NameController(NameService nameService, GeneratorService generatorService) {
        this.nameService = nameService;
        this.generatorService = generatorService;
    }

    @GetMapping
    public ResponseEntity<Page<Name>> getAllName(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size
    ) {
        page = page == null || page < 0 ? 0 : page;
        size = size == null || size < MIN_PAGE_SIZE ? DEFAULT_PAGE_SIZE : size;
        size = Math.min(MAX_PAGE_SIZE, size);

        Pageable pageRequest = Pageable.ofSize(size).withPage(page);
        return ResponseEntity.ok(nameService.findAll(pageRequest));
    }

    @GetMapping("/random")
    public ResponseEntity<Name> getRandomName(@RequestParam(value = "seed", required = false) String seed) {
        Gender randomGender = generatorService.getRandomGender();
        Region randomRegion = generatorService.getRandomNameReferencedRegion();

        Name randomName = generatorService.getRandomName(randomRegion, randomGender);
        return ResponseEntity.ok(randomName);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Name>> getNameById(@PathVariable("id") Long id) {
        Optional<Name> found = nameService.findById(id);
        if (found.isEmpty()) {
            throw new GenericResourceNotFoundException("A name with id %d not found".formatted(id));
        }
        return ResponseEntity.ok(found);
    }

    @PutMapping("/{nameId}")
    public ResponseEntity<Name> putName(
            UriComponentsBuilder uriBuilder,
            @PathVariable("nameId") Long nameId,
            @RequestBody NameDto newName
    ) {

        Name saved = nameService.update(nameId, newName);

        final URI newResourceLocation = uriBuilder
                .path(REGION_CONTROLLER_PATH)
                .path("/{id}")
                .build(saved.getId());
        return ResponseEntity.created(newResourceLocation).body(saved);
    }

    @PostMapping
    public ResponseEntity<Name> postName(@RequestBody NameDto name, UriComponentsBuilder uriBuilder) {
        Name saved = nameService.save(name);

        final URI newResourceLocation = uriBuilder
                .path(REGION_CONTROLLER_PATH)
                .path("/{id}")
                .build(saved.getId());
        return ResponseEntity.created(newResourceLocation).body(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Name> deleteName(@PathVariable("id") Long id) {
        if (!nameService.remove(id)) {
            throw new GenericResourceNotFoundException("A name with %s id could not be found.".formatted(id));
        }

        return ResponseEntity.noContent().build();
    }


}
