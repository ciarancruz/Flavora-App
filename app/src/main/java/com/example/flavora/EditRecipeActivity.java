package com.example.flavora;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class EditRecipeActivity extends AppCompatActivity {

    private EditText recipeNameEdt, descriptionEdt, ingredientsEdt, instructionsEdt;
    private ImageView imageEdt;
    private Button addRecipeBtn, takePhotoBtn, pickPhotoBtn;
    private String imageLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);

        recipeNameEdt = findViewById(R.id.editInputRecipe);
        descriptionEdt = findViewById(R.id.editInputDescription);
        ingredientsEdt = findViewById(R.id.editInputIngredients);
        instructionsEdt = findViewById(R.id.editInputInstructions);
        imageEdt = (ImageView) findViewById(R.id.editInputImage);

        Intent intent = getIntent();
        if (intent.hasExtra(AddRecipeActivity.EXTRA_ID)) {
            // if we get id for our data then we are
            // setting values to our edit text fields.
            recipeNameEdt.setText(intent.getStringExtra(AddRecipeActivity.EXTRA_RECIPE_NAME));
            descriptionEdt.setText(intent.getStringExtra(AddRecipeActivity.EXTRA_DESCRIPTION));
            ingredientsEdt.setText(intent.getStringExtra(AddRecipeActivity.EXTRA_INGREDIENTS));
            instructionsEdt.setText(intent.getStringExtra(AddRecipeActivity.EXTRA_INSTRUCTIONS));

            String imageLink = intent.getStringExtra(AddRecipeActivity.EXTRA_IMAGELINK);
            String imageChanged = stringToPath(imageLink);
            Drawable image = Drawable.createFromPath(imageChanged);
            imageEdt.setImageDrawable(image);
        }
    }

    public String stringToPath(String imageLink) {
        String root = getApplication().getExternalFilesDir("").getAbsolutePath();
        String id = imageLink.substring(imageLink.length() - 4 );
        String link = root + "/pictures/recipes_" + id + ".jpeg";
        return link;
    }

}