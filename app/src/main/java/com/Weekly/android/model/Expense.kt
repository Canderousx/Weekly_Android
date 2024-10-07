package com.Weekly.android.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Expense(val id:String,
                   val name: String,
                   val amount: Double,
                   val weekId: String,
                   val date: LocalDateTime,)



@Serializable
data class ExpensesList(
    val expenses: List<Expense>
)
