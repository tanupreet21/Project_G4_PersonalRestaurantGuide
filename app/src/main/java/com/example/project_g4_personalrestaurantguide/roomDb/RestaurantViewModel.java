package com.example.project_g4_personalrestaurantguide.roomDb;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class RestaurantViewModel extends AndroidViewModel {

    private final RestaurantRepository repository;
    private final LiveData<List<Restaurant>> allRestaurants;

    public RestaurantViewModel(@NonNull Application application){
        super(application);
        repository = new RestaurantRepository(application);
        allRestaurants = repository.getAllRestaurants();
    }

    public LiveData<List<Restaurant>> getAllRestaurants(){
        return allRestaurants;
    }

    public void insert(Restaurant restaurant){
        repository.insert(restaurant);
    }

    public void update(Restaurant restaurant){
        repository.update(restaurant);
    }

    public void delete(Restaurant restaurant){
        repository.delete(restaurant);
    }

    public LiveData<List<Restaurant>> searchByName(String query){
        return repository.searchByName(query);
    }

    public LiveData<List<Restaurant>> searchByTag(String tag){
        return repository.searchByTag(tag);
    }

    // 🔥 반드시 필요: Details & AddEdit에서 사용
    public LiveData<Restaurant> getRestaurantById(int id){
        return repository.getById(id);
    }
}
