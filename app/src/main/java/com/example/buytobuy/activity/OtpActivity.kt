package com.example.buytobuy.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().getReference("users")
        managementCart = ManagementCart(this)
        generatedOtp = intent.getStringExtra("generatedOtp")

        binding.verifyOtpBtn.setOnClickListener {
            val enteredOtp = binding.otpEditText.text.toString()
            verifyOtp(enteredOtp)
        }
        initBottomMenu()
    }

    private fun verifyOtp(otp: String) {
        binding.otpProgressBar.visibility = View.VISIBLE

        if (otp == generatedOtp) {
            binding.otpProgressBar.visibility = View.GONE
            Toast.makeText(this, "Payment Confirmed", Toast.LENGTH_SHORT).show()

            val currentTime = System.currentTimeMillis()

            // Sepet verilerini "historyItems" kategorisine taşı ve satın alma saatini güncelle
            moveCartToHistoryItems(currentTime)

            // Cart'tan verileri sil
            clearCartAfterOtpVerification()

            // OTP doğrulama başarılı, history ekranına geçiş yap
            startActivity(Intent(this, HistoryActivity::class.java))
        } else {
            binding.otpProgressBar.visibility = View.GONE
            Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show()
        }
    }

    private fun moveCartToHistoryItems(purchaseTime: Long) {
        val user = auth.currentUser
        if (user != null) {
            val cartRef = dbRef.child(user.uid).child("cart")
            val historyRef = dbRef.child(user.uid).child("historyItems")

            cartRef.get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val cartItems = ArrayList<ItemsModel>()
                    for (data in snapshot.children) {
                        val item = data.getValue(ItemsModel::class.java)
                        if (item != null) {
                            item.purchaseDate = purchaseTime
                            cartItems.add(item)
                        }
                    }

                    // "historyItems" kategorisine ekleme işlemi
                    for (item in cartItems) {
                        val newHistoryRef = historyRef.push()
                        newHistoryRef.setValue(item)
                    }
                }
            }.addOnFailureListener {
                Log.e("Firebase", "Failed to retrieve cart items: ${it.message}")
            }
        }
    }

    private fun clearCartAfterOtpVerification() {
        val user = auth.currentUser
        if (user != null) {
            val cartRef = dbRef.child(user.uid).child("cart")
            cartRef.removeValue().addOnSuccessListener {
                Log.d("Firebase", "Cart items removed successfully after OTP verification.")
            }.addOnFailureListener {
                Log.e("Firebase", "Failed to remove cart items after OTP verification: ${it.message}")
            }
        }
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
        binding.navProfile.setOnClickListener {
            startActivity(Intent(this@OtpActivity, ProfileActivity::class.java))
        }
    }

}