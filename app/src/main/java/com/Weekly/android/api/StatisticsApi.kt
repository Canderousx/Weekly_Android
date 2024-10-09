package com.Weekly.android.api

import com.Weekly.android.model.Response.AverageWeeklyExpenseResponse
import com.Weekly.android.model.Response.HowManyWeeksResponse
import com.Weekly.android.model.WeeksList
import com.Weekly.android.util.ApiConfiguration
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.utils.EmptyContent.contentType
import io.ktor.http.contentType

class StatisticsApi(apiConfiguration: ApiConfiguration):BaseApi(apiConfiguration) {


    suspend fun getAverageWeeklyExpense():Double?{
        val response = httpClient.get(serverUrl+"getAverageWeeklyExpense"){
            contentType(contentType)
        }
        if(response.status.value == 200){
            val average: AverageWeeklyExpenseResponse = response.body()
            return average.average
        }
        return null
    }

    suspend fun getHowManyWeeksInWeekly():Int?{
        val response = httpClient.get(serverUrl+"howManyWeeks"){
            contentType(contentType)
        }
        if(response.status.value == 200){
            val quantity: HowManyWeeksResponse = response.body()
            return quantity.quantity
        }
        return null
    }

    suspend fun getAverageTotalExpense():Double?{
        val response = httpClient.get(serverUrl+"getAverageTotalExpense"){
            contentType(contentType)
        }
        if(response.status.value == 200){
            val average: AverageWeeklyExpenseResponse = response.body()
            return average.average
        }
        return null
    }

    suspend fun getWeeks():WeeksList?{
        val response = httpClient.get(serverUrl+"getWeeks?page=1&page_size=500"){
            contentType(contentType)
        }
        if (response.status.value == 200){
            val weeks: WeeksList = response.body()
            return weeks
        }
        return null
    }



}