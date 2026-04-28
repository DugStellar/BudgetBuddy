package com.orizon.budgetbuddy

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CategoryDao {
    @Insert
    suspend fun insert(category: Category)

    @Query("SELECT * FROM category_table ORDER BY name ASC")
    fun getAllCategories(): LiveData<List<Category>>

    @Delete
    suspend fun delete(category: Category)

    @Query("SELECT * FROM category_table WHERE name = :name LIMIT 1")
    suspend fun getCategoryByName(name: String): Category?
}