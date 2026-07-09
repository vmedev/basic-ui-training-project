package lv.bootcamp.shelter.dto;

import lv.bootcamp.shelter.model.AnimalStatus;
import lv.bootcamp.shelter.model.AnimalType;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response body for a single animal returned by the API.
 * {@code adoptionNote} is only populated for ADMIN callers (see AnimalService#toResponse) —
 * everyone else just sees the plain {@code status}.
 */
public record AnimalResponse(

        @Schema(description = "Unique identifier", example = "1")
        Long id,

        @Schema(description = "Animal's name", example = "Luna")
        String name,

        @Schema(description = "Animal category", example = "CAT")
        AnimalType type,

        @Schema(description = "Breed of the animal", example = "Bengal")
        String breed,

        @Schema(description = "Age in years", example = "2")
        Integer age,

        @Schema(description = "Short personality description", example = "Calm and affectionate. Loves cuddles.")
        String description,

        @Schema(description = "Current adoption status", example = "AVAILABLE")
        AnimalStatus status,

        @Schema(description = "Path to the animal's photo", example = "/images/animals/Luna.jpeg")
        String imageUrl,

        @Schema(description = "Admin-only note about the adoption; null for other callers",
                example = "adopted by user on 2026-06-01")
        String adoptionNote
) {}