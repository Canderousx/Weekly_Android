package com.Weekly.android.model


import kotlinx.serialization.Serializable


@Serializable
data class User(
    val id: String,
    val username: String,
    val email: String,
    var weeklyPlan: Double,
    val currency: String,
):java.io.Serializable
