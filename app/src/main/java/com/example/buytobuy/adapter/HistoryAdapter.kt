package com.example.buytobuy.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.buytobuy.R
import com.example.buytobuy.model.ItemsModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryAdapter(private val historyList: List<ItemsModel>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val productName: TextView = itemView.findViewById(R.id.productName)
        private val purchaseDate: TextView = itemView.findViewById(R.id.purchaseDate)
        private val productImage: ImageView = itemView.findViewById(R.id.productImage)
        private val price : TextView = itemView.findViewById(R.id.priceTxt)

        fun bind(item: ItemsModel) {
            productName.text = item.title
            price.text = "$${item.price}"


            // Display formatted purchase date
            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            purchaseDate.text = dateFormat.format(item.purchaseDate ?: Date())

            // Load product image
            Glide.with(itemView.context).load(item.picUrl.firstOrNull()).into(productImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.history_item, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = historyList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = historyList.size
}