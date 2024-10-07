package com.Weekly

import android.app.Application
import com.Weekly.android.service.NavigationService
import com.Weekly.android.service.NotificationService
import com.Weekly.android.service.TokenService
import com.Weekly.android.util.ApiConfiguration
import com.Weekly.android.viewModel.EmailConfirmationModel
import com.Weekly.android.viewModel.HomeViewModel
import com.Weekly.android.viewModel.SignInViewModel
import com.Weekly.android.viewModel.SignUpViewModel
import com.Weekly.android.viewModel.WelcomeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class WeeklyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@WeeklyApplication)
            modules(
                module {
                    single {TokenService(androidContext())}
                    single {NavigationService(androidContext()) }
                    single {NotificationService(androidContext())}
                    single {ApiConfiguration(get())}

                    viewModel { WelcomeViewModel(get()) }
                    viewModel { HomeViewModel(get()) }
                    viewModel { SignInViewModel(get(),get()) }
                    viewModel { SignUpViewModel(get())}
                    viewModel { EmailConfirmationModel(get(),get()) }
                }
            )
        }

    }


}