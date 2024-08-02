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

    fun insertFood(item: ItemsModel) {
        var listFood = getListCart()
        val existAlready = listFood.any { it.title == item.title }
        val index = listFood.indexOfFirst { it.title == item.title }

        if (existAlready) {
            listFood[index].numberInCart = item.numberInCart
        } else {
            listFood.add(item)
        }
        tinyDB.putListObject("CartList", listFood)
        Toast.makeText(context, "Added to your Cart", Toast.LENGTH_SHORT).show()

        // Update Firebase database
        updateFirebaseCart(item)
    }

    private fun updateFirebaseCart(item: ItemsModel) {
        val user = auth.currentUser
        if (user != null) {
            dbRef.child(user.uid).child("cart").child(item.title).setValue(item)
                .addOnSuccessListener {
                    Toast.makeText(context, "Item added to Firebase cart", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Failed to add item to Firebase cart", Toast.LENGTH_SHORT).show()
                }
        }
    }

    fun getListCart(): ArrayList<ItemsModel> {
        return tinyDB.getListObject("CartList") ?: arrayListOf()
    }

    fun minusItem(listFood: ArrayList<ItemsModel>, position: Int, listener: ChangeNumberItemsListener) {
        val item = listFood[position]
        val user = auth.currentUser

        if (item.numberInCart == 1) {
            listFood.removeAt(position)
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

        tinyDB.putListObject("CartList", listFood)
        listener.onChanged()
    }

    fun plusItem(listFood: ArrayList<ItemsModel>, position: Int, listener: ChangeNumberItemsListener) {
        val item = listFood[position]
        val user = auth.currentUser

        // Increase the quantity in the local list
        item.numberInCart++

        // Update the Firebase database
        if (user != null) {
            dbRef.child(user.uid).child("cart").child(item.title).setValue(item)
                .addOnSuccessListener {
                    // Successfully updated in Firebase
                }
                .addOnFailureListener {
                    // Handle error
                }
        }

        // Update the local storage
        tinyDB.putListObject("CartList", listFood)
        listener.onChanged()
    }

    fun getTotalFee(): Double {
        val listFood = getListCart()
        var fee = 0.0
        for (item in listFood) {
            fee += item.price * item.numberInCart
        }
        return fee
    }

    fun clearCart() {
        val listFood = arrayListOf<ItemsModel>()
        tinyDB.putListObject("CartList", listFood)

        // Also clear the cart in Firebase
        val user = auth.currentUser
        if (user != null) {
            dbRef.child(user.uid).child("cart").removeValue()
                .addOnSuccessListener {
                    Toast.makeText(context, "Your cart has been cleared", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Failed to clear Firebase cart", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(context, "Your cart has been cleared", Toast.LENGTH_SHORT).show()
        }
    }
}