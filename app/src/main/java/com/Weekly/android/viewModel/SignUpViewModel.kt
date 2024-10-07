package com.Weekly.android.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.Weekly.android.api.UserDetailsApi
import com.Weekly.android.model.Request.SignupRequest
import com.Weekly.android.model.Response.ServerResponse
import com.Weekly.android.model.ServerOperationStatus
import com.Weekly.android.service.ValidatorService
import com.Weekly.android.util.ApiConfiguration

class SignUpViewModel(private val apiConfiguration: ApiConfiguration, ): BaseViewModel(apiConfiguration) {

    var email by mutableStateOf("")
    var username by  mutableStateOf("")
    var password by  mutableStateOf("")

    val userDetailsApi = UserDetailsApi(apiConfiguration)

    var usernameExists by mutableStateOf(false)

    var emailExists by mutableStateOf(false)

    var signupSend by mutableStateOf(false)


    fun getSignupRequest(): SignupRequest{
        return SignupRequest(username,email,password)
    }


    fun checkUsername(){
        serverConnection(
            method = {userDetailsApi.usernameExists(username)},
            onSuccess = {exists -> usernameExists = exists?: false},
        )
    }

    fun checkEmail(){
        serverConnection(
            method = {userDetailsApi.emailExists(email)},
            onSuccess = {exists -> emailExists = exists?:false},
        )
    }

    private fun validateForm():Boolean{
        if(!ValidatorService.SignupValidator.isNotEmpty(getSignupRequest())){
            operationStatus = ServerOperationStatus.ERROR
            serverResponse = ServerResponse("Every field is required")
            logger.info("Missing fields detected")
            return false
        }
        if(!ValidatorService.EmailValidator.isValid(email)){
            operationStatus = ServerOperationStatus.ERROR
            serverResponse = ServerResponse("Invalid email address")
            logger.info("Wrong email address detected")
            return false
        }
        checkUsername()
        checkEmail()
        return (usernameExists && emailExists)
    }

    fun signup(){
        operationStatus = ServerOperationStatus.UNKNOWN
        serverResponse = null
        if(!validateForm()){
            logger.info("Signup form is invalid!. HTTP Request aborted")
            return
        }
        signupSend = true
        serverConnection(
            method = {userDetailsApi.signup(getSignupRequest())},
            onSuccess = {message -> serverResponse = message},
        )

    }





}