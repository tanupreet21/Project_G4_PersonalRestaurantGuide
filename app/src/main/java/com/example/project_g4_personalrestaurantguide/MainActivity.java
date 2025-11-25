package com.example.project_g4_personalrestaurantguide;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private RestaurantAdapter adapter;
    private List<Restaurant> restaurantList;
    private Button addRestaurantBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupToolbar("Personal Restaurant Guide", false);


        recyclerView = findViewById(R.id.restaurantRecyclerView);
        addRestaurantBtn = findViewById(R.id.addRestaurantBtn);

        // Sample data for now
        restaurantList = new ArrayList<>();
        restaurantList.add(new Restaurant("Pizza Place", "Toronto", new String[]{"Italian", "Vegan"}, 4.5f, R.drawable.img));
        restaurantList.add(new Restaurant("Sushi Spot", "Vancouver", new String[]{"Japanese", "Non-Veg"}, 4.0f, R.drawable.img));

        adapter = new RestaurantAdapter(restaurantList, position -> {
            // Handle delete button click
            restaurantList.remove(position);
            adapter.notifyItemRemoved(position);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        addRestaurantBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddEditRestaurantActivity.class);
                startActivity(intent);
            }
        });

    }

}