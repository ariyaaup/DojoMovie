package com.example.dojomovie

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.telephony.SmsManager
import android.telephony.SmsManager.getDefault
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.dojomovie.helper.DatabaseHelper
import com.example.dojomovie.model.userData

class OTPActivity : AppCompatActivity() {

    private lateinit var countdownText: TextView
    private var currentOtp: String = ""
    private lateinit var smsManager : SmsManager
    private lateinit var Database_Helper : DatabaseHelper
    private var phoneNumbers = ""
    private lateinit var otp: EditText



    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        @Suppress("DEPRECATION")
        smsManager = getDefault()
        var phoneNum = intent.getStringExtra("PhoneNumber")
        var passwords = intent.getStringExtra("Password")
        phoneNumbers = phoneNum.toString()
        currentOtp = generateOtp(6)

        val otp = findViewById<EditText>(R.id.pinView)
        val OKButton = findViewById<Button>(R.id.OkButton)

        Database_Helper = DatabaseHelper(this)
        countdownText = findViewById(R.id.countdownText)

        startCountdown()
        sendSMS(phoneNum.toString(), currentOtp)

        OKButton.setOnClickListener(){
            if (otp.text.toString() == currentOtp) {
                val user = userData().apply {
                    id = 0
                    phoneNumber = phoneNum.toString()
                    password = passwords.toString()
                }
                Database_Helper.insertUser(user)

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }else{
                Log.d("Register", "Register Failed!")
                otp.setText("")
            }
        }

/*
        setupOtpMessageSafe(phoneNum ?: "")
*/



    }

  /*  private fun generateOtpMessage(phoneNumber: String): String {
        val maskedNumber = maskPhoneNumberFlexible(phoneNumber)
        return
    }*/
/*
    private fun maskPhoneNumberFlexible(phoneNumber: String): String {
        if (phoneNumber.isBlank() || phoneNumber.length <= 4) {
            return phoneNumber
        }

        val cleanNumber = phoneNumber.replace(Regex("[^0-9+]"), "")

        return when {
            cleanNumber.length in 5..6 -> {
                val first = cleanNumber.take(2)
                val last = cleanNumber.takeLast(2)
                "$first-xx-$last"
            }

            cleanNumber.length in 7..10 -> {
                val first = cleanNumber.take(3)
                val last = cleanNumber.takeLast(3)
                "$first-xxxx-$last"
            }

            cleanNumber.length >= 11 -> {
                val first = cleanNumber.take(4)
                val last = cleanNumber.takeLast(4)
                "$first-xxxx-$last"
            }

            else -> phoneNumber
        }
    }*/

  /*  private fun setupOtpMessageSafe(phoneNumber: String) {
        try {
            val messageTextView = findViewById<TextView>(R.id.pinView)
         *//*   val otpMessage = generateOtpMessage(phoneNumber)*//*
            messageTextView.text = otpMessage
        } catch (e: Exception) {
            Log.d("OTP", "OTP message TextView not found: ${e.message}")
            // Bisa set ke TextView lain atau tampilkan di Toast
            val otpMessage = generateOtpMessage(phoneNumber)
            Log.d("OTP", otpMessage)
        }
    }*/

    private fun startCountdown() {
        val timer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = millisUntilFinished / 1000
                countdownText.text = "Resend Code In : $secondsLeft s"
            }

            override fun onFinish() {
                countdownText.text = "Resend OTP Code"

                countdownText.setOnClickListener {
                    startCountdown()
                    val otp = findViewById<EditText>(R.id.pinView)
                    currentOtp = generateOtp(6) // save otp barunya
                    sendSMS(phoneNumbers.toString(), currentOtp)
                    otp.setText("") // reset
                    otp.isEnabled = true
                    otp.requestFocus()
                }

            }
        }
        timer.start()
    }
    fun generateOtp(length: Int = 6): String {
        val chars = "0123456789"
        return (1..length)
            .map { chars.random() }
            .joinToString("")
    }

    fun sendSMS(phoneNumber : String, otpCode : String){
        smsManager.sendTextMessage(phoneNumber, null, "Your OTP Code is : ${otpCode}. Do not " +
                "send this code" +
                "to everyone!", null, null)
    }
}

