package com.example.project_g4_personalrestaurantguide.roomDb;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RestaurantRepository {

    private final RestaurantDao restaurantDao;
    private final LiveData<List<Restaurant>> allRestaurants;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public RestaurantRepository(Application app){
        AppDatabase db = AppDatabase.getInstance(app);
        restaurantDao = db.restaurantDao();
        allRestaurants = restaurantDao.getAll();
    }

    public LiveData<List<Restaurant>> getAllRestaurants(){
        return allRestaurants;
    }

    public void insert(Restaurant r){
        executor.execute(() -> restaurantDao.insert(r));
    }

    public void update(Restaurant r){
        executor.execute(() -> restaurantDao.update(r));
    }

    public void delete(Restaurant r){
        executor.execute(() -> restaurantDao.delete(r));
    }

    public LiveData<List<Restaurant>> searchByName(String query){
        return restaurantDao.searchByName(query);
    }

    public LiveData<List<Restaurant>> searchByTag(String tag){
        return restaurantDao.searchByTag(tag);
    }

    // 🔥 Add/Edit/Details 화면에서 가장 중요
    public LiveData<Restaurant> getById(int id) {
        return restaurantDao.getById(id);
    }
}
