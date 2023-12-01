package com.example.flavora;

import static androidx.lifecycle.ViewModelProvider.*;

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
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Variables
    private RecyclerView recipesRV;
    private static final int ADD_RECIPE_REQUEST = 1;
    private static final int EDIT_RECIPE_REQUEST = 2;
    private ViewModal viewmodal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Variable for recycler view
        recipesRV = findViewById(R.id.rvRecipes);
        FloatingActionButton fab = findViewById(R.id.idFABAdd);


        // Navigation Bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_home) {
                return true;
            } else if (itemId == R.id.bottom_recipes) {
                startActivity(new Intent(getApplicationContext(), RecipesActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                return true;
            } else if (itemId == R.id.bottom_addRecipe) {
                Intent intent = new Intent(MainActivity.this, AddRecipeActivity.class);
                startActivityForResult(intent, ADD_RECIPE_REQUEST);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return true;
            } else if (itemId == R.id.bottom_convert) {
                startActivity(new Intent(getApplicationContext(), ConvertActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                return true;
            } else if (itemId == R.id.bottom_account) {
                startActivity(new Intent(getApplicationContext(), AccountActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                return true;
            }
            return false;
        });

        // adding on click listener for floating action button.
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // starting a new activity for adding a new course
                // and passing a constant value in it.
                Intent intent = new Intent(MainActivity.this, AddRecipeActivity.class);
                startActivityForResult(intent, ADD_RECIPE_REQUEST);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });


        recipesRV.setLayoutManager(new LinearLayoutManager(this));
        recipesRV.setHasFixedSize(true);

        final RVARecipe adapter = new RVARecipe();

        recipesRV.setAdapter(adapter);

        viewmodal = new ViewModelProvider(this).get(ViewModal.class);

        viewmodal.getAllRecipes().observe(this, new Observer<List<RecipeModel>>() {
            @Override
            public void onChanged(List<RecipeModel> models) {
                // when the data is changed in our models we are
                // adding that list to our adapter class.
                adapter.submitList(models);
            }
        });

        // TEMPORARY METHOD FOR DELETING DATA
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // on recycler view item swiped then we are deleting the item of our recycler view.
                viewmodal.delete(adapter.getRecipeAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Recipe deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recipesRV);

        // below line is use to attach this to recycler view.
        // below line is use to set item click listener for our item of recycler view.
        adapter.setOnItemClickListener(new RVARecipe.OnItemClickListener() {
            @Override
            public void onItemClick(RecipeModel model) {
                // after clicking on item of recycler view
                // we are opening a new activity and passing
                // a data to our activity.
                Intent intent = new Intent(MainActivity.this, AddRecipeActivity.class);
                intent.putExtra(AddRecipeActivity.EXTRA_ID, model.getId());
                intent.putExtra(AddRecipeActivity.EXTRA_RECIPE_NAME, model.getRecipeName());
                intent.putExtra(AddRecipeActivity.EXTRA_DESCRIPTION, model.getDescription());

                // below line is to start a new activity and
                // adding a edit course constant.
                startActivityForResult(intent, EDIT_RECIPE_REQUEST);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_RECIPE_REQUEST && resultCode == RESULT_OK) {
            String recipeName = data.getStringExtra(AddRecipeActivity.EXTRA_RECIPE_NAME);
            String recipeDescription = data.getStringExtra(AddRecipeActivity.EXTRA_DESCRIPTION);
            RecipeModel model = new RecipeModel(recipeName, recipeDescription);
            viewmodal.insert(model);
            Toast.makeText(this, "Recipe saved", Toast.LENGTH_SHORT).show();

        } else if (requestCode == EDIT_RECIPE_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddRecipeActivity.EXTRA_ID, -1);
            if (id == -1) {
                Toast.makeText(this, "Recipe can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }
            String recipeName = data.getStringExtra(AddRecipeActivity.EXTRA_RECIPE_NAME);
            String recipeDescription = data.getStringExtra(AddRecipeActivity.EXTRA_DESCRIPTION);
            RecipeModel model = new RecipeModel(recipeName, recipeDescription);
            model.setId(id);
            viewmodal.update(model);
            Toast.makeText(this, "Recipe updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Recipe not saved", Toast.LENGTH_SHORT).show();
        }
    }
}