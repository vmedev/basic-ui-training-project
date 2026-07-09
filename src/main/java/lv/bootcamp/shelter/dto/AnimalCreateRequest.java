package lv.bootcamp.shelter.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lv.bootcamp.shelter.model.AnimalType;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * JSON request body for creating a new animal via the REST API.
 * Status is not included; all new animals start as AVAILABLE.
 */
public record AnimalCreateRequest(

        @Schema(description = "Animal's name", example = "Milo")
        @NotBlank
        String name,

        @Schema(description = "Animal category", example = "CAT")
        @NotNull
        AnimalType type,

        @Schema(description = "Breed of the animal", example = "Siamese")
        String breed,

        @Schema(description = "Age in years", example = "3")
        @Min(0)
        Integer age,

        @Schema(description = "Short personality description", example = "Calm and affectionate")
        String description,

        @Schema(description = "Image filename served from /images/animals/", example = "luna.jpg")
        String imageUrl
) {}