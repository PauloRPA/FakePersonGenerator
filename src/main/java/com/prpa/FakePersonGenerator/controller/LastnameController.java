package com.prpa.FakePersonGenerator.controller;

import com.prpa.FakePersonGenerator.model.Lastname;
import com.prpa.FakePersonGenerator.model.Region;
import com.prpa.FakePersonGenerator.model.dtos.LastnameDto;
import com.prpa.FakePersonGenerator.model.enums.Gender;
import com.prpa.FakePersonGenerator.model.exceptions.GenericResourceNotFoundException;
import com.prpa.FakePersonGenerator.services.GeneratorService;
import com.prpa.FakePersonGenerator.services.LastnameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/lastname")
public class LastnameController {

    private final static String REGION_CONTROLLER_PATH = "/api/v1/lastname";
    private final static Integer DEFAULT_PAGE_SIZE = 20;
    private static final Integer MIN_PAGE_SIZE = 5;
    private static final Integer MAX_PAGE_SIZE = 200;

    private final LastnameService lastnameService;
    private final GeneratorService generatorService;

    @Autowired
    public LastnameController(LastnameService lastnameService, GeneratorService generatorService) {
        this.lastnameService = lastnameService;
        this.generatorService = generatorService;
    }

    @GetMapping
    public ResponseEntity<Page<Lastname>> getAllLastname(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size
    ) {
        page = page == null || page < 0 ? 0 : page;
        size = size == null || size < MIN_PAGE_SIZE ? DEFAULT_PAGE_SIZE : size;
        size = Math.min(MAX_PAGE_SIZE, size);

        Pageable pageRequest = Pageable.ofSize(size).withPage(page);
        return ResponseEntity.ok(lastnameService.findAll(pageRequest));
    }

    @GetMapping("/random")
    public ResponseEntity<Lastname> getRandomLastname(@RequestParam(value = "seed", required = false) String seed) {
        Region randomRegion = generatorService.getRandomLastnameReferencedRegion();

        Lastname randomLastname = generatorService.getRandomLastname(randomRegion);
        return ResponseEntity.ok(randomLastname);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Lastname>> getLastnameById(@PathVariable("id") Long id) {
        Optional<Lastname> found = lastnameService.findById(id);
        if (found.isEmpty()) {
            throw new GenericResourceNotFoundException("A lastname with id %d not found".formatted(id));
        }
        return ResponseEntity.ok(found);
    }

    @PutMapping("/{lastnameId}")
    public ResponseEntity<Lastname> putLastname(
            UriComponentsBuilder uriBuilder,
            @PathVariable("lastnameId") Long lastnameId,
            @RequestBody LastnameDto newLastname
    ) {

        Lastname saved = lastnameService.update(lastnameId, newLastname);

        final URI newResourceLocation = uriBuilder
                .path(REGION_CONTROLLER_PATH)
                .path("/{id}")
                .build(saved.getId());
        return ResponseEntity.created(newResourceLocation).body(saved);
    }

    @PostMapping
    public ResponseEntity<Lastname> postLastname(@RequestBody LastnameDto lastname, UriComponentsBuilder uriBuilder) {
        Lastname saved = lastnameService.save(lastname);

        final URI newResourceLocation = uriBuilder
                .path(REGION_CONTROLLER_PATH)
                .path("/{id}")
                .build(saved.getId());
        return ResponseEntity.created(newResourceLocation).body(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Lastname> deleteLastname(@PathVariable("id") Long id) {
        if (!lastnameService.remove(id)) {
            throw new GenericResourceNotFoundException("A lastname with %s id could not be found.".formatted(id));
        }

        return ResponseEntity.noContent().build();
    }


}
