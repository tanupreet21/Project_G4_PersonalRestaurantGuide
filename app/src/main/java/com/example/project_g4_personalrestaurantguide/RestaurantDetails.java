package com.example.project_g4_personalrestaurantguide;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RestaurantDetails extends BaseActivity {

    private Button buttonEdit;
    private Button buttonShowMap;
    private Button buttonDirections;
    private Button buttonShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_restaurant_details);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        setupToolbar("Restaurant Details", true);


        buttonEdit = findViewById(R.id.buttonEdit);
        buttonShowMap = findViewById(R.id.buttonShowMap);
        buttonDirections = findViewById(R.id.buttonDirections);
        buttonShare = findViewById(R.id.buttonShare);


        buttonEdit.setOnClickListener(v -> {
            Intent intent = new Intent(RestaurantDetails.this,
                    AddEditRestaurantActivity.class);
            startActivity(intent);
        });


        buttonShowMap.setOnClickListener(v ->
                Toast.makeText(this, "Show on map clicked", Toast.LENGTH_SHORT).show()
        );


        buttonDirections.setOnClickListener(v ->
                Toast.makeText(this, "Get directions clicked", Toast.LENGTH_SHORT).show()
        );


        buttonShare.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT,
                    "Check out this restaurant from my Personal Restaurant Guide!");
            startActivity(Intent.createChooser(shareIntent, "Share via"));
        });
    }
}
