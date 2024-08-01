package com.example.buytobuy.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.buytobuy.R
import com.example.buytobuy.adapter.HistoryAdapter
import com.example.buytobuy.databinding.ActivityHistoryBinding
import com.example.buytobuy.model.HistoryItem
import com.example.buytobuy.model.ItemsModel

class HistoryActivity : AppCompatActivity() {

    private lateinit var historyRecyclerView: RecyclerView
    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        historyRecyclerView = findViewById(R.id.historyRecyclerView)
        historyRecyclerView.layoutManager = GridLayoutManager(this, 2) // İki sütunlu grid layout

        // Test verileri ItemsModel'e uygun şekilde güncellendi
        val historyItems = listOf(
            ItemsModel(
                orderID = "123",
                title = "Product 1",
                description = "Description of Product 1",
                picUrl = arrayListOf("https://example.com/image1.jpg"),
                size = arrayListOf("M", "L"),
                price = 29.99,
                rating = 4.5,
                numberInCart = 1
            ),
            ItemsModel(
                orderID = "124",
                title = "Product 2",
                description = "Description of Product 2",
                picUrl = arrayListOf("https://example.com/image2.jpg"),
                size = arrayListOf("S", "XL"),
                price = 39.99,
                rating = 4.8,
                numberInCart = 1
            )
        )

        historyAdapter = HistoryAdapter(historyItems)
        historyRecyclerView.adapter = historyAdapter
    }
}