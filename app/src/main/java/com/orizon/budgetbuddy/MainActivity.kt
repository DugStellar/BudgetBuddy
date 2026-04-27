package com.orizon.budgetbuddy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // This matches activity_main.xml
        setContentView(R.layout.activity_main)

        // Find the button in your activity_main.xml
        // (Make sure the button in XML has android:id="@+id/btnStart")
        val btnStart = findViewById<Button>(R.id.btnStart)

        btnStart.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish() // Closes MainActivity so you don't go back to it
        }
    }
}