package com.example.buytobuy.sdk

import android.telephony.SmsManager

interface PaymentCallback {
    fun onSuccess(message: String)
    fun onFailure(errorMessage: String)
}

object PaymentSDK {
    fun startPayment(cardNo: String, expDate: String, cvv: String, amount: Double, callback: PaymentCallback) {
        // Dummy payment processing
        if (cardNo.isNotEmpty() && expDate.isNotEmpty() && cvv.isNotEmpty()) {
            callback.onSuccess("Payment initiated successfully")
        } else {
            callback.onFailure("Invalid payment details")
        }
    }

    fun confirmPayment(otp: String, callback: PaymentCallback) {
        // Dummy OTP verification
        if (otp == "123456") {
            callback.onSuccess("Payment confirmed successfully")
        } else {
            callback.onFailure("Invalid OTP")
        }
    }

    fun sendOtp(phoneNumber: String, otp: String) {
        // SMS gönderimi
        val message = "Your verification code is: $otp"
        SmsManager.getDefault().sendTextMessage(phoneNumber, null, message, null, null)
    }
}