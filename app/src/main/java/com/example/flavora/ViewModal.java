package com.example.flavora;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

// ROOM Code from GeeksForGeeks (MSD Lab 6)

public class ViewModal extends AndroidViewModel{
    // creating a new variable for course repository.
    private RecipeRepository repository;

    private LiveData<List<RecipeModel>> allRecipes;

    // constructor for our view modal.
    public ViewModal(@NonNull Application application) {
        super(application);
        repository = new RecipeRepository(application);
        allRecipes = repository.getAllRecipes();
    }

    public void insert(RecipeModel model) {
        repository.insert(model);
    }

    public void update(RecipeModel model) {
        repository.update(model);
    }

    public void delete(RecipeModel model) {
        repository.delete(model);
    }

    public void deleteAllRecipes() {
        repository.deleteAllRecipes();
    }

    public LiveData<List<RecipeModel>> getAllRecipes() {
        return allRecipes;
    }
}
