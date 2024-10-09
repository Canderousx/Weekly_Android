package com.Weekly.android.api

import com.Weekly.android.model.Currencies
import com.Weekly.android.model.ExpensesList
import com.Weekly.android.model.Request.NewExpenseRequest
import com.Weekly.android.model.Response.ServerResponse
import com.Weekly.android.model.Week
import com.Weekly.android.util.ApiConfiguration

class WeekApi(apiConfiguration: ApiConfiguration): BaseApi(apiConfiguration) {

    suspend fun getCurrentWeek(): Week?{
        return get("getCurrentWeek",Week::class.java)
    }

    suspend fun getAvailableCurrencies(): Currencies?{
        return get("getCurrenciesNames",Currencies::class.java)
    }

    suspend fun getWeekExpenses(weekId: String): ExpensesList?{
        return get("getExpenses?id=${weekId}&page_size=500&page=1",ExpensesList::class.java)
    }

    suspend fun addNewExpense(newExpense: NewExpenseRequest):ServerResponse?{
        return post("addExpense",ServerResponse::class.java,newExpense)
    }

    suspend fun editExpense(toEditId:String,newData: NewExpenseRequest):ServerResponse?{
        return post("editExpense?id=${toEditId}",ServerResponse::class.java,newData)
    }

    suspend fun deleteExpense(toDeleteId: String): ServerResponse?{
        return delete("deleteExpense?id=${toDeleteId}",ServerResponse::class.java)
    }

}