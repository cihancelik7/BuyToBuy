package com.example.buytobuy.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.buytobuy.R
import com.example.buytobuy.activity.AddressCrudActivity
import com.example.buytobuy.model.AddressModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

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
            addressDetails.text = "${address.street}, ${address.city}, ${address.postalCode}, ${address.country},${address.phoneNumber}"

            editAddressButton.setOnClickListener {
                val intent = Intent(context, AddressCrudActivity::class.java)
                intent.putExtra("addressId", address.id)
                intent.putExtra("title", address.title)
                intent.putExtra("street", address.street)
                intent.putExtra("city", address.city)
                intent.putExtra("postalCode", address.postalCode)
                intent.putExtra("country", address.country)
                intent.putExtra("phoneNumber", address.phoneNumber) // Telefon numarası gönderiliyor
                context.startActivity(intent)
            }

            deleteAddressButton.setOnClickListener {
                val user = FirebaseAuth.getInstance().currentUser
                user?.let {
                    val dbRef = FirebaseDatabase.getInstance().getReference("users").child(it.uid).child("addresses").child(address.id!!)
                    dbRef.removeValue().addOnSuccessListener {
                        Toast.makeText(context, "Address deleted", Toast.LENGTH_SHORT).show()
                        // Listeyi güncellemek için bir yöntem çağırabilirsiniz
                    }.addOnFailureListener {
                        Toast.makeText(context, "Failed to delete address", Toast.LENGTH_SHORT).show()
                    }
                }
            }
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