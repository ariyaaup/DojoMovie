/*
// Dalam HistoryActivity.kt
package com.example.dojomovie

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dojomovie.adapter.TransactionAdapter
import com.example.dojomovie.helper.DatabaseHelper
import com.example.dojomovie.model.transaction
import com.example.dojomovie.model.userLog
import com.google.android.material.navigation.NavigationView


class HistoryActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var transaction_Adapter: TransactionAdapter
    lateinit var toggle : ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val drawerLayout : DrawerLayout = findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.nav_view)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)


        toggle = ActionBarDrawerToggle(this, drawerLayout,toolbar, R.string.navigation_drawer_open,R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.drawerArrowDrawable.color = Color.parseColor("#FFFFFF")
        toggle.syncState()

        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    Toast.makeText(applicationContext, "Clicked Home", Toast.LENGTH_SHORT).show()
                }

                R.id.nav_person -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    Toast.makeText(applicationContext, "Clicked Profile", Toast.LENGTH_SHORT).show()
                }

                R.id.nav_history -> {
                    Toast.makeText(applicationContext, "Already in History", Toast.LENGTH_SHORT)
                        .show()
                }

            }

            true
        }


        dbHelper = DatabaseHelper(this)

        val transactionHistory: ArrayList<transaction> = ArrayList(dbHelper.getTransactionHistory
            (userLog.currentLoggedInUser?.id.toString()))

        transaction_Adapter = TransactionAdapter(transactionHistory)
        val recyclerView: RecyclerView = findViewById(R.id.RVTransaction)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = transaction_Adapter

    }
}
*/

// HistoryActivity.kt - Yang diperbaiki
package com.example.dojomovie

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dojomovie.adapter.TransactionAdapter
import com.example.dojomovie.helper.DatabaseHelper
import com.example.dojomovie.model.transaction
import com.example.dojomovie.model.userLog
import com.google.android.material.navigation.NavigationView

class HistoryActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var transaction_Adapter: TransactionAdapter
    private lateinit var recyclerView: RecyclerView
    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        // Setup Drawer Navigation
        setupDrawerNavigation()

        // Setup RecyclerView
        setupRecyclerView()
    }

    private fun setupDrawerNavigation() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.drawerArrowDrawable.color = Color.parseColor("#FFFFFF")
        toggle.syncState()

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    Toast.makeText(applicationContext, "Clicked Home", Toast.LENGTH_SHORT).show()
                }
                R.id.nav_person -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    Toast.makeText(applicationContext, "Clicked Profile", Toast.LENGTH_SHORT).show()
                }
                R.id.nav_history -> {
                    Toast.makeText(applicationContext, "Already in History", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }
    }

    private fun setupRecyclerView() {
        // Initialize database helper
        dbHelper = DatabaseHelper(this)

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.RVTransaction)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Load transaction history
        loadTransactionHistory()
    }

    private fun loadTransactionHistory() {
        // Get current user ID
        val currentUserId = userLog.currentLoggedInUser?.id?.toString()

        if (currentUserId != null) {
            // Get transaction history from database
            val transactionHistory: ArrayList<transaction> = ArrayList(
                dbHelper.getTransactionHistory(currentUserId)
            )

            Log.d("HistoryActivity", "Found ${transactionHistory.size} transactions for user $currentUserId")

            // Setup adapter
            transaction_Adapter = TransactionAdapter(transactionHistory)
            recyclerView.adapter = transaction_Adapter

            // Show message if no transactions found
            if (transactionHistory.isEmpty()) {
                Toast.makeText(this, "No transaction history found", Toast.LENGTH_SHORT).show()
            }
        } else {
            Log.e("HistoryActivity", "Current user ID is null")
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh data when returning to this activity
        loadTransactionHistory()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::dbHelper.isInitialized) {
            dbHelper.close()
        }
    }
}
