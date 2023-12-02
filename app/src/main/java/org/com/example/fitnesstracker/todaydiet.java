package org.com.example.fitnesstracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class todaydiet extends AppCompatActivity {

    private ArrayList<ConsumedFoodItem> todayFoodItems = new ArrayList<>();
    private RecyclerView recyclerView;
    private TextView calorieVal;
    private int totalCalories = 0;
    private ConsumedFoodAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todaydiet);

        recyclerView = findViewById(R.id.recyclerViewFoodItems);
        calorieVal = findViewById(R.id.calorieVal);

        setupButtons();

        loadTodayFoodItems();

        calculateTotalCalories();

        updateRecyclerView();

        adapter = new ConsumedFoodAdapter(todayFoodItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        MainActivity.totalCalorie = totalCalories;
        calorieVal.setText(String.valueOf(totalCalories));
    }

    @Override
    protected void onResume() {
        super.onResume();
        resetAndLoadData();
    }

    private void resetAndLoadData() {
        todayFoodItems.clear();
        loadTodayFoodItems();
        calculateTotalCalories();
        adapter.notifyDataSetChanged();
        MainActivity.totalCalorie = totalCalories;
        calorieVal.setText(String.valueOf(totalCalories));
    }

    private void setupButtons() {
        ImageButton foodItemButton = findViewById(R.id.imageButtonFoodItem);
        foodItemButton.setOnClickListener(view -> startActivity(new Intent(this, FoodList.class)));

        ImageButton welcomeButton = findViewById(R.id.imageButtonWelcome);
        welcomeButton.setOnClickListener(view -> startActivity(new Intent(this, MainActivity.class)));

        ImageButton todaysDietButton = findViewById(R.id.imageButtonDiet);
        todaysDietButton.setOnClickListener(view -> startActivity(new Intent(this, todaydiet.class)));

        ImageButton analysisButton = findViewById(R.id.imageButtonGraph);
        analysisButton.setOnClickListener(view -> startActivity(new Intent(this, Graph.class)));

        Button addItem = findViewById(R.id.buttonAddItem);
        addItem.setOnClickListener(view -> startActivity(new Intent(this, AddItem.class)));
    }

    private void loadTodayFoodItems() {
        String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        try (FileInputStream fis = openFileInput("user_diet.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4 && parts[3].equals(todayDate)) {
                    todayFoodItems.add(new ConsumedFoodItem(parts[0], Integer.parseInt(parts[1]), Integer.parseInt(parts[2])));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void calculateTotalCalories() {
        totalCalories = 0;
        for (ConsumedFoodItem item : todayFoodItems) {
            totalCalories += item.getCalories() * item.getQuantity();
        }
    }

    private void updateRecyclerView() {
        ConsumedFoodAdapter adapter = new ConsumedFoodAdapter(todayFoodItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    public static class ConsumedFoodItem {
        private final String name;
        private final int calories;
        private final int quantity;

        public ConsumedFoodItem(String name, int calories, int quantity) {
            this.name = name;
            this.calories = calories;
            this.quantity = quantity;
        }

        public String getName() {
            return name;
        }

        public int getCalories() {
            return calories;
        }

        public int getQuantity() {
            return quantity;
        }
    }
}
