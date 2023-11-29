package com.example.flavora;

public class RecipeModel {
    public RecipeModel(String recipeName, String description, String ingredients, String instructions, String notes) {
        this.recipeName = recipeName;
        this.description = description;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.notes = notes;
    }

    private String recipeName;
    private String description;
    private String ingredients;
    private String instructions;
    private String notes;
}
