package com.vanilla.sendotp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {

    private var otp: String? = null
    private var phone: String? = null
    private val message = "is your verification code."

    private lateinit var btnSendOTP: Button
    private lateinit var otpCode: TextInputEditText
    private lateinit var phoneNumber: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSendOTP = findViewById(R.id.btnSendOTP)
        otpCode = findViewById(R.id.otpCode)
        phoneNumber = findViewById(R.id.phoneNumber)

        btnSendOTP.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                sendOTP()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS), 100)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendOTP()
            } else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendOTP() {
        otp = otpCode.text.toString()
        phone = phoneNumber.text.toString()

        if (!otp.isNullOrEmpty() && !phone.isNullOrEmpty()) {
            val smsManager = SmsManager.getDefault()
            val parts = smsManager.divideMessage("$otp $message")
            smsManager.sendMultipartTextMessage(phone, null, parts, null, null)
            Toast.makeText(this, "OTP sent successfully!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Please enter both OTP and phone number", Toast.LENGTH_SHORT).show()
        }
    }
}
