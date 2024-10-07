package com.Weekly.android.model.Response

import kotlinx.serialization.Serializable

@Serializable
data class UsernameExistsResponse(
    val usernameExists: Boolean
)
