package com.Weekly.android.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class PasswordRecoveryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PasswordRecoveryForm()
        }
    }


    @Composable
    fun PasswordRecoveryForm(){
        var email by remember { mutableStateOf("") }
        Column(
            modifier = Modifier
                .background(Color.LightGray)
                .fillMaxSize().padding(start = 10.dp, end = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(text = "Password Recovery", style = TextStyle(fontSize = 30.sp))

            Spacer(modifier = Modifier.size(40.dp))
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Enter your email address") },
                modifier = Modifier.fillMaxWidth()

            )
            Spacer(Modifier.size(20.dp))
            Button(colors = ButtonDefaults.buttonColors(
                contentColor = Color.White,
                containerColor = Color(0xFF0A3F04)),
                onClick = {}) {

                Text(text="Submit")

            }
        }

    }
}
