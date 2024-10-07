package com.Weekly.android.util

import com.Weekly.android.exceptions.ForbiddenResponseException
import com.Weekly.android.exceptions.InternalServerErrorException
import com.Weekly.android.exceptions.SessionTimeOutException
import com.Weekly.android.model.Response.ServerResponse
import com.Weekly.android.service.LogService
import com.Weekly.android.service.TokenService
import io.ktor.client.HttpClient
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.errors.IOException
import kotlinx.serialization.json.Json


class ApiConfiguration(private val tokenService: TokenService) {

    private val serverUrl = "http://64.226.72.89:5000/"

    private val contentType = ContentType.Application.Json

    private val log = LogService()

    private val client = HttpClient {
        install(Logging) {
            level = LogLevel.ALL
        }
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
        defaultRequest {
            val authToken = tokenService.getAuthToken()
            if (!authToken.isNullOrEmpty()) {
                header("Authorization", "Bearer $authToken")
            }
        }

        HttpResponseValidator {
            validateResponse { response ->
                val statusCode = response.status.value
                if (statusCode != 200) {
                    val errorBody = response.bodyAsText()
                    val serverResponse: ServerResponse = try {
                        Json.decodeFromString(ServerResponse.serializer(), errorBody)
                    } catch (e: Exception) {
                        ServerResponse("Couldn't decode error body from response!")
                    }
                    log.error("HTTP RESPONSE CODE: $statusCode MSG:  ${serverResponse.message}")
                    when(response.status.value){
                        400 ->{}
                        401 -> throw SessionTimeOutException(serverResponse.message)
                        403 -> throw ForbiddenResponseException(serverResponse.message)
                        404 ->{}
                        500 -> throw InternalServerErrorException(serverResponse.message)
                    }

                }
            }

            handleResponseExceptionWithRequest { exception, _ ->
                when (exception) {
                    is ClientRequestException -> {
                        log.error("Client error: ${exception.response.status}")
                    }
                    is ServerResponseException -> {
                        log.error("Server error: ${exception.response.status}")
                    }
                    is IOException -> {
                        log.error("No Internet Connection Error!: ${exception.message}")
                    }
                    else -> {
                        log.error("Unknown error: ${exception.message}")
                    }
                }
            }
        }



    }

    fun handleSessionTimeOut(){
        tokenService.clearAuthToken()
    }

    fun getContentType(): ContentType {
        return contentType
    }

    fun getServerUrl(): String {
        return serverUrl
    }

    fun getClient(): HttpClient {
        return client
    }

}


