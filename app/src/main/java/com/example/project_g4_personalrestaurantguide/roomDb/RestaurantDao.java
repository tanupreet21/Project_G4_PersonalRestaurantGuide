package com.example.project_g4_personalrestaurantguide.roomDb;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RestaurantDao {

    @Insert
    void insert(Restaurant restaurant);

    @Update
    void update(Restaurant restaurant);

    @Delete
    void delete(Restaurant restaurant);

    @Query("SELECT * FROM restaurants ORDER BY name ASC")
    LiveData<List<Restaurant>> getAll();

    // 🔥 반드시 LiveData<Restaurant> 이어야 한다!!
    @Query("SELECT * FROM restaurants WHERE id = :id LIMIT 1")
    LiveData<Restaurant> getById(int id);

    @Query("SELECT * FROM restaurants WHERE name LIKE '%' || :query || '%'")
    LiveData<List<Restaurant>> searchByName(String query);

    @Query("SELECT * FROM restaurants WHERE tags LIKE '%' || :tag || '%'")
    LiveData<List<Restaurant>> searchByTag(String tag);
}
