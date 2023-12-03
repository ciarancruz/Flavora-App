package com.example.flavora;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class AddRecipeActivity extends AppCompatActivity {

    // Variables for input
    private EditText recipeNameEdt, descriptionEdt, ingredientsEdt, instructionsEdt;
    private ImageView imageEdt;
    private Button addRecipeBtn, takePhotoBtn, pickPhotoBtn ;

    // GeekForGeeks Code (MSD Lab 6)
    public static final String EXTRA_ID = "EXTRA_ID";
    public static final String EXTRA_RECIPE_NAME = "EXTRA_RECIPE";
    public static final String EXTRA_DESCRIPTION = "EXTRA_RECIPE_DESCRIPTION";
    public static final String EXTRA_INGREDIENTS = "EXTRA_INGREDIENTS";
    public static final String EXTRA_INSTRUCTIONS = "EXTRA_INSTRUCTIONS";
    public static final int IMAGE_REQUEST = 100;
    public static final int CAMERA_REQUEST = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        // Variables for each view
        recipeNameEdt = findViewById(R.id.inputRecipe);
        descriptionEdt = findViewById(R.id.inputDescription);
        ingredientsEdt = findViewById(R.id.inputIngredients);
        instructionsEdt = findViewById(R.id.inputInstructions);
        imageEdt = (ImageView) findViewById(R.id.inputImage);

        addRecipeBtn = findViewById(R.id.addRecipe);
        takePhotoBtn = findViewById(R.id.takePhoto);
        pickPhotoBtn = findViewById(R.id.pickPhoto);


        // Getting data via an intent
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            // if we get id for our data then we are
            // setting values to our edit text fields.
            recipeNameEdt.setText(intent.getStringExtra(EXTRA_RECIPE_NAME));
            descriptionEdt.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            ingredientsEdt.setText(intent.getStringExtra(EXTRA_INGREDIENTS));
            instructionsEdt.setText(intent.getStringExtra(EXTRA_INSTRUCTIONS));
        }

        addRecipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // getting text value from edittext and validating if
                // the text fields are empty or not.
                String recipeName = recipeNameEdt.getText().toString();
                String description = descriptionEdt.getText().toString();
                String ingredients = ingredientsEdt.getText().toString();
                String instructions = instructionsEdt.getText().toString();
                if (recipeName.isEmpty() || description.isEmpty() || ingredients.isEmpty() || instructions.isEmpty()) {
                    Toast.makeText(AddRecipeActivity.this, "Please enter valid recipe details.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Calling a method to save recipe
                saveRecipe(recipeName, description, ingredients, instructions);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        });


        pickPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        takePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

    }

    public void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Log.i("Flavora", "entered takephoto");
        if (intent.resolveActivity(getPackageManager()) != null) {
            Log.i("Flavora", "entered if statement in image");
            startActivityForResult(intent, CAMERA_REQUEST);
        }
    }

    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_REQUEST);
    }

    private void saveRecipe(String recipeName, String description, String ingredients, String instructions) {
        // inside this method we are passing
        // all the data via an intent.
        Intent data = new Intent();

        // in below line we are passing all our course detail.
        data.putExtra(EXTRA_RECIPE_NAME, recipeName);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_INGREDIENTS, ingredients);
        data.putExtra(EXTRA_INSTRUCTIONS, instructions);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK) {
            try {
                Uri selectedImage = data.getData();
                InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                imageEdt.setImageBitmap(BitmapFactory.decodeStream(imageStream));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap image = (Bitmap)extras.get("data");
            imageEdt.setImageBitmap(image);
        }
    }
}