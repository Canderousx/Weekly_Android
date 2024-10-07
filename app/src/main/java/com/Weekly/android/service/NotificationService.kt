package com.Weekly.android.service

import android.content.Context
import android.widget.Toast

class NotificationService(val context: Context) {

    fun showToast(text: String){
            Toast.makeText(context,text,Toast.LENGTH_SHORT).show()
    }


}