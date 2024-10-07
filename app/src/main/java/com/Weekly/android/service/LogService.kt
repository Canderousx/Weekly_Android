package com.Weekly.android.service

import android.util.Log

class LogService {

    private val tag: String = "WeeklyAppLog"

    fun info(message:String){
        Log.i(tag,message)
    }

    fun error(message: String){
        Log.e(tag,message)
    }

    fun debug(message: String){
        Log.d(tag,message)
    }

}