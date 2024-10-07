package com.Weekly.android.api

import com.Weekly.android.model.Request.EmailExistsRequest
import com.Weekly.android.model.Response.EmailExistsResponse
import com.Weekly.android.model.Response.ServerResponse
import com.Weekly.android.model.Request.SignupRequest
import com.Weekly.android.model.Request.UsernameExistsRequest
import com.Weekly.android.model.Request.WeeklyPlanSetupReq
import com.Weekly.android.model.User
import com.Weekly.android.model.Response.UsernameExistsResponse
import com.Weekly.android.service.LogService
import com.Weekly.android.util.ApiConfiguration
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.contentType

class UserDetailsApi(apiConfiguration: ApiConfiguration) {

    private val httpClient = apiConfiguration.getClient()
    private val serverUrl = apiConfiguration.getServerUrl()
    private val contentType = apiConfiguration.getContentType()
    private val logger = LogService()

    suspend fun getCurrentUser(): User?{
        val response = httpClient.get(serverUrl+"getCurrentUser"){
            contentType(contentType) }
        if(response.status.value == 200){
            val user:User = response.body()
            return user
        }
        return null
    }

    suspend fun usernameExists(username:String): Boolean{
        val usernameExistsRequest = UsernameExistsRequest(username)
        val response = httpClient.post(serverUrl+"username_exists"){
            setBody(usernameExistsRequest)
            contentType(contentType)
        }
        if(response.status.value == 200){
            val usernameExists: UsernameExistsResponse = response.body()
            return usernameExists.usernameExists
        }
        return false
    }

    suspend fun emailExists(email:String): Boolean{
        val emailExistsRequest = EmailExistsRequest(email)
        val response = httpClient.post(serverUrl+"email_exists"){
            setBody(emailExistsRequest)
            contentType(contentType)
        }
        if(response.status.value == 200){
            val emailExists: EmailExistsResponse = response.body()
            return emailExists.emailExists
        }
        return false
    }

    suspend fun signup(signupRequest: SignupRequest): ServerResponse{
        val response = httpClient.post(serverUrl+"signup"){
            setBody(signupRequest)
            contentType(contentType)
        }
        val serverResponse: ServerResponse = response.body()
        return serverResponse
    }

    suspend fun setWeeklyPlan(weeklyPlan: Double,currency: String,editMode:Boolean): ServerResponse?{
        val url = if(editMode) "editWeeklyPlan" else "setWeeklyPlan"
        val weeklyPlanRequest = WeeklyPlanSetupReq(weeklyPlan,currency)
        val response =  httpClient.post(serverUrl+url){
            setBody(weeklyPlanRequest)
            contentType(contentType)
        }
        if(response.status.value == 200){
            val serverResponse: ServerResponse = response.body()
            return serverResponse
        }
        return null
    }
}