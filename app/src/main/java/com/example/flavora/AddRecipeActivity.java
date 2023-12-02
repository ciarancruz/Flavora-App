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
    private EditText recipeNameEdt, descriptionEdt;
    private Button addRecipeBtn;

    public static final String EXTRA_ID = "com.gtappdevelopers.gfgroomdatabase.EXTRA_ID";
    public static final String EXTRA_RECIPE_NAME = "com.gtappdevelopers.gfgroomdatabase.RECIPE";
    public static final String EXTRA_DESCRIPTION = "com.gtappdevelopers.gfgroomdatabase.EXTRA_RECIPE_DESCRIPTION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        // Variables for each view
        recipeNameEdt = findViewById(R.id.inputRecipe);
        descriptionEdt = findViewById(R.id.inputDescription);
        addRecipeBtn = findViewById(R.id.addRecipe);


//        // Navigation Menu
//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
//        bottomNavigationView.setSelectedItemId(R.id.bottom_addRecipe);
//
//        bottomNavigationView.setOnItemSelectedListener(item -> {
//            int itemId = item.getItemId();
//            if (itemId == R.id.bottom_home) {
//                startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//                finish();
//                return true;
//            } else if (itemId == R.id.bottom_recipes) {
//                startActivity(new Intent(getApplicationContext(), RecipesActivity.class));
//                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//                finish();
//                return true;
//            } else if (itemId == R.id.bottom_addRecipe) {
//                return true;
//            } else if (itemId == R.id.bottom_convert) {
//                startActivity(new Intent(getApplicationContext(), ConvertActivity.class));
//                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//                finish();
//                return true;
//            }
//            return false;
//        });

        // Getting data via an intent
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            // if we get id for our data then we are
            // setting values to our edit text fields.
            recipeNameEdt.setText(intent.getStringExtra(EXTRA_RECIPE_NAME));
            descriptionEdt.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
        }

        addRecipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // getting text value from edittext and validating if
                // the text fields are empty or not.
                String recipeName = recipeNameEdt.getText().toString();
                String description = descriptionEdt.getText().toString();
                if (recipeName.isEmpty() || description.isEmpty()) {
                    Toast.makeText(AddRecipeActivity.this, "Please enter valid recipe details.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // calling a method to save our course.
                saveRecipe(recipeName, description);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        });

    }

    private void saveRecipe(String recipeName, String description) {
        // inside this method we are passing
        // all the data via an intent.
        Intent data = new Intent();

        // in below line we are passing all our course detail.
        data.putExtra(EXTRA_RECIPE_NAME, recipeName);
        data.putExtra(EXTRA_DESCRIPTION, description);
        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            // in below line we are passing our id.
            data.putExtra(EXTRA_ID, id);
        }

        // at last we are setting result as data.
        setResult(RESULT_OK, data);
        Log.d("Flavora", "Result code OK added: ");
        // displaying a toast message after adding the data
        Toast.makeText(this, "Recipe Added to Database. ", Toast.LENGTH_SHORT).show();
    }
}