package com.Weekly.android.api

import com.Weekly.android.model.Request.EmailExistsRequest
import com.Weekly.android.model.Response.EmailExistsResponse
import com.Weekly.android.model.Response.ServerResponse
import com.Weekly.android.model.Request.SignupRequest
import com.Weekly.android.model.Request.UsernameExistsRequest
import com.Weekly.android.model.Request.WeeklyPlanSetupReq
import com.Weekly.android.model.User
import com.Weekly.android.model.Response.UsernameExistsResponse
import com.Weekly.android.util.ApiConfiguration

class UserDetailsApi(apiConfiguration: ApiConfiguration): BaseApi(apiConfiguration) {

    suspend fun getCurrentUser(): User?{
        return get("getCurrentUser",User::class.java)
    }

    suspend fun usernameExists(username:String): Boolean{
        val usernameExistsRequest = UsernameExistsRequest(username)
        val response = post("username_exists",UsernameExistsResponse::class.java,usernameExistsRequest) ?: return false
        return response.usernameExists
    }

    suspend fun emailExists(email:String): Boolean{
        val emailExistsRequest = EmailExistsRequest(email)
        val response = post("email_exists",EmailExistsResponse::class.java,emailExistsRequest) ?: return false
        return response.emailExists
    }

    suspend fun signup(signupRequest: SignupRequest): ServerResponse{
        val response = post("signup",ServerResponse::class.java,signupRequest) ?: ServerResponse("")
        return response
    }

    suspend fun setWeeklyPlan(weeklyPlan: Double,currency: String,editMode:Boolean): ServerResponse?{
        val url = if(editMode) "editWeeklyPlan" else "setWeeklyPlan"
        val weeklyPlanRequest = WeeklyPlanSetupReq(weeklyPlan,currency)
        return post(url,ServerResponse::class.java,weeklyPlanRequest)
    }
}