package com.example.buytobuy.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.buytobuy.R
import com.example.buytobuy.adapter.AddressAdapter
import com.example.buytobuy.databinding.ActivityProfileBinding
import com.example.buytobuy.model.AddressModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var addressAdapter: AddressAdapter
    private var addressList = ArrayList<AddressModel>()
    private lateinit var dbRef: DatabaseReference
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().getReference("users")

        setupUI()
        fetchUserData()
        fetchAddressData()
        setupListeners()
    }

    private fun setupListeners() {
        binding.editProfileBtn.setOnClickListener {
          //  startActivity(this,EditProfileActivity::class.java)
        }
        binding.editAddressTxt.setOnClickListener {
        //    startActivity(this,EditAddressActivity::class.java  )
        }
    }

    private fun fetchUserData() {
        val user = auth.currentUser
        if (user!=null){
            dbRef.child(user.uid).child("profile").addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userName= snapshot.child("name").getValue(String::class.java)
                        binding.userNameTextView.text = userName ?: "Username.."
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }

    private fun fetchAddressData() {
        val user = auth.currentUser
        if (user != null) {
            dbRef.child(user.uid).child("addresses").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    addressList.clear()
                    for (data in snapshot.children) {
                        val address = data.getValue(AddressModel::class.java)
                        if (address != null) {
                            addressList.add(address)
                        }
                    }
                    addressAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    // Hata durumu
                }
            })
        }
    }

    private fun setupUI(){
        val user = auth.currentUser
        if (user!=null){
            dbRef.child(user.uid).child("profile").addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userName = snapshot.child("name").getValue(String::class.java)
                    binding.userNameTextView.text = userName ?: "User Name"
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }
}