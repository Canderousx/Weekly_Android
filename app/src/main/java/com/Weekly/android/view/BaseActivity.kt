package com.Weekly.android.view

import LoadingComposable
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import com.Weekly.android.model.ServerOperationStatus
import com.Weekly.android.service.LogService
import com.Weekly.android.service.ServerStatusHandler
import com.Weekly.android.viewModel.BaseViewModel

abstract class BaseActivity<T: BaseViewModel>: ComponentActivity() {

    abstract val viewModel: T
    private val logger = LogService()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ObserveServerStatus()
        }
    }

    @Composable
    fun ObserveServerStatus(){
        when (viewModel.operationStatus) {
            ServerOperationStatus.LOADING -> OnLoading()
            ServerOperationStatus.SUCCESS -> OnSuccess()
            ServerOperationStatus.NO_INTERNET_CONNECTION -> OnNoInternetConnection()
            ServerOperationStatus.SESSION_TIME_OUT_ERROR -> OnSessionTimeOut()
            ServerOperationStatus.ERROR -> OnServerError()
            ServerOperationStatus.FATAl_ERROR -> OnServerFatalError()
            ServerOperationStatus.UNKNOWN -> OnUnknownStatus()
        }
    }

    @Composable
    open fun OnLoading(){
        LoadingComposable(viewModel.serverResponse)
    }

    @Composable
    open fun OnSuccess(){
        Toast.makeText(this,"Operation Success!",Toast.LENGTH_LONG).show()
        logger.error("OnSuccess method hasn't been overridden!")
    }

    @Composable
    open fun OnNoInternetConnection(){
        OnLoading()
    }

    @Composable
    open fun OnSessionTimeOut(){
        ServerStatusHandler.sessionTimeOut(this)
        finish()
    }

    @Composable
    open fun OnServerError(){
        ServerStatusHandler.error(this,viewModel.serverResponse?.message?:"Unknown error")
    }

    @Composable
    open fun OnServerFatalError(){
        ServerStatusHandler.fatalError(this,viewModel.serverResponse?.message?:"Unknown error")
    }

    @Composable
    open fun OnUnknownStatus(){

    }



}