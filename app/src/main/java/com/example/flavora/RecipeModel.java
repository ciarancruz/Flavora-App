package com.example.flavora;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

// ROOM Code from GeeksForGeeks (MSD Lab 6)

@Entity(tableName = "recipe")
public class RecipeModel {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String recipeName;
    private String description;
    private String ingredients;
    private String instructions;
    private String imageLink;


    public RecipeModel(String recipeName, String description, String ingredients, String instructions, String imageLink) {
        this.recipeName = recipeName;
        this.description = description;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.imageLink = imageLink;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getRecipeName() {
        return recipeName;
    }
    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getIngredients() {
        return ingredients;
    }
    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }
    public String getInstructions() {
        return instructions;
    }
    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
    public String getImageLink() {
        return imageLink;
    }
    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

}