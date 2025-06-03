package com.example.dojomovie

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.dojomovie.helper.DatabaseHelper
import com.example.dojomovie.model.userLog

class LoginActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val dbHelper = DatabaseHelper(this)
        val userList = dbHelper.getUser()

        val signInButton = findViewById<Button>(R.id.btnSignIN)
        val phoneNumET = findViewById<EditText>(R.id.etPhoneNumLogin)
        val passwordET = findViewById<EditText>(R.id.etPasswordLogin)


        signInButton.setOnClickListener {
            val phoneInput = phoneNumET.text.toString()
            val passInput = passwordET.text.toString()

            dbHelper.getUser()

            val user = userList.find { it.phoneNumber == phoneInput && it.password == passInput }
            if (user != null) {
                userLog.currentLoggedInUser = user
                Log.d("Login", "User logged in: ${user.phoneNumber}")
            }


            if (user != null) {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                Log.d("login", "Sukses")
                Log.d("login", dbHelper.loggedUser?.id.toString())

            } else {
                Log.d("login", "Gagal - user tidak ditemukan")
            }
        }

        val tvRegister = findViewById<TextView>(R.id.tvRegisterHere)
        tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

}