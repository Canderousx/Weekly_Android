package com.Weekly.android.model.Request

import kotlinx.serialization.Serializable


@Serializable
data class EmailExistsRequest(
    val email: String
) {
}