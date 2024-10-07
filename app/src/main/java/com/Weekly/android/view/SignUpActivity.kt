package com.Weekly.android.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.Weekly.android.R
import com.Weekly.android.model.ServerOperationStatus
import com.Weekly.android.service.LogService
import com.Weekly.android.service.NotificationService
import com.Weekly.android.viewModel.SignUpViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SignUpActivity : BaseActivity<SignUpViewModel>() {
    override val viewModel by viewModel<SignUpViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SignUpWindow()
            ObserveServerStatus()
        }
    }
    private val logger = LogService();
    private val notifications = NotificationService(this)

    @Composable
    override fun OnSuccess() {
        logger.info("ON SUCCESS")
        if(viewModel.signupSuccess){
            Toast.makeText(this, viewModel.serverResponse?.message ?: "",Toast.LENGTH_LONG).show()
            val intent = Intent(this,WelcomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        }
    }

    @Composable
    override fun OnLoading() {
    }

    @Composable
    override fun OnServerError() {
        Toast.makeText(this,viewModel.serverResponse?.message?:"",Toast.LENGTH_LONG).show()
        viewModel.checkExists()
    }

    @Composable
    fun SignUpWindow(){
        Column(modifier = Modifier
            .background(Color.LightGray)
            .fillMaxSize()
            .padding(top = 100.dp, start = 15.dp, end = 15.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,) {
            Image(
                painter = painterResource(id = R.drawable.weekly_black_logo),
                contentDescription = "White logo",
                modifier = Modifier.size(150.dp),
                contentScale = ContentScale.Fit
            )
            Text("Account Creation")
            Spacer(Modifier.size(20.dp))
            SignUpForm()

        }
    }

    @Composable
    fun SignUpForm(){
        Column(verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally){
            AnimatedVisibility(
                visible = viewModel.usernameExists,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text("Username already exists.", style = TextStyle(color = Color.Red))
            }
            TextField(
                value = viewModel.username,
                onValueChange = {
                    viewModel.username = it

                                },
                label = { Text(text="Username") },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.size(5.dp))

            AnimatedVisibility(
                visible = viewModel.emailExists,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text("Email address already exists.", style = TextStyle(Color.Red))
            }
            TextField(
                value = viewModel.email,
                onValueChange = {
                    viewModel.email = it
                    },
                label = { Text(text="Email Address") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.size(5.dp))

            TextField(
                value = viewModel.password,
                onValueChange = {viewModel.password = it},
                label = { Text(text="Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(Modifier.size(10.dp))

            Button(colors = ButtonDefaults.buttonColors(
                contentColor = Color.White,
                containerColor = Color(0xFF0A3F04)
            ),
                onClick = {
                    viewModel.signup()
                }) {

                Text(text="Sign Up")

            }
        }
    }
}

