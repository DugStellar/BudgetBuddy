package com.orizon.budgetbuddy

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    // Adds or updates an expense
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: Expense)

    // Removes an expense (useful for swiping to delete)
    @Delete
    suspend fun deleteExpense(expense: Expense)

    // Retrieves all expenses, newest first, as a Flow for real-time UI updates
    @Query("SELECT * FROM expense_table ORDER BY date DESC")
    fun getAllExpenses(): Flow<List<Expense>>

    // Optional: Clears everything (good for a 'Reset' button)
    @Query("DELETE FROM expense_table")
    suspend fun deleteAllExpenses()
}