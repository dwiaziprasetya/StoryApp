package com.example.storyapp.repository

import com.example.storyapp.data.remote.response.RegisterResponse
import com.example.storyapp.data.remote.retrofit.ApiConfig
import com.example.storyapp.data.remote.retrofit.ApiService

class StroryAppRepository private constructor(
    private val apiService : ApiService
){

    suspend fun register(
        username: String,
        email: String,
        password: String
    ) : RegisterResponse {
        return apiService.register(username, email, password)
    }

    companion object {
        @Volatile
        private var instance : StroryAppRepository? = null
        fun getInstance(
            apiService: ApiService
        ) : StroryAppRepository =
            instance ?: synchronized(this) {
                instance ?: StroryAppRepository(apiService)
            }.also {
                instance = it
            }
    }

}