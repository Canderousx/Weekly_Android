package com.Weekly.android.viewModel
import com.Weekly.android.api.LoginApi
import com.Weekly.android.model.Request.LoginRequest
import com.Weekly.android.service.TokenService
import com.Weekly.android.util.ApiConfiguration

class SignInViewModel(private val apiConfiguration: ApiConfiguration,
    private val tokenService: TokenService): BaseViewModel(apiConfiguration) {

    private var loginApi = LoginApi(apiConfiguration)


    private fun saveAuthToken(token: String?){
        if(!token.isNullOrEmpty()){
            tokenService.saveAuthToken(token);
        }
        return
    }


    fun login(loginRequest: LoginRequest){
        serverConnection(
            method = {loginApi.loginUser(loginRequest)},
            onSuccess = {token -> saveAuthToken(token)},
        )

    }





}