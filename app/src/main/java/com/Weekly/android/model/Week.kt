package com.Weekly.android.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Week(val id:String,
                val expenses: Double,
                val weekStart: LocalDateTime,
                val weekEnd: LocalDateTime,)
{
    fun getStartDate():String{
        return weekStart.date.toString()
    }

    fun getEndDate():String{
        return weekEnd.date.toString()
    }
}

@Serializable
data class WeeksList(
    val weeks: List<Week>
)
