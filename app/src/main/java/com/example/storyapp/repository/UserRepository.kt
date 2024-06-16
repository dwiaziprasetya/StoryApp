package com.example.storyapp.repository

import com.example.storyapp.data.remote.response.LoginResponse
import com.example.storyapp.data.remote.response.RegisterResponse
import com.example.storyapp.data.remote.retrofit.ApiService

class UserRepository private constructor(
    private val apiService : ApiService
){

    suspend fun register(
        username: String,
        email: String,
        password: String
    ) : RegisterResponse {
        return apiService.register(username, email, password)
    }

    suspend fun login(
        email: String,
        password: String
    ) : LoginResponse {
        return apiService.login(email, password)
    }

    companion object {
        @Volatile
        private var instance : UserRepository? = null
        fun getInstance(
            apiService: ApiService
        ) : UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService)
            }.also {
                instance = it
            }
    }

}