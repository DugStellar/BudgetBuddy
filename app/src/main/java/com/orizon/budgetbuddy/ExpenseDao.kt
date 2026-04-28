package com.orizon.budgetbuddy

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ExpenseDao {
    @Insert
    suspend fun insert(expense: Expense)

    @Query("SELECT * FROM expense_table ORDER BY date DESC, id DESC")
    fun getAllExpenses(): LiveData<List<Expense>>

    @Query("SELECT * FROM expense_table WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getExpensesInRange(startDate: String, endDate: String): LiveData<List<Expense>>

    @Query("SELECT SUM(amount) FROM expense_table")
    fun getTotalExpenses(): LiveData<Double>

    @Query("SELECT SUM(amount) FROM expense_table WHERE date BETWEEN :startDate AND :endDate")
    fun getTotalExpensesInRange(startDate: String, endDate: String): LiveData<Double>

    @androidx.room.Delete
    suspend fun delete(expense: Expense)
}