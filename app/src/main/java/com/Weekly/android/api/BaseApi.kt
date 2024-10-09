package com.Weekly.android.api

import com.Weekly.android.service.LogService
import com.Weekly.android.util.ApiConfiguration
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.contentType

abstract class BaseApi(apiConfiguration: ApiConfiguration) {

    protected val httpClient = apiConfiguration.getClient()
    protected val serverUrl = apiConfiguration.getServerUrl()
    protected val contentType = apiConfiguration.getContentType()
    protected val logger = LogService()

    protected suspend inline fun <reified T> responseReader(response: HttpResponse, clazz: Class<T>):T?{
        if(response.status.value == 200){
            val obj: T = response.body()
            return obj
        }
        return null
    }

    protected suspend inline fun <reified T> get(url: String, responseClass: Class<T>): T?{
        val response = httpClient.get(serverUrl+url){
            contentType(contentType)
        }
        return responseReader(response,responseClass)
    }

    protected suspend inline fun <reified T,reified R> post(url: String, responseClass: Class<T>, body: R? = null): T?{
        val response = httpClient.post(serverUrl+url){
            contentType(contentType)
            if(body != null){
                setBody(body)
            }
        }
        return responseReader(response,responseClass)

    }

    protected suspend inline fun <reified T> delete(url: String, responseClass: Class<T>): T?{
        val response = httpClient.delete(serverUrl+url){
            contentType(contentType)
        }
        return responseReader(response,responseClass)
    }


}