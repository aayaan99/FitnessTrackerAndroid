package org.com.example.fitnesstracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import com.github.mikephil.charting.formatter.ValueFormatter;


public class Graph extends AppCompatActivity {

    private BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        setupNavigationButtons();
        barChart = findViewById(R.id.chart1);

        loadDataForGraph();
    }

    private void setupNavigationButtons() {
        ImageButton foodItemButton = findViewById(R.id.imageButtonFoodItem);
        foodItemButton.setOnClickListener(view -> startActivity(new Intent(this, FoodList.class)));

        ImageButton welcomeButton = findViewById(R.id.imageButtonWelcome);
        welcomeButton.setOnClickListener(view -> startActivity(new Intent(this, MainActivity.class)));

        ImageButton todaysDietButton = findViewById(R.id.imageButtonDiet);
        todaysDietButton.setOnClickListener(view -> startActivity(new Intent(this, todaydiet.class)));

        ImageButton analysisButton = findViewById(R.id.imageButtonGraph);
        analysisButton.setOnClickListener(view -> startActivity(new Intent(this, Graph.class)));
    }

    private void loadDataForGraph() {
        Map<String, Integer> calorieData = new HashMap<>();
        ArrayList<String> sortedDates = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());


        for (int i = 6; i >= 0; i--) {
            cal.add(Calendar.DATE, -i);
            String dateStr = sdf.format(cal.getTime());
            calorieData.put(dateStr, 0);
            sortedDates.add(dateStr);
            cal.add(Calendar.DATE, i); // Reset the date back
        }


        try (FileInputStream fis = openFileInput("user_diet.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String date = parts[3];
                    int calories = Integer.parseInt(parts[1]) * Integer.parseInt(parts[2]);
                    if (calorieData.containsKey(date)) {
                        calorieData.put(date, calorieData.get(date) + calories);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < sortedDates.size(); i++) {
            String date = sortedDates.get(i);
            entries.add(new BarEntry(i, calorieData.get(date)));
        }


        BarDataSet dataSet = new BarDataSet(entries, "Calorie Intake");
        dataSet.setColor(getResources().getColor(R.color.teal_700));
        dataSet.setValueTextColor(getResources().getColor(R.color.black));
        dataSet.setValueTextSize(12f);

        BarData barData = new BarData(dataSet);
        barChart.setData(barData);


        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            private final SimpleDateFormat displayFormat = new SimpleDateFormat("MMM dd", Locale.getDefault());
            @Override
            public String getFormattedValue(float value) {
                int index = (int) value;
                if (index >= 0 && index < sortedDates.size()) {
                    try {
                        Date date = sdf.parse(sortedDates.get(index));
                        return displayFormat.format(date);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return "";
            }
        });

        xAxis.setTextSize(10f);
        xAxis.setLabelCount(7);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setTextSize(10f);

        barChart.getAxisLeft().setAxisMinimum(0f);
        barChart.getAxisLeft().setAxisMaximum(Collections.max(calorieData.values()) * 1.2f);

        barChart.getDescription().setEnabled(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.setExtraBottomOffset(10f);

        barChart.notifyDataSetChanged();
        barChart.setBackgroundColor(getResources().getColor(R.color.chart_background));
        barChart.invalidate();
    }
}
