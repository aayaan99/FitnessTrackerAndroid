package org.com.example.fitnesstracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public static int totalCalorie;
    private static String user;
    TextView textWelcome, textSummary;
    private int maxCalorie = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textWelcome = findViewById(R.id.TextWelcome);
        textSummary = findViewById(R.id.TextSummary);

        setupButtons();
        loadUserProfile();
        getCalorie();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserProfile();
        getCalorie();
    }

    private void setupButtons() {
        ImageButton foodItemButton = findViewById(R.id.imageButtonFoodItem);
        foodItemButton.setOnClickListener(view -> startActivity(new Intent(this, FoodList.class)));

        ImageButton todaysDietButton = findViewById(R.id.imageButtonDiet);
        todaysDietButton.setOnClickListener(view -> startActivity(new Intent(this, todaydiet.class)));

        ImageButton analysisButton = findViewById(R.id.imageButtonGraph);
        analysisButton.setOnClickListener(view -> startActivity(new Intent(this, Graph.class)));

        Button editButton = findViewById(R.id.buttonEdit);
        editButton.setOnClickListener(view -> startActivity(new Intent(this, Profile.class)));
    }

    private void loadUserProfile() {
        try (FileInputStream fis = openFileInput("user_profile.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {
            String line = reader.readLine();
            if (line != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    user = parts[0];
                    maxCalorie = Integer.parseInt(parts[1]);
                    textWelcome.setText("Welcome " + user + "!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getCalorie() {
        totalCalorie = 0;
        String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        try (FileInputStream fis = openFileInput("user_diet.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4 && parts[3].equals(todayDate)) {
                    int calories = Integer.parseInt(parts[1]);
                    int quantity = Integer.parseInt(parts[2]);
                    totalCalorie += calories * quantity;
                }
            }
            textSummary.setText("Your calorie consumption today is " + totalCalorie + "/" + maxCalorie);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
