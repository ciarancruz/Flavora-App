package com.example.flavora;

public class RecipeModel {
    public RecipeModel(String recipeName, String description, String ingredients, String instructions, String notes) {
        this.recipeName = recipeName;
        this.description = description;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.notes = notes;
    }

    String recipeName;
    String description;
    String ingredients;
    String instructions;
    String notes;


}
