package com.Weekly.android.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.Weekly.android.R
import com.Weekly.android.viewModel.WelcomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class WelcomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel by viewModel<WelcomeViewModel>()
        viewModel.checkLoggedIn()
        setContent {
            if(viewModel.loggedIn){
                val intent = Intent(this,HomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                finish()
            }else{
                WelcomeWindow()
            }

        }
    }

    @Composable
    fun WelcomeWindow(){
        Column(modifier = Modifier.background(Color.LightGray).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            Image(
                painter = painterResource(id = R.drawable.weekly_black_logo),
                contentDescription = "White logo",
                modifier = Modifier.size(300.dp),
                contentScale = ContentScale.Fit
            )
            Row(){
                SignInButton()
                Spacer(modifier = Modifier.size(15.dp))
                SignUpButton()
            }

        }
    }


    @Composable
    fun SignInButton(){
        Button(colors = ButtonDefaults.buttonColors(
            contentColor = Color.White,
            containerColor = Color(0xFF0A3F04)
        ),
            onClick = {
                val intent = Intent(this, SignInActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                finish()

            } ) {
            Text(text="Sign In")
        }
    }

    @Composable
    fun SignUpButton(){
        Button(colors = ButtonDefaults.buttonColors(
            contentColor = Color.White,
            containerColor = Color.Blue
        ),
            onClick = {
                val intent = Intent(this, SignUpActivity::class.java)
                startActivity(intent)
            } ) {
            Text(text="Sign Up")
        }
    }
}