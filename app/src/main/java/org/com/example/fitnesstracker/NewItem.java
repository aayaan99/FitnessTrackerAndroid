package org.com.example.fitnesstracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class NewItem extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText nameEditText, caloriesEditText, infoEditText;
    private Button saveButton, uploadImageButton;
    private String imagePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

        nameEditText = findViewById(R.id.editTextText2);
        caloriesEditText = findViewById(R.id.editTextText3);
        infoEditText = findViewById(R.id.editTextText4);
        uploadImageButton = findViewById(R.id.upload);
        saveButton = findViewById(R.id.buttonAdd);
        Button backBtn;

        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNewItem();
            }
        });

        backBtn = findViewById(R.id.buttonBack);
        backBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, FoodList.class);
            startActivity(intent);
        });
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImage = data.getData();
            imagePath = selectedImage.toString();
        }
    }

    private void saveNewItem() {
        String name = nameEditText.getText().toString();
        String calories = caloriesEditText.getText().toString();
        String info = infoEditText.getText().toString();

        String fileContent = name + "," + calories + "," + info + "," + imagePath + "\n";

        try (FileOutputStream fos = openFileOutput("food_items.txt", MODE_APPEND)) {
            fos.write(fileContent.getBytes());
            Toast.makeText(this, "Item saved successfully", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error saving item", Toast.LENGTH_SHORT).show();
        }
        Intent intent = new Intent(this, FoodList.class);
        startActivity(intent);
    }
}
