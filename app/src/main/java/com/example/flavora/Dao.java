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

    // below method is use to
    // add data to database.
    @Insert
    void insert(RecipeModel model);

    // below method is use to update
    // the data in our database.
    @Update
    void update(RecipeModel model);

    // below line is use to delete a
    // specific course in our database.
    @Delete
    void delete(RecipeModel model);

    // on below line we are making query to
    // delete all courses from our database.
    @Query("DELETE FROM recipe")
    void deleteAllRecipes();

    // below line is to read all the courses from our database.
    // in this we are ordering our courses in ascending order
    // with our course name.
    @Query("SELECT * FROM recipe ORDER BY recipeName ASC")
    LiveData<List<RecipeModel>> getAllRecipes();
}
