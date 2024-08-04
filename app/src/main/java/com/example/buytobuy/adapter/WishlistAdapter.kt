package com.example.buytobuy.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.buytobuy.activity.DetailActivity
import com.example.buytobuy.databinding.ItemWishlistBinding
import com.example.buytobuy.model.ItemsModel

class WishlistAdapter(
    private val onRemoveClicked: (ItemsModel) -> Unit
) : ListAdapter<ItemsModel, WishlistAdapter.WishlistViewHolder>(DIFF_CALLBACK) {

    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishlistViewHolder {
        context = parent.context
        val binding = ItemWishlistBinding.inflate(LayoutInflater.from(context), parent, false)
        return WishlistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WishlistViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class WishlistViewHolder(private val binding: ItemWishlistBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ItemsModel) {
            binding.titleTxt.text = item.title
            binding.priceTxt.text = "$${item.price}"
            binding.ratingTxt.text = item.rating.toString()

            val requestOptions = RequestOptions().transform(CenterCrop())
            Glide.with(binding.root.context)
                .load(item.picUrl.firstOrNull()) // İlk resmi kullan
                .apply(requestOptions)
                .into(binding.pic)

            itemView.setOnClickListener {
                val intent = Intent(binding.root.context, DetailActivity::class.java)
                intent.putExtra("object", item)
                binding.root.context.startActivity(intent)
            }

            binding.favBtn.setOnClickListener {
                onRemoveClicked(item)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemsModel>() {
            override fun areItemsTheSame(oldItem: ItemsModel, newItem: ItemsModel): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(oldItem: ItemsModel, newItem: ItemsModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}