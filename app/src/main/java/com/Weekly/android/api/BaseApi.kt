package com.Weekly.android.api

import com.Weekly.android.service.LogService
import com.Weekly.android.util.ApiConfiguration

abstract class BaseApi(apiConfiguration: ApiConfiguration) {

    protected val httpClient = apiConfiguration.getClient()
    protected val serverUrl = apiConfiguration.getServerUrl()
    protected val contentType = apiConfiguration.getContentType()
    protected val logger = LogService()


}