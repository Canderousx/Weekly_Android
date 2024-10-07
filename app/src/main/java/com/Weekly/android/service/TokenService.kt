package com.Weekly.android.service

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class TokenService(context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "auth_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveAuthToken(token: String){
        with(sharedPreferences.edit()){
            putString("auth_token",token)
            apply()
        }
    }

    fun getAuthToken():String?{
        return sharedPreferences.getString("auth_token",null)
    }

    fun clearAuthToken(){
        with(sharedPreferences.edit()){
            remove("auth_token")
            apply()
        }
    }
}