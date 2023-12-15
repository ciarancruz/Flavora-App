package com.example.flavora;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class EditRecipeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText recipeNameEdt, descriptionEdt, ingredientsEdt, instructionsEdt;
    private ImageView imageEdt;
    private Button editAddRecipeBtn, editTakePhotoBtn, editPickPhotoBtn;
    private String imageLink;
    private String editDescriptionInput;

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
        setContentView(R.layout.activity_edit_recipe);

        //Spinner Dropdown
        Spinner descriptionEdt = findViewById(R.id.editInputDescription);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.description, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        descriptionEdt.setAdapter(adapter);
        descriptionEdt.setOnItemSelectedListener(this);

        recipeNameEdt = findViewById(R.id.editInputRecipe);
        ingredientsEdt = findViewById(R.id.editInputIngredients);
        instructionsEdt = findViewById(R.id.editInputInstructions);
        imageEdt = (ImageView) findViewById(R.id.editInputImage);

        editAddRecipeBtn = findViewById(R.id.editAddRecipe);
        editTakePhotoBtn = findViewById(R.id.editTakePhoto);
        editPickPhotoBtn = findViewById(R.id.editPickPhoto);

        Intent intent = getIntent();
        if (intent.hasExtra(AddRecipeActivity.EXTRA_ID)) {
            // if we get id for our data then we are
            // setting values to our edit text fields.
            recipeNameEdt.setText(intent.getStringExtra(AddRecipeActivity.EXTRA_RECIPE_NAME));

            ArrayAdapter myAdapter = (ArrayAdapter) descriptionEdt.getAdapter();
            int spinnerPosition = myAdapter.getPosition(intent.getStringExtra(AddRecipeActivity.EXTRA_DESCRIPTION));
            descriptionEdt.setSelection(spinnerPosition);

            ingredientsEdt.setText(intent.getStringExtra(AddRecipeActivity.EXTRA_INGREDIENTS));
            instructionsEdt.setText(intent.getStringExtra(AddRecipeActivity.EXTRA_INSTRUCTIONS));

            String imageLinkExtra = intent.getStringExtra(AddRecipeActivity.EXTRA_IMAGELINK);
            imageLink = imageLinkExtra;
            String imageChanged = stringToPath(imageLinkExtra);
            Drawable image = Drawable.createFromPath(imageChanged);
            imageEdt.setImageDrawable(image);
        }

        editAddRecipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // getting text value from edittext and validating if
                // the text fields are empty or not.
                String recipeName = recipeNameEdt.getText().toString();
                String description = editDescriptionInput;
                String ingredients = ingredientsEdt.getText().toString();
                String instructions = instructionsEdt.getText().toString();
                String image = imageLink;
                if (recipeName.isEmpty() || description.isEmpty() || ingredients.isEmpty() || instructions.isEmpty() ) {
                    Toast.makeText(EditRecipeActivity.this, "Please enter value in all fields.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (image.isEmpty()) {
                    Toast.makeText(EditRecipeActivity.this, "Please insert an image.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Calling a method to save recipe
                saveRecipe(recipeName, description, ingredients, instructions, image);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        });


        editPickPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        editTakePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

    }

    // Return a path with the id (last 4 digits) of the picture to uniquely identify each picture
    public String stringToPath(String imageLink) {
        String root = getApplication().getExternalFilesDir("").getAbsolutePath();
        String id = imageLink.substring(imageLink.length() - 4 );
        String link = root + "/pictures/recipes_" + id + ".jpeg";
        return link;
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

        // Passing all recipe details
        data.putExtra(EXTRA_RECIPE_NAME, recipeName);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_INGREDIENTS, ingredients);
        data.putExtra(EXTRA_INSTRUCTIONS, instructions);
        data.putExtra(EXTRA_IMAGELINK, image);
        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        Toast.makeText(this, "Recipe Added to Database.", Toast.LENGTH_SHORT).show();
    }

    // Store picture in "pictures" directory
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
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        editDescriptionInput = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}