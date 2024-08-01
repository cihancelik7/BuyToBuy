package com.example.buytobuy.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.buytobuy.databinding.ActivityOtpBinding
import com.example.buytobuy.sdk.PaymentCallback
import com.example.buytobuy.sdk.PaymentSDK

class OtpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOtpBinding
    private var generatedOtp: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        generatedOtp = intent.getStringExtra("generatedOtp")

        binding.verifyOtpBtn.setOnClickListener {
            val enteredOtp = binding.otpEditText.text.toString()
            verifyOtp(enteredOtp)
        }
    }

    private fun verifyOtp(otp: String) {
        binding.otpProgressBar.visibility = android.view.View.VISIBLE

        if (otp == generatedOtp) {
            binding.otpProgressBar.visibility = android.view.View.GONE
            Toast.makeText(this, "Payment Confirmed", Toast.LENGTH_SHORT).show()
            // Ödeme işleminin başarıyla tamamlandığı durum
        } else {
            binding.otpProgressBar.visibility = android.view.View.GONE
            Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show()
            // Hatalı OTP girişi durumu
        }
    }

}