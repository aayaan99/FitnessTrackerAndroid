package org.com.example.fitnesstracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Profile extends AppCompatActivity {

    private TextView textViewUser;
    private EditText editTextUsername, editTextGoal;
    private Button buttonSubmit, buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initializeViews();
        loadUserProfile();
        setupButtonListeners();
    }

    private void initializeViews() {
        textViewUser = findViewById(R.id.textViewUser);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextGoal = findViewById(R.id.editTextGoal);
        buttonSubmit = findViewById(R.id.buttonProfileSubmit);
        buttonBack = findViewById(R.id.buttonBack);
    }

    private void loadUserProfile() {
        try (FileInputStream fis = openFileInput("user_profile.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {
            String line = reader.readLine();
            if (line != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    editTextUsername.setText(parts[0]);
                    editTextGoal.setText(parts[1]);
                    textViewUser.setText(parts[0] + "'s Profile");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupButtonListeners() {
        buttonSubmit.setOnClickListener(view -> saveUserProfile());
        buttonBack.setOnClickListener(view -> finish());
    }

    private void saveUserProfile() {
        String username = editTextUsername.getText().toString().trim();
        String goal = editTextGoal.getText().toString().trim();

        if (validateInput(username, goal)) {
            try (FileOutputStream fos = openFileOutput("user_profile.txt", Context.MODE_PRIVATE);
                 OutputStreamWriter osw = new OutputStreamWriter(fos)) {
                osw.write(username + "," + goal + "\n");
                Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show();
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean validateInput(String username, String goal) {
        if (username.isEmpty()) {
            editTextUsername.setError("Username cannot be empty");
            return false;
        }
        if (goal.isEmpty()) {
            editTextGoal.setError("Goal cannot be empty");
            return false;
        }
        return true;
    }
}
