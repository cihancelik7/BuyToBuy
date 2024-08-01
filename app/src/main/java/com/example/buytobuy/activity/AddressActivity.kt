package com.example.buytobuy.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.buytobuy.databinding.ActivityAddressBinding

class AddressActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddressBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.submitAddressBtn.setOnClickListener {
            val phoneNumber = binding.phoneNumberEditText.text.toString()
            val intent = Intent(this, PaymentActivity::class.java)
            intent.putExtra("phoneNumber", phoneNumber)
            startActivity(intent)
        }
    }
}