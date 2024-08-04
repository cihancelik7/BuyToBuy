package com.example.buytobuy.activity

import android.content.Intent
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Toast
import com.example.buytobuy.databinding.ActivityPaymentBinding
import com.example.buytobuy.helper.PaymentHelper
import com.example.buytobuy.model.PaymentModel
import com.example.buytobuy.sdk.PaymentCallback

class PaymentActivity : BaseActivity() {

    private lateinit var binding: ActivityPaymentBinding
    private var generatedOtp: String? = null
    private var phoneNumber: String? = null
    private val paymentHelper = PaymentHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        phoneNumber = intent.getStringExtra("phoneNumber")

        binding.submitPaymentBtn.setOnClickListener {
            val cardNumber = binding.cardNumberEdit.text.toString()
            val expiryDate = binding.expiryDateEdit.text.toString()
            val cvv = binding.cvvEdit.text.toString()
            startPayment(cardNumber, expiryDate, cvv)
        }
        initBottomMenu()
    }

    private fun startPayment(cardNumber: String, expiryDate: String, cvv: String) {
        binding.paymentProgressBar.visibility = android.view.View.VISIBLE

        generatedOtp = (100000..999999).random().toString()

        val paymentModel = PaymentModel(
            cardNumber = cardNumber,
            expireDate = expiryDate,
            cvc = cvv,
            amount = 100.0
        )

        paymentHelper.initiatePayment(paymentModel, object : PaymentCallback {
            override fun onSuccess(message: String) {
                binding.paymentProgressBar.visibility = android.view.View.GONE
                Toast.makeText(this@PaymentActivity, "Payment Initiated: $message", Toast.LENGTH_SHORT).show()

                sendOtp(phoneNumber!!)

                val intent = Intent(this@PaymentActivity, OtpActivity::class.java)
                intent.putExtra("generatedOtp", generatedOtp)
                startActivity(intent)
            }

            override fun onFailure(errorMessage: String) {
                binding.paymentProgressBar.visibility = android.view.View.GONE
                Toast.makeText(this@PaymentActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun initBottomMenu() {
        binding.navCart.setOnClickListener {
            startActivity(Intent(this@PaymentActivity, CartActivity::class.java))
        }
        binding.wishlistBtn.setOnClickListener {
            startActivity(Intent(this@PaymentActivity, WishlistActivity::class.java))
        }
        binding.historyBtn.setOnClickListener {
            startActivity(Intent(this@PaymentActivity, HistoryActivity::class.java))
        }
        binding.navExplorer.setOnClickListener {
            startActivity(Intent(this@PaymentActivity, MainActivity::class.java))
        }
        binding.navProfile.setOnClickListener {
            startActivity(Intent(this@PaymentActivity, ProfileActivity::class.java))
        }
    }

    private fun sendOtp(phoneNumber: String) {
        val message = "$generatedOtp is your verification code."
        val smsManager = SmsManager.getDefault()
        smsManager.sendTextMessage(phoneNumber, null, message, null, null)
        Toast.makeText(this, "OTP sent to $phoneNumber", Toast.LENGTH_SHORT).show()
    }
}