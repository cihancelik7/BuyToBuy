package com.example.buytobuy.activity

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.buytobuy.databinding.ActivityAddressBinding
import com.example.buytobuy.model.AddressModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class AddressActivity : BaseActivity() {

    private lateinit var binding: ActivityAddressBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private lateinit var addressList: ArrayList<AddressModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().getReference("users")

        addressList = ArrayList()
        loadUserAddresses()

        binding.btnSelectAddress.setOnClickListener {
            showAddressSelectionDialog()
        }

        binding.submitAddressBtn.setOnClickListener {
            val phoneNumber = binding.phoneNumberEditText.text.toString()
            val intent = Intent(this, PaymentActivity::class.java)
            intent.putExtra("phoneNumber", phoneNumber)
            startActivity(intent)
        }

        initBottomMenu()
    }

    private fun loadUserAddresses() {
        val user = auth.currentUser
        user?.let {
            dbRef.child(it.uid).child("addresses").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    addressList.clear()
                    for (data in snapshot.children) {
                        val address = data.getValue(AddressModel::class.java)
                        if (address != null) {
                            addressList.add(address)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@AddressActivity, "Failed to load addresses: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun showAddressSelectionDialog() {
        val addressTitles = addressList.map { it.title }.toTypedArray()

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Address")
        builder.setItems(addressTitles) { dialog, which ->
            val selectedAddress = addressList[which]
            fillAddressFields(selectedAddress)
        }
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    private fun fillAddressFields(address: AddressModel) {
        binding.etFullName.setText(address.title)
        binding.etAddress.setText(address.street)
        binding.etCity.setText(address.city)
        binding.etState.setText(address.city) // Eğer eyalet bilgisi varsa buraya set edilebilir
        binding.etPostalCode.setText(address.postalCode)
        binding.phoneNumberEditText.setText(address.phoneNumber)
    }

    private fun initBottomMenu() {
        binding.navCart.setOnClickListener {
            startActivity(Intent(this@AddressActivity, CartActivity::class.java))
        }
        binding.wishlistBtn.setOnClickListener {
            startActivity(Intent(this@AddressActivity, WishlistActivity::class.java))
        }
        binding.historyBtn.setOnClickListener {
            startActivity(Intent(this@AddressActivity, HistoryActivity::class.java))
        }
        binding.navExplorer.setOnClickListener {
            startActivity(Intent(this@AddressActivity, MainActivity::class.java))
        }
        binding.navProfile.setOnClickListener {
            startActivity(Intent(this@AddressActivity, ProfileActivity::class.java))
        }
    }
}