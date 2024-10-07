package com.Weekly.android.model.Request

import kotlinx.serialization.Serializable


@Serializable
data class LoginRequest (
    val email: String,

    val password: String
    )