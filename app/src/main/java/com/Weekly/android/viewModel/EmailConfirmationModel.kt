package com.Weekly.android.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.Weekly.android.api.LoginApi
import com.Weekly.android.model.Response.ServerResponse
import com.Weekly.android.model.ServerOperationStatus
import com.Weekly.android.service.TokenService
import com.Weekly.android.util.ApiConfiguration

class EmailConfirmationModel(private val apiConfiguration: ApiConfiguration,
    private val tokenService: TokenService):BaseViewModel(apiConfiguration) {

    var token:String? by mutableStateOf(null)
    val loginApi = LoginApi(apiConfiguration)


    fun confirmEmail(){
        if(token == null){
            operationStatus = ServerOperationStatus.ERROR
            serverResponse = ServerResponse("Activation token is null!")
            return
        }
        tokenService.clearAuthToken()
        tokenService.saveAuthToken(token!!)
        serverConnection(
            method = {loginApi.confirmEmail()},
            onSuccess = {response -> serverResponse = response},
        )
        tokenService.clearAuthToken()
    }


}