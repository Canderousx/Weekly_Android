package com.Weekly.android.api
import com.Weekly.android.model.Request.LoginRequest
import com.Weekly.android.model.Response.ServerResponse
import com.Weekly.android.util.ApiConfiguration


class LoginApi(apiConfiguration: ApiConfiguration): BaseApi(apiConfiguration) {

    suspend fun loginUser(loginRequest: LoginRequest):String{
        val response = post("login",String::class.java,loginRequest) ?: return ""
        return response
    }

    suspend fun confirmEmail(): ServerResponse{
        val response = get("confirmEmail",ServerResponse::class.java) ?: return ServerResponse("")
        return response
    }

}