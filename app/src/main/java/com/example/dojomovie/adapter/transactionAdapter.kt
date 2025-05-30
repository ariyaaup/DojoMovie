// Dalam file transaction_adapter.kt
package com.example.dojomovie.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dojomovie.helper.DatabaseHelper
import com.example.dojomovie.model.transaction


class transactionAdapter(private var transactionList: ArrayList<transaction>) : RecyclerView
    .Adapter<transactionAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val movieName: TextView = itemView.findViewById(com.example.dojomovie.R.id.movieName)
        val quantity: TextView = itemView.findViewById(com.example.dojomovie.R.id.quantity)
        val price: TextView = itemView.findViewById(com.example.dojomovie.R.id.price)
    }

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(com.example.dojomovie.R.layout
            .activity_transaction, parent, false)
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
