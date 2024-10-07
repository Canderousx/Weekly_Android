package com.Weekly.android.view

import LoadingComposable
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import com.Weekly.android.model.Response.ServerResponse
import com.Weekly.android.model.ServerOperationStatus
import com.Weekly.android.viewModel.EmailConfirmationModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class EmailConfirmationActivity : ComponentActivity() {
    val viewModel by viewModel<EmailConfirmationModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ObserveServerStatus()
        }
        val data: Uri? = intent?.data
        if(data == null){
            toWelcomeWindow("Invalid attempt")
        }else{
            viewModel.token = data.getQueryParameter("token")
            viewModel.confirmEmail()
        }



    }

    private fun toWelcomeWindow(toastText: String){
        Toast.makeText(this,toastText,Toast.LENGTH_LONG).show()
        val intent = Intent(this,WelcomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }


    @Composable
    fun ObserveServerStatus(){
        when(viewModel.operationStatus){
            ServerOperationStatus.LOADING -> LoadingComposable(ServerResponse("Activating your account."))
            ServerOperationStatus.SUCCESS -> {toWelcomeWindow("Confirmation success! Log in!") }
            ServerOperationStatus.NO_INTERNET_CONNECTION -> LoadingComposable(ServerResponse("No internet connection! Retrying..."))
            ServerOperationStatus.SESSION_TIME_OUT_ERROR ->{toWelcomeWindow("Your link has expired. New has been sent.")}
            ServerOperationStatus.ERROR -> {
                LoadingComposable(ServerResponse("There was an error: ${viewModel.serverResponse?.message}"))
            }
            ServerOperationStatus.FATAl_ERROR -> {
                val intent = Intent(this,ErrorActivity::class.java)
                intent.putExtra("errorMsg",viewModel.serverResponse?.message)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                finish()
            }
            ServerOperationStatus.UNKNOWN -> {}
        }
    }








}
