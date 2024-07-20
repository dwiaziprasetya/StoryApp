package com.example.storyapp.repository

import com.example.storyapp.data.remote.response.DetailStoryResponse
import com.example.storyapp.data.remote.response.FileUploadResponse
import com.example.storyapp.data.remote.response.LoginResponse
import com.example.storyapp.data.remote.response.LoginResult
import com.example.storyapp.data.remote.response.RegisterResponse
import com.example.storyapp.data.remote.response.StoryResponse
import com.example.storyapp.data.remote.retrofit.ApiService
import com.example.storyapp.util.SessionPreferences
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UserRepository private constructor(
    private val apiService : ApiService,
    private val pref: SessionPreferences
){

    fun getSession() : Flow<LoginResult> {
        return pref.getSession()
    }

    suspend fun logout() {
        pref.logout()
    }

    suspend fun saveLoginData(loginResponse: LoginResult) {
        pref.saveLoginData(loginResponse)
    }

    suspend fun getDetailStory(id: String) : DetailStoryResponse {
        return apiService.getDetailStory(id)
    }

    suspend fun getStoriesWithLocation() : StoryResponse {
        return apiService.getStoriesWithLocation()
    }

    suspend fun getStories(): StoryResponse {
        return apiService.getStories()
    }

    suspend fun uploadImage(
        file: MultipartBody.Part,
        description: RequestBody
    ) : FileUploadResponse {
        return apiService.addStory(file, description)
    }

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
        fun getInstance(
            userPreference: SessionPreferences,
            apiService: ApiService
        ) = UserRepository(
            apiService,
            userPreference
        )
    }
}