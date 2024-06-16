package com.example.storyapp.data.remote.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object ApiConfig {
    fun getApiService() : ApiService {
        val loggingInterceptor =
            HttpLoggingInterceptor()
                .setLevel(
                    HttpLoggingInterceptor
                        .Level.BODY
                )
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://story-api.dicoding.dev/v1/")
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }
}