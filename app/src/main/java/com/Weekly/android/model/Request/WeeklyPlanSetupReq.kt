package com.Weekly.android.model.Request

import kotlinx.serialization.Serializable

@Serializable
data class WeeklyPlanSetupReq(
    val weeklyPlan: Double,
    val currency: String
) {
}