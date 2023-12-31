package com.example.flavora;

import static androidx.lifecycle.ViewModelProvider.*;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Variables
    private RecyclerView recipesRV;
    private ViewModal viewmodal;

    // Edit Recipe Activity Result Launcher
    ActivityResultLauncher<Intent> editRecipeLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult activityResult) {
                            int resultCode = activityResult.getResultCode();
                            Intent data = activityResult.getData();

                            if (resultCode == RESULT_OK) {
                                int id = data.getIntExtra(AddRecipeActivity.EXTRA_ID, -1);
                                if (id == -1) {
                                    Toast.makeText(MainActivity.this, "Recipe can't be updated", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                String recipeName = data.getStringExtra(AddRecipeActivity.EXTRA_RECIPE_NAME);
                                String recipeDescription = data.getStringExtra(AddRecipeActivity.EXTRA_DESCRIPTION);
                                String recipeIngredients = data.getStringExtra(AddRecipeActivity.EXTRA_INGREDIENTS);
                                String recipeInstructions = data.getStringExtra(AddRecipeActivity.EXTRA_INSTRUCTIONS);
                                String recipeImage = data.getStringExtra(AddRecipeActivity.EXTRA_IMAGELINK);
                                RecipeModel model = new RecipeModel(recipeName, recipeDescription, recipeIngredients, recipeInstructions, recipeImage);
                                model.setId(id);
                                viewmodal.update(model);
                                Toast.makeText(MainActivity.this, "Recipe updated", Toast.LENGTH_SHORT).show();
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            }
                            else {
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            }
                        }
                    }
            );


    // Add Recipe Activity Result Launcher
    ActivityResultLauncher<Intent> addRecipeLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult activityResult) {
                            int resultCode = activityResult.getResultCode();
                            Intent data = activityResult.getData();

                            if (resultCode == RESULT_OK) {
                                String recipeName = data.getStringExtra(AddRecipeActivity.EXTRA_RECIPE_NAME);
                                String recipeDescription = data.getStringExtra(AddRecipeActivity.EXTRA_DESCRIPTION);
                                String recipeIngredients = data.getStringExtra(AddRecipeActivity.EXTRA_INGREDIENTS);
                                String recipeInstructions = data.getStringExtra(AddRecipeActivity.EXTRA_INSTRUCTIONS);
                                String recipeImage = data.getStringExtra(AddRecipeActivity.EXTRA_IMAGELINK);
                                Log.d("Debug", "Saved to database "+recipeImage);
                                RecipeModel model = new RecipeModel(recipeName, recipeDescription, recipeIngredients, recipeInstructions, recipeImage);
                                viewmodal.insert(model);
                                Toast.makeText(MainActivity.this, "Recipe saved", Toast.LENGTH_SHORT).show();

                            }
                            else {
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            }
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Variable for recycler view
        recipesRV = findViewById(R.id.rvRecipes);

        // Navigation Bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_home) {
                return true;
            } else if (itemId == R.id.bottom_addRecipe) {
                Intent intent = new Intent(MainActivity.this, AddRecipeActivity.class);
                addRecipeLauncher.launch(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return true;
            } else if (itemId == R.id.bottom_convert) {
                startActivity(new Intent(getApplicationContext(), ConvertActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                return true;
            }
            return false;
        });


        // ROOM Code from GeeksForGeeks (MSD Lab 6)
        recipesRV.setLayoutManager(new LinearLayoutManager(this));
        recipesRV.setHasFixedSize(true);

        final RVARecipe adapter = new RVARecipe();
        recipesRV.setAdapter(adapter);
        viewmodal = new ViewModelProvider(this).get(ViewModal.class);


        // Observe changes in Recipe Model
        viewmodal.getAllRecipes().observe(this, new Observer<List<RecipeModel>>() {
            @Override
            public void onChanged(List<RecipeModel> models) {
                adapter.submitList(models);
            }
        });

        // Deleting Recipes
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            // Swiping on an item deletes it from database
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                viewmodal.delete(adapter.getRecipeAt(viewHolder.getBindingAdapterPosition()));
                Toast.makeText(MainActivity.this, "Recipe deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recipesRV);

        // Editing Recipe
        adapter.setOnItemClickListener(new RVARecipe.OnItemClickListener() {
            @Override
            public void onItemClick(RecipeModel model) {
                // Passing data to new edit activity
                Intent intent = new Intent(MainActivity.this, EditRecipeActivity.class);
                intent.putExtra(AddRecipeActivity.EXTRA_ID, model.getId());
                intent.putExtra(AddRecipeActivity.EXTRA_RECIPE_NAME, model.getRecipeName());
                intent.putExtra(AddRecipeActivity.EXTRA_DESCRIPTION, model.getDescription());
                intent.putExtra(AddRecipeActivity.EXTRA_INGREDIENTS, model.getIngredients());
                intent.putExtra(AddRecipeActivity.EXTRA_INSTRUCTIONS, model.getInstructions());
                intent.putExtra(AddRecipeActivity.EXTRA_IMAGELINK, model.getImageLink());

                editRecipeLauncher.launch(intent);
            }
        });

    }

}