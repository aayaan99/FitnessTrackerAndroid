package org.com.example.fitnesstracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class Welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        ImageButton FoodItemButton = (ImageButton) findViewById(R.id.imageButtonFoodItem);
        FoodItemButton.setOnClickListener(listener);
    }

    ImageButton.OnClickListener listener = new ImageButton.OnClickListener()
    {
        @Override
        public void onClick(View view) {
            Intent FIntent = new Intent(Welcome.this, FoodList.class);
            startActivity(FIntent);
        }


    };
}