package com.example.dojomovie

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.dojomovie.helper.DatabaseHelper

class RegisterActivity : AppCompatActivity() {
    private lateinit var DatabaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val signUpButton = findViewById<Button>(R.id.signUpButton)
        val phoneNumET = findViewById<EditText>(R.id.phoneNumET)
        val passwordET = findViewById<EditText>(R.id.passwordET)
        val confirmPasswordET = findViewById<EditText>(R.id.confirmPasswordET)

        DatabaseHelper = DatabaseHelper(this)

        signUpButton.setOnClickListener {
            val phoneNumber = phoneNumET.text.toString().trim()
            val password = passwordET.text.toString().trim()
            val confirmPassword = confirmPasswordET.text.toString().trim()

            // vallidasi input
            if (validateInput(phoneNumber, password, confirmPassword)) {
                checkSMSPermission(phoneNumber, password)

                // bersihin input
                phoneNumET.setText("")
                passwordET.setText("")
                confirmPasswordET.setText("")
            }
        }

        logExistingUsers()
    }

    private fun validateInput(phoneNumber: String, password: String, confirmPassword: String): Boolean {
        if (phoneNumber.isEmpty()) {
            // errorempety
            Log.d("InputError", "Phone number is empty")
            return false
        }

        if (password.isEmpty()) {
            //error empty
            Log.d("InputError", "Phone number is empty")
        return false
        }

        if (password != confirmPassword) {
            // ga sama passwordnya
            Log.d("InputError", "pass dont match")
            return false
        }

        return true
    }

    private fun logExistingUsers() {
        val userList = DatabaseHelper.getUser()
        for (user in userList) {
            Log.d(
                "UserData",
                "ID: ${user.id}, Phone: ${user.phoneNumber}, Password: ${user.password}"
            )
        }
    }

    fun registerClicked(view: View) {
        val intent = Intent(this, OTPActivity::class.java)
        startActivity(intent)
    }

    private fun checkSMSPermission(phoneNumber: String, password: String) {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.SEND_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request SMS permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.SEND_SMS),
                SMS_PERMISSION_REQUEST_CODE
            )
            Log.d("SMS_PERMISSION", "Requesting SMS permission")
        } else {
            // Permission already granted, proceed to OTP activity
            navigateToOTPActivity(phoneNumber, password)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            SMS_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("SMS_PERMISSION", "SMS permission granted")

                    val phoneNumber = findViewById<EditText>(R.id.phoneNumET).text.toString().trim()
                    val password = findViewById<EditText>(R.id.passwordET).text.toString().trim()
                    navigateToOTPActivity(phoneNumber, password)

                } else {
                    Log.d("SMS_PERMISSION", "SMS permission denied")

                }
            }
        }
    }

    private fun navigateToOTPActivity(phoneNumber: String, password: String) {
        val intent = Intent(this, OTPActivity::class.java)
        intent.putExtra("PhoneNumber", phoneNumber)
        intent.putExtra("Password", password)
        startActivity(intent)
    }

    companion object {
        private const val SMS_PERMISSION_REQUEST_CODE = 100
    }
}

