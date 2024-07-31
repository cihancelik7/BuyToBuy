package com.example.buytobuy.model

data class PaymentModel(
    val cardNumber: String,
    val expireDate: String,
    val cvc: String,
    val amount: Double
)