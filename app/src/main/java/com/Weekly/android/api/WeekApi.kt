package com.Weekly.android.api

import com.Weekly.android.model.Currencies
import com.Weekly.android.model.ExpensesList
import com.Weekly.android.model.Request.NewExpenseRequest
import com.Weekly.android.model.Response.ServerResponse
import com.Weekly.android.model.Week
import com.Weekly.android.service.LogService
import com.Weekly.android.util.ApiConfiguration
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.contentType

class WeekApi(apiConfiguration: ApiConfiguration): BaseApi(apiConfiguration) {

    suspend fun getCurrentWeek(): Week?{
        val response = httpClient.get(serverUrl+"getCurrentWeek"){
            contentType(contentType)}
        if(response.status.value == 200){
            val week:Week = response.body()
            return week
        }
        return null
    }

    suspend fun getAvailableCurrencies(): Currencies?{
        val response = httpClient.get(serverUrl+"getCurrenciesNames"){
            contentType(contentType)
        }
        if(response.status.value == 200){
            val currencies: Currencies = response.body()
            return currencies
        }
        return null
    }

    suspend fun getWeekExpenses(weekId: String): ExpensesList?{
        val response = httpClient.get(serverUrl+"getExpenses?id="+weekId+"&page_size=500&page=1"){
            contentType(contentType)
        }
        if (response.status.value == 200){
            val expenses: ExpensesList = response.body()
            return expenses
        }
        return null
    }

    suspend fun addNewExpense(newExpense: NewExpenseRequest):ServerResponse?{
        val response = httpClient.post(serverUrl+"addExpense"){
            contentType(contentType)
            setBody(newExpense)
        }
        if(response.status.value == 200){
            val serverResponse:ServerResponse = response.body()
            return serverResponse
        }
        return null
    }

    suspend fun editExpense(toEditId:String,newData: NewExpenseRequest):ServerResponse?{
        val response = httpClient.post(serverUrl+"editExpense?id=${toEditId}"){
            contentType(contentType)
            setBody(newData)
        }
        if(response.status.value == 200){
            val serverResponse:ServerResponse = response.body()
            return serverResponse
        }
        return null
    }

    suspend fun deleteExpense(toDeleteId: String): ServerResponse?{
        val response = httpClient.delete(serverUrl+"deleteExpense?id=${toDeleteId}"){
            contentType(contentType)
        }
        if(response.status.value == 200){
            val serverResponse: ServerResponse = response.body()
            return serverResponse
        }
        return null
    }

}