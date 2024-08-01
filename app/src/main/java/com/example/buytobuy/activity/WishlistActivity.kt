package com.example.buytobuy.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buytobuy.adapter.WishlistAdapter
import com.example.buytobuy.databinding.ActivityWishlistBinding
import com.example.buytobuy.model.ItemsModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class WishlistActivity : BaseActivity() {

    private lateinit var binding: ActivityWishlistBinding
    private lateinit var wishlistAdapter: WishlistAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWishlistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        wishlistAdapter = WishlistAdapter { item ->
            removeFromWishlist(item)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = wishlistAdapter
        initBottomMenu()
        loadWishlist()
    }

    private fun loadWishlist() {
        val user = auth.currentUser
        if (user != null) {
            db.collection("users")
                .document(user.uid)
                .collection("wishlist")
                .get()
                .addOnSuccessListener { documents ->
                    val items = documents.map { it.toObject(ItemsModel::class.java) }
                    wishlistAdapter.submitList(items)
                }
                .addOnFailureListener { e ->
                    // Handle error
                }
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
    }

    private fun removeFromWishlist(item: ItemsModel) {
        val user = auth.currentUser
        if (user != null) {
            db.collection("users")
                .document(user.uid)
                .collection("wishlist")
                .document(item.title)
                .delete()
                .addOnSuccessListener {
                    loadWishlist() // Reload wishlist to reflect changes
                }
                .addOnFailureListener { e ->
                    // Handle error
                }
        }
    }
}