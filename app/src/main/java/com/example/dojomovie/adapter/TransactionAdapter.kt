/*
// Dalam file transaction_adapter.kt
package com.example.dojomovie.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dojomovie.helper.DatabaseHelper
import com.example.dojomovie.model.transaction


class TransactionAdapter(private var transactionList: ArrayList<transaction>) : RecyclerView
    .Adapter<TransactionAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val movieName: TextView = itemView.findViewById(com.example.dojomovie.R.id.movieName)
        val quantity: TextView = itemView.findViewById(com.example.dojomovie.R.id.quantity)
        val price: TextView = itemView.findViewById(com.example.dojomovie.R.id.price)
    }

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(com.example.dojomovie.R.layout
            .item_transaction, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = transactionList[position]


        holder.quantity.text = "Quantity: ${currentItem.quantity}"

        val dbHelper = DatabaseHelper(holder.itemView.context)
        val movie = dbHelper.getMovieById(currentItem.movie_id)

        // Memastikan movie tidak null
        if (movie != null) {
            holder.movieName.text = movie.movieName
            holder.price.text = "Price : Rp ${movie.moviePrice.toString()}/Ticket"
        }

    }
}
*/
// TransactionAdapter.kt - Yang diperbaiki
package com.example.dojomovie.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dojomovie.R
import com.example.dojomovie.helper.DatabaseHelper
import com.example.dojomovie.model.transaction

class TransactionAdapter(private var transactionList: ArrayList<transaction>) :
    RecyclerView.Adapter<TransactionAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val movieName: TextView = itemView.findViewById(R.id.movieName)
        val quantity: TextView = itemView.findViewById(R.id.quantity)
        val price: TextView = itemView.findViewById(R.id.price)
        val transactionId: TextView = itemView.findViewById(R.id.transactionId)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = transactionList[position]

        // Set transaction ID
        holder.transactionId.text = "ID: #${currentItem.transaction_id}"

        // Set quantity
        holder.quantity.text = "Quantity: ${currentItem.quantity}"

        // Get movie details from database
        val dbHelper = DatabaseHelper(holder.itemView.context)
        val movie = dbHelper.getMovieById(currentItem.movie_id)

        // Set movie details
        if (movie != null) {
            holder.movieName.text = movie.movieName

            // Calculate total price (price per ticket * quantity)
            val totalPrice = movie.moviePrice * currentItem.quantity
            holder.price.text = "Total: Rp ${String.format("%,d", totalPrice)}"
        } else {
            holder.movieName.text = "Movie not found"
            holder.price.text = "Price: -"
        }
    }

    // Function to update data
    fun updateTransactions(newTransactions: ArrayList<transaction>) {
        transactionList = newTransactions
        notifyDataSetChanged()
    }
}
