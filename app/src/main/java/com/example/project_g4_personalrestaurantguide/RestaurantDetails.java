package com.example.project_g4_personalrestaurantguide;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.project_g4_personalrestaurantguide.roomDb.Restaurant;
import com.example.project_g4_personalrestaurantguide.roomDb.RestaurantViewModel;

/**
 * Show-only restaurant details screen.
 * - Displays image, description, tags, rating and address.
 * - "EDIT" opens AddEditRestaurantActivity in edit mode.
 * - "SHOW ON MAP" and "GET DIRECTIONS" open Google Maps.
 * - "SHARE" shares restaurant info as plain text.
 */
public class RestaurantDetails extends BaseActivity {

    // Buttons
    private Button buttonEdit;
    private Button buttonShowMap;
    private Button buttonDirections;
    private Button buttonShare;

    // UI fields
    private ImageView imageRestaurant;
    private TextView textDescription;
    private TextView textAddress;
    private TextView textRatingLabel;
    private RatingBar ratingBar;

    // Tag chips container
    private LinearLayout tagContainer;

    // DB
    private RestaurantViewModel restaurantViewModel;
    private Restaurant currentRestaurant;
    private int restaurantId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_restaurant_details);

        // Handle system bars (status/navigation)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // ViewModel
        restaurantViewModel = new ViewModelProvider(this)
                .get(RestaurantViewModel.class);

        // Get restaurant id passed from adapter
        restaurantId = getIntent().getIntExtra("restaurant_id", -1);
        if (restaurantId == -1) {
            Toast.makeText(this, "Restaurant not found.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Toolbar
        setupToolbar("Restaurant Details", true);

        // Bind UI
        imageRestaurant = findViewById(R.id.imageRestaurant);
        textDescription = findViewById(R.id.textDescription);
        textAddress = findViewById(R.id.textAddress);
        textRatingLabel = findViewById(R.id.textRatingLabel);
        ratingBar = findViewById(R.id.ratingBar);

        buttonEdit = findViewById(R.id.buttonEdit);
        buttonShowMap = findViewById(R.id.buttonShowMap);
        buttonDirections = findViewById(R.id.buttonDirections);
        buttonShare = findViewById(R.id.buttonShare);

        tagContainer = findViewById(R.id.tagContainer);

        // Load restaurant from DB
        restaurantViewModel.getRestaurantById(restaurantId)
                .observe(this, restaurant -> {
                    if (restaurant == null) {
                        Toast.makeText(this, "Restaurant not found in database.", Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }

                    currentRestaurant = restaurant;

                    // Image (static placeholder)
                    imageRestaurant.setImageResource(R.drawable.img);

                    // Description
                    if (restaurant.description == null || restaurant.description.isEmpty()) {
                        textDescription.setText("No description");
                    } else {
                        textDescription.setText(restaurant.description);
                    }

                    // Address
                    textAddress.setText(restaurant.address);

                    // Rating: if 0, hide stars and show text; otherwise show stars
                    if (restaurant.rating <= 0f) {
                        ratingBar.setVisibility(View.GONE);
                        textRatingLabel.setText("Rating (no rating yet)");
                    } else {
                        ratingBar.setVisibility(View.VISIBLE);
                        textRatingLabel.setText("Rating");
                        ratingBar.setRating(restaurant.rating);
                    }

                    // Tags: create small chips dynamically
                    tagContainer.removeAllViews();

                    if (restaurant.tags != null && !restaurant.tags.isEmpty()) {
                        String[] tags = restaurant.tags.split(",");

                        for (String t : tags) {
                            Button chip = new Button(this);
                            chip.setText(t.trim());
                            chip.setAllCaps(false);

                            // Smaller text for chips
                            chip.setTextSize(11);

                            // Background and text color
                            chip.setBackground(getDrawable(R.drawable.tag_chip_background));
                            chip.setTextColor(getColor(android.R.color.black));

                            // Remove default minimum size
                            chip.setMinWidth(0);
                            chip.setMinimumWidth(0);
                            chip.setMinHeight(0);
                            chip.setMinimumHeight(0);

                            // Inner padding
                            chip.setPadding(16, 6, 16, 6);

                            // Space between chips
                            LinearLayout.LayoutParams params =
                                    new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT);
                            params.setMargins(6, 0, 6, 0);

                            chip.setLayoutParams(params);
                            tagContainer.addView(chip);
                        }
                    }
                });

        // EDIT -> open Add/Edit in edit mode
        buttonEdit.setOnClickListener(v -> {
            if (currentRestaurant == null) return;

            Intent intent = new Intent(RestaurantDetails.this, AddEditRestaurantActivity.class);
            intent.putExtra("mode", "edit");
            intent.putExtra("restaurant_id", restaurantId);
            startActivity(intent);
        });

        // SHOW ON MAP -> geo URI
        buttonShowMap.setOnClickListener(v -> {
            if (currentRestaurant == null) return;

            String addr = currentRestaurant.address;
            if (addr == null || addr.isEmpty()) {
                Toast.makeText(this, "Address is empty.", Toast.LENGTH_SHORT).show();
                return;
            }

            Uri uri = Uri.parse("geo:0,0?q=" + Uri.encode(addr));
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri);
            mapIntent.setPackage("com.google.android.apps.maps");

            try {
                startActivity(mapIntent);
            } catch (Exception e) {
                // Fallback: open any maps app
                mapIntent.setPackage(null);
                startActivity(mapIntent);
            }
        });

        // GET DIRECTIONS -> navigation URI
        buttonDirections.setOnClickListener(v -> {
            if (currentRestaurant == null) return;

            String addr = currentRestaurant.address;
            if (addr == null || addr.isEmpty()) {
                Toast.makeText(this, "Address is empty.", Toast.LENGTH_SHORT).show();
                return;
            }

            Uri uri = Uri.parse("google.navigation:q=" + Uri.encode(addr));
            Intent navIntent = new Intent(Intent.ACTION_VIEW, uri);
            navIntent.setPackage("com.google.android.apps.maps");

            try {
                startActivity(navIntent);
            } catch (Exception e) {
                navIntent.setPackage(null);
                startActivity(navIntent);
            }
        });

        // SHARE -> text share intent
        buttonShare.setOnClickListener(v -> {
            if (currentRestaurant == null) return;

            StringBuilder sb = new StringBuilder();
            sb.append("Check out this restaurant from my Personal Restaurant Guide!\n\n");
            sb.append("Name: ").append(currentRestaurant.name).append("\n");
            sb.append("Address: ").append(currentRestaurant.address).append("\n");

            if (currentRestaurant.phone != null && !currentRestaurant.phone.isEmpty()) {
                sb.append("Phone: ").append(currentRestaurant.phone).append("\n");
            }
            if (currentRestaurant.tags != null && !currentRestaurant.tags.isEmpty()) {
                sb.append("Tags: ").append(currentRestaurant.tags).append("\n");
            }

            sb.append("Rating: ").append(currentRestaurant.rating).append(" / 5");

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, sb.toString());
            startActivity(Intent.createChooser(shareIntent, "Share restaurant"));
        });
    }
}
