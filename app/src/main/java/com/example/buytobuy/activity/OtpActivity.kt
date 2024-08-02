package com.example.buytobuy.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.buytobuy.databinding.ActivityOtpBinding
import com.example.buytobuy.helper.ManagementCart
import com.example.buytobuy.model.ItemsModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class OtpActivity : BaseActivity() {
    private lateinit var binding: ActivityOtpBinding
    private var generatedOtp: String? = null
    private lateinit var managementCart: ManagementCart
    private val itemsToBeMoved = mutableListOf<ItemsModel>()
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().reference.child("users")
        managementCart = ManagementCart(this)
        generatedOtp = intent.getStringExtra("generatedOtp")

        itemsToBeMoved.addAll(managementCart.getListCart()) // Collect cart items

        binding.verifyOtpBtn.setOnClickListener {
            val enteredOtp = binding.otpEditText.text.toString()
            verifyOtp(enteredOtp)
        }
        initBottomMenu()
    }

    private fun initBottomMenu() {
        binding.navCart.setOnClickListener {
            startActivity(Intent(this@OtpActivity, CartActivity::class.java))
        }
        binding.wishlistBtn.setOnClickListener {
            startActivity(Intent(this@OtpActivity, WishlistActivity::class.java))
        }
        binding.historyBtn.setOnClickListener {
            startActivity(Intent(this@OtpActivity, HistoryActivity::class.java))
        }
        binding.navExplorer.setOnClickListener {
            startActivity(Intent(this@OtpActivity, MainActivity::class.java))
        }
    }

    private fun verifyOtp(otp: String) {
        binding.otpProgressBar.visibility = View.VISIBLE

        if (otp == generatedOtp) {
            binding.otpProgressBar.visibility = View.GONE
            Toast.makeText(this, "Payment Confirmed", Toast.LENGTH_SHORT).show()

            val currentTime = System.currentTimeMillis()

            // Add purchase date to each item and store it in the history
            itemsToBeMoved.forEach { item ->
                item.purchaseDate = currentTime
                addItemToHistory(item)
            }

            // Clear the cart and navigate to HistoryActivity
            managementCart.clearCart()
            startActivity(Intent(this, HistoryActivity::class.java))
        } else {
            binding.otpProgressBar.visibility = View.GONE
            Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addItemToHistory(item: ItemsModel) {
        val user = auth.currentUser
        if (user!=null){
            item.numberInCart=1
            val historyRef = dbRef.child(user.uid).child("HistoryItems").child(item.title).setValue(item)
            .addOnSuccessListener {
                // Successfully added item to history
            }.addOnFailureListener { e ->
                // Handle the error, perhaps log it
            }
        }
    }
}