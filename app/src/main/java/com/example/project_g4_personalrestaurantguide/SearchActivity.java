package com.example.project_g4_personalrestaurantguide;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity {

    private RecyclerView searchRecyclerView;
    private RestaurantAdapter adapter;
    private List<Restaurant> restaurantList;
    private List<Restaurant> filteredList;
    private EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupToolbar("Search", true);

        searchRecyclerView = findViewById(R.id.searchRecyclerView);
        searchEditText = findViewById(R.id.searchEditText);

        restaurantList = new ArrayList<>();
        restaurantList.add(new Restaurant(
                "Pizza Place",
                "Toronto",
                new String[]{"Italian", "Vegan"},
                4.5f,
                R.drawable.img
        ));
        restaurantList.add(new Restaurant(
                "Sushi Spot",
                "Vancouver",
                new String[]{"Japanese", "Non-Veg"},
                4.0f,
                R.drawable.img
        ));
        restaurantList.add(new Restaurant(
                "Hakka Garden Chinese Restaurant",
                "East York",
                new String[]{"Chinese"},
                4.0f,
                R.drawable.img
        ));
        restaurantList.add(new Restaurant(
                "Mandi Afandi",
                "East York",
                new String[]{"Middle East"},
                3.5f,
                R.drawable.img
        ));


        filteredList = new ArrayList<>(restaurantList);

        adapter = new RestaurantAdapter(filteredList, null);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchRecyclerView.setAdapter(adapter);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterRestaurants(s.toString());
            }
        });
    }

    private void filterRestaurants(String query) {
        filteredList.clear();

        if (TextUtils.isEmpty(query)) {
            filteredList.addAll(restaurantList);
        } else {
            String lowerQuery = query.toLowerCase().trim();

            for (Restaurant restaurant : restaurantList) {
                String name = restaurant.getName().toLowerCase();
                String location = restaurant.getLocation().toLowerCase();
                String tagsJoined = TextUtils.join(", ", restaurant.getTags()).toLowerCase();

                if (name.contains(lowerQuery)
                        || location.contains(lowerQuery)
                        || tagsJoined.contains(lowerQuery)) {
                    filteredList.add(restaurant);
                }
            }
        }

        adapter.notifyDataSetChanged();
    }
}