package com.example.flavora;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.concurrent.ExecutionException;

public class AddRecipeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // Variables for input
    private EditText recipeNameEdt, ingredientsEdt, instructionsEdt;
    private ImageView imageEdt;
    private Button addRecipeBtn, takePhotoBtn, pickPhotoBtn;
    private String imageLink = "", cameraLink;
    private String descriptionInput;

    // GeekForGeeks Code (MSD Lab 6)
    public static final String EXTRA_ID = "EXTRA_ID";
    public static final String EXTRA_RECIPE_NAME = "EXTRA_RECIPE";
    public static final String EXTRA_DESCRIPTION = "EXTRA_RECIPE_DESCRIPTION";
    public static final String EXTRA_INGREDIENTS = "EXTRA_INGREDIENTS";
    public static final String EXTRA_INSTRUCTIONS = "EXTRA_INSTRUCTIONS";
    public static final String EXTRA_IMAGELINK = "EXTRA_IMAGELINK";

    // Request Camera
    ActivityResultLauncher<Intent> cameraRequestLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult activityResult) {
                            int resultCode = activityResult.getResultCode();
                            Intent data = activityResult.getData();

                            // Reference: https://developer.android.com/training/camera-deprecated/photobasics
                            if (resultCode == RESULT_OK) {
                                Bundle extras = data.getExtras();
                                Bitmap image = (Bitmap)extras.get("data");
                                Log.d("Debug", "Image link/path" + image);
                                imageEdt.setImageBitmap(image);
                                bitmapToURI(image);

                            }
                            // End reference
                        }
                    }
            );

    // Open gallery
    ActivityResultLauncher<Intent> openGalleryLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult activityResult) {
                            int resultCode = activityResult.getResultCode();
                            Intent data = activityResult.getData();

                            if (resultCode == RESULT_OK) {
                                Uri selectedImage = data.getData();
                                imageLink = selectedImage.toString();
                                imageEdt.setImageURI(selectedImage);
                                try {
                                    storeImageInDirectory(selectedImage);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }

                            }
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        //Spinner Dropdown
        Spinner descriptionEdt = findViewById(R.id.inputDescription);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.description, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        descriptionEdt.setAdapter(adapter);
        descriptionEdt.setOnItemSelectedListener(this);


        // Variables for each view
        recipeNameEdt = findViewById(R.id.inputRecipe);
        ingredientsEdt = findViewById(R.id.inputIngredients);
        instructionsEdt = findViewById(R.id.inputInstructions);
        imageEdt = (ImageView) findViewById(R.id.inputImage);

        addRecipeBtn = findViewById(R.id.addRecipe);
        takePhotoBtn = findViewById(R.id.takePhoto);
        pickPhotoBtn = findViewById(R.id.pickPhoto);


        // Getting data via an intent
        Intent intent = getIntent();
        if (intent.hasExtra(AddRecipeActivity.EXTRA_ID)) {
            recipeNameEdt.setText(intent.getStringExtra(AddRecipeActivity.EXTRA_RECIPE_NAME));

            // Getting text value of the dropdown selected item
            ArrayAdapter myAdapter = (ArrayAdapter) descriptionEdt.getAdapter();
            int spinnerPosition = myAdapter.getPosition(intent.getStringExtra(AddRecipeActivity.EXTRA_DESCRIPTION));
            descriptionEdt.setSelection(spinnerPosition);
            ingredientsEdt.setText(intent.getStringExtra(AddRecipeActivity.EXTRA_INGREDIENTS));
            instructionsEdt.setText(intent.getStringExtra(AddRecipeActivity.EXTRA_INSTRUCTIONS));

            String imageLink = intent.getStringExtra(AddRecipeActivity.EXTRA_IMAGELINK);
            String imageChanged = stringToPath(imageLink);
            Drawable image = Drawable.createFromPath(imageChanged);
            imageEdt.setImageDrawable(image);
        }


        addRecipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // getting text value from edittext and validating if
                // the text fields are empty or not.
                String recipeName = recipeNameEdt.getText().toString();
                String description = descriptionInput;
                String ingredients = ingredientsEdt.getText().toString();
                String instructions = instructionsEdt.getText().toString();
                String image = imageLink;
                Log.d("Debug", "" + image);
                if (recipeName.isEmpty() || description.isEmpty() || ingredients.isEmpty() || instructions.isEmpty() ) {
                    Toast.makeText(AddRecipeActivity.this, "Please enter value in all fields.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (image.isEmpty()) {
                    Toast.makeText(AddRecipeActivity.this, "Please insert an image.", Toast.LENGTH_SHORT).show();
                    return;
                }

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

    public String stringToPath(String imageLink) {
        String root = getApplication().getExternalFilesDir("").getAbsolutePath();
        String id = imageLink.substring(imageLink.length() - 4 );
        String link = root + "/pictures/recipes_" + id + ".jpeg";
        return link;
    }

    // Taking a photo Reference: https://developer.android.com/training/camera-deprecated/photobasics
    public void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            cameraRequestLauncher.launch(intent);
        }
    }

    // Picking a photo from gallery https://developer.android.com/training/data-storage/shared/documents-files
    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        openGalleryLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }

    private void saveRecipe(String recipeName, String description, String ingredients, String instructions, String image) {
        Intent data = new Intent();

        data.putExtra(EXTRA_RECIPE_NAME, recipeName);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_INGREDIENTS, ingredients);
        data.putExtra(EXTRA_INSTRUCTIONS, instructions);
        data.putExtra(EXTRA_IMAGELINK, image);
        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        // at last we are setting result as data.
        setResult(RESULT_OK, data);
    }

    // Storing image in a directory for future use
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

    // Convert a bitmap file to URI. Reference: https://stackoverflow.com/questions/8295773/how-can-i-transform-a-bitmap-into-a-uri
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        descriptionInput = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}