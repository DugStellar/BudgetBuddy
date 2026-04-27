package com.orizon.budgetbuddy

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DashboardActivity : AppCompatActivity() {

    private lateinit var adapter: ExpenseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // 1. Initialize UI Elements
        val tvTotalBalance = findViewById<TextView>(R.id.tvTotalBalance)
        val rvExpenses = findViewById<RecyclerView>(R.id.rvExpenses)
        val fabAdd = findViewById<FloatingActionButton>(R.id.fabAdd)

        // 2. Setup RecyclerView
        // We start with an empty list; the Observer will fill it up
        adapter = ExpenseAdapter(emptyList())
        rvExpenses.layoutManager = LinearLayoutManager(this)
        rvExpenses.adapter = adapter

        // 3. Initialize Database
        val database = AppDatabase.getDatabase(this)

        // 4. Observe the Total Balance
        // This updates the R0.00 text automatically
        database.expenseDao().getTotalExpenses().observe(this, Observer { total ->
            val displayTotal = total ?: 0.0
            tvTotalBalance.text = "R${String.format("%.2f", displayTotal)}"
        })

        // 5. Observe the List of Expenses
        // This updates the scrollable list whenever a new expense is added
        database.expenseDao().getAllExpenses().observe(this, Observer { expenses ->
            expenses?.let {
                adapter.updateData(it)
            }
        })

        // 6. Navigation to Add Expense Screen
        fabAdd.setOnClickListener {
            val intent = Intent(this, AddExpenseActivity::class.java)
            startActivity(intent)
        }
    }
}