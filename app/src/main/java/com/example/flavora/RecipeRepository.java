package com.example.flavora;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;


// ROOM Code from GeeksForGeeks (MSD Lab 6)
public class RecipeRepository {
    // below line is the create a variable
    // for dao and list for all courses.
    private Dao dao;
    private LiveData<List<RecipeModel>> allRecipes;

    // creating a constructor for our variables
    // and passing the variables to it.
    public RecipeRepository(Application application) {
        RecipeDatabase database = RecipeDatabase.getInstance(application);
        dao = database.Dao();
        allRecipes = dao.getAllRecipes();
    }

    // creating a method to insert the data to our database.
    public void insert(RecipeModel model) {
        new InsertRecipesAsyncTask(dao).execute(model);
    }

    // creating a method to update data in database.
    public void update(RecipeModel model) {
        new UpdateRecipesAsyncTask(dao).execute(model);
    }

    // creating a method to delete the data in our database.
    public void delete(RecipeModel model) {
        new DeleteRecipesAsyncTask(dao).execute(model);
    }

    // below is the method to delete all the courses.
    public void deleteAllRecipes() {
        new DeleteAllRecipesAsyncTask(dao).execute();
    }

    // below method is to read all the courses.
    public LiveData<List<RecipeModel>> getAllRecipes() {
        return allRecipes;
    }

    // we are creating a async task method to insert new course.
    private static class InsertRecipesAsyncTask extends AsyncTask<RecipeModel, Void, Void> {
        private Dao dao;

        private InsertRecipesAsyncTask(Dao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(RecipeModel... model) {
            // below line is use to insert our modal in dao.
            dao.insert(model[0]);
            return null;
        }
    }

    // we are creating a async task method to update our course.
    private static class UpdateRecipesAsyncTask extends AsyncTask<RecipeModel, Void, Void> {
        private Dao dao;

        private UpdateRecipesAsyncTask(Dao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(RecipeModel... models) {
            // below line is use to update
            // our modal in dao.
            dao.update(models[0]);
            return null;
        }
    }

    // we are creating a async task method to delete course.
    private static class DeleteRecipesAsyncTask extends AsyncTask<RecipeModel, Void, Void> {
        private Dao dao;

        private DeleteRecipesAsyncTask(Dao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(RecipeModel... models) {
            // below line is use to delete
            // our course modal in dao.
            dao.delete(models[0]);
            return null;
        }
    }

    // we are creating a async task method to delete all courses.
    private static class DeleteAllRecipesAsyncTask extends AsyncTask<Void, Void, Void> {
        private Dao dao;
        private DeleteAllRecipesAsyncTask(Dao dao) {
            this.dao = dao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            // on below line calling method
            // to delete all courses.
            dao.deleteAllRecipes();
            return null;
        }
    }
}
