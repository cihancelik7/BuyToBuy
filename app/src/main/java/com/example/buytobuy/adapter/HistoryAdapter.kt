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

class HistoryAdapter(private val historyList: List<ItemsModel>) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productName: TextView = itemView.findViewById(R.id.productName)
        val purchaseDate: TextView = itemView.findViewById(R.id.purchaseDate) // Eğer tarih bilgisi varsa
        val productImage: ImageView = itemView.findViewById(R.id.productImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.history_item, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = historyList[position]
        holder.productName.text = item.title
        // Eğer tarih bilgisi varsa kullanabilirsiniz
        // holder.purchaseDate.text = item.purchaseDate
        Glide.with(holder.itemView.context).load(item.picUrl.firstOrNull()).into(holder.productImage)
    }

    override fun getItemCount(): Int = historyList.size
}