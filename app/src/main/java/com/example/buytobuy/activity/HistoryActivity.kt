package com.example.buytobuy.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buytobuy.adapter.HistoryAdapter
import com.example.buytobuy.databinding.ActivityHistoryBinding
import com.example.buytobuy.model.ItemsModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HistoryActivity : BaseActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private val historyItems = mutableListOf<ItemsModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().getReference("users")

        setupRecyclerView()
        fetchHistoryData()
        initListName()
        initBottomMenu()
    }

    private fun setupRecyclerView() {
        binding.historyRecyclerView.layoutManager = LinearLayoutManager(this)
        historyAdapter = HistoryAdapter(historyItems)
        binding.historyRecyclerView.adapter = historyAdapter
    }

    private fun fetchHistoryData() {
        val user = auth.currentUser
        if (user != null) {
            val historyRef = dbRef.child(user.uid).child("historyItems")
            historyRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val historyItems = mutableListOf<ItemsModel>()
                    for (data in snapshot.children) {
                        val item = data.getValue(ItemsModel::class.java)
                        if (item != null) {
                            historyItems.add(item)
                        }
                    }
                    historyAdapter = HistoryAdapter(historyItems)
                    binding.historyRecyclerView.adapter = historyAdapter
                    historyAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    // Hata yönetimi
                    Log.e("Firebase", "Failed to fetch history items: ${error.message}")
                }
            })
        }
    }

    private fun initBottomMenu() {
        binding.navCart.setOnClickListener {
            startActivity(Intent(this@HistoryActivity, CartActivity::class.java))
        }
        binding.wishlistBtn.setOnClickListener {
            startActivity(Intent(this@HistoryActivity, WishlistActivity::class.java))
        }
        binding.historyBtn.setOnClickListener {
            startActivity(Intent(this@HistoryActivity, HistoryActivity::class.java))
        }
        binding.navExplorer.setOnClickListener {
            startActivity(Intent(this@HistoryActivity, MainActivity::class.java))
        }
        binding.navProfile.setOnClickListener {
            startActivity(Intent(this@HistoryActivity, ProfileActivity::class.java))
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