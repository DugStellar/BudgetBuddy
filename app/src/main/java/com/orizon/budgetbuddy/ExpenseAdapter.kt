package com.orizon.budgetbuddy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ExpenseAdapter(private var expenses: List<Expense>) : RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    // The ViewHolder finds the specific views in your item_expense.xml
    class ExpenseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvCategory: TextView = view.findViewById(R.id.tvItemCategory)
        val tvDescription: TextView = view.findViewById(R.id.tvItemDescription)
        val tvAmount: TextView = view.findViewById(R.id.tvItemAmount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_expense, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val item = expenses[position]
        holder.tvCategory.text = item.category
        holder.tvDescription.text = item.description
        holder.tvAmount.text = "R${String.format("%.2f", item.amount)}"
    }

    override fun getItemCount() = expenses.size

    // Helper function to identify which item was swiped in DashboardActivity
    fun getExpenseAt(position: Int): Expense {
        return expenses[position]
    }

    // Called by the LiveData observer to refresh the list automatically
    fun updateData(newExpenses: List<Expense>) {
        expenses = newExpenses
        notifyDataSetChanged()
    }
}