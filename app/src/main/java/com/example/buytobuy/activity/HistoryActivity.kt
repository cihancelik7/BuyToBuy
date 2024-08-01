package com.example.buytobuy.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.buytobuy.adapter.HistoryAdapter
import com.example.buytobuy.databinding.ActivityHistoryBinding
import com.example.buytobuy.model.ItemsModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HistoryActivity : BaseActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var historyAdapter: HistoryAdapter
    private val historyItems = mutableListOf<ItemsModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        fetchHistoryData()
    }

    private fun setupRecyclerView() {
        binding.historyRecyclerView.layoutManager = GridLayoutManager(this, 2) // İki kolonlu yapı
        historyAdapter = HistoryAdapter(historyItems)
        binding.historyRecyclerView.adapter = historyAdapter
    }

    private fun fetchHistoryData() {
        val databaseReference = FirebaseDatabase.getInstance().getReference("HistoryItems")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                historyItems.clear()
                for (data in snapshot.children) {
                    val item = data.getValue(ItemsModel::class.java)
                    if (item != null) {
                        historyItems.add(item)
                    }
                }
                historyAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("HistoryActivity", "Database error: ${error.message}")
            }
        })
    }
}