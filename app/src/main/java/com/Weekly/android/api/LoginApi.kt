package com.Weekly.android.api

import com.Weekly.android.model.Request.LoginRequest
import com.Weekly.android.model.Response.LoginResponse
import com.Weekly.android.model.Response.ServerResponse
import com.Weekly.android.service.LogService
import com.Weekly.android.util.ApiConfiguration
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.contentType
import io.ktor.http.HttpStatusCode


class LoginApi(apiConfiguration: ApiConfiguration): BaseApi(apiConfiguration) {

    suspend fun loginUser(loginRequest: LoginRequest):String{
        val response = httpClient.post(serverUrl + "login") {
            setBody(loginRequest)
            contentType(contentType)
        }
        if (response.status == HttpStatusCode.OK) {
            val loginResponse: LoginResponse = response.body()
            return loginResponse.authToken
        }
        else{
            return ""
        }

    }

    suspend fun confirmEmail(): ServerResponse{
        val response = httpClient.get(serverUrl+"confirmEmail"){
            contentType(contentType)
        }
        val serverResponse:ServerResponse = response.body()
        return serverResponse
    }

}