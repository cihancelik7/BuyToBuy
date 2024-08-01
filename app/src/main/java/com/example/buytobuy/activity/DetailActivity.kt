package com.example.buytobuy.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.buytobuy.R
import com.example.buytobuy.adapter.ColorAdapter
import com.example.buytobuy.adapter.SizeAdapter
import com.example.buytobuy.adapter.SliderAdapter
import com.example.buytobuy.databinding.ActivityDetailBinding
import com.example.buytobuy.helper.ManagementCart
import com.example.buytobuy.model.ItemsModel
import com.example.buytobuy.model.SliderModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference

class DetailActivity : BaseActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var item: ItemsModel
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private lateinit var managementCart: ManagementCart
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().getReference("users")
        managementCart = ManagementCart(this)

        // Ürün bilgilerini al
        getBundle()

        // Favori durumunu kontrol et
        checkFavoriteStatus()

        // Favori butonuna tıklama işlemi
        binding.favBtn.setOnClickListener {
            toggleFavorite()
        }

        // Diğer başlangıç ayarları
        banners()
        initList()
        initBottomMenu()
    }

    private fun getBundle() {
        item = intent.getParcelableExtra("object")!!

        binding.titleTxt.text = item.title
        binding.descriptionTxt.text = item.description
        binding.priceTxt.text = "$" + item.price
        // Diğer ürün bilgilerini ayarla
        binding.addToCartBtn.setOnClickListener {
            item.numberInCart = 1
            managementCart.insertFood(item)
        }
        binding.backBtn.setOnClickListener {
            finish()
        }
        binding.cartBtn.setOnClickListener{
            startActivity(Intent(this@DetailActivity, CartActivity::class.java))
        }
    }

    private fun checkFavoriteStatus() {
        val user = auth.currentUser
        if (user != null) {
            dbRef.child(user.uid).child("wishlist").child(item.title)
                .get().addOnSuccessListener {
                    if (it.exists()) {
                        isFavorite = true
                        binding.favBtn.setImageResource(R.drawable.favorite)
                    } else {
                        isFavorite = false
                        binding.favBtn.setImageResource(R.drawable.fav_icon)
                    }
                }
        }
    }

    private fun toggleFavorite() {
        val user = auth.currentUser
        if (user != null) {
            val wishlistRef = dbRef.child(user.uid).child("wishlist").child(item.title)
            if (isFavorite) {
                wishlistRef.removeValue().addOnSuccessListener {
                    isFavorite = false
                    binding.favBtn.setImageResource(R.drawable.fav_icon)
                }.addOnFailureListener {
                    // Hata durumunda kullanıcıya bildirim yap veya eski durumu geri al
                }
            } else {
                wishlistRef.setValue(item).addOnSuccessListener {
                    isFavorite = true
                    binding.favBtn.setImageResource(R.drawable.favorite)
                }.addOnFailureListener {
                    // Hata durumunda kullanıcıya bildirim yap veya eski durumu geri al
                }
            }
        }
    }

    private fun banners() {
        val sliderItems = ArrayList<SliderModel>()
        for (imageUrl in item.picUrl) {
            sliderItems.add(SliderModel(imageUrl))
        }
        binding.slider.adapter = SliderAdapter(sliderItems, binding.slider)
        binding.slider.clipToPadding = true
        binding.slider.clipChildren = true
        binding.slider.offscreenPageLimit = 1

        if (sliderItems.size > 1) {
            binding.dotIndicator.visibility = View.VISIBLE
            binding.dotIndicator.attachTo(binding.slider)
        }
    }
    private fun initBottomMenu() {
        binding.cartBtn.setOnClickListener {
            startActivity(Intent(this@DetailActivity, CartActivity::class.java))
        }
        binding.wishlistBtn.setOnClickListener {
            startActivity(Intent(this@DetailActivity, WishlistActivity::class.java))
        }
        binding.historyBtn.setOnClickListener {
            startActivity(Intent(this@DetailActivity, HistoryActivity::class.java))
        }
        binding.navExplorer.setOnClickListener {
            startActivity(Intent(this@DetailActivity, MainActivity::class.java))
        }
    }

    private fun initList() {
        val sizeList = ArrayList<String>()
        for (size in item.size) {
            sizeList.add(size.toString())
        }
        binding.sizeList.adapter = SizeAdapter(sizeList)
        binding.sizeList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val colorList = ArrayList<String>()
        for (imageUrl in item.picUrl) {
            colorList.add(imageUrl)
        }
        binding.colorList.adapter = ColorAdapter(colorList)
        binding.colorList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }
}