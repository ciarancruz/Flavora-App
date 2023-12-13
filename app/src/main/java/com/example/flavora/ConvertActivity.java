package com.example.flavora;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ConvertActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // Variables
    private ViewModal viewmodal;
    private String measurement1, measurement2;
    private Button convertBtn;
    private EditText measurement1ET;
    private TextView measurement2TV;

    // Add Recipe Activity Result Launcher
    ActivityResultLauncher<Intent> addRecipeLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult activityResult) {
                            int resultCode = activityResult.getResultCode();
                            Intent data = activityResult.getData();

                            if (resultCode == RESULT_OK) {
                                String recipeName = data.getStringExtra(AddRecipeActivity.EXTRA_RECIPE_NAME);
                                String recipeDescription = data.getStringExtra(AddRecipeActivity.EXTRA_DESCRIPTION);
                                String recipeIngredients = data.getStringExtra(AddRecipeActivity.EXTRA_INGREDIENTS);
                                String recipeInstructions = data.getStringExtra(AddRecipeActivity.EXTRA_INSTRUCTIONS);
                                String recipeImage = data.getStringExtra(AddRecipeActivity.EXTRA_IMAGELINK);
                                RecipeModel model = new RecipeModel(recipeName, recipeDescription, recipeIngredients, recipeInstructions, recipeImage);
                                viewmodal.insert(model);
                                Toast.makeText(ConvertActivity.this, "Recipe saved", Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                finish();
                            }
                            else {
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            }
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert);

        viewmodal = new ViewModelProvider(this).get(ViewModal.class);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_convert);

        convertBtn = findViewById(R.id.convertButton);
        measurement1ET = findViewById(R.id.measurement1);
        measurement2TV = findViewById(R.id.measurement2);


        // Navigation bar
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_home) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                return true;
            } else if (itemId == R.id.bottom_addRecipe) {
                Intent intent = new Intent(ConvertActivity.this, AddRecipeActivity.class);
                addRecipeLauncher.launch(intent);
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


        //Convert Button
        convertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (measurement1.equals("Fahrenheit") && measurement2.equals("Celsius")) {
                    fahrenheitCelsius(0);
                }
                else if (measurement1.equals("Celsius") && measurement2.equals("Fahrenheit")){
                    fahrenheitCelsius(1);
                }

                else if ((measurement1.equals("Grams") && measurement2.equals("Kilogrammes")) || (measurement1.equals("Millilitres") && measurement2.equals("Litres"))) {
                    litresAndKilogrammes(0);
                }
                else if ((measurement1.equals("Kilogrammes") && measurement2.equals("Grams")) || (measurement1.equals("Litres") && measurement2.equals("Millilitres"))){
                    litresAndKilogrammes(1);
                }
                else {
                    Toast.makeText(ConvertActivity.this, "Conversion not possible", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    // Fahrenheit and Celsius Conversion
    public void fahrenheitCelsius(int whichWay) { // 0 is fahrenheit to celsius and 1 is celsius to fahrenheit

        String value = measurement1ET.getText().toString();
        float valueAsFloat = Float.parseFloat(value);

        if (whichWay == 0) { // Fahrenheit to Celsius
            Float result = ((valueAsFloat - 32) * 5)/9;
            measurement2TV.setText(result.toString());
        }
        else if (whichWay == 1) { // Celsius to Fahrenheit
            Float result = ((valueAsFloat * 9) / 5) + 32;
            measurement2TV.setText(result.toString());
        }
    }

    // Millilitres and Litres Conversion (Can be used by grams and kilogrammes too)
    public void litresAndKilogrammes(int whichWay) { // 0 is millilitres to litres and 1 is litres to millilitres

        String value = measurement1ET.getText().toString();
        float valueAsFloat = Float.parseFloat(value);

        if (whichWay == 0) { // Gram to kilogram || Millilitres to litres
            Float result = (valueAsFloat / 1000);
            measurement2TV.setText(result.toString());
        }
        else if (whichWay == 1) { // Kilogram to gram || Litres to millilitres
            Float result = valueAsFloat * 1000;
            measurement2TV.setText(result.toString());
        }
    }

    // Overrides for the first spinner measurement
    class Measurement1 implements AdapterView.OnItemSelectedListener
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
        {
            measurement1 = parent.getItemAtPosition(position).toString();
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
            measurement2 = parent.getItemAtPosition(position).toString();
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