                package com.example.buytobuy.activity

                import android.os.Bundle
                import android.widget.Toast
                import androidx.appcompat.app.AppCompatActivity
                import com.example.buytobuy.databinding.ActivityAddressCrudBinding
                import com.example.buytobuy.model.AddressModel
                import com.google.firebase.auth.FirebaseAuth
                import com.google.firebase.database.DatabaseReference
                import com.google.firebase.database.FirebaseDatabase

                class AddressCrudActivity : BaseActivity() {
                    private lateinit var binding: ActivityAddressCrudBinding
                    private lateinit var auth: FirebaseAuth
                    private lateinit var dbRef: DatabaseReference
                    private var addressId: String? = null

                    override fun onCreate(savedInstanceState: Bundle?) {
                        super.onCreate(savedInstanceState)
                        binding = ActivityAddressCrudBinding.inflate(layoutInflater)
                        setContentView(binding.root)

                        auth = FirebaseAuth.getInstance()
                        dbRef = FirebaseDatabase.getInstance().getReference("users")

                        addressId = intent.getStringExtra("addressId")
                        loadAddressDetails()
                        setupListeners()
                    }

                    private fun loadAddressDetails() {
                        val title = intent.getStringExtra("title")
                        val street = intent.getStringExtra("street")
                        val city = intent.getStringExtra("city")
                        val postalCode = intent.getStringExtra("postalCode")
                        val country = intent.getStringExtra("country")
                        val phoneNumber = intent.getStringExtra("phoneNumber") // Telefon numarası alınır

                        binding.titleEditText.setText(title)
                        binding.streetEditText.setText(street)
                        binding.cityEditText.setText(city)
                        binding.postalCodeEditText.setText(postalCode)
                        binding.countryEditText.setText(country)
                        binding.phoneNumberEditText.setText(phoneNumber) // Telefon numarası yüklenir
                    }

                    private fun setupListeners() {
                        binding.saveAddressButton.setOnClickListener {
                            val title = binding.titleEditText.text.toString().trim()
                            val street = binding.streetEditText.text.toString().trim()
                            val city = binding.cityEditText.text.toString().trim()
                            val postalCode = binding.postalCodeEditText.text.toString().trim()
                            val country = binding.countryEditText.text.toString().trim()
                            val phoneNumber = binding.phoneNumberEditText.text.toString().trim() // Telefon numarası alınır

                            if (title.isNotEmpty() && street.isNotEmpty() && city.isNotEmpty() && postalCode.isNotEmpty() && country.isNotEmpty() && phoneNumber.isNotEmpty()) {
                                if (addressId != null) {
                                    updateAddress(addressId!!, title, street, city, postalCode, country, phoneNumber)
                                } else {
                                    saveAddress(title, street, city, postalCode, country, phoneNumber)
                                }
                            } else {
                                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    private fun updateAddress(addressId: String, title: String, street: String, city: String, postalCode: String, country: String, phoneNumber: String) {
                        val user = auth.currentUser
                        user?.let {
                            val address = AddressModel(
                                id = addressId,
                                title = title,
                                street = street,
                                city = city,
                                postalCode = postalCode,
                                country = country,
                                phoneNumber = phoneNumber // Telefon numarası kaydedilir
                            )
                            dbRef.child(it.uid).child("addresses").child(addressId).setValue(address).addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(this, "Address updated", Toast.LENGTH_SHORT).show()
                                    finish()
                                } else {
                                    Toast.makeText(this, "Failed to update address", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }

                    private fun saveAddress(title: String, street: String, city: String, postalCode: String, country: String, phoneNumber: String) {
                        val user = auth.currentUser
                        user?.let {
                            val addressId = dbRef.child(it.uid).child("addresses").push().key
                            if (addressId != null) {
                                val address = AddressModel(
                                    id = addressId,
                                    title = title,
                                    street = street,
                                    city = city,
                                    postalCode = postalCode,
                                    country = country,
                                    phoneNumber = phoneNumber // Telefon numarası kaydedilir
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