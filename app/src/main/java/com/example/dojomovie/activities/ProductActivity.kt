
package com.example.dojomovie

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dojomovie.helper.DatabaseHelper
import com.example.dojomovie.model.transaction
import com.example.dojomovie.model.userLog

class ProductActivity : AppCompatActivity() {

    private var currentQuantity = 0
    val dbHelper = DatabaseHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        val backButton = findViewById<ImageView>(R.id.arrowBack)
        val poster = findViewById<ImageView>(R.id.posterMovie)
        val judul = findViewById<TextView>(R.id.MovieTitle)
        val harga = findViewById<TextView>(R.id.MoviePrice)
        val quantity = findViewById<EditText>(R.id.quantity)
        val totalHarga = findViewById<TextView>(R.id.totalPriceText)
        val plusButton = findViewById<Button>(R.id.plus)
        val minButton = findViewById<Button>(R.id.minus)
        val buyNowButton = findViewById<Button>(R.id.buyNowButton)

        var movieImage = intent.getStringExtra("MovieImage")
        var movieName = intent.getStringExtra("MovieName")
        var moviePrice = intent.getIntExtra("MoviePrice", 0)
        var movieID = intent.getStringExtra("Movie_ID")

        backButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        val imageResource = resources.getIdentifier(movieImage, "drawable", packageName)
        poster.setImageResource(imageResource)
        judul.text = movieName
        harga.text = "Price : Rp ${moviePrice}"

        plusButton.setOnClickListener {
            currentQuantity += 1
            quantity.setText(currentQuantity.toString())
            updateTotalPrice(moviePrice, totalHarga)
        }

        minButton.setOnClickListener {
            if (currentQuantity > 1) {
                currentQuantity -= 1
                quantity.setText(currentQuantity.toString())
                updateTotalPrice(moviePrice, totalHarga)
            }
        }

        buyNowButton.setOnClickListener {
            val quantity = quantity.text.toString().toIntOrNull() ?: 0
            val loggedInUser = userLog.currentLoggedInUser
            if (loggedInUser == null) {
                Toast.makeText(this, "Please log in first", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                return@setOnClickListener
            }

            val transaction = transaction(0, movieID.toString(), loggedInUser.id.toString(), quantity)
            dbHelper.insertTransaction(transaction)

            if (quantity > 0) {
                Toast.makeText(this, "Transaction successful!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please enter a valid quantity", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateTotalPrice(moviePrice: Int, totalPriceText: TextView) {
        val totalPrice = currentQuantity * moviePrice
        totalPriceText.text = "Total Price: Rp $totalPrice"
    }
}

