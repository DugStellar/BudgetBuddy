package com.orizon.budgetbuddy

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddExpenseActivity : AppCompatActivity() {

    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)

        // Initialize database
        database = AppDatabase.getDatabase(this)

        val etAmount = findViewById<EditText>(R.id.etAmount)
        val etDescription = findViewById<EditText>(R.id.etDescription)
        val spinnerCategory = findViewById<Spinner>(R.id.spinnerCategory)
        val btnSave = findViewById<Button>(R.id.btnSave)
        val btnAddPhoto = findViewById<Button>(R.id.btnAddPhoto)

        // 1. Setup Category Spinner
        val categories = arrayOf("Food", "Transport", "Rent", "Entertainment", "Other")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories)
        spinnerCategory.adapter = adapter

        // 2. Save Button Logic
        btnSave.setOnClickListener {
            val amountText = etAmount.text.toString()
            val description = etDescription.text.toString()
            val category = spinnerCategory.selectedItem.toString()

            if (amountText.isNotEmpty() && description.isNotEmpty()) {
                val amount = amountText.toDouble()
                saveToDatabase(amount, description, category)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        // 3. Optional Photo Placeholder
        btnAddPhoto.setOnClickListener {
            Toast.makeText(this, "Camera integration coming in next phase", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveToDatabase(amount: Double, desc: String, cat: String) {
        // Run database operation on IO thread
        lifecycleScope.launch(Dispatchers.IO) {
            val newExpense = Expense(
                amount = amount,
                description = desc,
                category = cat,
                date = System.currentTimeMillis().toString()
            )

            database.expenseDao().insertExpense(newExpense)

            // Switch back to Main thread to update UI
            withContext(Dispatchers.Main) {
                Toast.makeText(this@AddExpenseActivity, "Expense Added!", Toast.LENGTH_SHORT).show()
                finish() // Closes this screen and goes back to Dashboard
            }
        }
    }
}