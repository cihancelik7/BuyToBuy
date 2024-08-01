package com.example.buytobuy.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buytobuy.R
import com.example.buytobuy.adapter.CartAdapter
import com.example.buytobuy.databinding.ActivityCartBinding
import com.example.buytobuy.helper.ChangeNumberItemsListener
import com.example.buytobuy.helper.ManagementCart
import com.example.buytobuy.model.ItemsModel

class CartActivity : BaseActivity() {
    private lateinit var binding: ActivityCartBinding
    private lateinit var managementCart: ManagementCart
    private var tax: Double = 0.0
    private lateinit var cartItems: ArrayList<ItemsModel>
    private val itemsToBeMoved = ArrayList<ItemsModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        managementCart = ManagementCart(this)
        cartItems = managementCart.getListCart() // managementCart başlatıldıktan sonra kullanılıyor

        setVariable()
        initCartList()
        calculateCart()

        binding.checkOutBtn.setOnClickListener {
            val intent = Intent(this@CartActivity, AddressActivity::class.java)
            startActivity(intent)
        }

        // Sepetteki tüm öğeleri itemsToBeMoved listesine ekleyin
        itemsToBeMoved.addAll(cartItems)
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
        tax = Math.round((managementCart.getTotalFee() * percentTax) * 100) / 100.0
        val total = Math.round((managementCart.getTotalFee() + tax + delivery) * 100) / 100
        val itemTotal = Math.round(managementCart.getTotalFee() * 100) / 100

        with(binding) {
            totalFeeTxt.text = "$$itemTotal"
            taxTxt.text = "$$tax"
            deliveryTxt.text = "$$delivery"
            totalTxt.text = "$$total"
        }
    }

    private fun setVariable() {
        binding.backBtn.setOnClickListener { finish() }
    }
}