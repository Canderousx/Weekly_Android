package com.Weekly.android.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Weekly.android.exceptions.InternalServerErrorException
import com.Weekly.android.exceptions.SessionTimeOutException
import com.Weekly.android.model.Response.ServerResponse
import com.Weekly.android.model.ServerOperationStatus
import com.Weekly.android.service.LogService
import com.Weekly.android.util.ApiConfiguration
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.ConnectException

abstract class BaseViewModel(
    private val apiConfiguration: ApiConfiguration,
): ViewModel() {

    var operationStatus by mutableStateOf<ServerOperationStatus>(ServerOperationStatus.UNKNOWN)
    var serverResponse by mutableStateOf<ServerResponse?>(null)
    val logger = LogService()

    protected open fun onSessionTimeOut(){
        apiConfiguration.handleSessionTimeOut()
    }

    protected open fun onError(){
    }

    protected open fun onFatalError(){

    }

    init {
        operationStatus = ServerOperationStatus.UNKNOWN
        serverResponse = null
    }

    protected fun <T> serverConnection(
        method: suspend () -> T?,
        onSuccess: (T?) -> Unit,
        retryDelay: Long = 2000L,
    ) {
        viewModelScope.launch {
            operationStatus = ServerOperationStatus.LOADING
            serverResponse = null
            while (operationStatus == ServerOperationStatus.LOADING || operationStatus == ServerOperationStatus.NO_INTERNET_CONNECTION) {
                try {
                    val result = method()
                    operationStatus = ServerOperationStatus.SUCCESS
                    onSuccess(result)
                    return@launch
                } catch (e: Exception) {
                    logger.error("Error: ${e.message}")
                    serverResponse = ServerResponse(e.message ?: "Unknown Error")
                    when(e){
                        is ConnectException->{
                            if (e.message == "Network is unreachable"){
                                operationStatus = ServerOperationStatus.NO_INTERNET_CONNECTION
                                delay(retryDelay)
                            }else{
                                operationStatus = ServerOperationStatus.ERROR
                                onError()
                            }
                        }
                        is SessionTimeOutException->{
                            operationStatus = ServerOperationStatus.SESSION_TIME_OUT_ERROR
                            onSessionTimeOut()
                        }
                        is InternalServerErrorException->{
                            logger.error("Internal Server Error 500")
                            operationStatus = ServerOperationStatus.FATAl_ERROR
                            onFatalError()
                        }
                        else ->{
                            logger.error(e.javaClass.name)
                            operationStatus = ServerOperationStatus.ERROR
                            onError()
                        }
                    }
                }
            }
        }
    }


}