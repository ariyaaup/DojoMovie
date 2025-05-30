
package com.example.dojomovie

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.dojomovie.helper.DatabaseHelper
import com.example.dojomovie.adapter.MovieAdapter
import com.example.dojomovie.model.Movie
import com.google.android.material.navigation.NavigationView
import org.json.JSONArray
import org.json.JSONException

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var toggle : ActionBarDrawerToggle
    private lateinit var requestQueue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.drawerArrowDrawable.color = Color.parseColor("#FFFFFF")
        toggle.syncState()

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> Toast.makeText(applicationContext, "Clicked Home", Toast.LENGTH_SHORT).show()
                R.id.nav_person -> startActivity(Intent(this, ProfileActivity::class.java))
                R.id.nav_history -> startActivity(Intent(this, HistoryActivity::class.java))
            }
            true
        }

        val recyclerView: RecyclerView = findViewById(R.id.RVHomePage)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        requestQueue = Volley.newRequestQueue(this)
        val url = "https://api.npoint.io/66cce8acb8f366d2a508"
        val request = JsonArrayRequest(Request.Method.GET, url, null,
            { response ->
                try {
                    val movieList = parseJSON(response)
                    val dbHelper = DatabaseHelper(this)
                    dbHelper.insertAllMovies(movieList)
                    recyclerView.adapter = MovieAdapter(movieList)
                    Log.d("HomeActivity", "Loaded ${movieList.size} movies")
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error ->
                Log.e("HomeActivity", "Network error: ${error.message}")
                Toast.makeText(applicationContext, "Error: $error", Toast.LENGTH_SHORT).show() }
        )
        requestQueue.add(request)
    }

    private fun parseJSON(jsonArray: JSONArray): ArrayList<Movie> {
        val movieList = ArrayList<Movie>()

            for (i in 0 until jsonArray.length()) {
                val movieObject = jsonArray.getJSONObject(i)
                val movie = Movie(
                    movieID = movieObject.getString("id"),
                    movieImage = movieObject.getString("image"),
                    movieName = movieObject.getString("title"),
                    moviePrice = movieObject.getInt("price")
                )
                movieList.add(movie)
            }

        return movieList
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return true
    }
}

