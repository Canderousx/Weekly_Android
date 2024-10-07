package com.Weekly.android.view
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class ErrorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val errorMsg:String? = intent.getStringExtra("errorMsg")
        super.onCreate(savedInstanceState)
        setContent {
            ErrorScreen(errorMsg)
        }
    }



    @Composable
    fun ErrorScreen(errorMsg: String?){
        Column(
            modifier = Modifier.fillMaxSize()
                .background(Color.LightGray)
                .padding(top = 50.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("FATAL ERROR", style = TextStyle(fontSize = 45.sp))
            Spacer(Modifier.size(25.dp))
            Text("Description", style = TextStyle(fontSize = 35.sp))
            Spacer(Modifier.size(15.dp))
            if (errorMsg != null) {
                Text(errorMsg)
            }

        }
    }




}


