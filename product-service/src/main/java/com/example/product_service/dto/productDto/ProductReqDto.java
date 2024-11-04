package com.example.product_service.dto.productDto;

import com.example.product_service.dto.inventoryDto.InventoryRequestDto;
import com.example.product_service.enums.Category;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO de requête pour la création d'un produit")
public class ProductReqDto {
    @Schema(description = "Identifiant unique du produit")
    private String id;

    @JsonProperty("name")  // Ajout de l'annotation JsonProperty
    @Schema(description = "Nom du produit", example = "Ordinateur portable")
    private String name;

    @JsonProperty("description")  // Ajout de l'annotation JsonProperty
    @Schema(description = "Description du produit", example = "Ordinateur portable haute performance pour jeux et travail")
    private String description;

    @JsonProperty("category")  // Ajout de l'annotation JsonProperty
    @Schema(description = "Catégorie du produit", example = "MODE")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Category category;

    @JsonProperty("price")  // Ajout de l'annotation JsonProperty
    @Schema(description = "Prix du produit", example = "999.99")
    private Double price;

    @JsonProperty("quantite")  // Ajout de l'annotation JsonProperty
    @Schema(description = "Quantite du produit", example = "10")
    private int quantite;
}