package com.eld.eld.api

import com.grapesnberries.curllogger.CurlLoggerInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

object APIClient {

    private var retrofit: Retrofit? = null

    fun getClientLogin(): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(CurlLoggerInterceptor())
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl("https://weblaunchpad.in/deetee_industries/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit!!
    }
}
