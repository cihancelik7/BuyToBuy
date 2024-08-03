package com.example.buytobuy.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.buytobuy.databinding.ActivityAddressCrudBinding
import com.example.buytobuy.model.AddressModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddressCrudActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddressCrudBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressCrudBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().getReference("users")

        setupListeners()
    }

    private fun setupListeners() {
        binding.saveAddressButton.setOnClickListener {
            val street = binding.streetEditText.text.toString().trim()
            val city = binding.cityEditText.text.toString().trim()
            val postalCode = binding.postalCodeEditText.text.toString().trim()
            val country = binding.countryEditText.text.toString().trim()

            if (street.isNotEmpty() && city.isNotEmpty() && postalCode.isNotEmpty() && country.isNotEmpty()) {
                saveAddress(street, city, postalCode, country)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveAddress(street: String, city: String, postalCode: String, country: String) {
        val user = auth.currentUser
        user?.let {
            val addressId = dbRef.child(it.uid).child("addresses").push().key
            if (addressId != null) {
                val address = AddressModel(
                    id = addressId,
                    street = street,
                    city = city,
                    postalCode = postalCode,
                    country = country
                )
                dbRef.child(it.uid).child("addresses").child(addressId).setValue(address).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Address saved", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Failed to save address", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}