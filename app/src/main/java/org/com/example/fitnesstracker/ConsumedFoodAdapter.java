package org.com.example.fitnesstracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ConsumedFoodAdapter extends RecyclerView.Adapter<ConsumedFoodAdapter.ViewHolder> {

    private final ArrayList<todaydiet.ConsumedFoodItem> foodItems;

    public ConsumedFoodAdapter(ArrayList<todaydiet.ConsumedFoodItem> foodItems) {
        this.foodItems = foodItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.consumed_food_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        todaydiet.ConsumedFoodItem item = foodItems.get(position);
        holder.foodName.setText(item.getName());
        holder.calories.setText(String.format("Calories: %d", item.getCalories()));
        holder.quantity.setText(String.format("Quantity: %d", item.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return foodItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView foodName, calories, quantity;

        public ViewHolder(View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.foodName);
            calories = itemView.findViewById(R.id.calories);
            quantity = itemView.findViewById(R.id.quantity);
        }
    }


}
