package com.example.project_g4_personalrestaurantguide;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MapActivity extends BaseActivity {

    private Button btnGetDirections;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        setupToolbar("Map View", true);

        btnGetDirections = findViewById(R.id.btn_get_directions);

        // Set up Get Directions button
        btnGetDirections.setOnClickListener(v -> {
            Toast.makeText(MapActivity.this,
                    "Opening Google Maps for directions...",
                    Toast.LENGTH_SHORT).show();
        });
    }

}