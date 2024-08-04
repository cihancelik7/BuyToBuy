package com.example.buytobuy.helper

import android.content.Context
import android.widget.Toast
import com.example.buytobuy.model.ItemsModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ManagementCart(val context: Context) {

    private val tinyDB = TinyDB(context)
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val dbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")




    fun getListCart(): ArrayList<ItemsModel> {
        return tinyDB.getListObject("CartList") ?: arrayListOf()
    }

    fun minusItem(listItems: ArrayList<ItemsModel>, position: Int, listener: ChangeNumberItemsListener) {
        val item = listItems[position]
        val user = auth.currentUser

        if (item.numberInCart == 1) {
            listItems.removeAt(position)
            if (user != null) {
                dbRef.child(user.uid).child("cart").child(item.title).removeValue()
                    .addOnSuccessListener {
                        // Successfully removed from Firebase
                    }
                    .addOnFailureListener {
                        // Handle error
                    }
            }
        } else {
            item.numberInCart--
            if (user != null) {
                dbRef.child(user.uid).child("cart").child(item.title).setValue(item)
                    .addOnSuccessListener {
                        // Successfully updated in Firebase
                    }
                    .addOnFailureListener {
                        // Handle error
                    }
            }
        }

        tinyDB.putListObject("CartList", listItems)
        listener.onChanged()
    }

    fun plusItem(listItems: ArrayList<ItemsModel>, position: Int, listener: ChangeNumberItemsListener) {
        val item = listItems[position]
        val user = auth.currentUser

        item.numberInCart++

        if (user != null) {
            dbRef.child(user.uid).child("cart").child(item.title).setValue(item)
                .addOnSuccessListener {
                    // Successfully updated in Firebase
                }
                .addOnFailureListener {
                    // Handle error
                }
        }


        tinyDB.putListObject("CartList", listItems)
        listener.onChanged()
    }
}