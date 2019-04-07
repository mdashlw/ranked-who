package ru.mdashlw.rankedwho.util

import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.SocketException
import java.net.SocketTimeoutException

fun OkHttpClient.newCall(url: String): String? =
    try {
        val request = Request.Builder()
            .apply {
                url(url)
            }
            .build()

        newCall(request)
            .execute()
            .body()!!
            .string()
    } catch (exception: SocketException) {
        null
    } catch (exception: SocketTimeoutException) {
        null
    }
