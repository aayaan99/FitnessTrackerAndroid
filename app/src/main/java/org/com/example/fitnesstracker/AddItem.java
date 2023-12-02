package org.com.example.fitnesstracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddItem extends AppCompatActivity {

    private Spinner spinner;
    private EditText editTextQuantity;
    private Button buttonSubmit, buttonCancel;
    private FoodItem selectedFoodItem;

    private List<FoodItem> foodItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        setupNavigationButtons();
        setupSpinnerAndButtons();
        loadFoodItems();
    }

    private void setupNavigationButtons() {
        ImageButton foodItemButton = findViewById(R.id.imageButtonFoodItem);
        foodItemButton.setOnClickListener(v -> navigateTo(FoodList.class));

        ImageButton welcomeButton = findViewById(R.id.imageButtonWelcome);
        welcomeButton.setOnClickListener(v -> navigateTo(MainActivity.class));

        ImageButton todaysDietButton = findViewById(R.id.imageButtonDiet);
        todaysDietButton.setOnClickListener(v -> navigateTo(todaydiet.class));

        ImageButton analysisButton = findViewById(R.id.imageButtonGraph);
        analysisButton.setOnClickListener(v -> navigateTo(Graph.class));
    }

    private void setupSpinnerAndButtons() {
        spinner = findViewById(R.id.spinner);
        editTextQuantity = findViewById(R.id.editTextText);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonCancel = findViewById(R.id.buttonCancel);

        buttonSubmit.setOnClickListener(v -> submitFoodItem());
        buttonCancel.setOnClickListener(v -> finish());
    }

    private void loadFoodItems() {
        try (FileInputStream fis = openFileInput("food_items.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length >= 2) {
                    foodItems.add(new FoodItem(parts[0], parts[1], parts.length > 2 ? parts[2] : "", parts.length > 3 ? parts[3] : ""));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getFoodNames());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedFoodItem = foodItems.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedFoodItem = null;
            }
        });
    }

    private List<String> getFoodNames() {
        List<String> names = new ArrayList<>();
        for (FoodItem item : foodItems) {
            names.add(item.getName());
        }
        return names;
    }

    private void submitFoodItem() {
        String quantity = editTextQuantity.getText().toString();
        if (selectedFoodItem != null && !quantity.isEmpty()) {
            try (FileOutputStream fos = openFileOutput("user_diet.txt", MODE_APPEND);
                 OutputStreamWriter osw = new OutputStreamWriter(fos)) {
                String record = String.format("%s,%s,%s,%s\n",
                        selectedFoodItem.getName(), selectedFoodItem.getCalories(), quantity, new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
                osw.write(record);
                Toast.makeText(this, "Item added to diet", Toast.LENGTH_SHORT).show();
                finish();

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error saving item", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please select a food item and enter the quantity", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateTo(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    private static class FoodItem {
        private final String name;
        private final String calories;
        private final String info;
        private final String imagePath;

        public FoodItem(String name, String calories, String info, String imagePath) {
            this.name = name;
            this.calories = calories;
            this.info = info;
            this.imagePath = imagePath;
        }

        public String getName() {
            return name;
        }

        public String getCalories() {
            return calories;
        }

        public String getInfo() {
            return info;
        }

        public String getImagePath() {
            return imagePath;
        }
    }
}
