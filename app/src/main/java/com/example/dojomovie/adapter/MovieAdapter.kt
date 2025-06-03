/*
package com.example.dojomovie.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dojomovie.helper.DatabaseHelper
import com.example.dojomovie.ProductActivity
import com.example.dojomovie.model.Movie

class MovieAdapter(private var movieList: ArrayList<Movie>) : RecyclerView.Adapter<MovieAdapter
    .MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val movieImage: ImageView = itemView.findViewById(com.example.dojomovie.R.id.posterFilmIV)
        val movieName: TextView = itemView.findViewById(com.example.dojomovie.R.id.movieNameTV)
        val moviePrice: TextView = itemView.findViewById(com.example.dojomovie.R.id.moviePriceTV)
    }

    private lateinit var Database_Helper: DatabaseHelper

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            com.example.dojomovie.R.layout
                .activity_movie_items, parent, false
        )
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val dbHelper = DatabaseHelper(holder.itemView.context)
        val movieList = dbHelper.getAllMovies()

        val currentItem = movieList[position]

        val imageResId = holder.itemView.context.resources.getIdentifier(
            currentItem.movieImage,
            "drawable", holder.itemView.context.packageName
        )

        if (imageResId != 0) {
            holder.movieImage.setImageResource(imageResId)
        } else {
            Log.e("ImageLoading", "Resource ID not found for ${currentItem.movieImage}")
        }

        holder.movieName.text = currentItem.movieName
        holder.moviePrice.text = "Rp ${currentItem.moviePrice}"

        holder.itemView.setOnClickListener {

            val context = it.context
            val intent = Intent(context, ProductActivity::class.java)
            intent.putExtra("MovieImage", currentItem.movieImage)
            intent.putExtra("MovieName", currentItem.movieName)
            intent.putExtra("MoviePrice", currentItem.moviePrice)
            intent.putExtra("Movie_ID", currentItem.movieID)
            context.startActivity(intent)
        }
    }

}*/

package com.example.dojomovie.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dojomovie.ProductActivity
import com.example.dojomovie.R
import com.example.dojomovie.model.Movie

class MovieAdapter(private var movieList: ArrayList<Movie>) : RecyclerView.Adapter<MovieAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val movieImage: ImageView = itemView.findViewById(R.id.tvMovieImage)
        val movieName: TextView = itemView.findViewById(R.id.tvMovieName)
        val moviePrice: TextView = itemView.findViewById(R.id.tvMoviePrice)
    }

     val imageResourceMap = mapOf(
        "MV001" to R.drawable.kong_zilla,
        "MV002" to R.drawable.final_fatalion,
        "MV003" to R.drawable.bond_jumpshoot
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.activity_movie_items,
            parent,
            false
        )
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = movieList[position]

        val imageResId = imageResourceMap[currentItem.movieID] ?: R.drawable.ic_launcher_background
        holder.movieImage.setImageResource(imageResId)

        holder.movieName.text = currentItem.movieName
        holder.moviePrice.text = "Rp ${currentItem.moviePrice}"

        holder.itemView.setOnClickListener {
            val context = it.context
            val intent = Intent(context, ProductActivity::class.java)
            intent.putExtra("MovieImage", currentItem.movieImage)
            intent.putExtra("MovieName", currentItem.movieName)
            intent.putExtra("MoviePrice", currentItem.moviePrice)
            intent.putExtra("Movie_ID", currentItem.movieID)
            context.startActivity(intent)
        }
    }

 /*   fun updateMovieList(newMovieList: ArrayList<Movie>) {
        this.movieList = newMovieList
        notifyDataSetChanged()
    }*/
}
