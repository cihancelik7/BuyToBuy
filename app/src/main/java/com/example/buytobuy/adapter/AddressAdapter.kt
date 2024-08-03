package com.example.buytobuy.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.buytobuy.R
import com.example.buytobuy.model.AddressModel

class AddressAdapter(
    private val context: Context,
    private var addressList: List<AddressModel>,
    private val onEditClicked: (AddressModel) -> Unit,
    private val onDeleteClicked: (AddressModel) -> Unit
) : RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {

    inner class AddressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val addressTitle: TextView = itemView.findViewById(R.id.addressTitle)
        val addressDetails: TextView = itemView.findViewById(R.id.addressDetails)
        val editAddressButton: ImageButton = itemView.findViewById(R.id.editAddressButton)
        val deleteAddressButton: ImageButton = itemView.findViewById(R.id.deleteAddressButton)

        fun bind(address: AddressModel) {
            addressTitle.text = address.title
            addressDetails.text = "${address.street}, ${address.city}, ${address.postalCode}, ${address.country}"

            editAddressButton.setOnClickListener { onEditClicked(address) }
            deleteAddressButton.setOnClickListener { onDeleteClicked(address) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_address, parent, false)
        return AddressViewHolder(view)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        holder.bind(addressList[position])
    }

    override fun getItemCount(): Int = addressList.size

    fun updateAddressList(newAddressList: List<AddressModel>) {
        addressList = newAddressList
        notifyDataSetChanged()
    }
}