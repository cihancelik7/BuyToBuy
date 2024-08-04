package com.example.buytobuy.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buytobuy.adapter.CartAdapter
import com.example.buytobuy.databinding.ActivityCartBinding
import com.example.buytobuy.helper.ChangeNumberItemsListener
import com.example.buytobuy.helper.ManagementCart
import com.example.buytobuy.model.ItemsModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class CartActivity : BaseActivity() {
    private lateinit var binding: ActivityCartBinding
    private lateinit var managementCart: ManagementCart
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private var tax: Double = 0.0
    private var cartItems = ArrayList<ItemsModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().getReference("users")

        managementCart = ManagementCart(this)
        setVariable()
        fetchCartData()
        initBottomMenu()

        binding.checkOutBtn.setOnClickListener {
            val intent = Intent(this@CartActivity, AddressActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initBottomMenu() {
        binding.cartBtn.setOnClickListener {
            startActivity(Intent(this@CartActivity, CartActivity::class.java))
        }
        binding.wishlistBtn.setOnClickListener {
            startActivity(Intent(this@CartActivity, WishlistActivity::class.java))
        }
        binding.historyBtn.setOnClickListener {
            startActivity(Intent(this@CartActivity, HistoryActivity::class.java))
        }
        binding.navExplorer.setOnClickListener {
            startActivity(Intent(this@CartActivity, MainActivity::class.java))
        }
        binding.navProfile.setOnClickListener {
            startActivity(Intent(this@CartActivity, ProfileActivity::class.java))
        }
    }

    private fun fetchCartData() {
        val user = auth.currentUser
        if (user != null) {
            dbRef.child(user.uid).child("cart").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    cartItems.clear()
                    for (data in snapshot.children) {
                        val item = data.getValue(ItemsModel::class.java)
                        if (item != null) {
                            cartItems.add(item)
                        }
                    }
                    initCartList()
                    calculateCart()
                }

                override fun onCancelled(error: DatabaseError) {
                    // Hata yönetimi
                }
            })
        }
    }

    private fun initCartList() {
        binding.viewCart.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.viewCart.adapter =
            CartAdapter(cartItems, this, object : ChangeNumberItemsListener {
                override fun onChanged() {
                    calculateCart()
                }
            })

        with(binding) {
            emptyTxt.visibility = if (cartItems.isEmpty()) View.VISIBLE else View.GONE
            scrollView2.visibility = if (cartItems.isEmpty()) View.GONE else View.VISIBLE
        }
    }

    private fun calculateCart() {
        val percentTax = 0.2
        val delivery = 30.0
        val itemTotal = Math.round(cartItems.sumOf { it.price * it.numberInCart } * 100) / 100.0
        tax = Math.round((itemTotal * percentTax) * 100) / 100.0
        val total = Math.round((itemTotal + tax + delivery) * 100) / 100.0

        if (cartItems.isNotEmpty()) {
            with(binding) {
                totalFeeTxt.text = "$$itemTotal"
                taxTxt.text = "$$tax"
                deliveryTxt.text = "$$delivery"
                totalTxt.text = "$$total"
            }
        }
        if (cartItems.isEmpty()) {
            with(binding) {
                totalFeeTxt.text = "$0"
                taxTxt.text = "$0"
                deliveryTxt.text = "$0"
                totalTxt.text = "$0"
            }
        }
    }

    private fun setVariable() {
        binding.backBtn.setOnClickListener { finish() }
    }
}