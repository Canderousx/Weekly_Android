package com.Weekly.android.api
import com.Weekly.android.model.Response.AverageWeeklyExpenseResponse
import com.Weekly.android.model.Response.HowManyWeeksResponse
import com.Weekly.android.model.WeeksList
import com.Weekly.android.util.ApiConfiguration

class StatisticsApi(apiConfiguration: ApiConfiguration):BaseApi(apiConfiguration) {


    suspend fun getAverageWeeklyExpense():Double?{
        val response = get("getAverageWeeklyExpense",AverageWeeklyExpenseResponse::class.java)
        return response?.average
    }

    suspend fun getHowManyWeeksInWeekly():Int?{
        val response = get("howManyWeeks",HowManyWeeksResponse::class.java)
        return response?.quantity
    }

    suspend fun getAverageTotalExpense():Double?{
        val response = get("getAverageTotalExpense",AverageWeeklyExpenseResponse::class.java)
        return response?.average
    }

    suspend fun getWeeks():WeeksList?{
        return get("getWeeks?page=1&page_size=500",WeeksList::class.java)
    }



}