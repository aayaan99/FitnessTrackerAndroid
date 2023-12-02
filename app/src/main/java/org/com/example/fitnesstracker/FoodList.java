package org.com.example.fitnesstracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FoodList extends AppCompatActivity {

    private Spinner spinner;
    private TextView calorieAmountText;
    private TextView infoText;
    private ImageView imageView;

    private List<FoodItem> foodItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);


        ImageButton foodItemButton = findViewById(R.id.imageButtonFoodItem);
        foodItemButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, FoodList.class);
            startActivity(intent);
        });

        ImageButton welcomeButton = findViewById(R.id.imageButtonWelcome);
        welcomeButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        ImageButton todaysDietButton = findViewById(R.id.imageButtonDiet);
        todaysDietButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, todaydiet.class);
            startActivity(intent);
        });

        ImageButton analysisButton = findViewById(R.id.imageButtonGraph);
        analysisButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, Graph.class);
            startActivity(intent);
        });

        Button newItemButton = findViewById(R.id.newItem);
        newItemButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewItem.class);
            startActivity(intent);
        });

        spinner = findViewById(R.id.spinner);
        calorieAmountText = findViewById(R.id.calorieamountText);
        infoText = findViewById(R.id.infoText);
        imageView = findViewById(R.id.imageView2);

        loadFoodItems();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, getFoodNames());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                FoodItem selectedFood = foodItems.get(position);
                updateFoodDetails(selectedFood);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private void loadFoodItems() {
        try (FileInputStream fis = openFileInput("food_items.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length >= 2) {
                    String name = parts[0];
                    String calories = parts[1];
                    String info = parts.length > 2 ? parts[2] : "";
                    String imagePath = parts.length > 3 ? parts[3] : "";

                    foodItems.add(new FoodItem(name, calories, info, imagePath));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private List<String> getFoodNames() {
        List<String> names = new ArrayList<>();
        for (FoodItem item : foodItems) {
            names.add(item.getName());

        }

        return names;
    }

    private void updateFoodDetails(FoodItem foodItem) {
        calorieAmountText.setText(foodItem.getCalories());
        infoText.setText(foodItem.getInfo());

        Bitmap bitmap = BitmapFactory.decodeFile(foodItem.getImagePath());
        imageView.setImageBitmap(bitmap);
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