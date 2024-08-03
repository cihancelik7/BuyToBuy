package com.example.buytobuy.activity

import android.content.ClipData.Item
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buytobuy.adapter.HistoryAdapter
import com.example.buytobuy.databinding.ActivityHistoryBinding
import com.example.buytobuy.model.ItemsModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HistoryActivity : BaseActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var progressBar: ProgressBar
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private var historyItems = ArrayList<ItemsModel>()
    private var totalSpent = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        progressBar =binding.progressBar
        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().getReference("users")
        progressBar.visibility = View.VISIBLE
        setupRecyclerView()
        fetchHistoryData()
        initBottomMenu()
        initListName()
    }

    private fun setupRecyclerView() {
        binding.historyRecyclerView.layoutManager = LinearLayoutManager(this)
        historyAdapter = HistoryAdapter(historyItems)
        binding.historyRecyclerView.adapter = historyAdapter
    }

    private fun fetchHistoryData() {
        val user = auth.currentUser
        if (user != null) {
            val databaseReference = dbRef.child(user.uid).child("HistoryItems")
            databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    historyItems.clear()
                    totalSpent = 0.0
                    for (data in snapshot.children) {
                        val item = data.getValue(ItemsModel::class.java)
                        if (item != null) {
                            historyItems.add(item)
                            totalSpent += item.price * item.numberInCart // Toplam harcama hesaplama
                        }
                    }
                    historyAdapter.notifyDataSetChanged()
                    updateTotalSpent()
                    progressBar.visibility = View.GONE
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("HistoryActivity", "Database error: ${error.message}")
                    progressBar.visibility = View.GONE
                }
            })
        }
    }
    private fun initListName() {
        val currentUser = auth.currentUser
        currentUser?.let {
            val email = it.email
            binding.userNameTextView.text = email
        }
    }
    private fun updateTotalSpent() {
        binding.totalSpentTextView.text = "Total Spent: $$totalSpent"
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

    companion object {
        fun addItemsToHistory(items: List<ItemsModel>) {
            val databaseReference = FirebaseDatabase.getInstance().getReference("HistoryItems")
            items.forEach { item ->
                val newRef = databaseReference.push()
                newRef.setValue(item)
            }
        }
    }
}