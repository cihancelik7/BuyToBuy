package com.example.buytobuy.model

data class HistoryItem(
    val productId: String,
    val productName: String,
    val productImage: String,
    val purchaseDate: String,
    val otp: String
)
