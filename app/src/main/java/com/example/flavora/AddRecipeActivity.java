package com.example.flavora;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AddRecipeActivity extends AppCompatActivity {

    // Variables for input
    private EditText recipeNameEdt, descriptionEdt, ingredientsEdt;
    private Button addRecipeBtn;

    // GeekForGeeks Code (MSD Lab 6)
    public static final String EXTRA_ID = "EXTRA_ID";
    public static final String EXTRA_RECIPE_NAME = "EXTRA_RECIPE";
    public static final String EXTRA_DESCRIPTION = "EXTRA_RECIPE_DESCRIPTION";
    public static final String EXTRA_INGREDIENTS = "EXTRA_INGREDIENTS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        // Variables for each view
        recipeNameEdt = findViewById(R.id.inputRecipe);
        descriptionEdt = findViewById(R.id.inputDescription);
        ingredientsEdt = findViewById(R.id.inputIngredients);
        addRecipeBtn = findViewById(R.id.addRecipe);


        // Getting data via an intent
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            // if we get id for our data then we are
            // setting values to our edit text fields.
            recipeNameEdt.setText(intent.getStringExtra(EXTRA_RECIPE_NAME));
            descriptionEdt.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            ingredientsEdt.setText(intent.getStringExtra(EXTRA_INGREDIENTS));
        }

        addRecipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // getting text value from edittext and validating if
                // the text fields are empty or not.
                String recipeName = recipeNameEdt.getText().toString();
                String description = descriptionEdt.getText().toString();
                String ingredients = ingredientsEdt.getText().toString();
                if (recipeName.isEmpty() || description.isEmpty() || ingredients.isEmpty()) {
                    Toast.makeText(AddRecipeActivity.this, "Please enter valid recipe details.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Calling a method to save recipe
                saveRecipe(recipeName, description, ingredients);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        });

    }

    private void saveRecipe(String recipeName, String description, String ingredients) {
        // inside this method we are passing
        // all the data via an intent.
        Intent data = new Intent();

        // in below line we are passing all our course detail.
        data.putExtra(EXTRA_RECIPE_NAME, recipeName);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_INGREDIENTS, ingredients);
        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            // in below line we are passing our id.
            data.putExtra(EXTRA_ID, id);
        }

        // at last we are setting result as data.
        setResult(RESULT_OK, data);
        // Toast Message Displayed
        Toast.makeText(this, "Recipe Added to Database.", Toast.LENGTH_SHORT).show();
    }
}