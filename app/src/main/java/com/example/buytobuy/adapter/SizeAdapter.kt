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
import com.example.buytobuy.databinding.ViewholderSizeBinding
import com.example.buytobuy.model.BrandModel

class SizeAdapter(val items: MutableList<String>) :
    RecyclerView.Adapter<SizeAdapter.Viewholder>() {

    private var selectedPosition = -1
    private var lastSelectedPosition = -1
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SizeAdapter.Viewholder {
        context = parent.context
        val binding = ViewholderSizeBinding.inflate(LayoutInflater.from(context), parent, false)
        return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: SizeAdapter.Viewholder, position: Int) {

        holder.binding.sizeTxt.text = items[position]

        holder.binding.root.setOnClickListener {
            lastSelectedPosition = selectedPosition
            selectedPosition = position
            notifyItemChanged(lastSelectedPosition)
            notifyItemChanged(selectedPosition)
        }

        if (selectedPosition == position) {
            holder.binding.colorLayout.setBackgroundResource(R.drawable.grey_bg)
            holder.binding.sizeTxt.setTextColor(context.resources.getColor(R.color.purple))
        } else {
            holder.binding.colorLayout.setBackgroundResource(R.drawable.grey_bg)
            holder.binding.sizeTxt.setTextColor(context.resources.getColor(R.color.black))

        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class Viewholder(val binding: ViewholderSizeBinding) : RecyclerView.ViewHolder(binding.root) {


    }
}