package com.example.buytobuy.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.buytobuy.R
import com.example.buytobuy.model.AddressModel

class AddressAdapter(private val addressList: List<AddressModel>) :
    RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {

    inner class AddressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val addressTitle: TextView = itemView.findViewById(R.id.addressTitle)
        val addressDetails: TextView = itemView.findViewById(R.id.addressDetails)
        val editAddressButton: Button = itemView.findViewById(R.id.editAddressButton)
        val deleteAddressButton: Button = itemView.findViewById(R.id.deleteAddressButton)

        fun bind(address: AddressModel) {
            addressTitle.text = address.title
            addressDetails.text = address.details

            editAddressButton.setOnClickListener {
                // Adres düzenleme işlemi için listener
            }

            deleteAddressButton.setOnClickListener {
                // Adresi silme işlemi için listener
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_address, parent, false)
        return AddressViewHolder(view)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        holder.bind(addressList[position])
    }

    override fun getItemCount(): Int = addressList.size
}