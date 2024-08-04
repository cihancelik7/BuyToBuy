package com.example.buytobuy.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buytobuy.adapter.WishlistAdapter
import com.example.buytobuy.databinding.ActivityWishlistBinding
import com.example.buytobuy.model.ItemsModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class WishlistActivity : BaseActivity() {

    private lateinit var binding: ActivityWishlistBinding
    private lateinit var wishlistAdapter: WishlistAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWishlistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().getReference("users")

        wishlistAdapter = WishlistAdapter { item ->
            removeFromWishlist(item)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = wishlistAdapter
        initBottomMenu()
        loadWishlist()
        initListName()
    }

    private fun loadWishlist() {
        val user = auth.currentUser
        if (user != null) {
            dbRef.child(user.uid).child("wishlist")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val items = mutableListOf<ItemsModel>()
                        for (data in snapshot.children) {
                            val item = data.getValue(ItemsModel::class.java)
                            if (item != null) {
                                items.add(item)
                            }
                        }
                        wishlistAdapter.submitList(items)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle error
                    }
                })
        }
    }

    private fun initBottomMenu() {
        binding.navCart.setOnClickListener {
            startActivity(Intent(this@WishlistActivity, CartActivity::class.java))
        }
        binding.wishlistBtn.setOnClickListener {
            startActivity(Intent(this@WishlistActivity, WishlistActivity::class.java))
        }
        binding.historyBtn.setOnClickListener {
            startActivity(Intent(this@WishlistActivity, HistoryActivity::class.java))
        }
        binding.navExplorer.setOnClickListener {
            startActivity(Intent(this@WishlistActivity, MainActivity::class.java))
        }
        binding.navProfile.setOnClickListener {
            startActivity(Intent(this@WishlistActivity, ProfileActivity::class.java))
        }
    }

    private fun removeFromWishlist(item: ItemsModel) {
        val user = auth.currentUser
        if (user != null) {
            dbRef.child(user.uid).child("wishlist").child(item.title)
                .removeValue()
                .addOnSuccessListener {
                    loadWishlist()
                }
                .addOnFailureListener { e ->
                    // Handle error
                }
        }
    }

    private fun initListName() {
        val currentUser = auth.currentUser
        currentUser?.let {
            val email = it.email
            binding.userNameTextView.text = email
        }
    }
}