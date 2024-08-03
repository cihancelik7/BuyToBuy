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

class ProfileActivity : AppCompatActivity() {
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

        binding.editProfileBtn.setOnClickListener {
            startActivity(Intent(this@ProfileActivity, EditProfileActivity::class.java))
        }

        binding.plusAddressBtn.setOnClickListener {
            startActivity(Intent(this, AddressCrudActivity::class.java))
        }
    }

    private fun setupRecyclerView(addressList: List<AddressModel>) {
        addressAdapter = AddressAdapter(
            this,
            addressList,
            onEditClicked = { address ->
                // Handle edit action
            },
            onDeleteClicked = { address ->
                // Handle delete action
            }
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
            val password = "YourPassword" // Şifreyi nasıl aldığınıza bağlı olarak değiştirin
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

    private fun maskPassword(password: String): String {
        return if (password.length > 2) {
            password.substring(0, 2) + "*".repeat(password.length - 2)
        } else {
            "*".repeat(password.length)
        }
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