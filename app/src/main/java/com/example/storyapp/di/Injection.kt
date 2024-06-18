    package com.example.storyapp.di

    import android.content.Context
    import android.util.Log
    import com.example.storyapp.data.remote.retrofit.ApiConfig
    import com.example.storyapp.repository.UserRepository
    import dataStore
    import kotlinx.coroutines.flow.first
    import kotlinx.coroutines.runBlocking

    object Injection {
        fun provideRepository(context: Context): UserRepository {
            val pref = SessionPreferences.getInstance(context.dataStore)

            val user = runBlocking { pref.getSession().first() }
            val token = user.token

            Log.d("Injection", "Token: $token")

            val apiService = ApiConfig.getApiService(token)
            return UserRepository.getInstance(apiService)
        }

    }