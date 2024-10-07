package com.Weekly.android.model.Request

import kotlinx.serialization.Serializable

@Serializable
data class NewExpenseRequest(
    val name: String,
    val amount: Double,
    val currency: String,
) {
}