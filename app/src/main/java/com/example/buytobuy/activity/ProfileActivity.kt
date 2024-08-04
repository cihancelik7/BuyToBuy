package com.example.buytobuy.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buytobuy.adapter.AddressAdapter
import com.example.buytobuy.databinding.ActivityProfileBinding
import com.example.buytobuy.model.AddressModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ProfileActivity : BaseActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private lateinit var addressAdapter: AddressAdapter
    private var addressList = ArrayList<AddressModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().getReference("users")

        setupRecyclerView(addressList)
        setupUserProfile()
        setupListeners()
        initBottomMenu()
        fetchAddresses()

        binding.editProfileBtn.setOnClickListener {
            startActivity(Intent(this@ProfileActivity, EditProfileActivity::class.java))
        }

        binding.plusAddressBtn.setOnClickListener {
            startActivity(Intent(this, AddressCrudActivity::class.java))
        }

        binding.backBtn.setOnClickListener {
            auth.signOut()
            val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun setupRecyclerView(addressList: List<AddressModel>) {
        addressAdapter = AddressAdapter(
            this,
            addressList,

        )
        binding.addressRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ProfileActivity)
            adapter = addressAdapter
        }
    }


    private fun setupUserProfile() {
        val user = auth.currentUser
        user?.let {
            binding.emailTextView.text = it.email
            val password = "YourPassword"
            binding.passwordTextView.text = maskPassword(password)

            dbRef.child(it.uid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val firstName = snapshot.child("name").getValue(String::class.java)
                    binding.userNameTextView.text = firstName ?: "Name Surname"

                    // Adresleri getirme
                    addressList.clear()
                    snapshot.child("addresses").children.forEach { addressSnapshot ->
                        val address = addressSnapshot.getValue(AddressModel::class.java)
                        if (address != null) {
                            addressList.add(address)
                        }
                    }
                    addressAdapter.updateAddressList(addressList)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Hata durumu
                }
            })
        }
    }

    private fun deleteAddress(address: AddressModel) {
        val user = auth.currentUser
        user?.let {
            dbRef.child(it.uid).child("addresses").child(address.id).removeValue().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    addressList.remove(address)
                    addressAdapter.updateAddressList(addressList)
                    binding.noAddressesTextView.visibility = if (addressList.isEmpty()) View.VISIBLE else View.GONE
                    Toast.makeText(this, "Address deleted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to delete address", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun fetchAddresses() {
        val user = auth.currentUser
        if (user != null) {
            dbRef.child(user.uid).child("addresses").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val addressList = mutableListOf<AddressModel>()
                    for (data in snapshot.children) {
                        val address = data.getValue(AddressModel::class.java)
                        if (address != null) {
                            addressList.add(address)
                        }
                    }
                    // Update the RecyclerView with the address list
                    if (addressList.isNotEmpty()) {
                        binding.noAddressesTextView.visibility = View.GONE
                        binding.addressRecyclerView.visibility = View.VISIBLE
                        addressAdapter.updateAddressList(addressList)
                    } else {
                        binding.noAddressesTextView.visibility = View.VISIBLE
                        binding.addressRecyclerView.visibility = View.GONE
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle the error scenario, if required
                }
            })
        }
    }

    private fun maskPassword(password: String): String {
        return "*".repeat(password.length)

    }

    private fun initBottomMenu() {
        binding.navCart.setOnClickListener {
            startActivity(Intent(this@ProfileActivity, CartActivity::class.java))
        }
        binding.wishlistBtn.setOnClickListener {
            startActivity(Intent(this@ProfileActivity, WishlistActivity::class.java))
        }
        binding.historyBtn.setOnClickListener {
            startActivity(Intent(this@ProfileActivity, HistoryActivity::class.java))
        }
        binding.navExplorer.setOnClickListener {
            startActivity(Intent(this@ProfileActivity, MainActivity::class.java))
        }
        binding.navProfile.setOnClickListener {
            startActivity(Intent(this@ProfileActivity, ProfileActivity::class.java))
        }
    }

    private fun setupListeners() {
        binding.editProfileBtn.setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
        }
    }
}