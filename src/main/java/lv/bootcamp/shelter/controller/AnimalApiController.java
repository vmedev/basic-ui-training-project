package lv.bootcamp.shelter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lv.bootcamp.shelter.dto.AnimalCreateRequest;
import lv.bootcamp.shelter.dto.AnimalResponse;
import lv.bootcamp.shelter.model.AnimalType;
import lv.bootcamp.shelter.service.AnimalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for shelter animal endpoints.
 * Returns JSON — does not render HTML pages.
 */
@Tag(name = "Animals", description = "Manage shelter animals")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/animals")
public class AnimalApiController {

    private final AnimalService animalService;

    @Operation(summary = "List all animals", description = "Returns every animal in the shelter.")
    @ApiResponse(responseCode = "200", description = "Animals returned successfully")
    @GetMapping
    public List<AnimalResponse> findAll() {
        return animalService.findAll();
    }

    @Operation(summary = "Find animal by id", description = "Returns a single animal.")
    @ApiResponse(responseCode = "200", description = "Animal found")
    @ApiResponse(responseCode = "404", description = "Animal not found")
    @GetMapping("/{id}")
    public ResponseEntity<AnimalResponse> findById(@PathVariable Long id) {
        return animalService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lists adopted animals. Restricted to ROLE_ADMIN — see SecurityConfig.
     * Read-only, so it's a good endpoint for testing role-based JWT authorization:
     * calling it repeatedly (e.g. with/without a token, or with a ROLE_USER token)
     * has no side effects, unlike {@code POST /api/animals}.
     */
    @Operation(summary = "List adopted animals", description = "Admin only. Returns all adopted animals.")
    @ApiResponse(responseCode = "200", description = "Adopted animals returned")
    @ApiResponse(responseCode = "403", description = "Admin role required")
    @GetMapping("/adopted")
    public List<AnimalResponse> findAdopted() {
        return animalService.findAdopted();
    }

    /**
     * Creates a new animal. Restricted to ROLE_ADMIN — see SecurityConfig.
     */
    @Operation(summary = "Create an animal", description = "Admin only. Adds a new animal to the shelter.")
    @ApiResponse(responseCode = "201", description = "Animal created")
    @ApiResponse(responseCode = "400", description = "Validation failed")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AnimalResponse create(@RequestBody @Valid AnimalCreateRequest request) {
        return animalService.create(request);
    }


    @Operation(summary = "List animal types", description = "Returns the available animal type values.")
    @ApiResponse(responseCode = "200", description = "Types returned")
    @GetMapping("/types")
    public List<AnimalType> findTypes() {
        return List.of(AnimalType.values());
    }

    /**
     * Adopts an animal as the currently logged-in user. Restricted to ROLE_USER
     * (not ROLE_ADMIN) — see SecurityConfig.
     */
    @Operation(summary = "Adopt an animal", description = "User only. Marks the animal as adopted by the current user.")
    @ApiResponse(responseCode = "200", description = "Animal adopted")
    @ApiResponse(responseCode = "409", description = "Animal already adopted")
    @PostMapping("/{id}/adopt")
    public ResponseEntity<AnimalResponse> adopt(@PathVariable Long id, Authentication authentication) {
        return animalService.adopt(id, authentication.getName())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleAlreadyAdopted(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
}
