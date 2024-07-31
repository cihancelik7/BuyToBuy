package com.example.buytobuy.helper

import com.example.buytobuy.model.PaymentModel
import com.example.buytobuy.sdk.PaymentCallback
import com.example.buytobuy.sdk.PaymentSDK

class PaymentHelper {

    fun initiatePayment(paymentModel: PaymentModel, callback: PaymentCallback) {
        PaymentSDK.startPayment(
            paymentModel.cardNumber,
            paymentModel.expireDate,
            paymentModel.cvc,
            paymentModel.amount,
            callback
        )
    }

    fun confirmPayment(otp: String, callback: PaymentCallback) {
        PaymentSDK.confirmPayment(otp, callback)
    }
}