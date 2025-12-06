package com.example.project_g4_personalrestaurantguide;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.project_g4_personalrestaurantguide.roomDb.Restaurant;
import com.example.project_g4_personalrestaurantguide.roomDb.RestaurantViewModel;

/**
 * Add/Edit Restaurant screen.
 * - User can type all fields.
 * - Validates required fields and tags format.
 * - Saves restaurant into Room database using ViewModel.
 * - For now, latitude/longitude are set to 0 (no geocoding).
 */
public class AddEditRestaurantActivity extends BaseActivity {

    // UI fields
    private EditText editName;
    private EditText editAddress;
    private EditText editPhone;
    private EditText editDescription;
    private EditText editTags;
    private RatingBar ratingBar;
    private Button buttonSave;
    private Button buttonCancel;

    // ViewModel
    private RestaurantViewModel restaurantViewModel;

    // Add / Edit mode
    private String mode = "add";
    private int restaurantId = -1;
    private Restaurant currentRestaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_edit);

        // Handle safe area insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Bind views
        editName = findViewById(R.id.editName);
        editAddress = findViewById(R.id.editAddress);
        editPhone = findViewById(R.id.editPhone);
        editDescription = findViewById(R.id.editDescription);
        editTags = findViewById(R.id.editTags);
        ratingBar = findViewById(R.id.ratingBar);
        buttonSave = findViewById(R.id.buttonSave);
        buttonCancel = findViewById(R.id.buttonCancel);

        // ViewModel
        restaurantViewModel = new ViewModelProvider(this)
                .get(RestaurantViewModel.class);

        //  mode & id
        Intent intent = getIntent();
        if (intent != null && "edit".equals(intent.getStringExtra("mode"))) {
            mode = "edit";
            restaurantId = intent.getIntExtra("restaurant_id", -1);
        }

        // Toolbar
        if ("edit".equals(mode)) {
            setupToolbar("Edit Restaurant", true);
        } else {
            setupToolbar("Add Restaurant", true);
        }


        if ("edit".equals(mode) && restaurantId != -1) {
            restaurantViewModel.getRestaurantById(restaurantId)
                    .observe(this, restaurant -> {
                        if (restaurant == null) {
                            Toast.makeText(this, "Restaurant not found.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        currentRestaurant = restaurant;

                        editName.setText(restaurant.name);
                        editAddress.setText(restaurant.address);
                        editPhone.setText(restaurant.phone);
                        editDescription.setText(restaurant.description);
                        editTags.setText(restaurant.tags);
                        ratingBar.setRating(restaurant.rating);
                    });
        }

        // Save button
        buttonSave.setOnClickListener(v -> saveRestaurant());

        // Cancel button just closes the screen
        buttonCancel.setOnClickListener(v -> finish());
    }

    /**
     * Collects data from inputs, validates it, and inserts/updates a Restaurant in DB.
     * No geocoding here: latitude/longitude are default 0.
     */
    private void saveRestaurant() {
        String name = editName.getText().toString().trim();
        String address = editAddress.getText().toString().trim();
        String phone = editPhone.getText().toString().trim();
        String description = editDescription.getText().toString().trim();
        String tags = editTags.getText().toString().trim();
        float rating = ratingBar.getRating();

        // 1. Required fields validation
        if (name.isEmpty() || address.isEmpty() || phone.isEmpty()) {
            Toast.makeText(
                    this,
                    "Name, Address and Phone are required.",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        // 2. Tags format validation: comma-separated, NO spaces
        //    Example: Coffee,lunch,asian
        if (!tags.isEmpty() && !tags.matches("^[^ ,]+(,[^ ,]+)*$")) {
            Toast.makeText(
                    this,
                    "Tags must be comma-separated with NO spaces.\nExample: Coffee,lunch,asian",
                    Toast.LENGTH_LONG
            ).show();
            return;
        }

        // 3. Restaurant object (add vs edit)
        Restaurant restaurant;
        if ("edit".equals(mode) && currentRestaurant != null) {
            // edit
            restaurant = currentRestaurant;
        } else {
            // add new restaurant
            restaurant = new Restaurant();
        }

        restaurant.name = name;
        restaurant.address = address;
        restaurant.phone = phone;
        restaurant.description = description;
        restaurant.tags = tags;
        restaurant.rating = rating;

        // 4. Latitude / longitude
        restaurant.latitude = 0.0;
        restaurant.longitude = 0.0;

        // 5. Save DB (insert or update)
        if ("edit".equals(mode)) {
            restaurantViewModel.update(restaurant);
        } else {
            restaurantViewModel.insert(restaurant);
        }

        // 6. Close screen and go back to list
        finish();
    }
}
