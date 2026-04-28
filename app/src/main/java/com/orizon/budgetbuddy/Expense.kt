package com.orizon.budgetbuddy

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expense_table")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val description: String,
    val amount: Double,
    val category: String,
    val date: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val photoUri: String? = null
)