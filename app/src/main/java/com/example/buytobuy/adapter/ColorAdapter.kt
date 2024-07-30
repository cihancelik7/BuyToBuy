package com.example.buytobuy.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.buytobuy.R
import com.example.buytobuy.databinding.ViewholderBrandBinding
import com.example.buytobuy.databinding.ViewholderColorBinding
import com.example.buytobuy.model.BrandModel

class ColorAdapter(val items: MutableList<String>) :
    RecyclerView.Adapter<ColorAdapter.Viewholder>() {

    private var selectedPosition = -1
    private var lastSelectedPosition = -1
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorAdapter.Viewholder {
        context = parent.context
        val binding = ViewholderColorBinding.inflate(LayoutInflater.from(context), parent, false)
        return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: ColorAdapter.Viewholder, position: Int) {

        Glide.with(holder.itemView.context)
            .load(items[position])
            .into(holder.binding.pic)

        holder.binding.root.setOnClickListener {
            lastSelectedPosition = selectedPosition
            selectedPosition = position
            notifyItemChanged(lastSelectedPosition)
            notifyItemChanged(selectedPosition)
        }

        if (selectedPosition == position) {
            holder.binding.colorLayout.setBackgroundResource(R.drawable.purple_button_bg)
        } else {
            holder.binding.colorLayout.setBackgroundResource(R.drawable.grey_bg)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class Viewholder(val binding: ViewholderColorBinding) : RecyclerView.ViewHolder(binding.root) {


    }
}