package com.orizon.budgetbuddy

import android.content.Intent
import android.os.Bundle
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DashboardActivity : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private lateinit var expenseAdapter: ExpenseAdapter
    private val startingBudget = 15000.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        database = AppDatabase.getDatabase(this)

        val tvTotalBalance = findViewById<TextView>(R.id.tvTotalBalance)
        val sbGoalProgress = findViewById<SeekBar>(R.id.sbGoalProgress)
        val fabAdd = findViewById<FloatingActionButton>(R.id.fabAdd)
        val rvExpenses = findViewById<RecyclerView>(R.id.rvExpenses)

        // Setup RecyclerView
        expenseAdapter = ExpenseAdapter(emptyList())
        rvExpenses.layoutManager = LinearLayoutManager(this)
        rvExpenses.adapter = expenseAdapter

        // Observe Data
        database.expenseDao().getAllExpenses().asLiveData().observe(this) { expenses ->
            expenseAdapter.updateData(expenses)

            val totalSpent = expenses.sumOf { it.amount }
            val currentBalance = startingBudget - totalSpent

            tvTotalBalance.text = "R${String.format("%.2f", currentBalance)}"

            val progressPercent = ((totalSpent / startingBudget) * 100).toInt()
            sbGoalProgress.progress = if (progressPercent > 100) 100 else progressPercent
        }

        // Swipe to Delete Logic
        val swipeHandler = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(r: RecyclerView, vh: RecyclerView.ViewHolder, t: RecyclerView.ViewHolder) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val expenseToDelete = expenseAdapter.getExpenseAt(position)

                // Delete from DB on background thread
                lifecycleScope.launch(Dispatchers.IO) {
                    database.expenseDao().deleteExpense(expenseToDelete)
                }
                Toast.makeText(this@DashboardActivity, "Expense removed", Toast.LENGTH_SHORT).show()
            }
        }
        ItemTouchHelper(swipeHandler).attachToRecyclerView(rvExpenses)

        fabAdd.setOnClickListener {
            startActivity(Intent(this, AddExpenseActivity::class.java))
        }

        sbGoalProgress.setOnTouchListener { _, _ -> true }
    }
}