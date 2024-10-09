package com.Weekly.android.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.Weekly.android.R
import com.Weekly.android.model.Request.LoginRequest
import com.Weekly.android.service.ValidatorService
import com.Weekly.android.viewModel.SignInViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignInActivity : BaseActivity<SignInViewModel>() {
    override val viewModel by viewModel<SignInViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SignInWindow()
            ObserveServerStatus()
        }
    }

    @Composable
    override fun OnSuccess() {
        Toast.makeText(this,"You've been logged in!",Toast.LENGTH_SHORT).show()
        val intent = Intent(this,HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }

    @Composable
    fun SignInWindow(){
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
            Text("Login to your Weekly Account!")
            Spacer(Modifier.size(20.dp))
            SignInForm()

        }
    }
    fun submit(email: String,password: String){
        val loginRequest = LoginRequest(email,password)
        if(!ValidatorService.LoginValidator.isNotEmpty(loginRequest)){
            viewModel.logger.info("Missing fields detected")
            Toast.makeText(this,"All fields are required!",Toast.LENGTH_LONG).show()
            return
        }
        viewModel.login(loginRequest)
    }

    @Composable
    fun SignInForm(){
        var email by rememberSaveable { mutableStateOf("") }
        var password by rememberSaveable { mutableStateOf("") }
        val context = LocalContext.current

        Column(verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally){

            TextField(
                value = email,
                onValueChange = {email = it},
                label = { Text(text="Email Address") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.size(5.dp))

            TextField(
                value = password,
                onValueChange = {password = it},
                label = { Text(text="Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )

            ClickableText(
                text = AnnotatedString("Forgot your password?"),
                onClick = {
                    val intent = Intent(context,PasswordRecoveryActivity::class.java)
                    startActivity(intent)
                    finish()
                },
                style = TextStyle(color = Color.Blue, textDecoration = TextDecoration.Underline)
            )

            Spacer(Modifier.size(10.dp))

            Button(colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = Color(0xFF0A3F04)),
                onClick = {
                    submit(email,password)
                }) {

                Text(text="Sign In")

            }
        }
    }

}




