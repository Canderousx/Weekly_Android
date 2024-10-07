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

    var signupSuccess by mutableStateOf(false)


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

    fun checkExists(){
        checkEmail()
        checkUsername()
    }

    private fun validateForm(): Boolean {
            if (!ValidatorService.SignupValidator.isNotEmpty(getSignupRequest())) {
                serverResponse = ServerResponse("Every field is required")
                logger.info("Missing fields detected")
                return false
            }
            if (!ValidatorService.EmailValidator.isValid(email)) {
                serverResponse = ServerResponse("Invalid email address")
                logger.info("Wrong email address detected")
                return false
            }
            return true
    }

    fun signup(){
        signupSuccess = false
        operationStatus = ServerOperationStatus.UNKNOWN
        serverResponse = null
        if(!validateForm()){
            operationStatus = ServerOperationStatus.ERROR
            logger.info("Signup form is invalid!. HTTP Request aborted")
            return
        }
        serverConnection(
            method = {userDetailsApi.signup(getSignupRequest())},
            onSuccess = {
                message -> serverResponse = message
                signupSuccess = true})
        }


    }