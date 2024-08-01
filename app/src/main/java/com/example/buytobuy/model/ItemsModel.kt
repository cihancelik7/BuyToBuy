package com.example.buytobuy.model

import android.os.Parcel
import android.os.Parcelable

data class ItemsModel(
    var orderID: String = "",
    var title: String = "",
    var description: String = "",
    var picUrl: ArrayList<String> = ArrayList(),
    var size: ArrayList<String> = ArrayList(),
    var price: Double = 0.0,
    var rating: Double = 0.0,
    var numberInCart: Int = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "", // orderID
        parcel.readString() ?: "", // title
        parcel.readString() ?: "", // description
        parcel.createStringArrayList() ?: ArrayList(), // picUrl
        parcel.createStringArrayList() ?: ArrayList(), // size
        parcel.readDouble(), // price
        parcel.readDouble(), // rating
        parcel.readInt() // numberInCart
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(orderID)
        dest.writeString(title)
        dest.writeString(description)
        dest.writeStringList(picUrl)
        dest.writeStringList(size)
        dest.writeDouble(price)
        dest.writeDouble(rating)
        dest.writeInt(numberInCart)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ItemsModel> {
        override fun createFromParcel(parcel: Parcel): ItemsModel {
            return ItemsModel(parcel)
        }

        override fun newArray(size: Int): Array<ItemsModel?> {
            return arrayOfNulls(size)
        }
    }
}