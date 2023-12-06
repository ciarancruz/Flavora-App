package com.example.flavora;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

public class AddRecipeActivity extends AppCompatActivity {

    // Variables for input
    private EditText recipeNameEdt, descriptionEdt, ingredientsEdt, instructionsEdt;
    private ImageView imageEdt;
    private Button addRecipeBtn, takePhotoBtn, pickPhotoBtn;
    private String imageLink, cameraLink;

    // GeekForGeeks Code (MSD Lab 6)
    public static final String EXTRA_ID = "EXTRA_ID";
    public static final String EXTRA_RECIPE_NAME = "EXTRA_RECIPE";
    public static final String EXTRA_DESCRIPTION = "EXTRA_RECIPE_DESCRIPTION";
    public static final String EXTRA_INGREDIENTS = "EXTRA_INGREDIENTS";
    public static final String EXTRA_INSTRUCTIONS = "EXTRA_INSTRUCTIONS";
    public static final String EXTRA_IMAGELINK = "EXTRA_IMAGELINK";

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


//        // Getting data via an intent
//        Intent intent = getIntent();
//        if (intent.hasExtra(EXTRA_ID)) {
//            // if we get id for our data then we are
//            // setting values to our edit text fields.
//            recipeNameEdt.setText(intent.getStringExtra(EXTRA_RECIPE_NAME));
//            descriptionEdt.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
//            ingredientsEdt.setText(intent.getStringExtra(EXTRA_INGREDIENTS));
//            instructionsEdt.setText(intent.getStringExtra(EXTRA_INSTRUCTIONS));
//            String image = intent.getStringExtra(EXTRA_IMAGELINK);
//            Uri imageLink = Uri.parse(image);
//            imageEdt.setImageURI(imageLink);
//        }

        addRecipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // getting text value from edittext and validating if
                // the text fields are empty or not.
                String recipeName = recipeNameEdt.getText().toString();
                String description = descriptionEdt.getText().toString();
                String ingredients = ingredientsEdt.getText().toString();
                String instructions = instructionsEdt.getText().toString();
                String image = imageLink;
                Log.d("Debug", "" + image);
                if (recipeName.isEmpty() || description.isEmpty() || ingredients.isEmpty() || instructions.isEmpty()) {
                    Toast.makeText(AddRecipeActivity.this, "Please enter valid recipe details.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Calling a method to save recipe
                saveRecipe(recipeName, description, ingredients, instructions, image);
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

    // Taking a photo
    public void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAMERA_REQUEST);
        }
    }

    // Picking a photo from gallery
    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_REQUEST);
    }

    private void saveRecipe(String recipeName, String description, String ingredients, String instructions, String image) {
        // inside this method we are passing
        // all the data via an intent.
        Intent data = new Intent();

        // in below line we are passing all our course detail.
        data.putExtra(EXTRA_RECIPE_NAME, recipeName);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_INGREDIENTS, ingredients);
        data.putExtra(EXTRA_INSTRUCTIONS, instructions);
        data.putExtra(EXTRA_IMAGELINK, image);
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

    private void storeImageInDirectory(Uri imageURI) throws IOException {
        // Find root folder
        String root = getApplication().getExternalFilesDir("").getAbsolutePath();
        Log.d("Debug", "" + root);

        // Create a folder in that root folder
        File rootDir = new File(root + "/pictures");
        rootDir.mkdir();

        Bitmap image = MediaStore.Images.Media.getBitmap(
                getApplication().getContentResolver(),
                imageURI
        );

        String stringImageId = imageURI.toString();
        String id = stringImageId.substring(stringImageId.length() - 4);
        Log.d("Debug", "Last 4: " + id);

        // Create file to store image
        File myNewImage = new File(root + "/pictures/recipes_" + id + ".jpeg");
        Log.d("Debug", "New file: " + myNewImage);

        FileOutputStream out = new FileOutputStream(myNewImage);
        image.compress(
                Bitmap.CompressFormat.JPEG,
                100,
                out
        );
        Log.d("Debug", "Out:" + out);

        out.close();
    }

    private void bitmapToURI (Bitmap imageBitmap) {
        File tempFile = new File(getCacheDir(), "temp.jpeg");
        try {
                FileOutputStream fos = new FileOutputStream(tempFile);
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        Uri uri = Uri.fromFile(tempFile);
        imageLink = uri.toString();
        try {
            storeImageInDirectory(uri);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
}


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            imageLink = selectedImage.toString();
            Log.d("Debug", "Image link saved " + imageLink);
            imageEdt.setImageURI(selectedImage);
            try {
                storeImageInDirectory(selectedImage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        // Reference: https://developer.android.com/training/camera-deprecated/photobasics
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap image = (Bitmap)extras.get("data");
            Log.d("Debug", "Image link/path" + image);
            imageEdt.setImageBitmap(image);
            bitmapToURI(image);

        }
        // End reference
    }
}