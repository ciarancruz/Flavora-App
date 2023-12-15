package com.example.flavora;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@androidx.room.Dao
public interface Dao {

    // ROOM Code from GeeksForGeeks (MSD Lab 6)

    // Add data to the database
    @Insert
    void insert(RecipeModel model);

    // Update data in the database
    @Update
    void update(RecipeModel model);

    // Delete data from the database
    @Delete
    void delete(RecipeModel model);

    // Query to delete all recipes from database
    @Query("DELETE FROM recipe")
    void deleteAllRecipes();

    // Query all data from our database in ascending order
    @Query("SELECT * FROM recipe ORDER BY recipeName ASC")
    LiveData<List<RecipeModel>> getAllRecipes();
}
