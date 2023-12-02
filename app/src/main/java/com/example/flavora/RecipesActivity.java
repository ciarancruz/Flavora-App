package com.example.flavora;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class RecipesActivity extends AppCompatActivity {

    private static final int ADD_RECIPE_REQUEST = 1;
    private ViewModal viewmodal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        viewmodal = new ViewModelProvider(this).get(ViewModal.class);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_recipes);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_home) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                return true;
            } else if (itemId == R.id.bottom_recipes) {
                return true;
            } else if (itemId == R.id.bottom_addRecipe) {
                Intent intent = new Intent(RecipesActivity.this, AddRecipeActivity.class);
                startActivityForResult(intent, ADD_RECIPE_REQUEST);
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
    }

    // Passing result from addrecipe to main recipe
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String recipeName = data.getStringExtra(AddRecipeActivity.EXTRA_RECIPE_NAME);
            String recipeDescription = data.getStringExtra(AddRecipeActivity.EXTRA_DESCRIPTION);
            RecipeModel model = new RecipeModel(recipeName, recipeDescription);
            viewmodal.insert(model);
            Toast.makeText(this, "Recipe saved", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }
        else {
            Toast.makeText(this, "Recipe not saved", Toast.LENGTH_SHORT).show();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
    }
}