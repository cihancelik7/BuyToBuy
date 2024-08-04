package com.example.buytobuy.helper


import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.os.Environment
import android.preference.PreferenceManager
import android.text.TextUtils
import android.util.Log
import com.example.buytobuy.model.ItemsModel
import com.google.gson.Gson
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Arrays


class TinyDB(appContext: Context?) {
    private val preferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(appContext)
    private var DEFAULT_APP_IMAGEDATA_DIRECTORY: String? = null








    fun getListString(key: String?): ArrayList<String> {
        return ArrayList(Arrays.asList(*TextUtils.split(preferences.getString(key, ""), "‚‗‚")))
    }


    // Put methods
    fun getListObject(key: String?): ArrayList<ItemsModel> {
        val gson = Gson()

        val objStrings = getListString(key)
        val playerList: ArrayList<ItemsModel> = ArrayList<ItemsModel>()

        for (jObjString in objStrings) {
            val player: ItemsModel = gson.fromJson(jObjString, ItemsModel::class.java)
            playerList.add(player)
        }
        return playerList
    }


    fun putListString(key: String?, stringList: ArrayList<String>) {
        checkForNullKey(key)
        val myStringList = stringList.toTypedArray<String>()
        preferences.edit().putString(key, TextUtils.join("‚‗‚", myStringList)).apply()
    }


    fun putListObject(key: String?, playerList: ArrayList<ItemsModel>) {
        checkForNullKey(key)
        val gson = Gson()
        val objStrings = ArrayList<String>()
        for (player in playerList) {
            objStrings.add(gson.toJson(player))
        }
        putListString(key, objStrings)
    }



    private fun checkForNullKey(key: String?) {
        if (key == null) {
            throw NullPointerException()
        }
    }
}