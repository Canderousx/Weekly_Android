package com.Weekly.android.service

import android.content.Context
import android.content.Intent
import android.os.Bundle

class NavigationService(private val context: Context) {

    fun navigateTo(activityClass: Class<*>,clearTask: Boolean = false,extras: Bundle? = null){
        val intent = Intent(context,activityClass)

        if(clearTask){
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        extras?.let {
            intent.putExtras(it)
        }
        context.startActivity(intent)
    }
}