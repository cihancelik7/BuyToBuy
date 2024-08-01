package com.example.buytobuy.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.buytobuy.databinding.ActivityOtpBinding
import com.example.buytobuy.helper.ManagementCart
import com.example.buytobuy.model.ItemsModel
import com.google.firebase.database.FirebaseDatabase

class OtpActivity : BaseActivity() {
    private lateinit var binding: ActivityOtpBinding
    private var generatedOtp: String? = null
    private lateinit var managementCart: ManagementCart
    private val itemsToBeMoved = mutableListOf<ItemsModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        managementCart = ManagementCart(this)
        generatedOtp = intent.getStringExtra("generatedOtp")

        itemsToBeMoved.addAll(managementCart.getListCart()) // Sepet verilerini itemsToBeMoved listesine ekle

        binding.verifyOtpBtn.setOnClickListener {
            val enteredOtp = binding.otpEditText.text.toString()
            verifyOtp(enteredOtp)
        }
    }

    private fun verifyOtp(otp: String) {
        binding.otpProgressBar.visibility = View.VISIBLE

        if (otp == generatedOtp) {
            binding.otpProgressBar.visibility = View.GONE
            Toast.makeText(this, "Payment Confirmed", Toast.LENGTH_SHORT).show()

            // Verileri Firebase'e ekle
            val databaseReference = FirebaseDatabase.getInstance().getReference("HistoryItems")
            for (item in itemsToBeMoved) {
                val newItemRef = databaseReference.push()
                newItemRef.setValue(item)
            }

            // Sepeti sıfırla
            managementCart.clearCart()

            // HistoryActivity'ye geçiş
            startActivity(Intent(this, HistoryActivity::class.java))
        } else {
            binding.otpProgressBar.visibility = View.GONE
            Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show()
        }
    }
}