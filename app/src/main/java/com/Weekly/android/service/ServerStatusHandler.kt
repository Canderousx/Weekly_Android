package com.Weekly.android.service
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import com.Weekly.android.view.ErrorActivity
import com.Weekly.android.view.WelcomeActivity

object ServerStatusHandler {

    fun sessionTimeOut(context:Context){
        Toast.makeText(context,"Your session has expired. Login.",Toast.LENGTH_LONG).show()
        val nav = NavigationService(context)
        nav.navigateTo(WelcomeActivity::class.java,true)
    }

    fun error(context: Context,message:String){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show()
    }

    fun fatalError(context: Context,message: String){
        error(context,message)
        val errorBundle = Bundle()
        errorBundle.putString("errorMsg",message)
        val nav = NavigationService(context)
        nav.navigateTo(ErrorActivity::class.java,true,errorBundle)
    }
}