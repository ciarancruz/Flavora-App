package com.example.flavora;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ConvertActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // Variables
    private static final int ADD_RECIPE_REQUEST = 1;
    private ViewModal viewmodal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert);

        viewmodal = new ViewModelProvider(this).get(ViewModal.class);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_convert);


        // Navigation bar
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_home) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                return true;
            } else if (itemId == R.id.bottom_recipes) {
                startActivity(new Intent(getApplicationContext(), RecipesActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                return true;
            } else if (itemId == R.id.bottom_addRecipe) {
                Intent intent = new Intent(ConvertActivity.this, AddRecipeActivity.class);
                startActivityForResult(intent, ADD_RECIPE_REQUEST);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return true;
            } else if (itemId == R.id.bottom_convert) {
                return true;
            }
            return false;
        });


        //Spinner Dropdown
        Spinner spinner = findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.measurements, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new Measurement1());

        //Spinner Dropdown 2
        Spinner spinner2 = findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.measurements, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(new Measurement2());



    }

    // Passing result from addrecipe to main recipe
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String recipeName = data.getStringExtra(AddRecipeActivity.EXTRA_RECIPE_NAME);
            String recipeDescription = data.getStringExtra(AddRecipeActivity.EXTRA_DESCRIPTION);
            String recipeIngredients = data.getStringExtra(AddRecipeActivity.EXTRA_INGREDIENTS);
            String recipeInstructions = data.getStringExtra(AddRecipeActivity.EXTRA_INSTRUCTIONS);
            String recipeImage = data.getStringExtra(AddRecipeActivity.EXTRA_IMAGELINK);
            RecipeModel model = new RecipeModel(recipeName, recipeDescription, recipeIngredients, recipeInstructions, recipeImage);
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

    // Overrides for the first spinner measurement
    class Measurement1 implements AdapterView.OnItemSelectedListener
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
        {
            String text = parent.getItemAtPosition(position).toString();
            Toast.makeText(ConvertActivity.this, "Measurement1 selected" + text, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    // Overrides for the second spinner measurement
    class Measurement2 implements AdapterView.OnItemSelectedListener
    {
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
        {
            String text = parent.getItemAtPosition(position).toString();
            Toast.makeText(ConvertActivity.this, "Measurement2 selected" + text, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}