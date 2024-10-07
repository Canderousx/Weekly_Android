package com.Weekly.android.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.Weekly.android.model.User
import com.Weekly.android.util.ApiConfiguration

class WeeklyPlanSetupModel(private val apiConfiguration: ApiConfiguration):BaseViewModel(apiConfiguration) {

    var currentUser: User? by mutableStateOf(null)
}