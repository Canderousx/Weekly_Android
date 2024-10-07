package com.Weekly.android.view

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.Weekly.android.model.User
import com.Weekly.android.viewModel.WeeklyPlanSetupModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class WeeklyPlanSetup : ComponentActivity() {
    val viewModel by viewModel<WeeklyPlanSetupModel>()
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.currentUser = intent.getSerializableExtra("currentUser",User::class.java)
        setContent {
            WeeklyPlanSetupWindow()
        }
    }


    @Composable
    fun WeeklyPlanSetupWindow(){
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Weekly Plan Setup!")
        }

    }
}