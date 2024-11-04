package com.example.product_service.enums;

public enum Category {
    ELECTRONIQUE,
    MODE,
    MAISON_ET_VIE,
    SPORT_ET_PLEIN_AIR,
    COSMETIQUE,
    PAPETERIE,
    SUPERMARCHE,
    AUTRE;

    // Méthode utilitaire pour conversion depuis String
    public static Category fromString(String value) {
        try {
            return Category.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Catégorie invalide: " + value);
        }
    }
}