package com.example.flavora;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerview;
    private RVARecipe rvaRecipe;

    private ArrayList<RecipeModel> recipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recipes = new ArrayList<RecipeModel>();
        //Recipes
        for(int i = 0; i < 20; i++) {
            recipes.add(new RecipeModel("Korean Dish", "Korean Recipe", "meat", "Cook", "Nothing"));
        }

        recyclerview = findViewById(R.id.rvRecipes);
        rvaRecipe = new RVARecipe(recipes);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setAdapter(rvaRecipe);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);


        // Navigation Bar
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
                startActivity(new Intent(getApplicationContext(), AddRecipeActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
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



    }
}