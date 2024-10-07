package com.Weekly.android.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Weekly.android.service.TokenService
import kotlinx.coroutines.launch

class WelcomeViewModel(
    val tokenService: TokenService,
): ViewModel() {

    var loggedIn:Boolean by mutableStateOf( false)

    fun checkLoggedIn(){
        viewModelScope.launch {
            loggedIn = !tokenService.getAuthToken().isNullOrEmpty()
        }
    }

}