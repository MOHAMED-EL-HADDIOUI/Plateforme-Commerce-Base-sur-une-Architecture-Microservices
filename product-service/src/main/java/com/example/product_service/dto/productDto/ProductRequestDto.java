package com.example.product_service.dto.productDto;
import com.example.product_service.dto.inventoryDto.InventoryRequestDto;
import com.example.product_service.enums.Category;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO de requête pour la création d'un produit")
public class ProductRequestDto implements Serializable {

    @Schema(description = "Identifiant unique du produit")
    private String id;

//    @NotBlank(message = "Le nom du produit ne peut pas être vide")
    @Size(max = 100, message = "Le nom du produit ne peut pas dépasser 100 caractères")
    @Schema(description = "Nom du produit", example = "Ordinateur portable")
    private String name;

//    @NotBlank(message = "La description du produit ne peut pas être vide")
    @Size(max = 500, message = "La description ne peut pas dépasser 500 caractères")
    @Schema(description = "Description du produit", example = "Ordinateur portable haute performance pour jeux et travail")
    private String description;

//    @NotNull(message = "La catégorie du produit est obligatoire")
    @Schema(description = "Catégorie du produit", example = "MODE")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Category category;

    @DecimalMin(value = "0.0", inclusive = false, message = "Le prix doit être supérieur à 0")
    @Schema(description = "Prix du produit", example = "999.99")
    private Double price;

//    @Valid
    private InventoryRequestDto inventoryRequestDto;
}

