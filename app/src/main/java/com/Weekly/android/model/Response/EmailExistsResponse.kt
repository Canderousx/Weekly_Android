package com.Weekly.android.model.Response

import kotlinx.serialization.Serializable

@Serializable
data class EmailExistsResponse(
    val emailExists: Boolean
)

